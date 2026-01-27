/*    */ package BOOT-INF.classes.com.qatraining.controller.ui;
/*    */ 
/*    */ import com.qatraining.dto.SalesSummaryDTO;
/*    */ import com.qatraining.service.ui.CategoryUIService;
/*    */ import com.qatraining.service.ui.DashboardUIService;
/*    */ import com.qatraining.service.ui.PlantUIService;
/*    */ import com.qatraining.util.UiSecurity;
/*    */ import jakarta.servlet.http.HttpSession;
/*    */ import org.springframework.stereotype.Controller;
/*    */ import org.springframework.ui.Model;
/*    */ import org.springframework.web.bind.annotation.GetMapping;
/*    */ 
/*    */ 
/*    */ @Controller
/*    */ public class DashboardUIController
/*    */ {
/*    */   private final CategoryUIService categoryUIService;
/*    */   private final PlantUIService plantUIService;
/*    */   private final DashboardUIService dashboardUIService;
/*    */   
/*    */   public DashboardUIController(CategoryUIService categoryUIService, PlantUIService plantUIService, DashboardUIService dashboardUIService) {
/* 22 */     this.categoryUIService = categoryUIService;
/* 23 */     this.plantUIService = plantUIService;
/* 24 */     this.dashboardUIService = dashboardUIService;
/*    */   }
/*    */ 
/*    */   
/*    */   @GetMapping({"/ui/dashboard"})
/*    */   public String dashboard(HttpSession session, Model model) {
/* 30 */     if (!UiSecurity.isLoggedIn(session)) {
/* 31 */       return "redirect:/ui/login";
/*    */     }
/*    */     
/* 34 */     if (!UiSecurity.hasRole(session, "ROLE_ADMIN") && 
/* 35 */       !UiSecurity.hasRole(session, "ROLE_USER")) {
/* 36 */       return "redirect:/ui/403";
/*    */     }
/*    */     
/* 39 */     model.addAttribute("activeMenu", "dashboard");
/*    */ 
/*    */     
/* 42 */     model.addAttribute("categorySummary", this.categoryUIService
/*    */         
/* 44 */         .getCategorySummary());
/*    */ 
/*    */ 
/*    */     
/* 48 */     model.addAttribute("plantSummary", this.plantUIService
/*    */         
/* 50 */         .getPlantSummary());
/*    */ 
/*    */ 
/*    */     
/* 54 */     SalesSummaryDTO salesSummary = this.dashboardUIService.getSalesSummary();
/*    */     
/* 56 */     model.addAttribute("salesSummary", salesSummary);
/*    */     
/* 58 */     return "dashboard";
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/controller/ui/DashboardUIController.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */