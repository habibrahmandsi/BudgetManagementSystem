package com.macrosoft.bms.data.dao.Impl;

import java.io.Serializable;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import com.macrosoft.bms.data.dao.AdminDAO;
import com.macrosoft.bms.data.model.*;
import eu.bitwalker.useragentutils.UserAgent;
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

    public void saveAuditTrial(String message, HttpServletRequest request) throws Exception {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String userSessionId = request.getRequestedSessionId();
        String userIpAddress = request.getRemoteAddr();
        User user = (User) request.getSession().getAttribute("currentUser");
        AuditTrial auditTrialItem = new AuditTrial();
        auditTrialItem.setActivityDate(new Date());
        auditTrialItem.setActivityDesc(message);
        if (userAgent != null) {
            if (userAgent.getBrowser() != null)
                auditTrialItem.setBrowserDetails(userAgent.getBrowser().getName() + "-" + userAgent.getBrowser().getBrowserType());
            if (userAgent.getOperatingSystem() != null) {
                auditTrialItem.setOsDetails(userAgent.getOperatingSystem().getName());
            }

            if (userAgent.getOperatingSystem() != null) {
                auditTrialItem.setDeviceType(userAgent.getOperatingSystem().getDeviceType().getName());
                auditTrialItem.setDeviceDetails(userAgent.getOperatingSystem().getManufacturer().getName());
                auditTrialItem.setIsMobileDevice(userAgent.getOperatingSystem().isMobileDevice());
            }
            auditTrialItem.setIpAddress(userIpAddress);
            auditTrialItem.setUserSessionId(userSessionId);
            auditTrialItem.setUser(user);
//            logger.debug("SMNLOG:auditTrialItem:"+auditTrialItem);
            this.save(auditTrialItem);
        } else {
            System.out.println("SMNLOG:User-agent is null:");
        }
    }

    @Override
    public ShareHolder getShareHolderById(Integer id) throws Exception {
        String HQL = "FROM ShareHolder WHERE id =:id";
        HQL += " ORDER BY id asc";

        Query query = getCurrentSession().createQuery(HQL);

        if(!StringUtils.isEmpty(id)){
            query.setParameter("id", id);
        }
        List<ShareHolder> list = query.list();

        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public Long getShareHolderDataSize() throws Exception{
        Query query = getCurrentSession().createQuery(
                "select count(*) from ShareHolder");
        return (Long)query.uniqueResult();
    }

    public Map<String, Object> getShareHolders(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception{
        System.out.println("start:"+start+" length:"+length+" sortColName:"+sortColName+" sortType:"+sortType+" searchKey:"+searchKey);
        Map<String, Object> result = new HashMap();

        StringBuilder sql = new StringBuilder("SELECT "
                + "new MAP("
                + " id as id, "
                + " name as name,"
                + " sex as sex,"
                + " religion as religion,"
                + " mobile as mobile,"
                + " nationalId as nationalId,"
                + " photoPath as photoPath,"
                + " photoName as photoName,"
                + " fathersName as fathersName,"
                + " mothersName as mothersName,"
                + " spouseName as spouseName,"
                + " currentAddress as currentAddress,"
                + " permanentAddress as permanentAddress,"
                + " email as email"
                + ") "
                + " FROM ShareHolder");

        if (!StringUtils.isEmpty(searchKey)) {
            sql.append(" WHERE (lower(name) LIKE lower(:name)");
            sql.append(" OR lower(sex) LIKE lower(:sex)");
            sql.append(" OR lower(religion) LIKE lower(:religion)");
            sql.append(" OR lower(mobile) LIKE lower(:mobile)");
            sql.append(" OR lower(nationalId) LIKE lower(:nationalId)");
            sql.append(" OR lower(fathersName) LIKE lower(:fathersName)");
            sql.append(" OR lower(mothersName) LIKE lower(:mothersName)");
            sql.append(" OR lower(spouseName) LIKE lower(:spouseName)");
            sql.append(" OR lower(email) LIKE lower(:email))");
        }

        if(!StringUtils.isEmpty(sortColName)){
            sql.append(" ORDER BY ");
            sql.append(sortColName);
            sql.append(" ");
            sql.append(sortType);
        }

        System.out.println("SQL::"+sql);
        Query query = getCurrentSession().createQuery(sql.toString());


        if (!StringUtils.isEmpty(searchKey)) {
            query.setParameter("name","%"+searchKey + "%");
            query.setParameter("sex","%"+searchKey + "%");
            query.setParameter("religion","%"+searchKey + "%");
            query.setParameter("mobile","%"+searchKey + "%");
            query.setParameter("nationalId","%"+searchKey + "%");
            query.setParameter("fathersName","%"+searchKey + "%");
            query.setParameter("mothersName","%"+searchKey + "%");
            query.setParameter("spouseName","%"+searchKey + "%");
            query.setParameter("email","%"+searchKey + "%");

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

    @Override
    public Long getDepositDataSize() throws Exception{
        Query query = getCurrentSession().createQuery(
                "select count(*) from Deposit");
        return (Long)query.uniqueResult();
    }

    public Map<String, Object> getDeposits(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception{
        System.out.println("start:"+start+" length:"+length+" sortColName:"+sortColName+" sortType:"+sortType+" searchKey:"+searchKey);
        Map<String, Object> result = new HashMap();

        StringBuilder sql = new StringBuilder("SELECT "
                + "new MAP("
                + " id as id, "
                + " shareHolder.id as shareHolderId,"
                + " shareHolder.name as name,"
                + " shareHolder.mobile as mobile,"
                + " shareHolder.photoPath as photoPath,"
                + " shareHolder.photoName as photoName,"
                + " shareHolder.fathersName as fathersName,"
                + " shareHolder.mothersName as mothersName,"
                + " shareHolder.email as email,"
                + " installment.name as installmentName,"
                + " amount as amount,"
                + " createdBy.username as createdBy,"
                + " date as date,"
                + " method as method,"
                + " referenceNo as referenceNo"
                + ") "
                + " FROM Deposit");

        if (!StringUtils.isEmpty(searchKey)) {
            sql.append(" WHERE (lower(shareHolder.name) LIKE lower(:name)");
            sql.append(" OR lower(shareHolder.mobile) LIKE lower(:mobile)");
//            sql.append(" OR lower(fathersName) LIKE lower(:fathersName)");
//            sql.append(" OR lower(mothersName) LIKE lower(:mothersName)");
//            sql.append(" OR lower(email) LIKE lower(:email))");
            sql.append(" OR lower(amount) LIKE lower(:amount)");
            sql.append(" OR lower(method) LIKE lower(:method)");
            sql.append(" OR lower(referenceNo) LIKE lower(:referenceNo)");
            sql.append(" OR lower(installment.name) LIKE lower(:installmentName))");
        }

        if(!StringUtils.isEmpty(sortColName)){
            sql.append(" ORDER BY ");
            sql.append(sortColName);
            sql.append(" ");
            sql.append(sortType);
        }

        System.out.println("SQL::"+sql);
        Query query = getCurrentSession().createQuery(sql.toString());

        if (!StringUtils.isEmpty(searchKey)) {
            query.setParameter("name", "%"+searchKey + "%");
            query.setParameter("mobile","%"+searchKey + "%");
//            query.setParameter("fathersName","%"+searchKey + "%");
//            query.setParameter("mothersName","%"+searchKey + "%");
//            query.setParameter("email","%"+searchKey + "%");
            query.setParameter("amount","%"+searchKey + "%");
            query.setParameter("method","%"+searchKey + "%");
            query.setParameter("referenceNo","%"+searchKey + "%");
            query.setParameter("installmentName","%"+searchKey + "%");
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

    @Override
    public Deposit getDepositById(Integer id) throws Exception {
        String HQL = "FROM Deposit WHERE id =:id";
        HQL += " ORDER BY id asc";

        Query query = getCurrentSession().createQuery(HQL);

        if(!StringUtils.isEmpty(id)){
            query.setParameter("id", id);
        }
        List<Deposit> list = query.list();

        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public Map<String, Object> getShareHolderForAutoComplete(String sortColName, String sortType, String searchKey) throws Exception {
        System.out.println(" sortColName:"+sortColName+" sortType:"+sortType+" searchKey:"+searchKey);
        Map<String, Object> result = new HashMap();

        StringBuilder sql = new StringBuilder("SELECT "
                + "new MAP("
                + " id as id, "
                + " name as name,"
                + " sex as sex,"
                + " religion as religion,"
                + " mobile as mobile,"
                + " nationalId as nationalId,"
                + " photoPath as photoPath,"
                + " photoName as photoName,"
                + " fathersName as fathersName,"
                + " mothersName as mothersName,"
                + " email as email"
                + ") "
                + " FROM ShareHolder");

        if (!StringUtils.isEmpty(searchKey)) {
            sql.append(" WHERE (lower(name) LIKE lower(:name)");
            sql.append(" OR lower(mobile) LIKE lower(:mobile)");
            sql.append(" OR lower(nationalId) LIKE lower(:nationalId)");
            sql.append(" OR lower(email) LIKE lower(:email))");
        }

        if(!StringUtils.isEmpty(sortColName)){
            sql.append(" ORDER BY ");
            sql.append(sortColName);
            sql.append(" ");
            sql.append(sortType);
        }

        System.out.println("SQL::"+sql);
        Query query = getCurrentSession().createQuery(sql.toString());


        if (!StringUtils.isEmpty(searchKey)) {
            query.setParameter("name","%"+searchKey + "%");
            query.setParameter("mobile","%"+searchKey + "%");
            query.setParameter("nationalId","%"+searchKey + "%");
            query.setParameter("email","%"+searchKey + "%");

        }

        List list = query.list();
        result.put("data", list);
        if (list != null && list.size() > 0)
            result.put("total", list.size());
        else
            result.put("total", 0);
        return result;
    }

    @Override
    public Double getTotalDepositAmount(Integer shareHolderId) throws Exception {
        String HQL = "select sum(amount) from Deposit";

        if(shareHolderId > 0){
            HQL += " WHERE shareHolder.id =:shareHolderId";
        }

        Query query = getCurrentSession().createQuery(HQL);

        if(shareHolderId > 0){
            query.setParameter("shareHolderId", shareHolderId);
        }
        return (Double)query.uniqueResult();
    }

    @Override
    public List<Map> getDepositListByShareHolderId(Integer shareHolderId) throws Exception {
        String sql = "SELECT "
                + "new MAP("
                + " id as id, "
                + " shareHolder.name as name,"
                + " shareHolder.mobile as mobile,"
                + " shareHolder.photoPath as photoPath,"
                + " shareHolder.photoName as photoName,"
                + " shareHolder.fathersName as fathersName,"
                + " shareHolder.mothersName as mothersName,"
                + " shareHolder.email as email,"
                + " installment.id as installmentId,"
                + " installment.name as installmentName,"
                + " amount as amount,"
                + " createdBy.username as createdBy,"
                + " date as date,"
                + " method as method,"
                + " referenceNo as referenceNo"
                + ") "
                + " FROM Deposit";

        if (shareHolderId != null && shareHolderId > 0) {
            sql += " WHERE shareHolder.id =:shareHolderId";
        }

        Query query = getCurrentSession().createQuery(sql);

        if (shareHolderId != null && shareHolderId > 0) {
            query.setParameter("shareHolderId", shareHolderId);
        }

        return query.list();
    }

    @Override
    public Installment getInstallmentById(Integer installmentId) throws Exception {
        String HQL = "FROM Installment WHERE id =:installmentId";
        HQL += " ORDER BY id asc";

        Query query = getCurrentSession().createQuery(HQL);

        if(!StringUtils.isEmpty(installmentId)){
            query.setParameter("installmentId", installmentId);
        }
        List<Installment> list = query.list();

        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Map> getInstallment() throws Exception {
        String sql = "SELECT "
                + "new MAP("
                + " id as id, "
                + " name as name,"
                + " active as active,"
                + " amountToPay as amountToPay,"
                + " created as created,"
                + " activeFrom as activeFrom"
//                + " createdBy.username as createdBy"
                + ") "
                + " FROM Installment";
//        String sql = "FROM Installment ORDER BY id desc";

        return getCurrentSession().createQuery(sql).list();
    }

    @Override
    public List<ExpenseItem> getAllExpenseItem() throws Exception {
        String HQL = "FROM ExpenseItem";
        HQL += " ORDER BY id asc";

        Query query = getCurrentSession().createQuery(HQL);

        List<ExpenseItem> list = query.list();

        if(list != null && list.size() > 0){
            return list;
        }
        return null;
    }

    @Override
    public Expense getExpenseById(Integer expenseId) throws Exception {
        String HQL = "FROM Expense WHERE id =:expenseId";
        HQL += " ORDER BY id asc";

        Query query = getCurrentSession().createQuery(HQL);

        if(!StringUtils.isEmpty(expenseId)){
            query.setParameter("expenseId", expenseId);
        }
        List<Expense> list = query.list();

        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public Double getTotalExpenseAmount(Date fromDate, Date toDate) throws Exception {
        String HQL = "select sum(amount) from Expense";

        if(fromDate != null && toDate != null){
            HQL += " WHERE date >=:fromDate AND date<=:toDate";
        }

        Query query = getCurrentSession().createQuery(HQL);

        if(fromDate != null && toDate != null){
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);
        }
        return (Double)query.uniqueResult();
    }

    @Override
    public List<Expense> getAllExpenseList(Date fromDate, Date toDate) throws Exception {
        String HQL = "FROM Expense";

        if(fromDate != null && toDate != null){
            HQL += " WHERE date >=:fromDate AND date<=:toDate";
        }

        HQL += " ORDER BY id asc";

        Query query = getCurrentSession().createQuery(HQL);

        if(fromDate != null && toDate != null){
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);
        }

        List<Expense> list = query.list();

        if(list != null && list.size() > 0){
            return list;
        }
        return null;
    }

    @Override
    public List<ShareHolder> getAllShareHolderList() throws Exception {
        String HQL = "FROM ShareHolder";
        HQL += " ORDER BY name asc";

        Query query = getCurrentSession().createQuery(HQL);

        List<ShareHolder> list = query.list();

        if(list != null && list.size() > 0){
            return list;
        }
        return null;
    }
}

