package com.neo.qiaoqiaochat.shiro;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 密码管理器
 * @author Neo Lin
 * @date  2020/4/5
 */
public class PasswordManager {
    private String algorithmName = "SHA-512";
    private Integer hashIterations = 5;
    private static PasswordManager passwordManager = new PasswordManager();
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    public PasswordManager() {
    }

    public static PasswordManager instance() {
        return passwordManager;
    }

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public String encryptPassword(String password, String salt) {
        String newPassword = (new SimpleHash(this.algorithmName, password, ByteSource.Util.bytes(salt), this.hashIterations)).toHex();
        return newPassword;
    }

    public String generateSalt() {
        return this.randomNumberGenerator.nextBytes().toHex();
    }

    public PasswordManager setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
        return this;
    }

    public PasswordManager setHashIterations(Integer hashIterations) {
        this.hashIterations = hashIterations;
        return this;
    }
}
