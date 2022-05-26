package com.crm.miniCRM.model.persistence;

import com.crm.miniCRM.model.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, MemberID> {


    Optional<Member> findById(MemberID id);

    @Query("select m from Member m where m.Id.community_ID = ?1")
    Collection<Member> findAllByIdCommunity_ID(Long id);
}