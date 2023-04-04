package com.example.Todo.security;

import com.example.Todo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "NMA8JPctFuna59f5";

    // 토큰 생성
    // jwt 토큰은 [ 헤더 / 정보(페이로드) / 서명 ] 으로 구성
    public String create(UserEntity userEntity){
        Date expireDate = Date.from(    // 토큰 만료 시간 설정
                Instant.now()
                        .plus(1, ChronoUnit.DAYS)); // 1일
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // 헤더 / 해싱 알고리즘 설정
                // 페이로드 해당 부분
                .setSubject(userEntity.getId())
                .setIssuer("todo app")  // 발행인
                .setIssuedAt(new Date())    // 발행시간
                .setExpiration(expireDate)  // 만료시간
                .compact();
    }
    // 토큰 검사(인증)
    public String validateAndGetUserId(String token){
        // 토큰 파싱
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)  // SECRET_KEY를 통한 토큰 해석
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
