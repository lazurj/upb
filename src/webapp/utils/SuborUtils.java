package webapp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import database.Database;
import database.dto.UserFileInfo;
import database.dto.Util.DtoUtils;

public class SuborUtils {
	
	public static String retrieveHash(int file_id, String file_name)
	{
		String hash= "";
		
		return hash;
	}
	
	public static String getHashByFilename(String filename, boolean test)
	{
		String hash = "";
		
		if (test)
			return "123";
	
	    try {
	    	PreparedStatement ps = Database.getConnection().prepareStatement("select hash_key from file_info inner join "
	    			+ "user_file on file_info.id == user_file.file_info_id where file_name = ?");
	    	ps.setString(1, filename);
	    	ResultSet rs = ps.executeQuery();
	            hash = rs.getString(0);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

		return hash;
	}
	
	/* pridanie hashu a jeho dlzky */
	public static void addHash(int length, String hash, File vstup_subor, File vystup_subor) throws IOException
	{
		/* prednastavenie */
		byte [] bajty_suboru = Files.readAllBytes(Paths.get(vstup_subor.getPath()));
		
		/* dlzka hashu */
		byte[] hash_bajty = hash.getBytes();
		int hash_dlzka = hash_bajty.length;
		
		/* vystupny buffer */
		ByteBuffer buf = ByteBuffer.allocateDirect(bajty_suboru.length+hash_bajty.length+8);
		
		/* pridavanie dlzky a hashu */
		buf.putInt(hash_dlzka);
		buf.put(hash_bajty);
		
		/* zapis buffera */
		FileChannel wChannel = new FileOutputStream(vstup_subor, false).getChannel();
		buf.flip();
		wChannel.write(buf);

		wChannel.close();
	}
	
	/* pridanie hashu a jeho dlzky */
	public static void addMac(int length, String mac, File vstup_subor, File vystup_subor) throws IOException
	{
		/* prednastavenie */
		byte [] bajty_suboru = Files.readAllBytes(Paths.get(vstup_subor.getPath()));
		
		/* dlzka hashu */
		byte[] mac_bajty = mac.getBytes();
		int mac_dlzka = mac_bajty.length;
		
		/* vystupny buffer */
		ByteBuffer buf = ByteBuffer.allocateDirect(bajty_suboru.length+mac_bajty.length+8);
		
		/* pridavanie dlzky a hashu */
		buf.putInt(mac_dlzka);
		buf.put(mac_bajty);
		
		/* zapis buffera */
		FileChannel wChannel = new FileOutputStream(vstup_subor, false).getChannel();
		buf.flip();
		wChannel.write(buf);

		wChannel.close();
	}
	
	
	public static void addStuff(File vstup_subor, File vystup_subor) throws IOException
	{
		String filename = vstup_subor.getName();
		String hash = getHashByFilename(filename, true); // test == true
		addHash(hash.length(), hash, vstup_subor, vystup_subor);
	}
	
	public static String stripHash(File vstup_subor) throws IOException
	{
		/* ziskaj velkost hash hlavicky */
		byte [] bajty_suboru = Files.readAllBytes(Paths.get(vstup_subor.getPath()));
		
		ByteBuffer buf = ByteBuffer.allocateDirect(bajty_suboru.length);
	
		int dlzka_hashu = buf.getInt(0);
		ByteBuffer buf_hash = buf.get(bajty_suboru, 1, dlzka_hashu+1);
		
		String hash = StandardCharsets.UTF_8.decode(buf_hash).toString();
		
		return hash;
	}
}
