package com.example.aicctvstream.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class TokenProvider {
    @Value("${secret.key}")
    private String SECRET_KEY;

    @Value("${token.password}")
    private String tokenPassword;

    public String create(String password){
        // 기한 1일
        Date expiryDate = Date.from(Instant.now().plus(30, ChronoUnit.DAYS));
//        {
//            "alg":"HS512"
//        }.
//        {
//            "sub":"40288093784915d201784916a40c001",
//            "iss":"demo app",
//            "iat":1595733657,
//            "exp":1596597657,
//        }.
//        //SECRET KEY를 이용해 서명한 부분
//        ~~

        if(password.equals(tokenPassword)){
            return Jwts.builder()
                    //header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                    //payload에 들어갈 내용
                    .setSubject("cctv")
                    .setIssuer("capstone")
                    .setIssuedAt(new Date())
                    .setExpiration(expiryDate)
                    .compact();
        }
        else{
            throw new RuntimeException("패스워드 불일치");
        }


    }
    public String validateAndGetUserId(String token){
        // parseClaimsJws 메서드가 Base64로 디코딩 및 파싱
        // 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용해 서명한 후 token의 서명과 비교
        // 위조되지 않았다면 페이로드(Claims) 리턴, 위조라면 예외를 날림.
        // 그 중 우리는 userId가 필요하므로 getBody를 부른다.
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
