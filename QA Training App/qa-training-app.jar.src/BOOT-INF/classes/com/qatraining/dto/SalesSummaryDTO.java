/*    */ package BOOT-INF.classes.com.qatraining.dto;
/*    */ 
/*    */ public class SalesSummaryDTO
/*    */ {
/*    */   private long totalSalesCount;
/*    */   private double totalRevenue;
/*    */   
/*    */   public SalesSummaryDTO(long totalSalesCount, double totalRevenue) {
/*  9 */     this.totalSalesCount = totalSalesCount;
/* 10 */     this.totalRevenue = totalRevenue;
/*    */   }
/*    */   
/*    */   public long getTotalSalesCount() {
/* 14 */     return this.totalSalesCount;
/*    */   }
/*    */   
/*    */   public double getTotalRevenue() {
/* 18 */     return this.totalRevenue;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/dto/SalesSummaryDTO.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */