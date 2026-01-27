/*    */ package BOOT-INF.classes.com.qatraining.config;
/*    */ 
/*    */ import jakarta.servlet.http.HttpSession;
/*    */ import java.io.IOException;
/*    */ import org.springframework.http.HttpRequest;
/*    */ import org.springframework.http.client.ClientHttpRequestExecution;
/*    */ import org.springframework.http.client.ClientHttpRequestInterceptor;
/*    */ import org.springframework.http.client.ClientHttpResponse;
/*    */ 
/*    */ public class JwtSessionInterceptor
/*    */   implements ClientHttpRequestInterceptor
/*    */ {
/*    */   private final HttpSession session;
/*    */   
/*    */   public JwtSessionInterceptor(HttpSession session) {
/* 16 */     this.session = session;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
/* 26 */     String token = (String)this.session.getAttribute("JWT_TOKEN");
/*    */     
/* 28 */     if (token != null) {
/* 29 */       request.getHeaders().setBearerAuth(token);
/*    */     }
/* 31 */     System.out.println("JWT SENT: " + token);
/* 32 */     return execution.execute(request, body);
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/config/JwtSessionInterceptor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */