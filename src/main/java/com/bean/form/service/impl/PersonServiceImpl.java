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
            PersonModel model = personDAO.findByFullNameAndPhoneNumber(personView.getFullName(), personView.getPhoneNumber());
            if(model == null){
                PersonModel newPerson = new PersonModel(personView.getFullName(), personView.getPhoneNumber());
                PersonModel savedPerson = personDAO.save(newPerson);
                return new PersonView(savedPerson);//.getId(), savedPerson.getFullName(), savedPerson.getPhoneNumber()
            } else {
                throw new PersonAlreadyExistsException(personView.getFullName(), personView.getPhoneNumber());
            }
    }

    @Override
    @Transactional
    public PersonView editPerson(PersonView oldPerson, PersonView newPerson) {
        PersonModel existPerson = personDAO.findByFullNameAndPhoneNumber(oldPerson.getFullName(), oldPerson.getPhoneNumber());
        if (existPerson == null){
            throw new PersonNotFoundException(oldPerson.getFullName(), oldPerson.getPhoneNumber());
        } else {
            existPerson.setFullName(newPerson.getFullName());
            existPerson.setPhoneNumber(newPerson.getPhoneNumber());
            PersonModel editedPerson = personDAO.save(existPerson);
            return new PersonView(editedPerson);
        }//EmptyResultDataAccessException e
    }

    @Override
    @Transactional
    public void deletePerson(PersonView personView) {
        PersonModel existPerson = personDAO.findByFullNameAndPhoneNumber(personView.getFullName(), personView.getPhoneNumber());
        if(existPerson == null) {
            throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());
        } else {
            personDAO.delete(existPerson.getId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonView> personCatalog() {
        List<PersonModel> allPersons = personDAO.findAll();
        Function<PersonModel, PersonView> mapPersonModelToPersonView = PersonView::new;
        return allPersons.stream().map(mapPersonModelToPersonView).collect(Collectors.toList());
    }



}
