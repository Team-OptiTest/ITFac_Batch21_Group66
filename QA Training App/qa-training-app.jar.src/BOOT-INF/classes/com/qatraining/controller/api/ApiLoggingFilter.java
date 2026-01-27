/*    */ package BOOT-INF.classes.com.qatraining.controller.api;
/*    */ 
/*    */ import jakarta.servlet.FilterChain;
/*    */ import jakarta.servlet.ServletException;
/*    */ import jakarta.servlet.ServletRequest;
/*    */ import jakarta.servlet.ServletResponse;
/*    */ import jakarta.servlet.http.HttpServletRequest;
/*    */ import jakarta.servlet.http.HttpServletResponse;
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.stereotype.Component;
/*    */ import org.springframework.web.filter.OncePerRequestFilter;
/*    */ import org.springframework.web.util.ContentCachingRequestWrapper;
/*    */ import org.springframework.web.util.ContentCachingResponseWrapper;
/*    */ 
/*    */ @Component
/*    */ public class ApiLoggingFilter
/*    */   extends OncePerRequestFilter {
/* 21 */   private static final Logger log = LoggerFactory.getLogger(com.qatraining.controller.api.ApiLoggingFilter.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/* 31 */     if (!request.getRequestURI().startsWith("/api/")) {
/* 32 */       filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
/*    */       
/*    */       return;
/*    */     } 
/* 36 */     ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
/*    */     
/* 38 */     ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
/*    */ 
/*    */     
/* 41 */     long start = System.currentTimeMillis();
/*    */     
/* 43 */     filterChain.doFilter((ServletRequest)wrappedRequest, (ServletResponse)wrappedResponse);
/*    */     
/* 45 */     long duration = System.currentTimeMillis() - start;
/*    */ 
/*    */     
/* 48 */     String requestBody = new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
/*    */ 
/*    */ 
/*    */     
/* 52 */     String responseBody = new String(wrappedResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
/*    */ 
/*    */     
/* 55 */     log.info("===== API CALL =====\n{} {}\nStatus: {}\nDuration: {} ms\nRequest Body: {}\nResponse Body: {}\n====================\n", new Object[] { request
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 64 */           .getMethod(), request
/* 65 */           .getRequestURI(), 
/* 66 */           Integer.valueOf(response.getStatus()), 
/* 67 */           Long.valueOf(duration), requestBody, responseBody });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 73 */     wrappedResponse.copyBodyToResponse();
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/controller/api/ApiLoggingFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */