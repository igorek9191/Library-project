package com.bean.form.controller;

import com.bean.form.exceptions.PersonExceptions.IncorrectPersonInputDataException;
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
import org.springframework.http.ResponseEntity;
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

    private static final String PERSON_NAME_PATTERN = "[А-Яа-я]{1,50}\\s{1,2}[А-Яа-я]{1,50}[\\.]{0,1}\\s{0,2}[А-Яа-я]{1,50}[\\.]{0,1}";
    private static final String PHONE_NUMBER_PATTERN = "[\\d]{11}";
    private static final String PHONE_NUMBER_PATTERN_2 = "[\\d]{6}";

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @CrossOrigin
    @ApiOperation(value = "add")
    @RequestMapping(value = "/person/add", method = {POST})
    public ResponseEntity<PersonView> addPerson(@RequestBody PersonView personView) {
        validateInputData(personView);

        PersonView data = personService.addPerson(personView);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation(value = "edit")
    @RequestMapping(value = "/person/edit", method = {POST})
    public ResponseEntity<PersonView> editPerson(@RequestBody PersonEdit personEdit) {
        PersonView oldPersonView = personEdit.oldPersonView();
        PersonView newPersonView = personEdit.newPersonView();

        validateInputData(oldPersonView);
        validateInputData(newPersonView);

        PersonView editedPerson = personService.editPerson(oldPersonView, newPersonView);
        return new ResponseEntity<>(editedPerson, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "delete")
    @RequestMapping(value = "/person/delete", method = {DELETE})
    public ResponseEntity<String> deletePerson(@RequestBody PersonView personView) {
        validateInputData(personView);

        personService.deletePerson(personView);
        return new ResponseEntity<>("DELETED", HttpStatus.OK);
    }

    @ApiOperation(value = "get all Persons", nickname = "get all Persons", httpMethod = "GET")
    @CrossOrigin
    @RequestMapping(value = "/persons/all", method = {GET})
    public ResponseEntity<List<PersonView>> personCatalog() {

        List<PersonView> data = personService.personCatalog();
        if (data.isEmpty()) return new ResponseEntity<>(data, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    public static void validateInputData(PersonView personView) {
        String personName = personView.getFullName();
        String phoneNumber = personView.getPhoneNumber();
        if(personName==null || !personName.matches(PERSON_NAME_PATTERN) ||
            phoneNumber==null || !(phoneNumber.matches(PHONE_NUMBER_PATTERN) || phoneNumber.matches(PHONE_NUMBER_PATTERN_2))) {
                throw new IncorrectPersonInputDataException();
        }
    }

}
