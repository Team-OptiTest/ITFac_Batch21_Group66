/*    */ package BOOT-INF.classes.com.qatraining.dto;
/*    */ 
/*    */ public class CategorySummaryDTO
/*    */ {
/*    */   private long mainCategories;
/*    */   private long subCategories;
/*    */   
/*    */   public CategorySummaryDTO(long mainCategories, long subCategories) {
/*  9 */     this.mainCategories = mainCategories;
/* 10 */     this.subCategories = subCategories;
/*    */   }
/*    */   
/*    */   public long getMainCategories() {
/* 14 */     return this.mainCategories;
/*    */   }
/*    */   
/*    */   public long getSubCategories() {
/* 18 */     return this.subCategories;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/dto/CategorySummaryDTO.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */