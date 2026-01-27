/*    */ package BOOT-INF.classes.com.qatraining.security.entrypoint;
/*    */ 
/*    */ import jakarta.servlet.http.HttpServletRequest;
/*    */ import jakarta.servlet.http.HttpServletResponse;
/*    */ import java.io.IOException;
/*    */ import org.springframework.security.core.AuthenticationException;
/*    */ import org.springframework.security.web.AuthenticationEntryPoint;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class UiAuthenticationEntryPoint
/*    */   implements AuthenticationEntryPoint
/*    */ {
/*    */   public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
/* 21 */     String uri = request.getRequestURI();
/*    */ 
/*    */     
/* 24 */     if (uri.equals("/ui/login")) {
/* 25 */       response.setStatus(200);
/*    */       return;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/security/entrypoint/UiAuthenticationEntryPoint.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */