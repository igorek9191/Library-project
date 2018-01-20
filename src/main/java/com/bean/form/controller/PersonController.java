package com.bean.form.controller;

import com.bean.form.exceptions.PersonExceptions.IncorrectPersonInputData;
import com.bean.form.exceptions.PersonExceptions.PersonAlreadyExistsException;
import com.bean.form.exceptions.PersonExceptions.PersonNotFoundException;
import com.bean.form.model.PersonModel;
import com.bean.form.service.PersonService;
import com.bean.form.service.impl.PersonServiceImpl;
import com.bean.form.view.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Api
@CrossOrigin
@RestController
@RequestMapping(value = "/personController", produces = APPLICATION_JSON_VALUE)
public class PersonController {

    private final TaskExecutor executor;
    private final PersonService personService;

    @Autowired
    public PersonController(TaskExecutor executor, PersonService personService) {
        this.executor = executor;
        this.personService = personService;
    }

    @CrossOrigin
    @ApiOperation(value = "add")
    @RequestMapping(value = "/person/add", method = {POST})
    public Response<PersonView, String> addPerson(@RequestBody PersonView personView) {

        PersonView data = null;
        String error = null;
        PersonModel personModel = null;

        PersonServiceImpl.validateInputData(personView);

        try {
            personModel = personService.findByFullNameAndTelNomber(personView);
        }
        catch (Exception e) {
            if (personModel == null) {
                data = personService.addPerson(personView);
                return new Response<>(data, error);
            }
            error = e.getMessage();
            return new Response<>(data, error);
        }
        throw new PersonAlreadyExistsException(personView.getFullName(), personView.getPhoneNumber());
    }

    @CrossOrigin
    @ApiOperation(value = "edit")
    @RequestMapping(value = "/person/edit", method = {POST})
    public Response<PersonView, String> editPerson(@RequestBody PersonEdit personEdit) {

        PersonView oldPersonView = personEdit.oldPersonView();
        PersonView newPersonView = personEdit.newPersonView();

        Long personId = null;
        PersonModel oldPersonModel = null;

        PersonServiceImpl.validateInputData(oldPersonView);
        PersonServiceImpl.validateInputData(newPersonView);

        try {
            oldPersonModel = personService.findByFullNameAndTelNomber(oldPersonView);
            personId = oldPersonModel.getId();
            PersonView savedPerson = personService.editPerson(newPersonView, personId);
            return new Response<>(savedPerson, null);
        } catch (Exception e) {
            if(oldPersonModel == null) throw new PersonNotFoundException(oldPersonView.getFullName(), oldPersonView.getPhoneNumber());
            return new Response<>(null, e.getMessage());
        }
    }

        @CrossOrigin
    @ApiOperation(value = "delete")
    @RequestMapping(value = "/person/delete", method = {DELETE})
    public Response<PersonView, String> deletePerson(@RequestBody PersonView personView) {

        PersonView data = null;
        String error = null;
        Long personId = null;

        PersonServiceImpl.validateInputData(personView);

        try {
            personId = personService.findByFullNameAndTelNomber(personView).getId();
        }
        catch (Exception e) {
            if (personId == null) throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());
            error = e.getMessage();
            return new Response<>(data, error);
        }
        data = personService.deletePerson(personId);
        return new Response<>(data, error);
    }

    @ApiOperation(value = "get all Persons", nickname = "get all Persons", httpMethod = "GET")
    @CrossOrigin
    @RequestMapping(value = "/persons/all", method = {GET})
    public Response<List<PersonView>, String> personCatalog() {

        List<PersonView> data = null;
        String errors = null;

        try {
            data = personService.personCatalog();
        }
        catch (Exception e) {
            errors = e.getMessage();
            return new Response<>(data, errors);
        }
        if (data.size() == 0) errors = "Нет читателей в БД";
        return new Response<>(data, errors);
    }

    @ExceptionHandler({IncorrectPersonInputData.class, PersonAlreadyExistsException.class, PersonNotFoundException.class})
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public ErrorResponse<List<String>> handleException(RuntimeException ex){
        ErrorResponse<List<String>> listOfResponse = new ErrorResponse<>();
        List<String> list = new ArrayList<>();
        list.add(ex.getMessage());
        listOfResponse.setErrors(list);
        return listOfResponse;
    }

}
