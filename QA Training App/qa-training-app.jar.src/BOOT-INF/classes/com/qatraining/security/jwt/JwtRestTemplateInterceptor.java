/*    */ package BOOT-INF.classes.com.qatraining.security.jwt;
/*    */ 
/*    */ import com.qatraining.security.jwt.JwtSessionHolder;
/*    */ import java.io.IOException;
/*    */ import org.springframework.http.HttpRequest;
/*    */ import org.springframework.http.client.ClientHttpRequestExecution;
/*    */ import org.springframework.http.client.ClientHttpRequestInterceptor;
/*    */ import org.springframework.http.client.ClientHttpResponse;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class JwtRestTemplateInterceptor
/*    */   implements ClientHttpRequestInterceptor {
/*    */   private final JwtSessionHolder jwtSessionHolder;
/*    */   
/*    */   public JwtRestTemplateInterceptor(JwtSessionHolder jwtSessionHolder) {
/* 17 */     this.jwtSessionHolder = jwtSessionHolder;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
/* 27 */     String token = this.jwtSessionHolder.getToken();
/*    */     
/* 29 */     if (token != null) {
/* 30 */       request.getHeaders().setBearerAuth(token);
/*    */     }
/*    */     
/* 33 */     return execution.execute(request, body);
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/security/jwt/JwtRestTemplateInterceptor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */