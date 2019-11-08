package database.dto;

import webapp.FileUploadHandler;

import java.io.File;

/**
 * Created by Jakub on 6.11.2019.
 */
public class User {

    private Long id;
    private String userName;
    private String password;
    private String salt;
    private String email;
    private String directory;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDirectory() {
        if(directory == null) {
            directory = FileUploadHandler.UPLOAD_DIRECTORY + File.separator +this.userName;
        }
        return directory;
    }

}
