/*    */ package BOOT-INF.classes.com.qatraining.dto;
/*    */ 
/*    */ import com.qatraining.entity.InventoryType;
/*    */ import java.time.LocalDateTime;
/*    */ 
/*    */ 
/*    */ public class InventoryDTO
/*    */ {
/*    */   private Long id;
/*    */   private Long plantId;
/*    */   private String plantName;
/*    */   private InventoryType type;
/*    */   private int quantity;
/*    */   private String note;
/*    */   private LocalDateTime createdAt;
/*    */   
/*    */   public InventoryDTO(Long id, Long plantId, String plantName, InventoryType type, int quantity, String note, LocalDateTime createdAt) {
/* 18 */     this.id = id;
/* 19 */     this.plantId = plantId;
/* 20 */     this.plantName = plantName;
/* 21 */     this.type = type;
/* 22 */     this.quantity = quantity;
/* 23 */     this.note = note;
/* 24 */     this.createdAt = createdAt;
/*    */   }
/*    */   
/*    */   public Long getId() {
/* 28 */     return this.id;
/* 29 */   } public Long getPlantId() { return this.plantId; }
/* 30 */   public String getPlantName() { return this.plantName; }
/* 31 */   public InventoryType getType() { return this.type; }
/* 32 */   public int getQuantity() { return this.quantity; }
/* 33 */   public String getNote() { return this.note; } public LocalDateTime getCreatedAt() {
/* 34 */     return this.createdAt;
/*    */   }
/* 36 */   public void setId(Long id) { this.id = id; }
/* 37 */   public void setPlantId(Long plantId) { this.plantId = plantId; }
/* 38 */   public void setPlantName(String plantName) { this.plantName = plantName; }
/* 39 */   public void setType(InventoryType type) { this.type = type; }
/* 40 */   public void setQuantity(int quantity) { this.quantity = quantity; }
/* 41 */   public void setNote(String note) { this.note = note; } public void setCreatedAt(LocalDateTime createdAt) {
/* 42 */     this.createdAt = createdAt;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/dto/InventoryDTO.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */