package neo.lin;

import com.neo.qiaoqiaochat.util.JwtUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class Test {

    public static void main(String[] args) {


//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//
//        JwtBuilder builder = Jwts.builder()
//
//                .setSubject("Joe").signWith(key);
//
//        long currentTimeMillis = System.currentTimeMillis();
//
//        long ttl = 30000; //30 mill seconds
//        long expireTime = currentTimeMillis + ttl;
//        Date now = new Date(currentTimeMillis);
//        Date expired = new Date(expireTime);
//
//        builder.setExpiration(expired)
//                .setNotBefore(now);
//
//        String compact = builder.compact();
//        System.out.println(compact);
//
//
//        //解码
//        JwtParser build = Jwts.parserBuilder().setSigningKey(key)
//                .build();
//        Jws<Claims> claimsJws = build.parseClaimsJws(compact);
//        Claims body = claimsJws.getBody();
//        String subject = body.getSubject();
//        System.out.println(subject);
//        Header header = claimsJws.getHeader();
//        System.out.println(1);

//        String s = JwtUtils.buildJWT("123");
//        System.out.println(s);
//        String s1 = JwtUtils.parseJWT(s);
//        System.out.println(s1);
//        System.out.println(1);
    }


}
