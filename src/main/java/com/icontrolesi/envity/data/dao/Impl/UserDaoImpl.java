package com.icontrolesi.envity.data.dao.Impl;

import com.icontrolesi.envity.data.dao.UserDAO;
import com.icontrolesi.envity.data.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class UserDaoImpl implements UserDAO {

    @PersistenceContext
    EntityManager entityManager;

    protected Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    protected final Session getSession() {
        return this.getCurrentSession();
    }


    public List<User> getAllUsers() {
        String HQL = "FROM User";
        return getCurrentSession().createQuery(HQL)
                .list();
    }

    @Override
    public User getUserByUserName(String username) {
        String HQL = "FROM User eu WHERE eu.username =:username";
        List<User> list = getCurrentSession().createQuery(HQL)
                .setParameter("username", username)
                .list();

        if(list != null && list.size() > 0)
            return list.get(0);
        else
        return null;
    }

    @Override
    public List<Map> getUserList(){
        String sql = "SELECT new MAP(id as id, firstName as firstname, lastName as lastname," +
                " username as username, email as email, enabled as enabled)  FROM User" +
                " ORDER BY firstname";
        return getCurrentSession().createQuery(sql).list();
    }

    @Override
    public User getUserById(Integer userId) {
        String HQL = "FROM User eu WHERE eu.id =:userId";
        List<User> list = getCurrentSession().createQuery(HQL)
                .setParameter("userId", userId)
                .list();

        if(list != null && list.size() > 0)
            return list.get(0);
        else
            return null;
    }

    @Override
    public Long getUserDataSize() {
        Query query = getCurrentSession().createQuery(
                "select count(*) from User");
        return (Long)query.uniqueResult();
    }

    @Override
    public Map<String, Object> getUsers(Integer start, Integer length, String sortColName, String sortType, String searchKey){
        Map<String, Object> result = new HashMap();
        StringBuilder sql = new StringBuilder("SELECT "
                     + "new MAP("
                     + " id as id, "
                     + " firstName as firstname,"
                     + " lastName as lastname,"
                     + " username as username,"
                     + " email as email,"
                     + " enabled as enabled"
                     + ") "
                     + " FROM User");

        if (!StringUtils.isEmpty(searchKey)) {
            sql.append(" WHERE lower(firstName) LIKE lower(:firstname)");
            sql.append(" OR lower(lastName) LIKE lower(:lastname)");
            sql.append(" OR lower(email) LIKE lower(:email)");
            sql.append(" OR lower(username) LIKE lower(:username)");
        }
        sql.append(" ORDER BY ");
        sql.append(sortColName);
        sql.append(" ");
        sql.append(sortType);

        Query query = getCurrentSession().createQuery(sql.toString());

        if (!StringUtils.isEmpty(searchKey)) {
            query.setParameter("firstname",searchKey + "%");
            query.setParameter("lastname",searchKey + "%");
            query.setParameter("email",searchKey + "%");
            query.setParameter("username",searchKey + "%");
        }
        query.setFirstResult(start);
        query.setMaxResults(length);
        System.out.println("start:"+start+" length:"+length);
        List list = query.list();
        result.put("data", list);
        if (list != null && list.size() > 0)
            result.put("total", list.size());
        else
            result.put("total", 0);
        return result;
    }

}

