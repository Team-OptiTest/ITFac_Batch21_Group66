/*    */ package BOOT-INF.classes.com.qatraining.dto;
/*    */ 
/*    */ public class CategoryResponseDTO {
/*    */   private Long id;
/*    */   private String name;
/*    */   private String parentName;
/*    */   
/*    */   public CategoryResponseDTO(Long id, String name, String parentName) {
/*  9 */     this.id = id;
/* 10 */     this.name = name;
/* 11 */     this.parentName = parentName;
/*    */   }
/*    */   
/*    */   public Long getId() {
/* 15 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(Long id) {
/* 19 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 23 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 27 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getParentName() {
/* 31 */     return this.parentName;
/*    */   }
/*    */   
/*    */   public void setParentName(String parentName) {
/* 35 */     this.parentName = parentName;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/dto/CategoryResponseDTO.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */