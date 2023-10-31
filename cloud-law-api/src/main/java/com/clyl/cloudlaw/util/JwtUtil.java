package com.clyl.cloudlaw.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author xiugou798
 */
@Slf4j
@Component
public class JwtUtil {

    public static String HEADER = "Authorization";
    /**
     * 过期时间7天
     **/
    public static long EXPIRE = 60 * 60 * 24 * 7 * 1000;
    public static String TOKEN_PREFIX = "Bearer ";

    private static final String SECRET = "CLYLTokenSecret";
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * 创建令牌
     * 生成jwt
     *
     * @param userId 用户id
     * @param openId openId
     * @return token
     */
    public static String createToken(String openId, String userId) {
        try {
            // 过期时间
            Date expireDate = new Date(System.currentTimeMillis() + EXPIRE);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "jwt");
            header.put("alg", "HS256");
            String token = JWT.create()
                    .withHeader(header)
                    .withClaim("phone", openId)
                    .withClaim("userId", userId)
                    .withExpiresAt(expireDate)
                    .sign(Algorithm.HMAC256(SECRET));
            log.info("token-------->" + token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 验证token
     *
     * @param token token
     * @return username
     */
    public static Map<String, Object> verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token.replace(TOKEN_PREFIX, ""));
            Map<String, Object> res = new HashMap<>();
            res.put("phone", jwt.getClaim("phone").asString());
            res.put("userId", jwt.getClaim("userId").asString());
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 检查token是否合格
     *
     * @param token  令牌
     * @param userId 用户id
     * @return {@link Boolean}
     */
    public static Boolean check(String token, String userId) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token.replace(TOKEN_PREFIX, ""));
            String userId1 = jwt.getClaim("userId").asString();
            if (userId.equals(userId1)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查
     *
     * @param token  令牌
     * @param userId 用户id
     * @param phone  电话
     * @return {@link Boolean}
     */
    public static Boolean check(String token, String phone, String userId) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token.replace(TOKEN_PREFIX, ""));
            String phone1 = jwt.getClaim("phone").asString();
            String userId1 = jwt.getClaim("userId").asString();
            if (phone.equals(phone1) && userId.equals(userId1)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断token过期时间是否小于5分钟
     *
     * @param token null
     * @return null
     */
    public static boolean isTokenExpired(String token) throws Exception {
        try {
            Date expiresAt;
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token.replace(TOKEN_PREFIX, ""));
            expiresAt = jwt.getExpiresAt();
            // System.out.println("expiresAt----->" + expiresAt);
            return (expiresAt.getTime() - System.currentTimeMillis()) < 60 * 5 * 1000;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("token 验证失败");
        }
    }
}
