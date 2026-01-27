/*    */ package BOOT-INF.classes.com.qatraining.service.ui;
/*    */ 
/*    */ import com.qatraining.dto.SalesSummaryDTO;
/*    */ import com.qatraining.repository.SaleRepository;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ @Service
/*    */ public class DashboardUIService
/*    */ {
/*    */   @Autowired
/*    */   private SaleRepository saleRepository;
/*    */   
/*    */   public SalesSummaryDTO getSalesSummary() {
/* 15 */     Object[] result = this.saleRepository.getSalesSummary();
/*    */ 
/*    */     
/* 18 */     Object[] data = (Object[])result[0];
/*    */     
/* 20 */     System.out.println("Count: " + String.valueOf(data[0]));
/* 21 */     System.out.println("Sum: " + String.valueOf(data[1]));
/*    */ 
/*    */     
/* 24 */     long totalSalesCount = ((Number)data[0]).longValue();
/* 25 */     double totalRevenue = ((Number)data[1]).doubleValue();
/*    */     
/* 27 */     return new SalesSummaryDTO(totalSalesCount, totalRevenue);
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/service/ui/DashboardUIService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */