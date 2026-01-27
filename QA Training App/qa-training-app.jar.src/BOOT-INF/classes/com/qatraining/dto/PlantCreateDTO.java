/*    */ package BOOT-INF.classes.com.qatraining.dto;
/*    */ 
/*    */ import jakarta.validation.constraints.DecimalMin;
/*    */ import jakarta.validation.constraints.Min;
/*    */ import jakarta.validation.constraints.NotBlank;
/*    */ import jakarta.validation.constraints.NotNull;
/*    */ import jakarta.validation.constraints.Size;
/*    */ 
/*    */ public class PlantCreateDTO {
/*    */   @NotBlank(message = "Plant name is required")
/*    */   @Size(min = 3, max = 25, message = "Plant name must be between 3 and 25 characters")
/*    */   private String name;
/*    */   @NotNull(message = "Price is required")
/*    */   @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
/*    */   private Double price;
/*    */   @NotNull(message = "Quantity is required")
/*    */   @Min(value = 0L, message = "Quantity cannot be negative")
/*    */   private Integer quantity;
/*    */   @NotNull(message = "Category is required")
/*    */   private Long categoryId;
/*    */   
/*    */   public String getName() {
/* 23 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 27 */     this.name = name;
/*    */   }
/*    */   
/*    */   public Double getPrice() {
/* 31 */     return this.price;
/*    */   }
/*    */   
/*    */   public void setPrice(Double price) {
/* 35 */     this.price = price;
/*    */   }
/*    */   
/*    */   public Integer getQuantity() {
/* 39 */     return this.quantity;
/*    */   }
/*    */   
/*    */   public void setQuantity(Integer quantity) {
/* 43 */     this.quantity = quantity;
/*    */   }
/*    */   
/*    */   public Long getCategoryId() {
/* 47 */     return this.categoryId;
/*    */   }
/*    */   
/*    */   public void setCategoryId(Long categoryId) {
/* 51 */     this.categoryId = categoryId;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/dto/PlantCreateDTO.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */