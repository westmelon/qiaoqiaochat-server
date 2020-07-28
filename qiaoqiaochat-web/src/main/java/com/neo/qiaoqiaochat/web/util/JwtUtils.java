package com.neo.qiaoqiaochat.web.util;

import com.neo.qiaoqiaochat.web.config.Audience;
import com.neo.qiaoqiaochat.web.exception.BusinessException;
import com.neo.qiaoqiaochat.web.model.ResultCode;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JwtUtils {
    private static Logger logger = LoggerFactory.getLogger(JwtUtils.class);


    public static String parseJWT(String token, String base64Security) {

        //解码
        try {
            JwtParser build = Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .build();
            Jws<Claims> claimsJws = build.parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            String subject = body.getSubject();
            return subject;
        } catch (ExpiredJwtException eje) {
            logger.error("*****TOKEN 过期*****", eje);
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
    }


    public static String buildJWT(String account, Audience audience) {

        // 使用HS256加密算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(audience.getBase64Secret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());


        JwtBuilder builder = Jwts.builder()
                .setSubject(account)
                .signWith(signingKey);

        long nowMillis = System.currentTimeMillis();

        long ttlMillis = 3600000; //3600 mill seconds
        long expireTime = nowMillis + ttlMillis;
        Date now = new Date(nowMillis);
        Date expired = new Date(expireTime);

        builder.setExpiration(expired)
                .setNotBefore(now);

        String token = builder.compact();
        return token;
    }
}
