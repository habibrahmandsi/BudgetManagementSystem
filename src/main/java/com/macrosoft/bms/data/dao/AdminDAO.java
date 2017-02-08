package com.macrosoft.bms.data.dao;

import com.macrosoft.bms.data.model.Deposit;
import com.macrosoft.bms.data.model.ShareHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
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
}
