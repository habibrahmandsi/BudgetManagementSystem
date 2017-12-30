package com.macrosoft.bms.data.model.Entity;

import javax.persistence.*;

/**
 * Created by habib on 7/12/17.
 */
@Entity
@Table(name = "document_container")
public class DocumentContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private DocumentContainerType documentContainerType;

    @Column(name = "doc_container_search_id")
    private Long DocContainerSearchId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentContainerType getDocumentContainerType() {
        return documentContainerType;
    }

    public void setDocumentContainerType(DocumentContainerType documentContainerType) {
        this.documentContainerType = documentContainerType;
    }

    public Long getDocContainerSearchId() {
        return DocContainerSearchId;
    }

    public void setDocContainerSearchId(Long docContainerSearchId) {
        DocContainerSearchId = docContainerSearchId;
    }
}
