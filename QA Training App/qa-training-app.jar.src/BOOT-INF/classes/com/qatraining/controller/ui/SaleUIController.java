/*     */ package BOOT-INF.classes.com.qatraining.controller.ui;
/*     */ 
/*     */ import com.qatraining.dto.SaleCreateDTO;
/*     */ import com.qatraining.entity.Sale;
/*     */ import com.qatraining.service.ui.PlantUIService;
/*     */ import com.qatraining.service.ui.SaleUIService;
/*     */ import com.qatraining.util.UiSecurity;
/*     */ import jakarta.servlet.http.HttpSession;
/*     */ import jakarta.validation.Valid;
/*     */ import org.springframework.data.domain.Page;
/*     */ import org.springframework.stereotype.Controller;
/*     */ import org.springframework.ui.Model;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
/*     */ import org.springframework.web.bind.annotation.ModelAttribute;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/*     */ 
/*     */ @Controller
/*     */ public class SaleUIController {
/*     */   private final SaleUIService saleUIService;
/*     */   private final PlantUIService plantUIService;
/*     */   
/*     */   public SaleUIController(SaleUIService saleUIService, PlantUIService plantUIService) {
/*  27 */     this.saleUIService = saleUIService;
/*  28 */     this.plantUIService = plantUIService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/ui/sales"})
/*     */   public String salesPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "soldAt") String sortField, @RequestParam(defaultValue = "desc") String sortDir, Model model, HttpSession session) {
/*  40 */     if (!UiSecurity.isLoggedIn(session)) {
/*  41 */       return "redirect:/ui/login";
/*     */     }
/*     */     
/*  44 */     if (!UiSecurity.hasRole(session, "ROLE_ADMIN") && 
/*  45 */       !UiSecurity.hasRole(session, "ROLE_USER")) {
/*  46 */       return "redirect:/ui/403";
/*     */     }
/*     */ 
/*     */     
/*  50 */     Page<Sale> salesPage = this.saleUIService.getSalesPaged(page, size, sortField, sortDir);
/*     */     
/*  52 */     model.addAttribute("sales", salesPage.getContent());
/*  53 */     model.addAttribute("page", salesPage);
/*  54 */     model.addAttribute("sortField", sortField);
/*  55 */     model.addAttribute("sortDir", sortDir);
/*  56 */     model.addAttribute("activeMenu", "sales");
/*     */     
/*  58 */     return "sales";
/*     */   }
/*     */ 
/*     */   
/*     */   @GetMapping({"/ui/sales/new"})
/*     */   public String showSellForm(Model model, HttpSession session) {
/*  64 */     if (!UiSecurity.isLoggedIn(session)) {
/*  65 */       return "redirect:/ui/login";
/*     */     }
/*     */     
/*  68 */     if (!UiSecurity.hasRole(session, "ROLE_ADMIN")) {
/*  69 */       return "redirect:/ui/403";
/*     */     }
/*     */     
/*  72 */     model.addAttribute("activeMenu", "sales");
/*  73 */     model.addAttribute("sale", new SaleCreateDTO());
/*  74 */     model.addAttribute("plants", this.plantUIService.getAvailablePlants());
/*     */     
/*  76 */     return "sale-form";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/ui/sales"})
/*     */   public String sellPlant(@Valid @ModelAttribute("sale") SaleCreateDTO dto, BindingResult result, Model model, HttpSession session) {
/*  86 */     if (!UiSecurity.hasRole(session, "ROLE_ADMIN")) {
/*  87 */       return "redirect:/ui/403";
/*     */     }
/*     */     
/*  90 */     if (result.hasErrors()) {
/*  91 */       model.addAttribute("plants", this.plantUIService.getAvailablePlants());
/*  92 */       return "sale-form";
/*     */     } 
/*     */     
/*     */     try {
/*  96 */       this.saleUIService.sellPlant(dto.getPlantId(), dto.getQuantity());
/*  97 */       return "redirect:/ui/sales";
/*     */     }
/*  99 */     catch (Exception ex) {
/*     */       
/* 101 */       model.addAttribute("errorMessage", ex.getMessage());
/* 102 */       model.addAttribute("plants", this.plantUIService.getAvailablePlants());
/* 103 */       return "sale-form";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/ui/sales/delete/{id}"})
/*     */   public String deleteSale(@PathVariable Long id, RedirectAttributes redirectAttributes) {
/*     */     try {
/* 113 */       this.saleUIService.deleteSale(id);
/* 114 */       redirectAttributes.addFlashAttribute("successMessage", "Sale deleted successfully");
/*     */ 
/*     */     
/*     */     }
/* 118 */     catch (RuntimeException ex) {
/* 119 */       redirectAttributes.addFlashAttribute("errorMessage", ex
/*     */           
/* 121 */           .getMessage());
/*     */     } 
/*     */ 
/*     */     
/* 125 */     return "redirect:/ui/sales";
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/controller/ui/SaleUIController.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */