/*    */ package BOOT-INF.classes.com.qatraining.config;
/*    */ 
/*    */ import io.swagger.v3.oas.models.Components;
/*    */ import io.swagger.v3.oas.models.OpenAPI;
/*    */ import io.swagger.v3.oas.models.info.Info;
/*    */ import io.swagger.v3.oas.models.security.SecurityRequirement;
/*    */ import io.swagger.v3.oas.models.security.SecurityScheme;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ public class OpenApiConfig
/*    */ {
/*    */   @Bean
/*    */   public OpenAPI openAPI() {
/* 22 */     SecurityScheme jwtScheme = (new SecurityScheme()).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").in(SecurityScheme.In.HEADER).name("Authorization");
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 27 */     SecurityScheme basicScheme = (new SecurityScheme()).type(SecurityScheme.Type.HTTP).scheme("basic");
/*    */ 
/*    */     
/* 30 */     return (new OpenAPI())
/* 31 */       .info((new Info())
/* 32 */         .title("QA Training Application")
/* 33 */         .version("1.0.0")
/* 34 */         .description("**Flower Inventory Management System**\n\n### 👤 User Roles\n**ADMIN**\n- Full access (GET, POST, PUT, DELETE)\n- Can manage Categories, Plants, Inventory, Sales\n\n**TESTUSER**\n- Read-only access (GET)\n- Cannot create, update, or delete data\n\n### 🔐 Authentication\n- JWT Authentication\n- Use Swagger **Authorize** button\n\n### 🎯 Purpose\nThis application is intentionally designed for\n**API & UI automation testing and bug discovery**.\n"))
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 55 */       .addSecurityItem((new SecurityRequirement()).addList("basicAuth"))
/* 56 */       .components((new Components())
/*    */         
/* 58 */         .addSecuritySchemes("BearerAuth", jwtScheme))
/*    */ 
/*    */       
/* 61 */       .addSecurityItem((new SecurityRequirement()).addList("BearerAuth"))
/* 62 */       .addSecurityItem((new SecurityRequirement()).addList("BasicAuth"));
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/config/OpenApiConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */