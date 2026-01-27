/*    */ package BOOT-INF.classes.com.qatraining.service.api;
/*    */ 
/*    */ import com.qatraining.entity.Inventory;
/*    */ import com.qatraining.entity.InventoryType;
/*    */ import com.qatraining.entity.Plant;
/*    */ import com.qatraining.entity.Sale;
/*    */ import com.qatraining.repository.InventoryRepository;
/*    */ import com.qatraining.repository.PlantRepository;
/*    */ import com.qatraining.repository.SaleRepository;
/*    */ import java.time.LocalDateTime;
/*    */ import java.util.List;
/*    */ import java.util.NoSuchElementException;
/*    */ import org.springframework.data.domain.Page;
/*    */ import org.springframework.data.domain.Pageable;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class SaleService
/*    */ {
/*    */   private final SaleRepository saleRepository;
/*    */   private final PlantRepository plantRepository;
/*    */   private final InventoryRepository inventoryRepository;
/*    */   
/*    */   public SaleService(SaleRepository saleRepository, PlantRepository plantRepository, InventoryRepository inventoryRepository) {
/* 28 */     this.saleRepository = saleRepository;
/* 29 */     this.plantRepository = plantRepository;
/* 30 */     this.inventoryRepository = inventoryRepository;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Sale sellPlant(Long plantId, int quantity) {
/* 36 */     Plant plant = (Plant)this.plantRepository.findById(plantId).orElseThrow(() -> new NoSuchElementException("Plant not found"));
/*    */     
/* 38 */     if (quantity > plant.getQuantity().intValue()) {
/* 39 */       throw new IllegalArgumentException(plant
/* 40 */           .getName() + " has only " + plant.getName() + " items available in stock");
/*    */     }
/*    */ 
/*    */     
/* 44 */     Sale sale = new Sale();
/* 45 */     sale.setPlant(plant);
/* 46 */     sale.setQuantity(quantity);
/* 47 */     sale.setTotalPrice(plant.getPrice().doubleValue() * quantity);
/* 48 */     sale.setSoldAt(LocalDateTime.now());
/*    */     
/* 50 */     Inventory inventory = new Inventory();
/* 51 */     inventory.setPlant(plant);
/* 52 */     inventory.setType(InventoryType.OUT);
/* 53 */     inventory.setQuantity(quantity);
/* 54 */     inventory.setNote("Sale");
/* 55 */     inventory.setCreatedAt(LocalDateTime.now());
/*    */     
/* 57 */     plant.setQuantity(Integer.valueOf(plant.getQuantity().intValue() - quantity));
/*    */     
/* 59 */     this.plantRepository.save(plant);
/* 60 */     this.inventoryRepository.save(inventory);
/*    */     
/* 62 */     return (Sale)this.saleRepository.save(sale);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Sale> getAllSales() {
/* 67 */     return this.saleRepository.findAll();
/*    */   }
/*    */   
/*    */   public Sale getSaleById(Long id) {
/* 71 */     return (Sale)this.saleRepository.findById(id)
/* 72 */       .orElseThrow(() -> new NoSuchElementException("Sale not found: " + id));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Page<Sale> getSalesPaged(Pageable pageable) {
/* 78 */     return this.saleRepository.findAll(pageable);
/*    */   }
/*    */ 
/*    */   
/*    */   public Sale updateSale(Sale sale) {
/* 83 */     return (Sale)this.saleRepository.save(sale);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void deleteSale(Long saleId) {
/* 89 */     Sale sale = (Sale)this.saleRepository.findById(saleId).orElseThrow(() -> new NoSuchElementException("Sale not found"));
/*    */ 
/*    */ 
/*    */     
/* 93 */     this.saleRepository.delete(sale);
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/service/api/SaleService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */