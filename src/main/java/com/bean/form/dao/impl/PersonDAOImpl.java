package com.bean.form.dao.impl;

import com.bean.form.dao.PersonDAO;
import com.bean.form.model.BookModel;
import com.bean.form.model.PersonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PersonDAOImpl implements PersonDAO {

    private final EntityManager entityManager;

    @Autowired
    public PersonDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public PersonModel findByFullNameAndTelNomber(String personName, String telephoneNumber) {
        TypedQuery<PersonModel> query = entityManager.createQuery("SELECT p FROM PersonModel p WHERE fullName =:fullName AND phoneNumber =:telephoneNumber", PersonModel.class);
        query.setParameter("fullName", personName);
        query.setParameter("telephoneNumber", telephoneNumber);
        return query.getSingleResult();
    }

    @Override
    public PersonModel findById(Long id) {
        return entityManager.find(PersonModel.class, id);
    }

    @Override
    public PersonModel saveNewPerson(PersonModel personModel) {
        entityManager.persist(personModel);
        return entityManager.find(PersonModel.class, personModel.getId());
    }

    @Override
    public PersonModel editPerson(PersonModel personModel) {
        return entityManager.merge(personModel);
    }

    @Override
    public void deletePerson(Long Id) {
        PersonModel personModel = entityManager.find(PersonModel.class, Id);
        entityManager.remove(personModel);
    }

    @Override
    public List<PersonModel> allPersons() {
        TypedQuery<PersonModel> query = entityManager.createQuery("SELECT p FROM PersonModel p", PersonModel.class);
        return query.getResultList();
    }
}
