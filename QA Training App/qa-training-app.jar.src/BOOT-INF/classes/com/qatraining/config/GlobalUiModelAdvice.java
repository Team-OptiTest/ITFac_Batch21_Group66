/*    */ package BOOT-INF.classes.com.qatraining.config;
/*    */ 
/*    */ import com.qatraining.util.UiSecurity;
/*    */ import jakarta.servlet.http.HttpSession;
/*    */ import org.springframework.web.bind.annotation.ControllerAdvice;
/*    */ import org.springframework.web.bind.annotation.ModelAttribute;
/*    */ 
/*    */ @ControllerAdvice
/*    */ public class GlobalUiModelAdvice
/*    */ {
/*    */   @ModelAttribute("isAdmin")
/*    */   public boolean isAdmin(HttpSession session) {
/* 13 */     return UiSecurity.hasRole(session, "ROLE_ADMIN");
/*    */   }
/*    */   
/*    */   @ModelAttribute("isUser")
/*    */   public boolean isUser(HttpSession session) {
/* 18 */     return UiSecurity.hasRole(session, "ROLE_USER");
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/config/GlobalUiModelAdvice.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */