package com.bean.form.service;

import com.bean.form.model.PersonModel;
import org.springframework.dao.EmptyResultDataAccessException;
import com.bean.form.view.PersonView;

import java.util.List;

public interface PersonService {

    PersonView addPerson(PersonView personView);

    PersonView editPerson(PersonView oldPerson, PersonView newPerson);

    void deletePerson (PersonView personView);

    List<PersonView> personCatalog ();

}
