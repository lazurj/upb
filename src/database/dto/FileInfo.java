package database.dto;

/**
 * Created by Jakub on 6.11.2019.
 */
public class FileInfo {

    private Long id;
    private String fileName;
    private String mac;

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
}
