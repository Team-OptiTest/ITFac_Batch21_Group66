/*     */ package BOOT-INF.classes.com.qatraining.service.api;
/*     */ 
/*     */ import com.qatraining.dto.PlantSummaryDTO;
/*     */ import com.qatraining.entity.Category;
/*     */ import com.qatraining.entity.Plant;
/*     */ import com.qatraining.exception.DuplicateResourceException;
/*     */ import com.qatraining.repository.CategoryRepository;
/*     */ import com.qatraining.repository.PlantRepository;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.springframework.data.domain.Page;
/*     */ import org.springframework.data.domain.Pageable;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class PlantService
/*     */ {
/*     */   private final PlantRepository plantRepository;
/*     */   private final CategoryRepository categoryRepository;
/*     */   
/*     */   public PlantService(PlantRepository plantRepository, CategoryRepository categoryRepository) {
/*  24 */     this.plantRepository = plantRepository;
/*  25 */     this.categoryRepository = categoryRepository;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Plant addPlant(Long categoryId, Plant plant) {
/*  31 */     Category category = (Category)this.categoryRepository.findById(categoryId).orElseThrow(() -> new NoSuchElementException("Category not found"));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  36 */     if (category.getParent() == null) {
/*  37 */       throw new IllegalArgumentException("Plants can only be added to sub-categories");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  43 */     if (this.plantRepository.existsByNameIgnoreCaseAndCategoryId(plant
/*  44 */         .getName(), categoryId))
/*     */     {
/*  46 */       throw new DuplicateResourceException("Plant '" + plant
/*  47 */           .getName() + "' already exists in this category");
/*     */     }
/*     */ 
/*     */     
/*  51 */     plant.setCategory(category);
/*  52 */     return (Plant)this.plantRepository.save(plant);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Plant> getAllPlants() {
/*  57 */     return this.plantRepository.findAll();
/*     */   }
/*     */   
/*     */   public List<Plant> getPlantsByCategory(Long categoryId) {
/*  61 */     return this.plantRepository.findByCategoryId(categoryId);
/*     */   }
/*     */   
/*     */   public Plant updatePlant(Plant plant) {
/*  65 */     return (Plant)this.plantRepository.save(plant);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Plant updatePlant(Long id, Plant updatedPlant) {
/*  71 */     Plant existing = (Plant)this.plantRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Plant not found"));
/*     */ 
/*     */ 
/*     */     
/*  75 */     Category category = updatedPlant.getCategory();
/*  76 */     if (category != null && category.getParent() == null) {
/*  77 */       throw new IllegalArgumentException("Plants can only belong to sub-categories");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     existing.setName(updatedPlant.getName());
/*  92 */     existing.setPrice(updatedPlant.getPrice());
/*  93 */     existing.setQuantity(updatedPlant.getQuantity());
/*     */     
/*  95 */     return (Plant)this.plantRepository.save(existing);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void deletePlant(Long plantId) {
/* 101 */     this.plantRepository.deleteById(plantId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Page<Plant> getPlantsPaged(String name, Long categoryId, Pageable pageable) {
/* 109 */     if (name != null && !name.isBlank() && categoryId != null) {
/* 110 */       return this.plantRepository
/* 111 */         .findByNameContainingIgnoreCaseAndCategory_Id(name, categoryId, pageable);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 116 */     if (name != null && !name.isBlank()) {
/* 117 */       return this.plantRepository
/* 118 */         .findByNameContainingIgnoreCase(name, pageable);
/*     */     }
/*     */     
/* 121 */     if (categoryId != null) {
/* 122 */       return this.plantRepository
/* 123 */         .findByCategory_Id(categoryId, pageable);
/*     */     }
/*     */     
/* 126 */     return this.plantRepository.findAll(pageable);
/*     */   }
/*     */   
/*     */   public Plant getById(Long id) {
/* 130 */     return (Plant)this.plantRepository.findById(id)
/* 131 */       .orElseThrow(() -> new NoSuchElementException("Plant not found: " + id));
/*     */   }
/*     */ 
/*     */   
/*     */   public PlantSummaryDTO getPlantSummary() {
/* 136 */     long totalPlants = this.plantRepository.count();
/* 137 */     long lowStockPlants = this.plantRepository.countByQuantityLessThan(5);
/* 138 */     return new PlantSummaryDTO(totalPlants, lowStockPlants);
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/service/api/PlantService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */