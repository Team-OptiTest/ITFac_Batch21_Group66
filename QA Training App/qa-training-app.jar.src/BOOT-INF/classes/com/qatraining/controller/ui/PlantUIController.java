/*     */ package BOOT-INF.classes.com.qatraining.controller.ui;
/*     */ 
/*     */ import com.qatraining.dto.PlantCreateDTO;
/*     */ import com.qatraining.dto.PlantDTO;
/*     */ import com.qatraining.dto.PlantEditResponseDTO;
/*     */ import com.qatraining.service.ui.CategoryUIService;
/*     */ import com.qatraining.service.ui.PlantUIService;
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
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/*     */ 
/*     */ @Controller
/*     */ @RequestMapping({"/ui/plants"})
/*     */ public class PlantUIController {
/*     */   private final PlantUIService plantUIService;
/*     */   
/*     */   public PlantUIController(PlantUIService plantUIService, CategoryUIService categoryUIService) {
/*  29 */     this.plantUIService = plantUIService;
/*  30 */     this.categoryUIService = categoryUIService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final CategoryUIService categoryUIService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping
/*     */   public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "name") String sortField, @RequestParam(defaultValue = "asc") String sortDir, @RequestParam(required = false) String name, @RequestParam(required = false) Long categoryId, Model model, HttpSession session) {
/*  47 */     if (!UiSecurity.isLoggedIn(session)) {
/*  48 */       return "redirect:/ui/login";
/*     */     }
/*     */     
/*  51 */     if (!UiSecurity.hasRole(session, "ROLE_ADMIN") && 
/*  52 */       !UiSecurity.hasRole(session, "ROLE_USER")) {
/*  53 */       return "redirect:/ui/403";
/*     */     }
/*     */ 
/*     */     
/*  57 */     Page<PlantDTO> plantPage = this.plantUIService.getPlantsPaged(page, size, sortField, sortDir, name, categoryId);
/*     */ 
/*     */ 
/*     */     
/*  61 */     model.addAttribute("plants", plantPage.getContent());
/*  62 */     model.addAttribute("page", plantPage);
/*     */     
/*  64 */     model.addAttribute("sortField", sortField);
/*  65 */     model.addAttribute("sortDir", sortDir);
/*  66 */     model.addAttribute("searchName", name);
/*  67 */     model.addAttribute("selectedCategoryId", categoryId);
/*     */     
/*  69 */     model.addAttribute("categories", this.categoryUIService
/*     */         
/*  71 */         .getSubCategoriesOnly());
/*     */ 
/*     */     
/*  74 */     model.addAttribute("activePage", "plants");
/*     */     
/*  76 */     return "plants";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/add"})
/*     */   public String addPlantForm(HttpSession session, Model model) {
/*  85 */     if (!UiSecurity.isLoggedIn(session)) {
/*  86 */       return "redirect:/ui/login";
/*     */     }
/*  88 */     if (!UiSecurity.hasRole(session, "ROLE_ADMIN")) {
/*  89 */       return "redirect:/ui/403";
/*     */     }
/*     */     
/*  92 */     model.addAttribute("plant", new PlantCreateDTO());
/*  93 */     model.addAttribute("categories", this.plantUIService.getSubCategoriesOnly());
/*  94 */     model.addAttribute("activePage", "plants");
/*     */     
/*  96 */     return "plants-add";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/add"})
/*     */   public String addPlantSubmit(@Valid @ModelAttribute("plant") PlantCreateDTO dto, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
/* 108 */     if (!UiSecurity.hasRole(session, "ROLE_ADMIN")) {
/* 109 */       return "redirect:/ui/403";
/*     */     }
/*     */     
/* 112 */     if (bindingResult.hasErrors()) {
/* 113 */       model.addAttribute("categories", this.plantUIService.getSubCategoriesOnly());
/* 114 */       return "plants-add";
/*     */     } 
/*     */     
/*     */     try {
/* 118 */       this.plantUIService.createPlant(dto);
/* 119 */       redirectAttributes.addFlashAttribute("successMessage", "Plant added successfully");
/*     */     }
/* 121 */     catch (Exception e) {
/* 122 */       model.addAttribute("categories", this.plantUIService.getSubCategoriesOnly());
/* 123 */       model.addAttribute("errorMessage", e.getMessage());
/* 124 */       System.out.println("++++++++++++++++++++++++" + e.getMessage());
/* 125 */       return "plants-add";
/*     */     } 
/*     */     
/* 128 */     return "redirect:/ui/plants";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/edit/{id}"})
/*     */   public String editPlantForm(@PathVariable Long id, HttpSession session, Model model) {
/* 140 */     if (!UiSecurity.hasRole(session, "ROLE_ADMIN")) {
/* 141 */       return "redirect:/ui/403";
/*     */     }
/*     */     
/* 144 */     PlantEditResponseDTO plant = this.plantUIService.getPlantById(id);
/*     */     
/* 146 */     PlantCreateDTO dto = new PlantCreateDTO();
/* 147 */     dto.setName(plant.getName());
/* 148 */     dto.setPrice(plant.getPrice());
/* 149 */     dto.setQuantity(plant.getQuantity());
/* 150 */     dto.setCategoryId(plant.getCategoryId());
/*     */     
/* 152 */     model.addAttribute("plant", dto);
/* 153 */     model.addAttribute("plantId", id);
/* 154 */     model.addAttribute("categories", this.plantUIService.getSubCategoriesOnly());
/*     */     
/* 156 */     return "plants-add";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/edit/{id}"})
/*     */   public String editPlantSubmit(@PathVariable Long id, @Valid @ModelAttribute("plant") PlantCreateDTO dto, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
/* 169 */     if (!UiSecurity.hasRole(session, "ROLE_ADMIN")) {
/* 170 */       return "redirect:/ui/403";
/*     */     }
/*     */     
/* 173 */     if (bindingResult.hasErrors()) {
/* 174 */       model.addAttribute("categories", this.plantUIService.getSubCategoriesOnly());
/* 175 */       model.addAttribute("plantId", id);
/* 176 */       return "plants-add";
/*     */     } 
/*     */     
/*     */     try {
/* 180 */       this.plantUIService.updatePlant(id, dto);
/* 181 */       redirectAttributes.addFlashAttribute("successMessage", "Plant updated successfully");
/*     */     }
/* 183 */     catch (Exception e) {
/* 184 */       model.addAttribute("categories", this.plantUIService.getSubCategoriesOnly());
/* 185 */       model.addAttribute("errorMessage", e.getMessage());
/* 186 */       model.addAttribute("plantId", id);
/* 187 */       System.out.println("#######################" + e.getMessage());
/* 188 */       return "plants-add";
/*     */     } 
/*     */     
/* 191 */     return "redirect:/ui/plants";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/delete/{id}"})
/*     */   public String delete(@PathVariable Long id, RedirectAttributes redirect) {
/* 201 */     this.plantUIService.deletePlant(id);
/* 202 */     redirect.addFlashAttribute("successMessage", "Plant deleted successfully");
/*     */ 
/*     */ 
/*     */     
/* 206 */     return "redirect:/ui/plants";
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/controller/ui/PlantUIController.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */