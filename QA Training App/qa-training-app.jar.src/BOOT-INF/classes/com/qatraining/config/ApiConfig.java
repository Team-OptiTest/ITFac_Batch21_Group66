/*    */ package BOOT-INF.classes.com.qatraining.config;
/*    */ 
/*    */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ @ConfigurationProperties(prefix = "api")
/*    */ public class ApiConfig
/*    */ {
/*    */   private String baseUrl;
/*    */   
/*    */   public String getBaseUrl() {
/* 13 */     return this.baseUrl;
/*    */   }
/*    */   
/*    */   public void setBaseUrl(String baseUrl) {
/* 17 */     this.baseUrl = baseUrl;
/*    */   }
/*    */   
/*    */   public String loginUrl() {
/* 21 */     return this.baseUrl + "/api/auth/login";
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/config/ApiConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */