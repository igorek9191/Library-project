package com.bean.form.service;


import com.bean.form.model.PersonModel;
import com.bean.form.view.PersonView;

import java.util.List;

public interface PersonService {

    PersonView addPerson(PersonView personView);

    PersonView editPerson(PersonView oldPerson, Long Id);

    PersonView deletePerson (Long ID);

    List<PersonView> personCatalog ();

    //PersonView findByFullNameAndTelNomber(String personName, int telephoneNumber);
    PersonModel findByFullNameAndTelNomber(PersonView personView);
}
