package com.teknowmics.smartdocs.upload;

import org.apache.commons.lang.RandomStringUtils;

/**
 *
 * @author administrator
 */
public class Constants {
   
    private Constants() {

    }

    public static final String LOGIN_NAME = "login_name";//User crendentials to login
    
    public static final String PASSWORD = "password";//User crendentials to login
    
    public static final String PROTOCOL ="protocol";//host protocol
    
    public static final String HOST = "host_name"; // Need to use host name
    
    public static final String COOKIE_STR = "Cookie";

    public static final String SUCCESS_CODE = "SD200";

    public static final String PROJECT_NAME = RandomStringUtils.randomAlphanumeric(10).toLowerCase();
    
    public static final String FOLDER_NAME = RandomStringUtils.randomAlphanumeric(10).toLowerCase();
    
    public static final String SOURCE_PATH = "sourcePath";//Source path the system local folder from where will upload document
    
    public static final String LOGIN_URL = "/server/user/api/sd6.0/auth/login.xml";
    
    public static final String PROJECT_CREATE_URL = "/server/user/api/sd6.0/project/create.xml";

    public static final String PROJECT_CREATE_FOLDER_URL = "/server/user/api/v6.0/project/folder/add.xml";
    
    public static final String PROJECT_CREATE_FILE_URL = "/server/user/api/v6.0/project/file/create.xml";
    
    
}
