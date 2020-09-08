package com.neo.qiaoqiaochat.web.util;

import com.neo.qiaoqiaochat.web.config.Audience;
import com.neo.qiaoqiaochat.web.exception.BusinessException;
import com.neo.qiaoqiaochat.web.model.ResultCode;
import com.neo.qiaoqiaochat.web.model.vo.UserAccountVO;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JwtUtils {

    private static Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * 由字符串生成加密key
     */
    public static SecretKey generalKey(String secret) {
        String stringKey = secret;
        byte[] encodedKey = null;
        SecretKey key = null;
        try {
            encodedKey = Base64.decode(stringKey);
            key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        } catch (Exception e) {
            logger.error("message:" + e);
        }
        return key;
    }


    public static UserAccountVO parseJWT(String token, String base64Security) {
        if(StringUtils.isEmpty(token)) {
            throw new BusinessException(ResultCode.NO_LOGIN);
        }
        //解码
        try {
            JwtParser build = Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .build();
            Jws<Claims> claimsJws = build.parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            UserAccountVO vo = new UserAccountVO();
            vo.setId(claims.get("id") == null ? null : Long.valueOf(claims.get("id").toString()));
            vo.setAccount(claims.get("account") == null ? null : claims.get("account").toString());
            vo.setNickName(claims.get("nickName") == null ? null : claims.get("nickName").toString());
            vo.setHeadImageIndex(claims.get("headImageIndex") == null ? null : claims.get("headImageIndex").toString());
            return vo;
        } catch (ExpiredJwtException eje) {
            logger.error("*****TOKEN 过期*****", eje);
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
    }


    public static String buildJWT(UserAccountVO account, Audience audience) {

        // 使用HS256加密算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(audience.getBase64Secret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(String.valueOf(account.getId())).claim("id", String.valueOf(account.getId()))
                .claim("account", account.getAccount()).claim("nickName", account.getNickName())
                .claim("headImageIndex", account.getHeadImageIndex()).signWith(signingKey);

        long nowMillis = System.currentTimeMillis();

        long ttlMillis = audience.getExpiresSecond();
        long expireTime = nowMillis + ttlMillis;
        Date now = new Date(nowMillis);
        Date expired = new Date(expireTime);

        builder.setExpiration(expired).setNotBefore(now);

        return builder.compact();
    }

}
