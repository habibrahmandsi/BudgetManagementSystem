package com.macrosoft.bms.data.model.Entity;

public enum DocumentContainerType {

    TAG("Tag"),
    FOLDER("Folder"),
    SAVED_SEARCH("Saved Search"),
    WORKFLOW_STATE("Workflow State");

    private String label;

    DocumentContainerType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }
}