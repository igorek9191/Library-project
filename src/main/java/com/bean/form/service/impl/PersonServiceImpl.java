package com.bean.form.service.impl;

import com.bean.form.dao.PersonDAO;
import com.bean.form.exceptions.PersonExceptions.IncorrectPersonInputData;
import com.bean.form.exceptions.PersonExceptions.PersonNotFoundException;
import com.bean.form.model.PersonModel;
import com.bean.form.service.PersonService;
import com.bean.form.view.PersonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonDAO personDAO;

    @Autowired
    public PersonServiceImpl(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    @Transactional
    public PersonView addPerson(PersonView personView) {
        PersonModel personModel = new PersonModel(personView.getFullName(), personView.getPhoneNumber());
        PersonModel person = personDAO.saveNewPerson(personModel);
        return new PersonView(person.getFullName(), person.getPhoneNumber());
    }

    @Override
    @Transactional
    public PersonView editPerson(PersonView newPerson, Long Id) {
        PersonModel newPersonModel = new PersonModel(Id, newPerson.getFullName(), newPerson.getPhoneNumber());
        PersonModel savedPerson = personDAO.editPerson(newPersonModel);
        return new PersonView(savedPerson.getFullName(), savedPerson.getPhoneNumber());
    }

    @Override
    @Transactional
    public PersonView deletePerson(Long ID) {
        //PersonModel personModel = new PersonModel(personView.getId(), personView.getFullName(), personView.getPhoneNumber());
        PersonModel personModel = personDAO.deletePerson(ID);
        return new PersonView(personModel.getFullName(), personModel.getPhoneNumber());
    }

    @Override
    @Transactional
    public PersonModel findByFullNameAndTelNomber(PersonView personView) {
        //validateInputData(personView);
        PersonModel personModel = null;
        personModel = personDAO.findByFullNameAndTelNomber(personView.getFullName(), personView.getPhoneNumber());
        //PersonView person = new PersonView(personModel.getId(), personModel.getFullName(), personModel.getPhoneNumber());
        return personModel;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonView> personCatalog() {
        List<PersonModel> allPersons = personDAO.allPersons();
        Function<PersonModel, PersonView> mapPersonModelToPersonView = person ->  {
            PersonView personView = new PersonView();

            personView.fullName = person.getFullName();
            personView.phoneNumber = person.getPhoneNumber();
            return personView;
        };
        return allPersons.stream().map(mapPersonModelToPersonView).collect(Collectors.toList());
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
