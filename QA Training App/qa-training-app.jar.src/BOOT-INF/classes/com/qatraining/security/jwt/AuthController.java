/*    */ package BOOT-INF.classes.com.qatraining.security.jwt;
/*    */ 
/*    */ import com.qatraining.security.jwt.JwtUtil;
/*    */ import com.qatraining.security.jwt.dto.JwtLoginRequest;
/*    */ import com.qatraining.security.jwt.dto.JwtLoginResponse;
/*    */ import java.util.List;
/*    */ import org.springframework.security.authentication.AuthenticationManager;
/*    */ import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
/*    */ import org.springframework.security.core.Authentication;
/*    */ import org.springframework.security.core.GrantedAuthority;
/*    */ import org.springframework.web.bind.annotation.PostMapping;
/*    */ import org.springframework.web.bind.annotation.RequestBody;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ @RestController
/*    */ @RequestMapping({"/api/auth"})
/*    */ public class AuthController {
/*    */   private final AuthenticationManager authenticationManager;
/*    */   
/*    */   public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
/* 22 */     this.authenticationManager = authenticationManager;
/* 23 */     this.jwtUtil = jwtUtil;
/*    */   }
/*    */   private final JwtUtil jwtUtil;
/*    */   
/*    */   @PostMapping({"/login"})
/*    */   public JwtLoginResponse login(@RequestBody JwtLoginRequest request) {
/* 29 */     Authentication authentication = this.authenticationManager.authenticate((Authentication)new UsernamePasswordAuthenticationToken(request
/*    */           
/* 31 */           .getUsername(), request
/* 32 */           .getPassword()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 39 */     List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
/*    */     
/* 41 */     String token = this.jwtUtil.generateToken(authentication
/* 42 */         .getName(), roles);
/*    */ 
/*    */ 
/*    */     
/* 46 */     return new JwtLoginResponse(token);
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/security/jwt/AuthController.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */