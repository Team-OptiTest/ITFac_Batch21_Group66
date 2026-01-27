/*    */ package BOOT-INF.classes.com.qatraining.security.jwt;
/*    */ 
/*    */ import io.jsonwebtoken.Claims;
/*    */ import io.jsonwebtoken.Jwts;
/*    */ import io.jsonwebtoken.SignatureAlgorithm;
/*    */ import io.jsonwebtoken.security.Keys;
/*    */ import java.util.Date;
/*    */ import java.util.List;
/*    */ import javax.crypto.SecretKey;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class JwtUtil
/*    */ {
/* 18 */   private final SecretKey secretKey = Keys.hmacShaKeyFor("qa-training-secret-key-qa-training-secret-key"
/* 19 */       .getBytes());
/*    */   
/* 21 */   private final long EXPIRATION_TIME = 3600000L;
/*    */ 
/*    */ 
/*    */   
/*    */   public String generateToken(String username, List<String> roles) {
/* 26 */     return Jwts.builder()
/* 27 */       .setSubject(username)
/* 28 */       .claim("roles", roles)
/* 29 */       .setIssuedAt(new Date())
/* 30 */       .setExpiration(new Date(System.currentTimeMillis() + 3600000L))
/* 31 */       .signWith(this.secretKey, SignatureAlgorithm.HS256)
/* 32 */       .compact();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean validateToken(String token) {
/*    */     try {
/* 38 */       getClaims(token);
/* 39 */       return true;
/* 40 */     } catch (Exception e) {
/* 41 */       return false;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String extractUsername(String token) {
/* 47 */     return getClaims(token).getSubject();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> extractRoles(String token) {
/* 53 */     return (List<String>)getClaims(token).get("roles");
/*    */   }
/*    */ 
/*    */   
/*    */   private Claims getClaims(String token) {
/* 58 */     return (Claims)Jwts.parserBuilder()
/* 59 */       .setSigningKey(this.secretKey)
/* 60 */       .build()
/* 61 */       .parseClaimsJws(token)
/* 62 */       .getBody();
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/security/jwt/JwtUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */