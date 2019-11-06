package database.dto.Util;

import database.dto.User;

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
                result.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
