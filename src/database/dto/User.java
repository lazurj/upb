package database.dto;

import webapp.FileUploadHandler;

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
    private String privateKey;
    private String publicKey;


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

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getDirectory() {
        if(directory == null) {
            directory = FileUploadHandler.UPLOAD_DIRECTORY;
        }
        return directory;
    }

}
