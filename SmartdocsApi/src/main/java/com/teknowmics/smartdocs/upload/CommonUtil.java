package com.teknowmics.smartdocs.upload;

import com.teknowmics.smartdocs.xml.Header;
import com.teknowmics.smartdocs.xml.Item;
import com.teknowmics.smartdocs.xml.KeyValue;
import com.teknowmics.smartdocs.xml.Result;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author administrator
 */
public class CommonUtil {

    public static final Queue<File> FILE_QUEUE = new ConcurrentLinkedQueue<>();

    public static String cookie;

    public static String projectId;

    public static String folderId;

    /*
     * This is used to user login to smartdocs
     * loginName and passpword are credentials of user to login
     * If login is done successfully then get SD200 success code 
     */
    public static boolean login() throws Exception {
        Map<String, String> paramaMap = new HashMap<>();
        paramaMap.put("loginName", Constants.LOGIN_NAME);
        paramaMap.put("password", Constants.PASSWORD);
        Result response = sendPost(Constants.LOGIN_URL, paramaMap, false);
        if (Constants.SUCCESS_CODE.equalsIgnoreCase(response.getHeader().getCode())) {
            return true;
        }
        throw new RuntimeException("Unable to user login");
    }

    /*
     * This is used to create new project in smartdocs
     * Project name and project end date are mandatory input parameter
     * If project is created successfully then get SD200 success code
     * Get projectId from smartdocs response
     */
    public static boolean createProject() throws Exception {
        Map<String, String> paramaMap = new HashMap<>();
        paramaMap.put("projectName", Constants.PROJECT_NAME);
        LocalDateTime now = LocalDateTime.now().plusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String projectEndDate = now.format(formatter);
        paramaMap.put("endDate", projectEndDate);
        Result response = sendPost(Constants.PROJECT_CREATE_URL, paramaMap, true);
        Header header = response.getHeader();
        if (Constants.SUCCESS_CODE.equalsIgnoreCase(header.getCode())) {
            Item folderItem = response.getBody().getData().getItems().getItem().get(0);
            KeyValue idKeyValue = folderItem.getKeyValue().get(0);
            projectId = idKeyValue.getValue();
            return true;
        } else {
            throw new RuntimeException("Unable to create project");
        }
    }

    /*
     * This is used to create new folder in smartdocs
     * Project id and project folder name are mandatory input parameter
     * If project folder is created successfully then get SD200 success code
     * Get folderId from smartdocs response
     */
    public static boolean createFolder() throws Exception {
        Map<String, String> paramaMap = new HashMap<>();
        paramaMap.put("name", Constants.FOLDER_NAME);
        paramaMap.put("projectId", projectId);
        Result response = sendPost(Constants.PROJECT_CREATE_FOLDER_URL, paramaMap, true);
        Header header = response.getHeader();
        if (Constants.SUCCESS_CODE.equalsIgnoreCase(header.getCode())) {
            Item folderItem = response.getBody().getData().getItems().getItem().get(0);
            KeyValue idKeyValue = folderItem.getKeyValue().get(0);
            folderId = idKeyValue.getValue();
            return true;
        }
        throw new RuntimeException("Unable to create folder");
    }

    /*
     * This is used to upload document to smartdocs
     * Project file as multipart file and project id and folder id are mandatory input parameter
     * If document upload successfully then get SD200 success code
     */
    public static boolean upload() throws Exception {
        String url = Constants.PROTOCOL + "://" + Constants.HOST + Constants.PROJECT_CREATE_FILE_URL;
        HttpPost httpPost = new HttpPost(url);
        setPostSessionHeaders(httpPost);
        File root = new File(Constants.SOURCE_PATH);
        Collection<File> files = FileUtils.listFiles(root, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        FILE_QUEUE.addAll(files);
        StringBuilder result = new StringBuilder();
        while (!FILE_QUEUE.isEmpty()) {
            File tempFile = FILE_QUEUE.poll();
            FileBody fileBody = new FileBody(new File(tempFile.getAbsolutePath()), ContentType.MULTIPART_FORM_DATA);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("projectFile", fileBody);
            builder.addPart("projectId", new StringBody(projectId, ContentType.TEXT_PLAIN));
            builder.addPart("folderId", new StringBody(folderId, ContentType.TEXT_PLAIN));
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            CloseableHttpResponse response = executeReq(httpPost);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        }
        Result response = getResultObject(result.toString());
        Header header = response.getHeader();
        if (Constants.SUCCESS_CODE.equalsIgnoreCase(header.getCode())) {
            return true;
        }
        throw new RuntimeException("Unable to upload file to smartdocs");
    }

    /*
    * This is used to stop process excution
    */
    public static void stopExec() {
        System.exit(1);
    }

    /*
    * This is used to set http request header
    */
    private static HttpPost setPostSessionHeaders(HttpPost post) {
        post.setHeader(Constants.COOKIE_STR, cookie);
        return post;
    }

    public static CloseableHttpResponse executeReq(HttpPost httpPost) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        return client.execute(httpPost);
    }

    public static Result sendPost(String url, Map<String, String> parameters, boolean setCookie) throws Exception {
        url = Constants.PROTOCOL + "://" + Constants.HOST + url;
        StringBuilder paramBuilder = new StringBuilder();
        if (parameters != null && !parameters.isEmpty()) {
            Set<String> keySet = parameters.keySet();
            for (String name : keySet) {
                if (paramBuilder.length() > 1) {
                    paramBuilder.append("&");
                }
                String value = parameters.get(name);
                if (value == null) {
                    value = "";
                } else {
                    value = URLEncoder.encode(value, "UTF-8");
                }
                paramBuilder.append(name).append("=").append(value);
            }
        }
        URL obj = new URL(url);
        HttpURLConnection con = null;
        if ("http".equalsIgnoreCase(Constants.PROTOCOL)) {
            con = (HttpURLConnection) obj.openConnection();
        } else {
            con = (HttpsURLConnection) obj.openConnection();
        }

        con.setRequestMethod("POST");
        con.setDoOutput(true);
        if (setCookie) {
            con.setRequestProperty(Constants.COOKIE_STR, cookie);
        }

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            if (paramBuilder.length() > 1) {
                wr.writeBytes(paramBuilder.toString());
            }
            wr.flush();
        }

        int responseCode = con.getResponseCode();
        if (HttpsURLConnection.HTTP_OK != responseCode) {
            throw new RuntimeException("Http/s call failed with Response Code: " + responseCode);
        }

        if (cookie == null) {
            cookie = con.getHeaderField("Set-Cookie");
        }

        StringBuilder response;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return getResultObject(response.toString());
    }

    public static Result getResultObject(String xmlResponse) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Result.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(xmlResponse);
        Result result = (Result) jaxbUnmarshaller.unmarshal(reader);
        return result;
    }

}
