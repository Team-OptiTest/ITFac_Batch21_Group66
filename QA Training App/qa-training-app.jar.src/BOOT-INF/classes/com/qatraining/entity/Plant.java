/*    */ package BOOT-INF.classes.com.qatraining.entity;
/*    */ 
/*    */ import com.qatraining.entity.Category;
/*    */ import io.swagger.v3.oas.annotations.media.Schema;
/*    */ import jakarta.persistence.Column;
/*    */ import jakarta.persistence.Entity;
/*    */ import jakarta.persistence.GeneratedValue;
/*    */ import jakarta.persistence.GenerationType;
/*    */ import jakarta.persistence.Id;
/*    */ import jakarta.persistence.JoinColumn;
/*    */ import jakarta.persistence.ManyToOne;
/*    */ import jakarta.persistence.Table;
/*    */ import jakarta.persistence.UniqueConstraint;
/*    */ import jakarta.validation.constraints.DecimalMin;
/*    */ import jakarta.validation.constraints.Min;
/*    */ import jakarta.validation.constraints.NotBlank;
/*    */ import jakarta.validation.constraints.NotNull;
/*    */ import jakarta.validation.constraints.Size;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Entity
/*    */ @Table(name = "plants", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "category_id"})})
/*    */ public class Plant
/*    */ {
/*    */   @Id
/*    */   @GeneratedValue(strategy = GenerationType.IDENTITY)
/*    */   private Long id;
/*    */   @NotBlank(message = "Plant name is required")
/*    */   @Size(min = 3, max = 25, message = "Plant name must be between 3 and 25 characters")
/*    */   @Column(nullable = false)
/*    */   @Schema(description = "Plant name (must be unique within the same category, case-insensitive)", example = "Anthurium", minLength = 3, maxLength = 25, requiredMode = Schema.RequiredMode.REQUIRED)
/*    */   private String name;
/*    */   @NotNull(message = "Price is required")
/*    */   @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
/*    */   @Schema(description = "Price per unit", example = "150.00", minimum = "0.01")
/*    */   private Double price;
/*    */   @NotNull(message = "Quantity is required")
/*    */   @Min(value = 0L, message = "Quantity cannot be negative")
/*    */   @Schema(description = "Available quantity", example = "25", minimum = "0")
/*    */   private Integer quantity;
/*    */   @ManyToOne
/*    */   @JoinColumn(name = "category_id")
/*    */   @Schema(description = "Category to which the plant belongs")
/*    */   private Category category;
/*    */   
/*    */   public Long getId() {
/* 59 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(Long id) {
/* 63 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 67 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 71 */     this.name = name;
/*    */   }
/*    */   
/*    */   public Double getPrice() {
/* 75 */     return this.price;
/*    */   }
/*    */   
/*    */   public void setPrice(Double price) {
/* 79 */     this.price = price;
/*    */   }
/*    */   
/*    */   public Integer getQuantity() {
/* 83 */     return this.quantity;
/*    */   }
/*    */   
/*    */   public void setQuantity(Integer quantity) {
/* 87 */     this.quantity = quantity;
/*    */   }
/*    */   
/*    */   public Category getCategory() {
/* 91 */     return this.category;
/*    */   }
/*    */   
/*    */   public void setCategory(Category category) {
/* 95 */     this.category = category;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/entity/Plant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */