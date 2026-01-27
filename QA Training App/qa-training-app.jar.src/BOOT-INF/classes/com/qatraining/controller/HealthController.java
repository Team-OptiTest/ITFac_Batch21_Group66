/*    */ package BOOT-INF.classes.com.qatraining.controller;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.web.bind.annotation.GetMapping;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ 
/*    */ @RestController
/*    */ @RequestMapping({"/api"})
/*    */ public class HealthController
/*    */ {
/*    */   @GetMapping({"/health"})
/*    */   public Map<String, String> health() {
/* 15 */     return Map.of("status", "UP");
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/controller/HealthController.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */