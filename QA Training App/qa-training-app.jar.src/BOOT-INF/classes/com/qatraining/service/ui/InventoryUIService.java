/*    */ package BOOT-INF.classes.com.qatraining.service.ui;
/*    */ 
/*    */ import com.qatraining.dto.InventoryDTO;
/*    */ import com.qatraining.entity.Inventory;
/*    */ import com.qatraining.service.api.InventoryService;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class InventoryUIService
/*    */ {
/*    */   private final InventoryService inventoryService;
/*    */   
/*    */   public InventoryUIService(InventoryService inventoryService) {
/* 17 */     this.inventoryService = inventoryService;
/*    */   }
/*    */   
/*    */   public List<InventoryDTO> getInventoryByPlant(Long plantId) {
/* 21 */     List<Inventory> inventories = this.inventoryService.getInventoryByPlant(plantId);
/*    */     
/* 23 */     return (List<InventoryDTO>)inventories.stream()
/* 24 */       .map(inv -> new InventoryDTO(inv.getId(), inv.getPlant().getId(), inv.getPlant().getName(), inv.getType(), inv.getQuantity(), inv.getNote(), inv.getCreatedAt()))
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 33 */       .collect(Collectors.toList());
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/service/ui/InventoryUIService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */