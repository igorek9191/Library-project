package com.bean.form.controller;

import com.bean.form.exceptions.PersonExceptions.IncorrectPersonInputData;
import com.bean.form.exceptions.PersonExceptions.PersonAlreadyExistsException;
import com.bean.form.exceptions.PersonExceptions.PersonNotFoundException;
import com.bean.form.service.PersonService;
import com.bean.form.view.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        validateInputData(personView);

        PersonView data = null;

        try {
            data = personService.findByFullNameAndTelNomber(personView);
        } catch (EmptyResultDataAccessException e) {
            if (data == null) {
                data = personService.addPerson(personView);
                return new Response<>(data, null);
            }
        }
        throw new PersonAlreadyExistsException(personView.getFullName(), personView.getPhoneNumber());
    }

    @CrossOrigin
    @ApiOperation(value = "edit")
    @RequestMapping(value = "/person/edit", method = {POST})
    public Response<PersonView, String> editPerson(@RequestBody PersonEdit personEdit) {

        PersonView oldPersonView = personEdit.oldPersonView();
        PersonView newPersonView = personEdit.newPersonView();

        validateInputData(oldPersonView);
        validateInputData(newPersonView);

        Long personId = null;
        PersonView oldPerson = null;

        try {
            oldPerson = personService.findPersonWithId(oldPersonView);
        } catch (EmptyResultDataAccessException e) {
            if(oldPerson == null) throw new PersonNotFoundException(oldPersonView.getFullName(), oldPersonView.getPhoneNumber());
        }
        personId = oldPerson.getId();
        PersonView savedPerson = personService.editPerson(newPersonView, personId);
        return new Response<>(savedPerson, null);
    }

        @CrossOrigin
    @ApiOperation(value = "delete")
    @RequestMapping(value = "/person/delete", method = {DELETE})
    public Response<PersonView, String> deletePerson(@RequestBody PersonView personView) {

        validateInputData(personView);

        PersonView data = null;

        try {
            data = personService.findPersonWithId(personView);
        } catch (EmptyResultDataAccessException e) {
            if (data == null) throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());
        }
        personService.deletePerson(data);
        return new Response<>(data, null);
    }

    @ApiOperation(value = "get all Persons", nickname = "get all Persons", httpMethod = "GET")
    @CrossOrigin
    @RequestMapping(value = "/persons/all", method = {GET})
    public Response<List<PersonView>, String> personCatalog() {

        List<PersonView> data = null;

        data = personService.personCatalog();
        if (data.size() == 0) return new Response<>(data, "Нет читателей в БД");
        return new Response<>(data, null);
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

    public static void validateInputData(PersonView personView) {

        Pattern personNamePt = Pattern.compile("[А-Яа-я]{1,50}(\\s){1,2}[А-Яа-я]{1,50}[\\.]{0,1}(\\s){0,2}[А-Яа-я]{1,50}[\\.]{0,1}");
        Pattern personPhoneNumberPt = Pattern.compile("[\\d]{11}");//[\d]{1}[\s]{0,1}[\d]{3}[\s]{0,1}[\d]{3}[\s]{0,1}[\d]{2}[\s]{0,1}[\d]{2}
        Pattern personPhoneNumberPt2 = Pattern.compile("[\\d]{6}");//[\d]{2}[\s]{0,1}[\d]{2}[\s]{0,1}[\d]{2}[\s]{0,1}

        Matcher personNameMt = personNamePt.matcher(personView.getFullName());
        Matcher personPhoneNumberMt = personPhoneNumberPt.matcher((String.valueOf(personView.getPhoneNumber())));
        Matcher personPhoneNumberMt2 = personPhoneNumberPt2.matcher((String.valueOf(personView.getPhoneNumber())));

        boolean personNameMatch = personNameMt.matches();
        boolean personPhoneNumberMatch = personPhoneNumberMt.matches();
        boolean personPhoneNumberMatch2 = personPhoneNumberMt2.matches();

        if(!personNameMatch || !(personPhoneNumberMatch || personPhoneNumberMatch2)) {
            throw new IncorrectPersonInputData();
        }

    }

}
