/*    */ package BOOT-INF.classes.com.qatraining.dto;
/*    */ 
/*    */ import jakarta.validation.constraints.Min;
/*    */ import jakarta.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ public class SaleCreateDTO
/*    */ {
/*    */   @NotNull(message = "Plant is required")
/*    */   private Long plantId;
/*    */   @Min(value = 1L, message = "Quantity must be greater than 0")
/*    */   private int quantity;
/*    */   
/*    */   public Long getPlantId() {
/* 15 */     return this.plantId;
/*    */   }
/*    */   
/*    */   public int getQuantity() {
/* 19 */     return this.quantity;
/*    */   }
/*    */   
/*    */   public void setPlantId(Long plantId) {
/* 23 */     this.plantId = plantId;
/*    */   }
/*    */   
/*    */   public void setQuantity(int quantity) {
/* 27 */     this.quantity = quantity;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/dto/SaleCreateDTO.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */