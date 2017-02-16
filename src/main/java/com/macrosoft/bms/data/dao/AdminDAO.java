package com.macrosoft.bms.data.dao;

import com.macrosoft.bms.data.model.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AdminDAO extends GenericTableDao<Object, Serializable>{
    void saveAuditTrial(String message,HttpServletRequest request) throws Exception;
    ShareHolder getShareHolderById(Integer id) throws Exception;
    Long getShareHolderDataSize() throws Exception;
    Map<String, Object> getShareHolders(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception;
    Long getDepositDataSize() throws Exception;
    Map<String, Object> getDeposits(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception;
    Deposit getDepositById(Integer id) throws Exception;
    Map<String, Object> getShareHolderForAutoComplete(String sortColName, String sortType, String searchKey) throws Exception;
    Double getTotalDepositAmount(Integer shareHolderId) throws Exception;
    List<Map> getDepositListByShareHolderId(Integer shareHolderId) throws Exception;
    Installment getInstallmentById(Integer installmentId) throws Exception;
    List<Map> getInstallment() throws Exception;
    List<ExpenseItem> getAllExpenseItem() throws Exception;
    Expense getExpenseById(Integer expenseId) throws Exception;
    Double getTotalExpenseAmount(Date fromDate, Date toDate) throws Exception;
    List<Expense> getAllExpenseList(Date fromDate, Date toDate) throws Exception;
    List<ShareHolder> getAllShareHolderList() throws Exception;
    List<Map> getShareHolderWiseTotalDeposit(Integer installmentId, Integer shareHolderId) throws Exception;
    User getUserByUserName(String userName) throws Exception;
}
