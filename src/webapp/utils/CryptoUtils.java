package webapp.utils;

import org.apache.commons.io.FileUtils;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Properties;

import static webapp.utils.AsyncCrypto.HMAC_SHA256;

public class CryptoUtils
{
	private static final String ALGORITHM="AES";
	private static final String TRANSFORMATION="AES";
	private static Properties keySaltProp = null;
	private static Properties fileKeyProp = null;

	private static Properties privateKeyProp = null;
	private static Properties publicKeyProp = null;

	public static boolean encrypt(String key, String salt, File inputFile, File outputFile, byte[] hashofFile) throws Exception {


		boolean pass = false;

		pass = doCrypto(Cipher.ENCRYPT_MODE, key, salt, inputFile, outputFile);
		if (!pass){
			return false;
		}

		//byte[] foo = Base64.getDecoder().decode(outputFile);

		byte [] foo = Files.readAllBytes(Paths.get(outputFile.getPath()));
		int one = 1;
		ByteBuffer buf = ByteBuffer.allocateDirect(foo.length+hashofFile.length+4);
		int hashofFileLength = hashofFile.length;
		buf.putInt(hashofFile.length);

		buf.put(hashofFile);
		buf.put(foo);


		FileChannel wChannel = new FileOutputStream(outputFile, false).getChannel();
		buf.flip();
		wChannel.write(buf);

		wChannel.close();
		//System.out.println(outputFile);
		return true;
	}
	
	public static boolean decrypt(String key, String salt, File inputFile, File outputFile) throws Exception {

		File fooFile = new File (inputFile.getName());
		FileUtils.copyFile(inputFile,fooFile);


		byte [] foo1 = Files.readAllBytes(Paths.get(fooFile.getPath()));
		byte [] foo = foo1.clone();

		try {
			//ByteBuffer buf = ByteBuffer.allocateDirect(foo.length);
			ByteBuffer buf = ByteBuffer.wrap(foo);
			int hashlength = buf.getInt();
			byte[] hash1 =  new byte[hashlength];
			buf.get(hash1);

			byte[] data = new byte[buf.remaining()];
			buf.get(data);

			ByteBuffer bufContent = ByteBuffer.allocateDirect(data.length);
			bufContent.put(data);
			//System.out.println(buf);

			FileChannel wChannel = new FileOutputStream(fooFile, false).getChannel();
			bufContent.flip();
			wChannel.write(bufContent);

			wChannel.close();

			if (doCrypto(Cipher.DECRYPT_MODE, key, salt, fooFile, outputFile)){
				String content = new String(Files.readAllBytes(Paths.get(outputFile.getPath())));
				byte[] hashofFile = AsyncCrypto.hmacDigestBytes(content,"password",HMAC_SHA256);
				boolean retval = Arrays.equals(hashofFile, hash1);
				if (retval){

					fooFile.delete();
					return true;
				}

			}
			fooFile.delete();
			return false;

		}
		catch (Exception e){
			return false;
		}




	}
	
	private static boolean doCrypto(int cipherMode, String key, String salt, File inputFile, File outputFile) throws Exception {
		try{
			SecretKey secretKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(new PBEKeySpec(key.toCharArray(), salt.getBytes(), 128, 128));
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
	        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);
			cipher.init(cipherMode, secretKeySpec);
			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int)inputFile.length()];
			inputStream.read(inputBytes);
			byte[] outputBytes = cipher.doFinal(inputBytes);
			inputStream.close();
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(outputBytes);
			outputStream.close();
			return true;

		} catch(NoSuchPaddingException | NoSuchAlgorithmException
		| InvalidKeyException|BadPaddingException
		| IllegalBlockSizeException|IOException ex) {

			//throw new Exception("Errorencrypting/decryptingfile"+ex.getMessage(),ex);
			return false;
		}

	}
	
	public static String generateRandomKey(int length) {
		return new RandomString(length).nextString();
	}


	public static  Properties getKeySaltProp() {
		if(keySaltProp == null) {
			try {
				keySaltProp = new Properties();
				InputStream in = CryptoUtils.class.getResourceAsStream("key_salt.properties");
				keySaltProp.load(in);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return keySaltProp ;
	}

	public static  String getSaltByKey(String key) {
		return getKeySaltProp()!= null ? keySaltProp.getProperty(key) : null;
	}

	public static void setSalt(String key, String salt) throws IOException {
		// if (getKeySaltProp()!=null){
		keySaltProp = getKeySaltProp();
		keySaltProp.setProperty(key, salt);
		OutputStream output = new FileOutputStream(CryptoUtils.class.getResource("key_salt.properties").getPath());
		keySaltProp.store(output,null);

	}




	public static  Properties getFileKeyProp() {
		if(fileKeyProp == null) {
			try {
				fileKeyProp = new Properties();
				InputStream in = CryptoUtils.class.getResourceAsStream("file_key.properties");

				fileKeyProp.load(in);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileKeyProp;
	}

	public static  String getKeyFromFile(String fileName) {
		return getFileKeyProp()!= null ? fileKeyProp.getProperty(fileName) : null;
	}

	public static void setKey(String fileName, String key) throws IOException {
		// if (getKeySaltProp()!=null){
		fileKeyProp = getFileKeyProp();
		fileKeyProp.setProperty(fileName, key);
		OutputStream output = new FileOutputStream(CryptoUtils.class.getResource("file_key.properties").getPath());
		fileKeyProp.store(output,null);

	}



	///////////////////////////////////////////////////////


	public static  Properties getPrivateKeyProp() {
		if(publicKeyProp == null) {
			try {
				publicKeyProp = new Properties();
				InputStream in = CryptoUtils.class.getResourceAsStream("public_private.properties");

				publicKeyProp.load(in);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return publicKeyProp;
	}

	public static  String getPrivateKeyFromFile(String fileName) {
		return getPrivateKeyProp()!= null ? privateKeyProp.getProperty(fileName) : null;
	}

	public static void setPublicKey(String publicKey, String privatekey) throws IOException {
		// if (getKeySaltProp()!=null){
		privateKeyProp = getPrivateKeyProp();
		privateKeyProp.setProperty(publicKey, privatekey);
		OutputStream output = new FileOutputStream(CryptoUtils.class.getResource("public_private.properties").getPath());
		privateKeyProp.store(output,null);

	}

	private void copyFileUsingChannel(File src, File dest) throws IOException {
		FileChannel sourceChannel = null;
		FileChannel destinationChannel = null;
		try {
			sourceChannel = new FileInputStream(src).getChannel();
			destinationChannel = new FileOutputStream(dest).getChannel();
			destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		} finally {
			sourceChannel.close();
			destinationChannel.close();
		}
	}




}

