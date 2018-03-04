package com.bean.form.dao;

import com.bean.form.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PersonDAO extends JpaRepository<PersonModel, Integer>{

    PersonModel findByFullNameAndPhoneNumber(String fullName, String phoneNumber);

    PersonModel save(PersonModel personModel);

    void delete(Integer id);

    List<PersonModel> findAll();
}
