/*     */ package BOOT-INF.classes.com.qatraining.controller.ui;
/*     */ import com.qatraining.dto.CategoryCreateDTO;
/*     */ import com.qatraining.dto.CategoryEditResponseDTO;
/*     */ import com.qatraining.dto.CategoryResponseDTO;
/*     */ import com.qatraining.dto.PageResponse;
/*     */ import com.qatraining.service.ui.CategoryUIService;
/*     */ import com.qatraining.util.UiSecurity;
/*     */ import jakarta.servlet.http.HttpSession;
/*     */ import jakarta.validation.Valid;
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
/*     */ public class CategoryUIController {
/*     */   public CategoryUIController(CategoryUIService categoryUIService) {
/*  23 */     this.categoryUIService = categoryUIService;
/*     */   }
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
/*     */   @GetMapping({"/ui/categories"})
/*     */   public String categories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String name, @RequestParam(required = false) Long parentId, @RequestParam(defaultValue = "id") String sortField, @RequestParam(defaultValue = "asc") String sortDir, HttpSession session, Model model) {
/*  38 */     if (!UiSecurity.isLoggedIn(session)) {
/*  39 */       return "redirect:/ui/login";
/*     */     }
/*     */     
/*  42 */     if (!UiSecurity.hasRole(session, "ROLE_ADMIN") && 
/*  43 */       !UiSecurity.hasRole(session, "ROLE_USER")) {
/*  44 */       return "redirect:/ui/403";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  50 */     PageResponse<CategoryResponseDTO> pageResult = this.categoryUIService.getCategoriesPaged(page, size, name, parentId, sortField, sortDir);
/*     */ 
/*     */ 
/*     */     
/*  54 */     model.addAttribute("categories", pageResult.getContent());
/*  55 */     model.addAttribute("page", pageResult);
/*     */     
/*  57 */     model.addAttribute("parents", this.categoryUIService.getParentCategories());
/*     */     
/*  59 */     model.addAttribute("selectedParentId", parentId);
/*  60 */     model.addAttribute("searchName", name);
/*     */ 
/*     */     
/*  63 */     model.addAttribute("sortField", sortField);
/*  64 */     model.addAttribute("sortDir", sortDir);
/*     */     
/*  66 */     model.addAttribute("currentPage", Integer.valueOf(pageResult.getNumber()));
/*  67 */     model.addAttribute("totalPages", Integer.valueOf(pageResult.getTotalPages()));
/*  68 */     model.addAttribute("activePage", "categories");
/*     */ 
/*     */     
/*  71 */     return "categories";
/*     */   }
/*     */ 
/*     */   
/*     */   @GetMapping({"/ui/categories/add"})
/*     */   public String addCategoryForm(HttpSession session, Model model) {
/*  77 */     if (!UiSecurity.isLoggedIn(session)) {
/*  78 */       return "redirect:/ui/login";
/*     */     }
/*  80 */     if (!UiSecurity.hasRole(session, "ROLE_ADMIN")) {
/*  81 */       return "redirect:/ui/403";
/*     */     }
/*     */     
/*  84 */     model.addAttribute("parents", this.categoryUIService.getParentCategories());
/*  85 */     model.addAttribute("category", new CategoryCreateDTO());
/*  86 */     model.addAttribute("activePage", "categories");
/*     */     
/*  88 */     return "categories-add";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/ui/categories/add"})
/*     */   public String addCategorySubmit(@Valid @ModelAttribute("category") CategoryCreateDTO dto, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
/* 100 */     if (!UiSecurity.isLoggedIn(session)) {
/* 101 */       return "redirect:/ui/login";
/*     */     }
/*     */     
/* 104 */     if (!UiSecurity.hasRole(session, "ROLE_ADMIN") && 
/* 105 */       !UiSecurity.hasRole(session, "ROLE_USER")) {
/* 106 */       return "redirect:/ui/403";
/*     */     }
/*     */     
/* 109 */     if (bindingResult.hasErrors()) {
/* 110 */       model.addAttribute("parents", this.categoryUIService.getParentCategories());
/* 111 */       model.addAttribute("activePage", "categories");
/* 112 */       return "categories-add";
/*     */     } 
/*     */     
/*     */     try {
/* 116 */       this.categoryUIService.createCategory(dto);
/* 117 */       redirectAttributes.addFlashAttribute("successMessage", "Category created successfully");
/*     */     }
/* 119 */     catch (Exception e) {
/* 120 */       model.addAttribute("parents", this.categoryUIService.getParentCategories());
/* 121 */       model.addAttribute("errorMessage", e.getMessage());
/* 122 */       model.addAttribute("activePage", "categories");
/* 123 */       return "categories-add";
/*     */     } 
/*     */     
/* 126 */     return "redirect:/ui/categories";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/ui/categories/edit/{id}"})
/*     */   public String editCategoryForm(@PathVariable Long id, HttpSession session, Model model) {
/* 137 */     if (!UiSecurity.isLoggedIn(session)) {
/* 138 */       return "redirect:/ui/login";
/*     */     }
/*     */     
/* 141 */     if (!UiSecurity.hasRole(session, "ROLE_ADMIN") && 
/* 142 */       !UiSecurity.hasRole(session, "ROLE_USER")) {
/* 143 */       return "redirect:/ui/403";
/*     */     }
/*     */ 
/*     */     
/* 147 */     CategoryEditResponseDTO category = this.categoryUIService.getCategoryById(id);
/*     */     
/* 149 */     CategoryCreateDTO dto = new CategoryCreateDTO();
/* 150 */     dto.setName(category.getName());
/* 151 */     dto.setParentId(category.getParentId());
/*     */     
/* 153 */     model.addAttribute("category", dto);
/* 154 */     model.addAttribute("parents", this.categoryUIService.getParentCategories());
/* 155 */     model.addAttribute("categoryId", id);
/* 156 */     model.addAttribute("activePage", "categories");
/*     */     
/* 158 */     return "categories-add";
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
/*     */   @PostMapping({"/ui/categories/edit/{id}"})
/*     */   public String editCategorySubmit(@PathVariable Long id, @Valid @ModelAttribute("category") CategoryCreateDTO dto, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
/* 171 */     if (!UiSecurity.isLoggedIn(session)) {
/* 172 */       return "redirect:/ui/login";
/*     */     }
/*     */     
/* 175 */     if (!UiSecurity.hasRole(session, "ROLE_ADMIN") && 
/* 176 */       !UiSecurity.hasRole(session, "ROLE_USER")) {
/* 177 */       return "redirect:/ui/403";
/*     */     }
/*     */     
/* 180 */     if (bindingResult.hasErrors()) {
/* 181 */       model.addAttribute("parents", this.categoryUIService.getParentCategories());
/* 182 */       model.addAttribute("categoryId", id);
/* 183 */       model.addAttribute("activePage", "categories");
/* 184 */       return "categories-add";
/*     */     } 
/*     */     
/*     */     try {
/* 188 */       this.categoryUIService.updateCategory(id, dto);
/* 189 */       redirectAttributes.addFlashAttribute("successMessage", "Category updated successfully");
/*     */     }
/* 191 */     catch (Exception e) {
/* 192 */       model.addAttribute("parents", this.categoryUIService.getParentCategories());
/* 193 */       model.addAttribute("categoryId", id);
/* 194 */       model.addAttribute("errorMessage", e.getMessage());
/* 195 */       model.addAttribute("activePage", "categories");
/* 196 */       return "categories-add";
/*     */     } 
/*     */     
/* 199 */     return "redirect:/ui/categories";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/ui/categories/delete/{id}"})
/*     */   public String deleteCategory(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
/* 209 */     if (!UiSecurity.isLoggedIn(session)) {
/* 210 */       return "redirect:/ui/login";
/*     */     }
/*     */     
/* 213 */     if (!UiSecurity.hasRole(session, "ROLE_ADMIN") && 
/* 214 */       !UiSecurity.hasRole(session, "ROLE_USER")) {
/* 215 */       return "redirect:/ui/403";
/*     */     }
/*     */     
/*     */     try {
/* 219 */       this.categoryUIService.deleteCategory(id);
/* 220 */       redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully");
/* 221 */     } catch (RuntimeException e) {
/* 222 */       redirectAttributes.addFlashAttribute("errorMessage", e
/* 223 */           .getMessage());
/*     */     } 
/*     */ 
/*     */     
/* 227 */     return "redirect:/ui/categories";
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/controller/ui/CategoryUIController.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */