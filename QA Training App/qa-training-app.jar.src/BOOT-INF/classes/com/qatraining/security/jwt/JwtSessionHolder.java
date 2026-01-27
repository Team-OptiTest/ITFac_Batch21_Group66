/*    */ package BOOT-INF.classes.com.qatraining.security.jwt;
/*    */ 
/*    */ import jakarta.servlet.http.HttpSession;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class JwtSessionHolder
/*    */ {
/*    */   private final HttpSession session;
/*    */   
/*    */   public JwtSessionHolder(HttpSession session) {
/* 12 */     this.session = session;
/*    */   }
/*    */   
/*    */   public String getToken() {
/* 16 */     Object token = this.session.getAttribute("JWT_TOKEN");
/* 17 */     return (token != null) ? token.toString() : null;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/security/jwt/JwtSessionHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */