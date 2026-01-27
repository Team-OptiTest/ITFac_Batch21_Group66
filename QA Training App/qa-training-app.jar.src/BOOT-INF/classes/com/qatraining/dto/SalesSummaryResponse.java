/*    */ package BOOT-INF.classes.com.qatraining.dto;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ 
/*    */ 
/*    */ public class SalesSummaryResponse
/*    */ {
/*    */   private long totalSalesCount;
/*    */   private long totalQuantitySold;
/*    */   private BigDecimal totalRevenue;
/*    */   
/*    */   public SalesSummaryResponse(long totalSalesCount, long totalQuantitySold, BigDecimal totalRevenue) {
/* 13 */     this.totalSalesCount = totalSalesCount;
/* 14 */     this.totalQuantitySold = totalQuantitySold;
/* 15 */     this.totalRevenue = totalRevenue;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public long getTotalSalesCount() {
/* 21 */     return this.totalSalesCount;
/*    */   }
/*    */   
/*    */   public long getTotalQuantitySold() {
/* 25 */     return this.totalQuantitySold;
/*    */   }
/*    */   
/*    */   public BigDecimal getTotalRevenue() {
/* 29 */     return this.totalRevenue;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/dto/SalesSummaryResponse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */