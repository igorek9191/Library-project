package com.bean.form.service;

import org.springframework.dao.EmptyResultDataAccessException;
import com.bean.form.view.PersonView;

import java.util.List;

public interface PersonService {

    PersonView addPerson(PersonView personView);

    PersonView editPerson(PersonView oldPerson, Long Id);

    void deletePerson (PersonView personView);

    List<PersonView> personCatalog ();

    //PersonView findByFullNameAndTelNomber(String personName, int telephoneNumber);
    PersonView findByFullNameAndTelNomber(PersonView personView) throws EmptyResultDataAccessException;

    PersonView findPersonWithId (PersonView personView);
}
