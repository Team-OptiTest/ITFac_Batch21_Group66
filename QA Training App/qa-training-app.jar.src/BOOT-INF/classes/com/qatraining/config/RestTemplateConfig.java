/*    */ package BOOT-INF.classes.com.qatraining.config;
/*    */ 
/*    */ import com.qatraining.config.JwtSessionInterceptor;
/*    */ import jakarta.servlet.http.HttpSession;
/*    */ import java.util.List;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.web.client.RestTemplate;
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ public class RestTemplateConfig
/*    */ {
/*    */   @Bean
/*    */   public RestTemplate restTemplate(HttpSession session) {
/* 16 */     RestTemplate restTemplate = new RestTemplate();
/* 17 */     restTemplate.setInterceptors(
/* 18 */         List.of(new JwtSessionInterceptor(session)));
/*    */     
/* 20 */     return restTemplate;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/config/RestTemplateConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */