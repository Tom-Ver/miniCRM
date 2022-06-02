package com.crm.miniCRM.model.persistence;


import com.crm.miniCRM.model.PersonAddress;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface PersonAddressRepository extends CrudRepository<PersonAddress, PersonAddressID> {

    Optional<PersonAddress> findById(PersonAddressID personAddressID);

    @Query("select p from PersonAddress p where p.Id.person_ID = ?1")
    Collection<PersonAddress> findByPersonId(Long personId);
}