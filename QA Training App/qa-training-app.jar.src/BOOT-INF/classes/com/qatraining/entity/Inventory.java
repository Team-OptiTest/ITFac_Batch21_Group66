/*    */ package BOOT-INF.classes.com.qatraining.entity;
/*    */ 
/*    */ import com.qatraining.entity.InventoryType;
/*    */ import com.qatraining.entity.Plant;
/*    */ import io.swagger.v3.oas.annotations.media.Schema;
/*    */ import jakarta.persistence.Entity;
/*    */ import jakarta.persistence.EnumType;
/*    */ import jakarta.persistence.Enumerated;
/*    */ import jakarta.persistence.GeneratedValue;
/*    */ import jakarta.persistence.GenerationType;
/*    */ import jakarta.persistence.Id;
/*    */ import jakarta.persistence.JoinColumn;
/*    */ import jakarta.persistence.ManyToOne;
/*    */ import jakarta.persistence.Table;
/*    */ import jakarta.validation.constraints.Min;
/*    */ import jakarta.validation.constraints.NotNull;
/*    */ import java.time.LocalDateTime;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Entity
/*    */ @Table(name = "inventory")
/*    */ public class Inventory
/*    */ {
/*    */   @Id
/*    */   @GeneratedValue(strategy = GenerationType.IDENTITY)
/*    */   private Long id;
/*    */   @ManyToOne
/*    */   @JoinColumn(name = "plant_id", nullable = false)
/*    */   private Plant plant;
/*    */   @NotNull
/*    */   @Enumerated(EnumType.STRING)
/*    */   @Schema(description = "Inventory movement type", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"IN", "OUT"})
/*    */   private InventoryType type;
/*    */   @Min(1L)
/*    */   @Schema(description = "Quantity moved", example = "10", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
/*    */   private int quantity;
/*    */   private String note;
/*    */   private LocalDateTime createdAt;
/*    */   
/*    */   public Long getId() {
/* 47 */     return this.id;
/*    */   }
/*    */   
/*    */   public Plant getPlant() {
/* 51 */     return this.plant;
/*    */   }
/*    */   
/*    */   public InventoryType getType() {
/* 55 */     return this.type;
/*    */   }
/*    */   
/*    */   public int getQuantity() {
/* 59 */     return this.quantity;
/*    */   }
/*    */   
/*    */   public String getNote() {
/* 63 */     return this.note;
/*    */   }
/*    */   
/*    */   public LocalDateTime getCreatedAt() {
/* 67 */     return this.createdAt;
/*    */   }
/*    */   
/*    */   public void setId(Long id) {
/* 71 */     this.id = id;
/*    */   }
/*    */   
/*    */   public void setPlant(Plant plant) {
/* 75 */     this.plant = plant;
/*    */   }
/*    */   
/*    */   public void setType(InventoryType type) {
/* 79 */     this.type = type;
/*    */   }
/*    */   
/*    */   public void setQuantity(int quantity) {
/* 83 */     this.quantity = quantity;
/*    */   }
/*    */   
/*    */   public void setNote(String note) {
/* 87 */     this.note = note;
/*    */   }
/*    */   
/*    */   public void setCreatedAt(LocalDateTime createdAt) {
/* 91 */     this.createdAt = createdAt;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/entity/Inventory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */