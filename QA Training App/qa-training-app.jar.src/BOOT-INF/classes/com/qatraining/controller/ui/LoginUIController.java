/*    */ package BOOT-INF.classes.com.qatraining.controller.ui;
/*    */ 
/*    */ import com.qatraining.config.ApiConfig;
/*    */ import com.qatraining.dto.JwtLoginResponse;
/*    */ import com.qatraining.util.JwtUtils;
/*    */ import jakarta.servlet.http.HttpSession;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Controller;
/*    */ import org.springframework.web.bind.annotation.GetMapping;
/*    */ import org.springframework.web.bind.annotation.PostMapping;
/*    */ import org.springframework.web.bind.annotation.RequestParam;
/*    */ import org.springframework.web.client.HttpClientErrorException;
/*    */ import org.springframework.web.client.RestTemplate;
/*    */ 
/*    */ @Controller
/*    */ public class LoginUIController {
/*    */   @Autowired
/*    */   private ApiConfig apiConfig;
/*    */   private final RestTemplate restTemplate;
/*    */   
/*    */   public LoginUIController(RestTemplate restTemplate) {
/* 26 */     this.restTemplate = restTemplate;
/*    */   }
/*    */   
/*    */   @GetMapping({"/ui/login"})
/*    */   public String loginPage() {
/* 31 */     return "login";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PostMapping({"/ui/login"})
/*    */   public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
/* 40 */     HashMap<String, String> request = new HashMap<>();
/* 41 */     request.put("username", username);
/* 42 */     request.put("password", password);
/*    */     
/* 44 */     String url = this.apiConfig.loginUrl();
/*    */ 
/*    */     
/*    */     try {
/* 48 */       JwtLoginResponse response = (JwtLoginResponse)this.restTemplate.postForObject(url, request, JwtLoginResponse.class, new Object[0]);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 54 */       if (response == null || response.getToken() == null) {
/* 55 */         return "redirect:/ui/login?error";
/*    */       }
/*    */ 
/*    */       
/* 59 */       session.setAttribute("JWT_TOKEN", response.getToken());
/*    */ 
/*    */       
/* 62 */       Map<String, Object> payload = JwtUtils.decodePayload(response.getToken());
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 67 */       List<String> roles = ((List)payload.get("roles")).stream().map(Object::toString).toList();
/*    */       
/* 69 */       session.setAttribute("USER_ROLES", new HashSet<>(roles));
/*    */       
/* 71 */       return "redirect:/ui/dashboard";
/*    */     }
/* 73 */     catch (org.springframework.web.client.HttpClientErrorException.Unauthorized ex) {
/*    */       
/* 75 */       return "redirect:/ui/login?error";
/* 76 */     } catch (Exception ex) {
/*    */       
/* 78 */       ex.printStackTrace();
/* 79 */       return "redirect:/ui/login?error";
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/controller/ui/LoginUIController.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */