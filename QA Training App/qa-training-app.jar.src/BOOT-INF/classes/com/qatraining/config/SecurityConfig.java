/*    */ package BOOT-INF.classes.com.qatraining.config;
/*    */ import com.qatraining.security.entrypoint.ApiAuthenticationEntryPoint;
/*    */ import com.qatraining.security.entrypoint.UiAuthenticationEntryPoint;
/*    */ import com.qatraining.security.jwt.JwtAuthenticationFilter;
/*    */ import jakarta.servlet.Filter;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.security.authentication.AuthenticationManager;
/*    */ import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
/*    */ import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
/*    */ import org.springframework.security.config.annotation.web.builders.HttpSecurity;
/*    */ import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
/*    */ import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
/*    */ import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
/*    */ import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
/*    */ import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
/*    */ import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
/*    */ import org.springframework.security.config.http.SessionCreationPolicy;
/*    */ import org.springframework.security.crypto.password.PasswordEncoder;
/*    */ import org.springframework.security.web.AuthenticationEntryPoint;
/*    */ import org.springframework.security.web.SecurityFilterChain;
/*    */ import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/*    */ 
/*    */ @EnableMethodSecurity
/*    */ @Configuration
/*    */ public class SecurityConfig {
/*    */   private final JwtAuthenticationFilter jwtAuthenticationFilter;
/*    */   
/*    */   public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, ApiAuthenticationEntryPoint apiAuthenticationEntryPoint, UiAuthenticationEntryPoint uiAuthenticationEntryPoint) {
/* 30 */     this.jwtAuthenticationFilter = jwtAuthenticationFilter;
/* 31 */     this.apiAuthenticationEntryPoint = apiAuthenticationEntryPoint;
/* 32 */     this.uiAuthenticationEntryPoint = uiAuthenticationEntryPoint;
/*    */   }
/*    */   private final ApiAuthenticationEntryPoint apiAuthenticationEntryPoint; private final UiAuthenticationEntryPoint uiAuthenticationEntryPoint;
/*    */   
/*    */   @Bean
/*    */   SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
/* 38 */     http
/* 39 */       .securityMatcher(new String[] { "/api/**", "/swagger-ui/**", "/v3/api-docs/**"
/*    */         
/* 41 */         }).csrf(csrf -> csrf.disable())
/*    */       
/* 43 */       .authorizeHttpRequests(auth -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)auth.requestMatchers(new String[] {
/*    */ 
/*    */ 
/*    */             
/*    */             "/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**"
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 52 */           })).permitAll().anyRequest()).authenticated()).exceptionHandling(ex -> ex.authenticationEntryPoint((AuthenticationEntryPoint)this.apiAuthenticationEntryPoint))
/*    */ 
/*    */ 
/*    */       
/* 56 */       .addFilterBefore((Filter)this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
/*    */ 
/*    */       
/* 59 */       .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
/*    */ 
/*    */ 
/*    */     
/* 63 */     return (SecurityFilterChain)http.build();
/*    */   }
/*    */ 
/*    */   
/*    */   @Bean
/*    */   SecurityFilterChain uiSecurity(HttpSecurity http) throws Exception {
/* 69 */     http
/* 70 */       .securityMatcher(new String[] {
/*    */ 
/*    */           
/*    */           "/ui/login", "/ui/logout", "/login", "/css/**", "/js/**", "/images/**"
/*    */ 
/*    */ 
/*    */         
/* 77 */         }).csrf(csrf -> csrf.disable())
/*    */       
/* 79 */       .authorizeHttpRequests(auth -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)auth.anyRequest()).permitAll())
/*    */ 
/*    */ 
/*    */       
/* 83 */       .formLogin(form -> form.disable())
/* 84 */       .httpBasic(basic -> basic.disable());
/*    */     
/* 86 */     return (SecurityFilterChain)http.build();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Bean
/*    */   public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
/* 94 */     return authenticationConfiguration.getAuthenticationManager();
/*    */   }
/*    */   
/*    */   @Bean
/*    */   PasswordEncoder passwordEncoder() {
/* 99 */     return (PasswordEncoder)new BCryptPasswordEncoder();
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/config/SecurityConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */