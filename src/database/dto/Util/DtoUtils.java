package database.dto.Util;

import database.Database;
import database.dto.FileInfo;
import database.dto.User;
import database.dto.UserFileInfo;
import database.dto.UserKey;

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
                result.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }



    public static List<UserKey> convertToUserKey(ResultSet rs) {
        List<UserKey> result = new ArrayList<>();
        if(rs == null) {
            return result;
        }
        try {
            while (rs.next()) {
                UserKey uk = new UserKey();
                uk.setId(rs.getLong("id"));
                uk.setPrivateKey(rs.getString("private_key"));
                uk.setPublicKey(rs.getString("public_key"));
                uk.setUserId(rs.getLong("user_id"));
                result.add(uk);
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
                ufi.setUserKeyId(rs.getLong("user_key_id"));
                ufi.setHashKey(rs.getString("hash_key"));
                result.add(ufi);
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
