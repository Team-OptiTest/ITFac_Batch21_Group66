/*    */ package BOOT-INF.classes.com.qatraining.controller.ui;
/*    */ 
/*    */ import jakarta.servlet.http.HttpServletRequest;
/*    */ import jakarta.servlet.http.HttpSession;
/*    */ import org.springframework.security.core.context.SecurityContextHolder;
/*    */ import org.springframework.stereotype.Controller;
/*    */ import org.springframework.web.bind.annotation.GetMapping;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Controller
/*    */ public class LogoutUIController
/*    */ {
/*    */   @GetMapping({"/ui/logout"})
/*    */   public String logout(HttpServletRequest request, HttpSession session) {
/* 19 */     session.invalidate();
/*    */ 
/*    */     
/* 22 */     SecurityContextHolder.clearContext();
/*    */     
/* 24 */     return "redirect:/ui/login?logout";
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/controller/ui/LogoutUIController.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */