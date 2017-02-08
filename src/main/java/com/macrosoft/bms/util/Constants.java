package com.macrosoft.bms.util;


import java.io.File;

public class Constants {
	public static final String SIMPLE_DATE_FORMAT= "MM/dd/yyyy HH:mm:ss";
	public static final String AJAX_COMMON_REQUEST_HEADER= "X-Requested-With";
    public static final String sEcho= "sEcho";
    public static final String iSortCOL= "order[0][column]";
    public static final String sSearch= "search[value]";
    public static final String sortType= "order[0][dir]";
    public static final String IDISPLAY_LENGTH = "length";
    public static final String IDISPLAY_START= "start";
    public static final String MESSAGE_FILE_PATH = "/messages_en.properties";
    public static final String APPLICATION_CONFIGURATION_FILE_PATH = "/application.properties";
    public static final String CONFIG_CONFIGURATION_FILE_PATH = System.getProperty("user.dir")+ File.separator +"config.properties";
    public static final String DATE_FORMAT="dd/MM/yyyy";
    public static final String UPLOAD_BASE_DIR="uploadFiles";
    public static final String UPLOAD_EMP_PHOTO_DIR="shareHolderPhoto";
    public static final int THUMBNAIL_IMAGE_SIZE=100;

}
