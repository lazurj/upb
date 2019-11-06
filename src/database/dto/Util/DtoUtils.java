package database.dto.Util;

import database.dto.FileInfo;
import database.dto.User;
import database.dto.UserFileInfo;

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
                u.setPrivateKey(rs.getString("private_key"));
                u.setPublicKey(rs.getString("public_key"));
                u.setSalt(rs.getString("salt"));
                u.setEmail(rs.getString("email"));
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
                /*ufi.setFileName(rs.getString("file_name"));
                ufi.setMac(rs.getString("mac"));*/
                result.add(ufi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
