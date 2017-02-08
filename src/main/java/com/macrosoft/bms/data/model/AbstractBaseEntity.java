package com.macrosoft.bms.data.model;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * @Author: Md. Habibur Rahaman Sumon
 * Date: 10/31/2016
 * Time: 14:28 PM
 */

@MappedSuperclass
public abstract class AbstractBaseEntity implements Lifecycle {

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Override
	public boolean onSave(Session session) throws CallbackException {
//            setCreated(new Date());
//            setCreatedBy(Utils.getLoggedInUsername());
        return false;
    }

    @Override
	public boolean onUpdate(Session session)throws CallbackException {
//        setModified(new Date());
//        setModifiedBy(Utils.getLoggedInUsername());
        return false;
    }

    @Override
	public boolean onDelete(Session session) throws CallbackException {
        return false;
    }

    @Override
	public void onLoad(Session session, Serializable serializable) {

    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
