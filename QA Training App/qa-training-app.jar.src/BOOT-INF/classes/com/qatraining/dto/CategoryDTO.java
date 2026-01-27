/*    */ package BOOT-INF.classes.com.qatraining.dto;
/*    */ 
/*    */ 
/*    */ public class CategoryDTO
/*    */ {
/*    */   private Long id;
/*    */   private String name;
/*    */   private com.qatraining.dto.CategoryDTO parent;
/*    */   
/*    */   public Long getId() {
/* 11 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(Long id) {
/* 15 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 19 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 23 */     this.name = name;
/*    */   }
/*    */   
/*    */   public com.qatraining.dto.CategoryDTO getParent() {
/* 27 */     return this.parent;
/*    */   }
/*    */   
/*    */   public void setParent(com.qatraining.dto.CategoryDTO parent) {
/* 31 */     this.parent = parent;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/dto/CategoryDTO.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */