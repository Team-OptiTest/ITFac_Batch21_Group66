/*    */ package BOOT-INF.classes.com.qatraining.dto;
/*    */ 
/*    */ import jakarta.validation.constraints.NotBlank;
/*    */ import jakarta.validation.constraints.Size;
/*    */ 
/*    */ 
/*    */ public class CategoryCreateDTO
/*    */ {
/*    */   @NotBlank(message = "Category name is required")
/*    */   @Size(min = 3, max = 10, message = "Category name must be between 3 and 10 characters")
/*    */   private String name;
/*    */   private Long parentId;
/*    */   
/*    */   public String getName() {
/* 15 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 19 */     this.name = name;
/*    */   }
/*    */   
/*    */   public Long getParentId() {
/* 23 */     return this.parentId;
/*    */   }
/*    */   
/*    */   public void setParentId(Long parentId) {
/* 27 */     this.parentId = parentId;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/dto/CategoryCreateDTO.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */