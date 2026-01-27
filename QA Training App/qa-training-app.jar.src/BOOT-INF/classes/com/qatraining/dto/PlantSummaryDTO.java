/*    */ package BOOT-INF.classes.com.qatraining.dto;
/*    */ 
/*    */ public class PlantSummaryDTO
/*    */ {
/*    */   private long totalPlants;
/*    */   private long lowStockPlants;
/*    */   
/*    */   public PlantSummaryDTO(long totalPlants, long lowStockPlants) {
/*  9 */     this.totalPlants = totalPlants;
/* 10 */     this.lowStockPlants = lowStockPlants;
/*    */   }
/*    */   
/*    */   public long getTotalPlants() {
/* 14 */     return this.totalPlants;
/*    */   }
/*    */   
/*    */   public long getLowStockPlants() {
/* 18 */     return this.lowStockPlants;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/dto/PlantSummaryDTO.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */