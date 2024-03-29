package database.dto;

/**
 * Created by Jakub on 6.11.2019.
 */
public class UserFileInfo {

    Long id;
    Long userId;
    Long fileInfoId;
    User user;
    FileInfo fileInfo;
    String hashKey;
    Boolean ownerFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFileInfoId() {
        return fileInfoId;
    }

    public void setFileInfoId(Long fileInfoId) {
        this.fileInfoId = fileInfoId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    public Boolean getOwnerFlag() {
        return ownerFlag;
    }

    public void setOwnerFlag(Boolean ownerFlag) {
        this.ownerFlag = ownerFlag;
    }
}
