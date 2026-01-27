/*    */ package BOOT-INF.classes.com.qatraining.entity;
/*    */ 
/*    */ import com.qatraining.entity.Plant;
/*    */ import jakarta.persistence.Entity;
/*    */ import jakarta.persistence.GeneratedValue;
/*    */ import jakarta.persistence.GenerationType;
/*    */ import jakarta.persistence.Id;
/*    */ import jakarta.persistence.JoinColumn;
/*    */ import jakarta.persistence.ManyToOne;
/*    */ import jakarta.persistence.Table;
/*    */ import java.time.LocalDateTime;
/*    */ 
/*    */ @Entity
/*    */ @Table(name = "sales")
/*    */ public class Sale {
/*    */   @Id
/*    */   @GeneratedValue(strategy = GenerationType.IDENTITY)
/*    */   private Long id;
/*    */   @ManyToOne
/*    */   @JoinColumn(name = "plant_id", nullable = false)
/*    */   private Plant plant;
/*    */   private int quantity;
/*    */   private double totalPrice;
/*    */   private LocalDateTime soldAt;
/*    */   
/*    */   public Long getId() {
/* 27 */     return this.id;
/*    */   }
/*    */   
/*    */   public Plant getPlant() {
/* 31 */     return this.plant;
/*    */   }
/*    */   
/*    */   public int getQuantity() {
/* 35 */     return this.quantity;
/*    */   }
/*    */   
/*    */   public double getTotalPrice() {
/* 39 */     return this.totalPrice;
/*    */   }
/*    */   
/*    */   public LocalDateTime getSoldAt() {
/* 43 */     return this.soldAt;
/*    */   }
/*    */   
/*    */   public void setId(Long id) {
/* 47 */     this.id = id;
/*    */   }
/*    */   
/*    */   public void setPlant(Plant plant) {
/* 51 */     this.plant = plant;
/*    */   }
/*    */   
/*    */   public void setQuantity(int quantity) {
/* 55 */     this.quantity = quantity;
/*    */   }
/*    */   
/*    */   public void setTotalPrice(double totalPrice) {
/* 59 */     this.totalPrice = totalPrice;
/*    */   }
/*    */   
/*    */   public void setSoldAt(LocalDateTime soldAt) {
/* 63 */     this.soldAt = soldAt;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/entity/Sale.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */