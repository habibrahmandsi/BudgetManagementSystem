package com.macrosoft.bms.data.model.Entity;

import javax.persistence.*;

/**
 * Created by habib on 7/12/17.
 */
@Entity
@Table(name = "case_team")
public class CaseTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name="case_id")
    private Case aCase;

    @ManyToOne()
    @JoinColumn(name="team_id")
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Case getaCase() {
        return aCase;
    }

    public void setaCase(Case aCase) {
        this.aCase = aCase;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
