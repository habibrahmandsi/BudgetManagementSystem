package com.icontrolesi.envity.data.dao.Impl;

import java.io.Serializable;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.icontrolesi.envity.data.dao.AdminDAO;
import com.icontrolesi.envity.data.model.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.StringUtils;

@Repository
@Transactional
public class AdminDAOImpl implements AdminDAO {

    @PersistenceContext
    EntityManager entityManager;

    protected Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    protected final Session getSession() {
        return this.getCurrentSession();
    }


    @Override
    public void saveOrUpdate(Object record) {
        getCurrentSession().saveOrUpdate(record);
    }

    @Override
    public void save(Object record) {
        getCurrentSession().save(record);
    }

    @Override
    public Object get(Serializable pk) {
        return null;
    }

    @Override
    public void update(Object record) {
        getCurrentSession().update(record);
        getCurrentSession().flush();
    }

    @Override
    public void delete(Object record) {
        getCurrentSession().delete(record);
    }

}

