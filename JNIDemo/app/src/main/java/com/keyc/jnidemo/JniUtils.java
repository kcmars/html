package com.keyc.jnidemo;

import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2019/2/19.
 */

public class JniUtils {
    public static native String setStringJni();
    public static native byte[] getKey();
    public static native byte[] getIv();

    private static byte[] keyValue;
    private static byte[] iv;

    public static final String APP_KEY = "12345678";
    public static final String SIGN_KEY = "jiwW@1*@sdqqoHUW@1&4#SU1SUHU1koaw";

    private static SecretKey key;
//    private static Key  key;
    private static AlgorithmParameterSpec paramSpec;
    private static Cipher ecipher;

    /**
     * 对请求参数进行排序
     * @param params
     * @return
     */
    public static String serializationParams(Map<String,String> params){
        //升序排列
        Map<String, String> sortMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                return str1.compareTo(str2);
            }
        });
        sortMap.putAll(params);
        StringBuffer sb = new StringBuffer();
        for (String key :sortMap.keySet()) {
            String value = sortMap.get(key) + "";
            sb.append(key).append("=").append(value).append("&");
        }
        sb.append("key").append("=").append(SIGN_KEY);
        return sb.toString();
    }

    /**
     * 计算md5
     * @param string
     * @return
     */
    public static String MD5(String string) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final class CryptoProvider extends Provider {
        /**
         * Creates a Provider and puts parameters
         */
        public CryptoProvider() {
            super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG",
                    "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }

    /**加密**/
    public static String encode(String msg) {
        String str ="";
        try {
            //用密钥和一组算法参数初始化此 cipher
            ecipher.init(Cipher.ENCRYPT_MODE,key,paramSpec);
            //加密并转换成16进制字符串
            str = asHex(ecipher.doFinal(msg.getBytes()));
        } catch (BadPaddingException e) {
        } catch (InvalidKeyException e) {
        } catch (InvalidAlgorithmParameterException e) {
        } catch (IllegalBlockSizeException e) {
        }
        return str;
    }

    /**解密**/
    public static String decode(String value) {
        try {
            ecipher.init(Cipher.DECRYPT_MODE,key,paramSpec);
            return new String(ecipher.doFinal(asBin(value)));
        } catch (BadPaddingException e) {
        } catch (InvalidKeyException e) {
        } catch (InvalidAlgorithmParameterException e) {
        } catch (IllegalBlockSizeException e) {
        }
        return"";
    }

    /**转16进制**/
    private static String asHex(byte buf[]) {
        StringBuffer strbuf =new StringBuffer(buf.length * 2);
        int i;
        for (i = 0;i <buf.length;i++) {
            if (((int)buf[i] & 0xff) < 0x10)//小于十前面补零
                strbuf.append("0");
            strbuf.append(Long.toString((int)buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    /**转2进制**/
    private static byte[] asBin(String src) {
        if (src.length() < 1)
            return null;
        byte[]encrypted =new byte[src.length() / 2];
        for (int i = 0;i <src.length() / 2;i++) {
            int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);//取高位字节
            int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);//取低位字节
            encrypted[i] = (byte) (high * 16 +low);
        }
        return encrypted;
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     * @throws java.lang.Exception
     */
    private static Key getMyKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }


    /**
     * 转换密钥
     * @param key
     * @return
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(dks);
        // 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码  
        // SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);  
        return secretKey;
    }

    /**
     * 加密
     * @param encryptString
     * @param encryptKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String encryptString, String encryptKey) throws Exception {
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());

        return new String(Base64.encode(encryptedData, Base64.NO_WRAP));
    }

    /**
     * 解密
     * @param decryptString
     * @param decryptKey
     * @return
     * @throws Exception
     */
    @NonNull
    public static String decrypt(String decryptString, String decryptKey) throws Exception {
        byte[] byteMi = Base64.decode(decryptString, Base64.NO_WRAP);
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte decryptedData[] = cipher.doFinal(byteMi);

        return new String(decryptedData);
    }

    static {
        keyValue = getKey();
        iv = getIv();
        Log.i("TAG", "onCreate1: " + keyValue.length);
        Log.i("TAG", "onCreate2: " + iv.length);
        if(null != keyValue && null !=iv) {
            KeyGenerator kgen;
            try {
                kgen = KeyGenerator.getInstance("AES");
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG", new CryptoProvider());
                random.setSeed(APP_KEY.getBytes());
                kgen.init(128, random);
                key = kgen.generateKey();
//                key = (SecretKey) getMyKey(APP_KEY.getBytes());
                Log.i("TAG", "onCreate3: " + key);
                paramSpec = new IvParameterSpec(iv);
                ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
