/*    */ package BOOT-INF.classes.com.qatraining.service.api;
/*    */ 
/*    */ import com.qatraining.entity.Inventory;
/*    */ import com.qatraining.entity.InventoryType;
/*    */ import com.qatraining.entity.Plant;
/*    */ import com.qatraining.repository.InventoryRepository;
/*    */ import com.qatraining.repository.PlantRepository;
/*    */ import java.time.LocalDateTime;
/*    */ import java.util.List;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class InventoryService
/*    */ {
/*    */   private final InventoryRepository inventoryRepository;
/*    */   private final PlantRepository plantRepository;
/*    */   
/*    */   public InventoryService(InventoryRepository inventoryRepository, PlantRepository plantRepository) {
/* 21 */     this.inventoryRepository = inventoryRepository;
/* 22 */     this.plantRepository = plantRepository;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Inventory adjustStock(Long plantId, Inventory inventory) {
/* 28 */     Plant plant = (Plant)this.plantRepository.findById(plantId).orElseThrow(() -> new RuntimeException("Plant not found"));
/*    */     
/* 30 */     if (inventory.getType() == InventoryType.OUT && plant.getQuantity().intValue() < inventory.getQuantity()) {
/* 31 */       throw new RuntimeException("Not enough stock");
/*    */     }
/*    */     
/* 34 */     inventory.setPlant(plant);
/* 35 */     inventory.setCreatedAt(LocalDateTime.now());
/*    */     
/* 37 */     if (inventory.getType() == InventoryType.IN) {
/* 38 */       plant.setQuantity(Integer.valueOf(plant.getQuantity().intValue() + inventory.getQuantity()));
/*    */     } else {
/* 40 */       plant.setQuantity(Integer.valueOf(plant.getQuantity().intValue() - inventory.getQuantity()));
/*    */     } 
/*    */     
/* 43 */     this.plantRepository.save(plant);
/* 44 */     return (Inventory)this.inventoryRepository.save(inventory);
/*    */   }
/*    */   
/*    */   public List<Inventory> getInventoryByPlant(Long plantId) {
/* 48 */     return this.inventoryRepository.findByPlantId(plantId);
/*    */   }
/*    */ 
/*    */   
/*    */   public Inventory update(Inventory inventory) {
/* 53 */     return (Inventory)this.inventoryRepository.save(inventory);
/*    */   }
/*    */   
/*    */   public void delete(Long inventoryId) {
/* 57 */     this.inventoryRepository.deleteById(inventoryId);
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/service/api/InventoryService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */