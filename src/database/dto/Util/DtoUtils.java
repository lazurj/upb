package database.dto.Util;

import database.Database;
import database.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub on 6.11.2019.
 */
public class DtoUtils {

    public static List<User> convertToUser(ResultSet rs) {
        List<User> result = new ArrayList<>();
        if(rs == null) {
            return result;
        }
        try {
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setPassword(rs.getString("password"));
                u.setUserName(rs.getString("userName"));
                u.setSalt(rs.getString("salt"));
                u.setEmail(rs.getString("email"));
                u.setPrivateKey(rs.getString("private_key"));
                u.setPublicKey(rs.getString("public_key"));
                result.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<FileInfo> convertToFileInfo(ResultSet rs) {
        List<FileInfo> result = new ArrayList<>();
        if(rs == null) {
            return result;
        }
        try {
            while (rs.next()) {
                FileInfo fi = new FileInfo();
                fi.setId(rs.getLong("id"));
                fi.setFileName(rs.getString("file_name"));
                fi.setMac(rs.getString("mac"));
                result.add(fi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<UserFileInfo> convertToUserFileInfo(ResultSet rs) {
        List<UserFileInfo> result = new ArrayList<>();
        if(rs == null) {
            return result;
        }
        try {
            while (rs.next()) {
                UserFileInfo ufi = new UserFileInfo();
                ufi.setId(rs.getLong("id"));
                ufi.setUserId(rs.getLong("user_id"));
                ufi.setFileInfoId(rs.getLong("file_info_id"));
                ufi.setHashKey(rs.getString("hash_key"));
                result.add(ufi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Comment> convertComment(ResultSet rs) {
        List<Comment> result = new ArrayList<>();
        if(rs == null) {
            return result;
        }
        try {
            while (rs.next()) {
                Comment c = new Comment();
                c.setId(rs.getLong("id"));
                c.setFileId(rs.getLong("file_id"));
                c.setUserId(rs.getLong("user_id"));
                c.setText(rs.getString("text"));
                c.setCreateDate(rs.getDate("create_date"));
                result.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Request> convertRequest(ResultSet rs) {
        List<Request> result = new ArrayList<>();
        if(rs == null) {
            return result;
        }
        try {
            while (rs.next()) {
                Request r = new Request();
                r.setId(rs.getLong("id"));
                r.setFileId(rs.getLong("file_id"));
                r.setRequestUserId(rs.getLong("user_id"));
                r.setOwnerId(rs.getLong("owner_id"));
                r.setCreateDate(rs.getDate("create_date"));
                result.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<File> getUserFiles(List<UserFileInfo> userFiles) {
        List<File> result = new ArrayList<>();
        for (UserFileInfo userFile : userFiles) {
            result.add(userFile.getFileInfo().getFile(userFile.getUser().getUserName()));
        }
        return result;
    }


    public static UserFileInfo getUserFileByName(List<UserFileInfo> userFiles, String fileName) {
        for (UserFileInfo userFile : userFiles) {
            if(fileName.equals(userFile.getFileInfo().getFileName())) {
                return userFile;
            }
        }
        return null;
    }

    public static User getLoggedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Long userId = (Long)session.getAttribute("loggedUser");
        if(userId == null ) {
            return null;
        }
        return Database.findUserById(userId);
    }
}
