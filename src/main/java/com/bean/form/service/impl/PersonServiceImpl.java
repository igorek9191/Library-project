package com.bean.form.service.impl;

import com.bean.form.dao.PersonDAO;
import com.bean.form.model.PersonModel;
import com.bean.form.service.PersonService;
import com.bean.form.view.PersonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;

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
        return new PersonView(person.getId(), person.getFullName(), person.getPhoneNumber());
    }

    @Override
    @Transactional
    public PersonView editPerson(PersonView newPerson, Long Id) {
        PersonModel newPersonModel = new PersonModel(Id, newPerson.getFullName(), newPerson.getPhoneNumber());
        PersonModel savedPerson = personDAO.editPerson(newPersonModel);
        return new PersonView(savedPerson.getId(), savedPerson.getFullName(), savedPerson.getPhoneNumber());
    }

    @Override
    @Transactional
    public void deletePerson(PersonView personView) {
        personDAO.deletePerson(personView.getId());
    }

    @Override
    @Transactional
    public PersonView findByFullNameAndTelNomber(PersonView personView) throws EmptyResultDataAccessException {
        PersonModel personModel = personDAO.findByFullNameAndTelNomber(personView.getFullName(), personView.getPhoneNumber());
        PersonView person = new PersonView(personModel.getFullName(), personModel.getPhoneNumber());
        return person;
    }

    @Override
    public PersonView findPersonWithId(PersonView personView) {
        PersonModel personModel = personDAO.findByFullNameAndTelNomber(personView.getFullName(), personView.getPhoneNumber());
        PersonView person = new PersonView(personModel.getId(), personModel.getFullName(), personModel.getPhoneNumber());
        return person;
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



}
