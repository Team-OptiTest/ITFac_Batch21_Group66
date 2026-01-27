/*    */ package BOOT-INF.classes.com.qatraining.entity;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonBackReference;
/*    */ import io.swagger.v3.oas.annotations.media.Schema;
/*    */ import jakarta.persistence.Column;
/*    */ import jakarta.persistence.Entity;
/*    */ import jakarta.persistence.GeneratedValue;
/*    */ import jakarta.persistence.GenerationType;
/*    */ import jakarta.persistence.Id;
/*    */ import jakarta.persistence.JoinColumn;
/*    */ import jakarta.persistence.ManyToOne;
/*    */ import jakarta.persistence.OneToMany;
/*    */ import jakarta.persistence.Table;
/*    */ import jakarta.persistence.UniqueConstraint;
/*    */ import jakarta.validation.constraints.NotBlank;
/*    */ import jakarta.validation.constraints.Size;
/*    */ import java.util.List;
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
/*    */ @Table(name = "categories", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "parent_id"})})
/*    */ @Schema(description = "Flower category (Main or Sub-category)")
/*    */ public class Category
/*    */ {
/*    */   @Id
/*    */   @GeneratedValue(strategy = GenerationType.IDENTITY)
/*    */   private Long id;
/*    */   @NotBlank(message = "Category name is mandatory")
/*    */   @Size(min = 3, max = 10, message = "Category name must be between 3 and 10 characters")
/*    */   @Column(nullable = false)
/*    */   @Schema(description = "Category name (Sub category name must be UNIQUE for same Main Category)", example = "Anthurium", minLength = 3, maxLength = 10, requiredMode = Schema.RequiredMode.REQUIRED)
/*    */   private String name;
/*    */   @ManyToOne
/*    */   @JoinColumn(name = "parent_id")
/*    */   @JsonBackReference
/*    */   @Schema(description = "Parent category (null for main category)", example = "1")
/*    */   private com.qatraining.entity.Category parent;
/*    */   @OneToMany(mappedBy = "parent")
/*    */   @Schema(description = "List of sub-categories")
/*    */   private List<com.qatraining.entity.Category> subCategories;
/*    */   
/*    */   public Long getId() {
/* 55 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 59 */     return this.name;
/*    */   }
/*    */   
/*    */   public com.qatraining.entity.Category getParent() {
/* 63 */     return this.parent;
/*    */   }
/*    */   
/*    */   public List<com.qatraining.entity.Category> getSubCategories() {
/* 67 */     return this.subCategories;
/*    */   }
/*    */   
/*    */   public void setId(Long id) {
/* 71 */     this.id = id;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 75 */     this.name = name;
/*    */   }
/*    */   
/*    */   public void setParent(com.qatraining.entity.Category parent) {
/* 79 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public void setSubCategories(List<com.qatraining.entity.Category> subCategories) {
/* 83 */     this.subCategories = subCategories;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/entity/Category.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */