/*    */ package BOOT-INF.classes.com.qatraining.config;
/*    */ 
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.security.core.userdetails.User;
/*    */ import org.springframework.security.core.userdetails.UserDetails;
/*    */ import org.springframework.security.crypto.password.PasswordEncoder;
/*    */ import org.springframework.security.provisioning.InMemoryUserDetailsManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ public class UserConfig
/*    */ {
/*    */   @Bean
/*    */   InMemoryUserDetailsManager users(PasswordEncoder encoder) {
/* 20 */     UserDetails admin = User.builder().username("admin").password(encoder.encode("admin123")).roles(new String[] { "ADMIN" }).build();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 26 */     UserDetails testUser = User.builder().username("testuser").password(encoder.encode("test123")).roles(new String[] { "USER" }).build();
/*    */     
/* 28 */     return new InMemoryUserDetailsManager(new UserDetails[] { admin, testUser });
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/config/UserConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */