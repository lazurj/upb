package database;

import database.dto.FileInfo;
import database.dto.User;
import database.dto.UserFileInfo;
import database.dto.UserKey;
import database.dto.Util.DtoUtils;
import webapp.utils.AsyncCrypto;
import webapp.utils.CryptoUtils;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;
import java.util.List;

import static webapp.utils.AsyncCrypto.HMAC_SHA256;

/**
 * Created by Jakub on 6.11.2019.
 */
public class Database {

    private static synchronized Connection getConnection () {
        try {
            Class.forName("org.sqlite.JDBC");
            //TODO prepis si cestu
            return DriverManager.getConnection("jdbc:sqlite:C:/Users/Rastik/Desktop/UPBsample.db");
            //return DriverManager.getConnection("jdbc:sqlite:C:/Users/Domin/OneDrive/Plocha/upb-master/sample.db");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createNewDatabase(String fileName) {

        try {
            Statement statement = getConnection().createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("create table if not exists user (id integer PRIMARY KEY AUTOINCREMENT, username string NOT NULL, password string NOT NULL, salt string NOT NULL, email string NOT NULL)");
            statement.executeUpdate("create table if not exists file_info (id integer PRIMARY KEY AUTOINCREMENT, file_name string NOT NULL, mac string NOT NULL)");
            statement.executeUpdate("create table if not exists user_file (id integer PRIMARY KEY AUTOINCREMENT, file_info_id integer NOT NULL, user_id integer NOT NULL, user_key_id integer NOT NULL, hash_key string NOT NULL, FOREIGN KEY(user_id) REFERENCES user(id),FOREIGN KEY(file_info_id) REFERENCES file_info(id), FOREIGN KEY(user_key_id) REFERENCES user_key(id))");
            statement.executeUpdate("create table if not exists user_key (id integer PRIMARY KEY AUTOINCREMENT, user_id integer NOT NULL, private_key string NOT NULL, public_key string NOT NULL, FOREIGN KEY(user_id) REFERENCES user(id))");

            //statement.executeUpdate("insert into user values(1,'leo', 'password', 'salt', 'awd', 'awd','asdf')");
            //statement.executeUpdate("insert into user values(2,'admin', 'EL5h9EpBFGjo9lr3k3K7uBlJ7g1oQ4O/9bXP6AlIx+0=', 'salt2', 'awd', 'awd','asdf')");
            //statement.executeUpdate("insert into file_info values(1,'leo', 'password', 'salt', 'awd', 'awd','asdf')");
            //statement.executeUpdate("insert into user values(2, 'yui')");
            /*ResultSet rs = statement.executeQuery("select * from person");
            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }*/
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (getConnection()!= null)
                    getConnection().close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    public static String login(String userName, String pwd) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("select * from user where name='" +userName +"' and password = '" + pwd +"' ");
            while (rs.next()) {
                // read the result set
                return rs.getString("private_key");
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    public static User findUserByName(String userName) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from user where username = ?");
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            List<User> users = DtoUtils.convertToUser(rs);
            return !users.isEmpty() ? users.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User findUserById(Long id) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from user where id = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            List<User> users = DtoUtils.convertToUser(rs);
            return !users.isEmpty() ? users.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FileInfo findFileInfoById(Long id) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from file_info where id = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            List<FileInfo> files = DtoUtils.convertToFileInfo(rs);
            return !files.isEmpty() ? files.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserKey findUserKeyById(Long id) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from user_key where id = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            List<UserKey> keys = DtoUtils.convertToUserKey(rs);
            return !keys.isEmpty() ? keys.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserKey findMaxUserKeyByUserId(Long userId) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from user_key where user_id = ? order by id desc");
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            List<UserKey> keys = DtoUtils.convertToUserKey(rs);
            return !keys.isEmpty() ? keys.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FileInfo findFileInfoByName(String fileName) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from file_info where file_name = ?");
            ps.setString(1, fileName);
            ResultSet rs = ps.executeQuery();
            List<FileInfo> files = DtoUtils.convertToFileInfo(rs);
            return !files.isEmpty() ? files.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<UserFileInfo> findUserFilesByUserId(Long userId) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from user_file where user_id= ?");
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            List<UserFileInfo> results = DtoUtils.convertToUserFileInfo(rs);
            for(UserFileInfo result : results) {
                result.setUser(findUserById(result.getUserId()));
                result.setFileInfo(findFileInfoById(result.getFileInfoId()));
                result.setUserKey(findUserKeyById(result.getUserKeyId()));
            }
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long insertUser(String username, String password, String email){
        Long userId = null;
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO user (username, password, email, salt) VALUES (?,?,?,?)",  Statement.RETURN_GENERATED_KEYS);

            String salt = CryptoUtils.generateRandomKey(18);
            byte[] hashPassword = AsyncCrypto.hmacDigestBytes(password,salt,HMAC_SHA256);
            String hashPassordString = Base64.getEncoder().encodeToString(hashPassword);

            ps.setString(1,username);
            ps.setString(2,hashPassordString);
            ps.setString(3,email);
            ps.setString(4,salt);
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    userId = generatedKeys.getLong(1);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    public static Long insertUserKey(Long userId){
        Long id = null;
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO user_key (user_id, public_key, private_key) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);

            AsyncCrypto asyncCrypto = new AsyncCrypto();
            ps.setLong(1,userId);
            ps.setString(2,asyncCrypto.PublicKeyString());
            ps.setString(3,asyncCrypto.PrivateKeyString());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static Long insertFile(String fileName, String mac){
        Long fileId = null;
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO file_info (file_name, mac) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,fileName);
            ps.setString(2,mac);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                fileId = generatedKeys.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fileId;
    }

    public static Long insertUserFile(Long userId, Long fileId, Long key_id, String hashKey) {
        Long id = null;
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO user_file (file_info_id, user_id, user_key_id, hash_key) VALUES (?,?,?,?)");
            ps.setLong(1, fileId);
            ps.setLong(2, userId);
            ps.setLong(3, key_id);
            ps.setString(4, hashKey);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
             id = generatedKeys.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static List<User> findOtherUsers(Long Id) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from user where id != ?");
            ps.setLong(1, Id);
            ResultSet rs = ps.executeQuery();
            List<User> results = DtoUtils.convertToUser(rs);
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
