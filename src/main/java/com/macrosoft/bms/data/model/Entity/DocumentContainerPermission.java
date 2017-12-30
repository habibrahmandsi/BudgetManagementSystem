package com.macrosoft.bms.data.model.Entity;

import javax.persistence.*;

/**
 * Created by habib on 7/12/17.
 */
@Entity
@Table(name = "document_container_permission")
public class DocumentContainerPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="document_container_id")
    private DocumentContainer documentContainer;

    @ManyToOne
    @JoinColumn(name="team_id")
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentContainer getDocumentContainer() {
        return documentContainer;
    }

    public void setDocumentContainer(DocumentContainer documentContainer) {
        this.documentContainer = documentContainer;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
