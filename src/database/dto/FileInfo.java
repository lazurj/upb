package database.dto;

import webapp.FileUploadHandler;

import java.io.File;
import java.util.List;

/**
 * Created by Jakub on 6.11.2019.
 */
public class FileInfo {

    private Long id;
    private String fileName;
    private String mac;
    private List<Comment> commentList;

    private File file;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public File getFile() {
        if(file == null) {
            file = new File(FileUploadHandler.UPLOAD_DIRECTORY +File.separator +this.fileName);
        }
        return file;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}
