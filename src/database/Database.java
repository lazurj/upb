package database;

import database.dto.User;
import database.dto.UserFileInfo;
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
            return DriverManager.getConnection("jdbc:sqlite:C:/Users/Rastik/Desktop/UPB/sample.db");
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

            statement.executeUpdate("create table if not exists user (id integer PRIMARY KEY AUTOINCREMENT, username string NOT NULL, password string NOT NULL, salt string NOT NULL, private_key string NOT NULL, public_key string NOT NULL, email string NOT NULL)");
            statement.executeUpdate("create table if not exists file_info (id integer PRIMARY KEY AUTOINCREMENT, file_name string NOT NULL, mac string NOT NULL)");
            statement.executeUpdate("create table if not exists user_file (id integer PRIMARY KEY AUTOINCREMENT, file_info_id integer NOT NULL, user_id integer NOT NULL,hash_key string NOT NULL, FOREIGN KEY(user_id) REFERENCES user(id),FOREIGN KEY(file_info_id) REFERENCES file_info(id))");
            statement.executeUpdate("insert into user values(1,'leo', 'password', 'salt', 'awd', 'awd','asdf')");
            statement.executeUpdate("insert into user values(2,'admin', 'EL5h9EpBFGjo9lr3k3K7uBlJ7g1oQ4O/9bXP6AlIx+0=', 'salt2', 'awd', 'awd','asdf')");
 //         statement.executeUpdate("insert into user values(2, 'yui')");
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

    public static List<UserFileInfo> findUserFilesByUserId(Long userId) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from user_file_info uf)" +
                    " join user u on uf.user_id = u.id" +
                    " join file_info fi on uf.file_id= fi.id" +
                    " where userId= ?");
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            return DtoUtils.convertToUserFileInfo(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insertUser(String username, String password, String email){
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO user (username, password, email, salt, public_key, private_key) VALUES (?,?,?,?,?,?)");



            String salt = CryptoUtils.generateRandomKey(18);
            byte[] hashPassword = AsyncCrypto.hmacDigestBytes(password,salt,HMAC_SHA256);
            String hashPassordString = Base64.getEncoder().encodeToString(hashPassword);

            AsyncCrypto asyncCrypto = new AsyncCrypto();



            ps.setString(1,username);
            ps.setString(2,hashPassordString);
            ps.setString(3,email);
            ps.setString(4,salt);
            ps.setString(5,asyncCrypto.PublicKeyString());
            ps.setString(6,asyncCrypto.PrivateKeyString());
            ps.executeUpdate();



        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }


}
