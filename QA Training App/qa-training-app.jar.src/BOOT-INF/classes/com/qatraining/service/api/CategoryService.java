/*     */ package BOOT-INF.classes.com.qatraining.service.api;
/*     */ 
/*     */ import com.qatraining.dto.CategoryResponseDTO;
/*     */ import com.qatraining.dto.CategorySummaryDTO;
/*     */ import com.qatraining.dto.CategoryUpdateDTO;
/*     */ import com.qatraining.entity.Category;
/*     */ import com.qatraining.exception.DuplicateResourceException;
/*     */ import com.qatraining.repository.CategoryRepository;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.springframework.data.domain.Page;
/*     */ import org.springframework.data.domain.PageRequest;
/*     */ import org.springframework.data.domain.Pageable;
/*     */ import org.springframework.data.domain.Sort;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class CategoryService
/*     */ {
/*     */   private final CategoryRepository repository;
/*     */   
/*     */   public CategoryService(CategoryRepository repository) {
/*  24 */     this.repository = repository;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Category save(Category category) {
/*  30 */     if (category.getParent() == null) {
/*     */       
/*  32 */       if (this.repository.existsByNameIgnoreCaseAndParentIsNull(category.getName())) {
/*  33 */         throw new DuplicateResourceException("Main category '" + category
/*  34 */             .getName() + "' already exists");
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  42 */       Long parentId = category.getParent().getId();
/*     */       
/*  44 */       if (this.repository.existsByNameIgnoreCaseAndParent_Id(category
/*  45 */           .getName(), parentId))
/*     */       {
/*  47 */         throw new DuplicateResourceException("Sub-category '" + category
/*  48 */             .getName() + "' already exists under this parent");
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  54 */     if (category.getParent() != null && category.getParent().getId() == null) {
/*  55 */       category.setParent(null);
/*     */     }
/*     */     
/*  58 */     return (Category)this.repository.save(category);
/*     */   }
/*     */   
/*     */   public CategorySummaryDTO getSummary() {
/*  62 */     long mainCount = this.repository.countByParentIsNull();
/*  63 */     long subCount = this.repository.countByParentIsNotNull();
/*  64 */     return new CategorySummaryDTO(mainCount, subCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Category> getMainCategories() {
/*  72 */     return this.repository.findByParentIsNull();
/*     */   }
/*     */   
/*     */   public List<CategoryResponseDTO> getAll() {
/*  76 */     return this.repository.findAll()
/*  77 */       .stream()
/*  78 */       .map(cat -> new CategoryResponseDTO(cat.getId(), cat.getName(), (cat.getParent() != null) ? cat.getParent().getName() : "-"))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  85 */       .toList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Category> search(String name, Long parentId) {
/*  92 */     if (name != null) {
/*  93 */       return this.repository.findByNameContainingIgnoreCase(name);
/*     */     }
/*     */     
/*  96 */     if (parentId != null) {
/*  97 */       return this.repository.findByParent_Id(parentId);
/*     */     }
/*     */     
/* 100 */     return this.repository.findAll();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<CategoryResponseDTO> getFiltered(String name, Long parentId) {
/*     */     List<Category> categories;
/* 107 */     if (name != null && parentId != null) {
/*     */       
/* 109 */       categories = this.repository.findByNameContainingIgnoreCaseAndParentId(name, parentId);
/* 110 */     } else if (name != null) {
/*     */       
/* 112 */       categories = this.repository.findByNameContainingIgnoreCase(name);
/* 113 */     } else if (parentId != null) {
/*     */       
/* 115 */       categories = this.repository.findByParent_Id(parentId);
/*     */     } else {
/* 117 */       categories = this.repository.findAll();
/*     */     } 
/*     */     
/* 120 */     return categories.stream()
/* 121 */       .map(cat -> new CategoryResponseDTO(cat.getId(), cat.getName(), (cat.getParent() != null) ? cat.getParent().getName() : "-"))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 126 */       .toList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Page<CategoryResponseDTO> getCategoriesPaged(int page, int size, String name, Long parentId, String sortField, String sortDir) {
/*     */     Page<Category> result;
/* 137 */     switch (sortField) { case "name": 
/*     */       case "parent": 
/*     */       default:
/* 140 */         break; }  String sortBy = "id";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(new String[] { sortBy }).descending() : Sort.by(new String[] { sortBy }).ascending();
/*     */ 
/*     */ 
/*     */     
/* 150 */     PageRequest pageRequest = PageRequest.of(page, size, sort);
/*     */ 
/*     */ 
/*     */     
/* 154 */     if (name != null && !name.isBlank() && parentId != null) {
/* 155 */       result = this.repository.findByNameContainingIgnoreCaseAndParentId(name, parentId, (Pageable)pageRequest);
/*     */     }
/* 157 */     else if (name != null && !name.isBlank()) {
/* 158 */       result = this.repository.findByNameContainingIgnoreCase(name, (Pageable)pageRequest);
/* 159 */     } else if (parentId != null) {
/* 160 */       result = this.repository.findByParentId(parentId, (Pageable)pageRequest);
/*     */     } else {
/* 162 */       result = this.repository.findAll((Pageable)pageRequest);
/*     */     } 
/*     */     
/* 165 */     return result.map(cat -> new CategoryResponseDTO(cat.getId(), cat.getName(), (cat.getParent() != null) ? cat.getParent().getName() : "-"));
/*     */   }
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
/*     */   public Category getById(Long id) {
/* 178 */     return (Category)this.repository.findById(id)
/* 179 */       .orElseThrow(() -> new NoSuchElementException("Category not found: " + id));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Category update(Long id, CategoryUpdateDTO dto) {
/* 186 */     if (dto.getParentId() != null && dto.getParentId().equals(id)) {
/* 187 */       throw new IllegalArgumentException("Category cannot be its own parent");
/*     */     }
/*     */     
/* 190 */     Category category = getById(id);
/*     */     
/* 192 */     category.setName(dto.getName());
/*     */     
/* 194 */     if (dto.getParentId() != null) {
/* 195 */       Category parent = getById(dto.getParentId());
/* 196 */       category.setParent(parent);
/*     */     } else {
/* 198 */       category.setParent(null);
/*     */     } 
/*     */     
/* 201 */     return (Category)this.repository.save(category);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteById(Long id) {
/* 207 */     if (!this.repository.existsById(id)) {
/* 208 */       throw new NoSuchElementException("Category not found: " + id);
/*     */     }
/*     */ 
/*     */     
/* 212 */     if (this.repository.existsByParent_Id(id)) {
/* 213 */       throw new IllegalStateException("Cannot delete category. Please delete sub-categories first.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 218 */     this.repository.deleteById(id);
/*     */   }
/*     */   
/*     */   public List<Category> getSubCategories() {
/* 222 */     return this.repository.findByParentIsNotNull();
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/service/api/CategoryService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */