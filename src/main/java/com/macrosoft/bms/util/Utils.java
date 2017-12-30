package com.macrosoft.bms.util;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import jersey.repackaged.com.google.common.collect.Lists;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Utils {

    public static List<JSONObject> getJsonObjectListFromJSONArray(JSONArray jsonArray) {
        List<JSONObject> jsonList = null;
        if (jsonArray.size() > 0) {
            jsonList = Lists.newArrayListWithExpectedSize(jsonArray.size());
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonList.add(jsonArray.getJSONObject(i));
            }
        }
        return jsonList;
    }

    public static Boolean isNullJsonElement(JSONObject jsonObject, String element) {
        return jsonObject.containsKey(element) == false || jsonObject.getString(element) == null || "null".equals(jsonObject.getString(element));
    }

    public static String getDateStringForSuffix(Date date, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static String getLoggedInUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public static boolean matchPassword(String plainPassword, String encryptedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(plainPassword, encryptedPassword);
    }


    public static String getMessageBundlePropertyValue(String key) {
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource(Constants.MESSAGE_FILE_PATH));
            return props.getProperty(key);
        } catch (Exception ex) {
            return "";
        }
    }

    public static void setGreenMessage(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        List greenMessages = (List) session.getAttribute("greenMessage");
        if (greenMessages == null) {
            greenMessages = new ArrayList();
        }
        greenMessages.add(message);
        session.setAttribute("greenMessage", greenMessages);
    }


    public static void setErrorMessage(HttpServletRequest request, String error) {
        HttpSession session = request.getSession();
        List redMessages = (List) session.getAttribute("redMessage");
        if (redMessages == null) {
            redMessages = new ArrayList();
        }
        redMessages.add(error);
        session.setAttribute("redMessage", redMessages);
    }

    public static void setBlueMessage(HttpServletRequest request, String error) {
        HttpSession session = request.getSession();
        List redMessages = (List) session.getAttribute("blueMessage");
        if (redMessages == null) {
            redMessages = new ArrayList();
        }
        redMessages.add(error);
        session.setAttribute("blueMessage", redMessages);
    }

    public static Object getGreenMessage(HttpServletRequest request) {
        if (request.getSession().getAttribute("greenMessage") != null) {
            Object message = request.getSession().getAttribute("greenMessage");
            request.getSession().removeAttribute("greenMessage");
            return message;
        }
        return "";
    }

    public static Object getErrorMessage(HttpServletRequest request) {
        if (request.getSession().getAttribute("redMessage") != null) {
            Object message = request.getSession().getAttribute("redMessage");
            request.getSession().removeAttribute("redMessage");
            return message;
        }
        return "";
    }

    public static Object getBlueMessage(HttpServletRequest request) {
        if (request.getSession().getAttribute("blueMessage") != null) {
            Object message = request.getSession().getAttribute("blueMessage");
            request.getSession().removeAttribute("blueMessage");
            return message;
        }
        return "";
    }

    public static String getStringFromFile(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        int x = fis.available();
        byte b[] = new byte[x];
        fis.read(b);
        fis.close();
        return new String(b);
    }


    public static String getApplicationPropertyValue(String key) {
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource(Constants.APPLICATION_CONFIGURATION_FILE_PATH));
            return props.getProperty(key);
        } catch (Exception ex) {
            return "";
        }
    }

    public static java.util.Date getTodaysDate() {
        return new java.util.Date();
    }

    public static String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() +
                ":" + request.getServerPort() +
                request.getContextPath();
    }

    public static Long getTimeMilliseconds(Date date){
        if(date == null)
            return 0l;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    public static String getUniqueKey(){
        return UUID.randomUUID().toString();
    }

    public static Double getTwoDigitAfterDecimal(Double value){
        return Double.parseDouble(new DecimalFormat("##.##").format(value));
    }
}
