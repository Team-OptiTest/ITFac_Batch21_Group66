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
/*    */ public class ApiAuthenticationEntryPoint
/*    */   implements AuthenticationEntryPoint
/*    */ {
/*    */   public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
/* 21 */     response.setStatus(401);
/* 22 */     response.setContentType("application/json");
/*    */     
/* 24 */     response.getWriter().write("{\n  \"status\": 401,\n  \"error\": \"UNAUTHORIZED\",\n  \"message\": \"Unauthorized - Use Basic Auth or JWT\"\n}\n");
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/security/entrypoint/ApiAuthenticationEntryPoint.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */