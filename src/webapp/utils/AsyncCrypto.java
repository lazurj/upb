package webapp.utils;

import database.Database;
import database.dto.User;
import database.dto.UserFileInfo;
import org.apache.commons.io.FileUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class AsyncCrypto {
    private PrivateKey privatekey;
    private PublicKey publickey;
    public static final String HMAC_SHA256 = "HmacSHA256";
    private static String algorithm = "RSA";
    private static int algoSize = 512;
    private static String cipherAlgorithm = "RSA/ECB/PKCS1Padding";

    /**
     * Async crypto
     * @throws NoSuchAlgorithmException
     */
    public AsyncCrypto() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(algoSize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.privatekey = keyPair.getPrivate();
        this.publickey = keyPair.getPublic();
    }

    public PrivateKey getPrivatekey() {
        return this.privatekey;
    }

    public PrivateKey getPrivateKey(String base64PrivateKey){
        PrivateKey privateKey = null;
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        }
        catch (Exception e){
            return null;
        }
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
          //  e.printStackTrace();
            return null;
        }
        return privateKey;
    }

    public PublicKey getPublickey() {
        return this.publickey;
    }


    public String PrivateKeyString() {
        return Base64.getEncoder().encodeToString(this.privatekey.getEncoded());
    }

    /**
     *
     * @return
     */
    public String PublicKeyString() {
        return Base64.getEncoder().encodeToString(this.publickey.getEncoded());
    }

//    public static PublicKey getPublicKey(String base64PublicKey) {
//        PublicKey publicKey = null;
//        try {
//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            publicKey = keyFactory.generatePublic(keySpec);
//            return publicKey;
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
//        return publicKey;
//    }

    /**
     * Funkcia vezme
     * @param message
     * @param publicKey
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] encrypt(String message, PublicKey publicKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message.getBytes());
    }

    /**
     *
     * @param data
     * @param privateKey
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
        }
        catch (Exception e){
            return null;
        }

        return new String(cipher.doFinal(data));
    }

    public PublicKey getPublicKey(String base64PublicKey){
        PublicKey publicKey = null;
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }




    public static String hmacDigest(String msg, String keyString, String algo) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance(algo);
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return digest;
    }
    public static byte[] hmacDigestBytes(String msg, String keyString, String algo) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance(algo);
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));
            return bytes;

        } catch (UnsupportedEncodingException e) {
        }catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }return new byte[0];
    }



            public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    public static boolean shareFile(User owner, User user, UserFileInfo ownerFile, String privateKey){
        try {
            AsyncCrypto asyncCrypto = new AsyncCrypto();

            String hashKey = ownerFile.getHashKey();
            byte[] data = Base64.getDecoder().decode(hashKey);
            String fullKey = null;
            //PrivateKey pk = asyncCrypto.getPrivateKey(owner.getPrivateKey());
            PrivateKey pk = asyncCrypto.getPrivateKey(privateKey);
            if (pk != null) {
                fullKey = asyncCrypto.decrypt(data, pk);
                //generovanie symetrickeho kluca
                //zasifrovanie sym kluca verejnym klucom
                byte[] encKey = asyncCrypto.encrypt(fullKey, asyncCrypto.getPublicKey(user.getPublicKey()));
                String encKeyValue = Base64.getEncoder().encodeToString(encKey);

                Database.insertUserFile(user.getId(), ownerFile.getFileInfoId(), encKeyValue, false);
            }

        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public static boolean encUserFile(User user, File file , String fileName, boolean isOwner){
        try {
            File tmpFile = new File(user.getDirectory() + File.separator + "temp_" +fileName);
            FileUtils.copyFile(file, tmpFile);

            //generovanie symetrickeho kluca
            String key = CryptoUtils.generateRandomKey(16);
            String salt = CryptoUtils.generateRandomKey(18);
            String fullKey = key + salt;
            //zasifrovanie sym kluca verejnym klucom
            AsyncCrypto asyncCrypto = new AsyncCrypto();
            byte[] encKey = asyncCrypto.encrypt(fullKey,asyncCrypto.getPublicKey(user.getPublicKey()));
            String encKeyValue = Base64.getEncoder().encodeToString(encKey);

            Long fileId = Database.insertFile(file.getName(), "mac");
            Database.insertUserFile(user.getId(), fileId, encKeyValue, isOwner);
            String content = new String(Files.readAllBytes(Paths.get(file.getPath())));

            // String content1 = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(file.getPath())));
            byte[] hashofFile = AsyncCrypto.hmacDigestBytes(content,"password",HMAC_SHA256);
            CryptoUtils.encrypt(key, salt,tmpFile , file, hashofFile,encKey);
            tmpFile.delete();
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

}
