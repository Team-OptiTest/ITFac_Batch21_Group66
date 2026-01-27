/*    */ package BOOT-INF.classes.com.qatraining.security.jwt;
/*    */ import com.qatraining.security.jwt.JwtUtil;
/*    */ import jakarta.servlet.FilterChain;
/*    */ import jakarta.servlet.ServletException;
/*    */ import jakarta.servlet.ServletRequest;
/*    */ import jakarta.servlet.ServletResponse;
/*    */ import jakarta.servlet.http.HttpServletRequest;
/*    */ import jakarta.servlet.http.HttpServletResponse;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
/*    */ import org.springframework.security.core.Authentication;
/*    */ import org.springframework.security.core.authority.SimpleGrantedAuthority;
/*    */ import org.springframework.security.core.context.SecurityContextHolder;
/*    */ import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
/*    */ import org.springframework.stereotype.Component;
/*    */ import org.springframework.web.filter.OncePerRequestFilter;
/*    */ 
/*    */ @Component
/*    */ public class JwtAuthenticationFilter extends OncePerRequestFilter {
/*    */   public JwtAuthenticationFilter(JwtUtil jwtUtil) {
/* 24 */     this.jwtUtil = jwtUtil;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private final JwtUtil jwtUtil;
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/* 34 */     String authHeader = request.getHeader("Authorization");
/*    */     
/* 36 */     if (authHeader != null && authHeader.startsWith("Bearer ")) {
/*    */       
/* 38 */       String token = authHeader.substring(7);
/*    */       
/* 40 */       if (this.jwtUtil.validateToken(token)) {
/*    */         
/* 42 */         String username = this.jwtUtil.extractUsername(token);
/* 43 */         List<String> roles = this.jwtUtil.extractRoles(token);
/*    */ 
/*    */ 
/*    */         
/* 47 */         List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>)roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
/*    */         
/* 49 */         UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 55 */         authentication.setDetails((new WebAuthenticationDetailsSource())
/* 56 */             .buildDetails(request));
/*    */ 
/*    */         
/* 59 */         SecurityContextHolder.getContext()
/* 60 */           .setAuthentication((Authentication)authentication);
/*    */       } 
/*    */     } 
/*    */     
/* 64 */     filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/security/jwt/JwtAuthenticationFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */