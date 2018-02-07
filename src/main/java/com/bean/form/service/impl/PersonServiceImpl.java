package com.bean.form.service.impl;

import com.bean.form.dao.PersonDAO;
import com.bean.form.exceptions.PersonExceptions.PersonAlreadyExistsException;
import com.bean.form.exceptions.PersonExceptions.PersonNotFoundException;
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
        try {
            findByFullNameAndTelNomber(personView);
        } catch (EmptyResultDataAccessException e) {
            PersonModel newPerson = new PersonModel(personView.getFullName(), personView.getPhoneNumber());
            PersonModel savedPerson = personDAO.saveNewPerson(newPerson);
            return new PersonView(savedPerson.getId(), savedPerson.getFullName(), savedPerson.getPhoneNumber());
        }
        throw new PersonAlreadyExistsException(personView.getFullName(), personView.getPhoneNumber());
    }

    @Override
    @Transactional
    public PersonView editPerson(PersonView oldPerson, PersonView newPerson) {
        PersonView existPerson;
        try {
            existPerson = findPersonWithId(oldPerson);
        } catch (EmptyResultDataAccessException e) {
            throw new PersonNotFoundException(oldPerson.getFullName(), oldPerson.getPhoneNumber());
        }

        PersonModel newPersonModel = new PersonModel(existPerson.getId(), newPerson.getFullName(), newPerson.getPhoneNumber());
        PersonModel savedPerson = personDAO.editPerson(newPersonModel);
        return new PersonView(savedPerson.getId(), savedPerson.getFullName(), savedPerson.getPhoneNumber());
    }

    @Override
    @Transactional
    public void deletePerson(PersonView personView) {
        PersonView existPerson;
        try {
            existPerson = findPersonWithId(personView);
        } catch (EmptyResultDataAccessException e) {
            throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());
        }

        personDAO.deletePerson(existPerson.getId());
    }

    @Override
    @Transactional
    public PersonView findByFullNameAndTelNomber(PersonView personView) throws EmptyResultDataAccessException {
        PersonModel personModel = personDAO.findByFullNameAndTelNomber(personView.getFullName(), personView.getPhoneNumber());
        return new PersonView(personModel.getFullName(), personModel.getPhoneNumber());
    }

    @Override
    public PersonView findPersonWithId(PersonView personView) {
        PersonModel personModel = personDAO.findByFullNameAndTelNomber(personView.getFullName(), personView.getPhoneNumber());
        return new PersonView(personModel.getId(), personModel.getFullName(), personModel.getPhoneNumber());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonView> personCatalog() {
        List<PersonModel> allPersons = personDAO.allPersons();
        Function<PersonModel, PersonView> mapPersonModelToPersonView = PersonView::new;/*person ->  {
            PersonView personView = new PersonView();
            personView.id = person.getId();
            personView.fullName = person.getFullName();
            personView.phoneNumber = person.getPhoneNumber();
            return personView;
        };*/
        return allPersons.stream().map(mapPersonModelToPersonView).collect(Collectors.toList());
    }



}
