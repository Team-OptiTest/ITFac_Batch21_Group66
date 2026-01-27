/*    */ package BOOT-INF.classes.com.qatraining.controller.ui;
/*    */ 
/*    */ import org.springframework.stereotype.Controller;
/*    */ import org.springframework.web.bind.annotation.GetMapping;
/*    */ 
/*    */ @Controller
/*    */ public class ErrorUIController
/*    */ {
/*    */   @GetMapping({"/ui/403"})
/*    */   public String forbidden() {
/* 11 */     return "403";
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/controller/ui/ErrorUIController.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */