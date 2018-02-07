package com.bean.form.dao;

import com.bean.form.model.PersonModel;

import java.util.List;

public interface PersonDAO {

    PersonModel findByFullNameAndTelNomber(String personName, String telephoneNumber);

    PersonModel findById(Long id);

    PersonModel saveNewPerson (PersonModel personModel);

    PersonModel editPerson (PersonModel personModel);

    void deletePerson (Long Id);

    List<PersonModel> allPersons();
}
