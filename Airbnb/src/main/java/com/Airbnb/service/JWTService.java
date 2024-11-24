package com.Airbnb.service;

import com.Airbnb.entity.PropertyUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiry.duration}")
    private int expiryTime;

    private Algorithm algorithm;

    private final static String USER_NAME = "username";

    @PostConstruct
    public void postConstruct() {
        algorithm  = Algorithm.HMAC256(algorithmKey);
    }

    public String generateToken(PropertyUser propertyUSer) {
              return JWT.create()
                      .withClaim(USER_NAME, propertyUSer.getUsername())
                      .withExpiresAt(new Date(System.currentTimeMillis()+expiryTime))
                      .withIssuer(issuer)
                      .sign(algorithm);
    }

    public String getUsername(String token)
    {
        DecodedJWT decodedJwt =JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return decodedJwt.getClaim(USER_NAME).asString();
    }
}



