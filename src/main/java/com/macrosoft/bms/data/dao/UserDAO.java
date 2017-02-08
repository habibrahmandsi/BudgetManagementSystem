package com.macrosoft.bms.data.dao;

import com.macrosoft.bms.data.model.User;

import java.util.List;
import java.util.Map;

public interface UserDAO {
    User getUserByUserName(String username);
    List<Map> getUserList();
    User getUserById(Integer userId);
    Long getUserDataSize();
    Map<String, Object> getUsers(Integer start, Integer length, String sortColName, String sortType, String searchKey);
}