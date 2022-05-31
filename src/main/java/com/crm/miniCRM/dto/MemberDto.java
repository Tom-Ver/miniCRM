package com.crm.miniCRM.dto;

import com.crm.miniCRM.model.persistence.MemberID;

import java.time.LocalDate;

public class MemberDto {

    private Long person_ID;
    private Long community_ID;
    private LocalDate since;
    private LocalDate until;//default 9999-21-31

    public MemberDto(){}

    public MemberDto(MemberID id, LocalDate since, LocalDate until) {
        person_ID = id.getPerson_ID();
        community_ID = id.getCommunity_ID();
        this.since = since;
        this.until = until;
    }

    public MemberDto (MemberID id, LocalDate since){
        this(id, since, LocalDate.of(9999,12,31));
    }

    public Long getPerson_ID() {
        return person_ID;
    }

    public void setPerson_ID(Long person_ID) {
        this.person_ID = person_ID;
    }

    public Long getCommunity_ID() {
        return community_ID;
    }

    public void setCommunity_ID(Long community_ID) {
        this.community_ID = community_ID;
    }

    public LocalDate getSince() {
        return since;
    }

    public void setSince(LocalDate since) {
        this.since = since;
    }

    public LocalDate getUntil() {
        return until;
    }

    public void setUntil(LocalDate until) {
        this.until = until;
    }
}
