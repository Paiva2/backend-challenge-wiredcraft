package org.com.wired.adapters.strategy.infra.auth.jwtAuthentication;

import io.jsonwebtoken.Jwts;
import org.com.wired.adapters.strategy.infra.auth.AuthenticationStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class JwtAuthentication implements AuthenticationStrategy {
    private static final String ISSUER = "backend-challenge-app";
    private static final Integer TOKEN_LIFE_TIME_HOURS = 5;

    @Value("${jwt.keys.private}")
    private String RSA_PRIVATE_KEY_STRING;

    @Value("${jwt.keys.public}")
    private String RSA_PUBLIC_KEY_STRING;

    @Override
    public String createAuth(String subject) {
        String token;

        try {
            token = Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .issuer(ISSUER)
                .claim("alg", "RS256")
                .claim("roles", List.of("user"))
                .expiration(expirationTime())
                .signWith(getPrivateKey(), Jwts.SIG.RS256)
                .compact();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return token;
    }

    @Override
    public void verifyAuth(String jwt) {

    }

    private Date expirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, TOKEN_LIFE_TIME_HOURS);

        return calendar.getTime();
    }

    private RSAPrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyBytes = pemKeyBytes(RSA_PRIVATE_KEY_STRING);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory rsaKeyInstance = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey) rsaKeyInstance.generatePrivate(keySpec);
    }

    private RSAPublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = pemKeyBytes(RSA_PUBLIC_KEY_STRING);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(publicKeyBytes);
        KeyFactory rsaKeyInstance = KeyFactory.getInstance("RSA");

        return (RSAPublicKey) rsaKeyInstance.generatePublic(keySpec);
    }

    private byte[] pemKeyBytes(String keyString) {
        keyString = keyString.replace("-----BEGIN PRIVATE KEY-----", "");
        keyString = keyString.replace("-----END PRIVATE KEY-----", "");
        keyString = keyString.replace("-----BEGIN PUBLIC KEY-----", "");
        keyString = keyString.replace("-----END PUBLIC KEY-----", "");
        keyString = keyString.replaceAll("\\s+", "");

        return Base64.getDecoder().decode(keyString);
    }
}
