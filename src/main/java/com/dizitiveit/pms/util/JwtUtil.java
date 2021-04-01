package com.dizitiveit.pms.util;

import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date; 
 import java.util.HashMap; 
 import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
 import io.jsonwebtoken.Jwts; 
 import io.jsonwebtoken.SignatureAlgorithm;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.core.userdetails.UserDetails; 
 import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dizitiveit.pms.model.Users;

@Service
public class JwtUtil {

	 private String SECRET_KEY = "1096076897863288";
	 // private String SHARED_SECRET_KEY="83906264";
  
  public String extractUsername(String token) {
	  return extractClaim(token, Claims::getSubject); 
	  }
  
  public Date extractExpiration(String token) { 
	  return extractClaim(token,Claims::getExpiration);
	  }
  
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
  final Claims claims = extractAllClaims(token); 
  return claimsResolver.apply(claims); 
  } 
  private Claims extractAllClaims(String token)
  { 
	  return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody(); 
	  }
  
  private Boolean isTokenExpired(String token) { 
	  return extractExpiration(token).before(new Date()); 
	  }
  
  public String generateToken(UserDetails userDetails) {
	  Map<String, Object> claims = new HashMap<>();
	  return createToken(claims, userDetails.getUsername());
	  }
  
  private String createToken(Map<String, Object> claims, String subject) {
  return Jwts.builder().setClaims(claims).setSubject(subject)
.setIssuedAt(new Date(System.currentTimeMillis())) 
.setExpiration(new Date(System.currentTimeMillis() + 100800000 * 60 * 60 * 1))
 .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
  }
  
  
  public String createRefreshToken() {
	  String refreshToken = UUID.randomUUID().toString();
	  return refreshToken;
  }
  
  
  public Boolean validateToken(String token, UserDetails userDetails) { 
	  final String username = extractUsername(token); 
	  return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); 
	  }
  
  

  public static SecretKey getAESKey() throws NoSuchAlgorithmException {
      KeyGenerator keyGen = KeyGenerator.getInstance("AES");
      keyGen.init(256, SecureRandom.getInstanceStrong());
      return keyGen.generateKey();
  }
}
