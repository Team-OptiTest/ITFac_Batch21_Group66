/*    */ package BOOT-INF.classes.com.qatraining.controller.ui;
/*    */ 
/*    */ import com.qatraining.entity.Inventory;
/*    */ import com.qatraining.entity.Plant;
/*    */ import com.qatraining.repository.PlantRepository;
/*    */ import com.qatraining.service.api.InventoryService;
/*    */ import java.util.List;
/*    */ import org.springframework.stereotype.Controller;
/*    */ import org.springframework.ui.Model;
/*    */ import org.springframework.web.bind.annotation.GetMapping;
/*    */ import org.springframework.web.bind.annotation.PathVariable;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Controller
/*    */ public class InventoryUIController
/*    */ {
/*    */   private final InventoryService inventoryService;
/*    */   private final PlantRepository plantRepository;
/*    */   
/*    */   public InventoryUIController(InventoryService inventoryService, PlantRepository plantRepository) {
/* 22 */     this.inventoryService = inventoryService;
/* 23 */     this.plantRepository = plantRepository;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @GetMapping({"/ui/inventory/plant/{plantId}"})
/*    */   public String viewInventory(@PathVariable Long plantId, Model model) {
/* 30 */     Plant plant = (Plant)this.plantRepository.findById(plantId).orElseThrow(() -> new RuntimeException("Plant not found"));
/*    */     
/* 32 */     List<Inventory> inventoryList = this.inventoryService.getInventoryByPlant(plantId);
/*    */     
/* 34 */     model.addAttribute("plantName", plant.getName());
/* 35 */     model.addAttribute("plantId", plant.getId());
/* 36 */     model.addAttribute("inventoryList", inventoryList);
/*    */     
/* 38 */     return "inventory";
/*    */   }
/*    */ 
/*    */   
/*    */   @GetMapping({"/ui/inventory"})
/*    */   public String redirectDefault() {
/* 44 */     return "redirect:/ui/inventory/plant/1";
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/controller/ui/InventoryUIController.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */