package database;

import database.dto.*;
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

    public static synchronized Connection getConnection () {
        try {
            Class.forName("org.sqlite.JDBC");
            //TODO prepis si cestu
            return DriverManager.getConnection("jdbc:sqlite:C:/Users/Jakub/Desktop/UPB/sample3.db");
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

            statement.executeUpdate("create table if not exists user (id integer PRIMARY KEY AUTOINCREMENT, username string NOT NULL, password string NOT NULL, salt string NOT NULL, email string NOT NULL, private_key string NOT NULL, public_key string NOT NULL)");
            statement.executeUpdate("create table if not exists file_info (id integer PRIMARY KEY AUTOINCREMENT, file_name string NOT NULL, mac string NOT NULL)");
            statement.executeUpdate("create table if not exists user_file (id integer PRIMARY KEY AUTOINCREMENT, file_info_id integer NOT NULL, user_id integer NOT NULL, hash_key string NOT NULL, owner_flag boolean  NOT NULL, FOREIGN KEY(user_id) REFERENCES user(id),FOREIGN KEY(file_info_id) REFERENCES file_info(id))");
            statement.executeUpdate("create table if not exists comment (id integer PRIMARY KEY AUTOINCREMENT, text string, file_id integer NOT NULL, user_id integer not null, create_date date , FOREIGN KEY(user_id) REFERENCES user(id), FOREIGN KEY(file_id) REFERENCES file_info(id))");
            statement.executeUpdate("create table if not exists request (id integer PRIMARY KEY AUTOINCREMENT, file_id integer NOT NULL, owner_id integer not null, user_id integer not null, create_date date, active_flag boolean, FOREIGN KEY(owner_id) REFERENCES user(id), FOREIGN KEY(user_id) REFERENCES user(id), FOREIGN KEY(file_id) REFERENCES file_info(id))");

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

    public static List<Comment> findCommentByFileId(Long fileId) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from comment where file_id = ?");
            ps.setLong(1, fileId);
            ResultSet rs = ps.executeQuery();
            List<Comment> comments = DtoUtils.convertToComment(rs);
            for(Comment c : comments) {
                c.setUser(findUserById(c.getUserId()));
            }
            return comments;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Request> findRequestsByFileId(Long fileId) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from request where file_id = ? and active_flag = 1");
            ps.setLong(1, fileId);
            ResultSet rs = ps.executeQuery();
            List<Request> requests = DtoUtils.convertToRequest(rs);
            for(Request req : requests) {
                req.setRequestUser(findUserById(req.getRequestUserId()));
            }
            return requests;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Request findRequestsByFileIdAndUserId(Long fileId, Long userId) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from request where file_id = ? and user_id = ? and active_flag = 1");
            ps.setLong(1, fileId);
            ps.setLong(2, userId);
            ResultSet rs = ps.executeQuery();
            List<Request> requests = DtoUtils.convertToRequest(rs);
            for(Request req : requests) {
                req.setRequestUser(findUserById(req.getRequestUserId()));
            }
            return requests != null && !requests.isEmpty() ? requests.get(0) : null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Request findRequestsById(Long id) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from request where id = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            List<Request> requests = DtoUtils.convertToRequest(rs);
            for(Request req : requests) {
                req.setRequestUser(findUserById(req.getRequestUserId()));
            }
            return requests != null && !requests.isEmpty() ? requests.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<FileInfo> getAllFileInfo() {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from file_info");
            ResultSet rs = ps.executeQuery();
            List<FileInfo> result = DtoUtils.convertToFileInfo(rs);
            for(FileInfo f : result) {
                f.setCommentList(findCommentByFileId(f.getId()));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<FileInfo> getFilesByOwnerFlag(Long userId, boolean isOwner) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select fi.* from file_info fi join user_file uf on uf.file_info_id = fi.id where uf.user_id = ? and uf.owner_flag = ?");
            ps.setLong(1,userId);
            ps.setBoolean(2, isOwner);
            ResultSet rs = ps.executeQuery();
            List<FileInfo> result = DtoUtils.convertToFileInfo(rs);
            for(FileInfo f : result) {
                f.setCommentList(findCommentByFileId(f.getId()));
            }
            return result;
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
            }
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static UserFileInfo findUserFilesByUserIdAndFileId(Long userId, Long fileId) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from user_file where user_id= ? and file_info_id = ?");
            ps.setLong(1, userId);
            ps.setLong(2, fileId);
            ResultSet rs = ps.executeQuery();
            List<UserFileInfo> results = DtoUtils.convertToUserFileInfo(rs);
            for(UserFileInfo result : results) {
                result.setUser(findUserById(result.getUserId()));
                result.setFileInfo(findFileInfoById(result.getFileInfoId()));
            }
            return results != null && !results .isEmpty() ? results.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static Long insertUser(String username, String password, String email){
        Long userId = null;
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO user (username, password, email, salt, private_key, public_key) VALUES (?,?,?,?,?,?)",  Statement.RETURN_GENERATED_KEYS);

            String salt = CryptoUtils.generateRandomKey(18);
            byte[] hashPassword = AsyncCrypto.hmacDigestBytes(password,salt,HMAC_SHA256);
            String hashPassordString = Base64.getEncoder().encodeToString(hashPassword);

            ps.setString(1,username);
            ps.setString(2,hashPassordString);
            ps.setString(3,email);
            ps.setString(4,salt);
            AsyncCrypto asyncCrypto = null;
            try {
                asyncCrypto = new AsyncCrypto();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            ps.setString(5,asyncCrypto.PrivateKeyString());
            ps.setString(6,asyncCrypto.PublicKeyString());
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

    public static Long insertUserFile(Long userId, Long fileId, String hashKey, boolean isOwner) {
        Long id = null;
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO user_file (file_info_id, user_id, owner_flag, hash_key) VALUES (?,?,?,?)");
            ps.setLong(1, fileId);
            ps.setLong(2, userId);
            ps.setBoolean(3,isOwner);
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

    public static Long insertComment(Long fileId, Long userId, String text){
        Long id = null;
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO comment (user_id, file_id, text, create_date) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1,userId);
            ps.setLong(2,fileId);
            ps.setString(3,text);
            ps.setDate(4, new Date(new java.util.Date().getTime()));
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

    public static Long insertRequest(Long fileId, Long userId, Long ownerId){
        Long id = null;
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO request (user_id, file_id, owner_id ,create_date, active_flag) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1,userId);
            ps.setLong(2,fileId);
            ps.setLong(3,ownerId);
            ps.setDate(4, new Date(new java.util.Date().getTime()));
            ps.setBoolean(5, true);
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

    public static void deactivateRequest(Long requestId) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE request SET active_flag = false where id = ?");
            ps.setLong(1, requestId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public static User findFileOwner(Long fileId) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from user_file where file_info_id = ? and owner_flag = 1");
            ps.setLong(1, fileId);
            ResultSet rs = ps.executeQuery();
            List<UserFileInfo> results = DtoUtils.convertToUserFileInfo(rs);
            if(results.size() == 1) {
                return findUserById(results.get(0).getUserId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void DeleteRowFromUserFile(Long id)
    {
        try
        {
            PreparedStatement ps = getConnection().prepareStatement("DELETE FROM user_file WHERE id = ?");
            ps.setLong(1,id);
            ps.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public static UserFileInfo findUserFileByUserIdandFile(Long userId,Long file_info_id) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from user_file where user_id= ? and file_info_id = ?");
            ps.setLong(1, userId);
            ps.setLong(2, file_info_id);
            ResultSet rs = ps.executeQuery();
            List<UserFileInfo> result = DtoUtils.convertToUserFileInfo(rs);

            return result.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void DeleteRowFromFileInfo(Long id)
    {
        try
        {
            PreparedStatement ps = getConnection().prepareStatement("DELETE FROM file_info WHERE id = ?");
            ps.setLong(1,id);
            ps.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

}
