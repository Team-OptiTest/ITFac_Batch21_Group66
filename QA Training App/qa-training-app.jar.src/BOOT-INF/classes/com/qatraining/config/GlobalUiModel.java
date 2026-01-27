/*    */ package BOOT-INF.classes.com.qatraining.config;
/*    */ 
/*    */ import jakarta.servlet.http.HttpSession;
/*    */ import org.springframework.web.bind.annotation.ControllerAdvice;
/*    */ import org.springframework.web.bind.annotation.ModelAttribute;
/*    */ 
/*    */ @ControllerAdvice
/*    */ public class GlobalUiModel
/*    */ {
/*    */   @ModelAttribute("roles")
/*    */   public Object roles(HttpSession session) {
/* 12 */     return session.getAttribute("USER_ROLES");
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/config/GlobalUiModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */