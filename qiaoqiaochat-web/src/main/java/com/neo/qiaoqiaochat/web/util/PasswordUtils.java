package com.neo.qiaoqiaochat.web.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;
import com.neo.qiaoqiaochat.web.config.Audience;

/**
 * @author linyi 2020-09-08
 */
public class PasswordUtils {


    /**
     * 提取摘要
     * @param password
     * @return
     */
    public static String sin(String password){
        Audience audience = SpringUtils.getBean(Audience.class);
        String salt = audience.getSalt();
        return getSHA256Str(password, salt);
    }

    /**
     * sha256算法
     * @param str
     * @param salt
     * @return
     */
    public static String getSHA256Str(String str, String salt){
        MessageDigest messageDigest;
        String encodeStr = "";
        String sourceStr = str + salt;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(sourceStr.getBytes("UTF-8"));
            encodeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

}
