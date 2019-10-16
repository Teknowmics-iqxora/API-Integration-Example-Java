package com.teknowmics.smartdocs.upload;

/**
 *
 * @author administrator
 */
public class UploadDocument {

    public static void main(String[] args) {
        try {
            //Smartdocs Login
            if (!CommonUtil.login()) {
                CommonUtil.stopExec();
            }
            System.out.println("Login is done successfully...");
            //Create project
            if (!CommonUtil.createProject()) {
                CommonUtil.stopExec();
            }
            System.out.println("Project has created successfully...");
            //Create project folder
            if (!CommonUtil.createFolder()) {
                CommonUtil.stopExec();
            }
            System.out.println("Folder has created successfully...");
            //Upload the project document
            if (!CommonUtil.upload()) {
                CommonUtil.stopExec();
            }
            System.out.println("Document has uploaded successfully...");
        } catch (Exception ex) {
            System.out.println("Error :" + ex);
            CommonUtil.stopExec();
        }
    }

}
