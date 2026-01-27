/*    */ package BOOT-INF.classes.com.qatraining.security.jwt.dto;
/*    */ 
/*    */ public class JwtLoginResponse
/*    */ {
/*    */   private String token;
/*  6 */   private String tokenType = "Bearer";
/*    */   
/*    */   public JwtLoginResponse(String token) {
/*  9 */     this.token = token;
/*    */   }
/*    */   
/*    */   public String getToken() {
/* 13 */     return this.token;
/*    */   }
/*    */   
/*    */   public String getTokenType() {
/* 17 */     return this.tokenType;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/security/jwt/dto/JwtLoginResponse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */