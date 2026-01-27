/*     */ package BOOT-INF.classes.com.qatraining.controller.api;
/*     */ import com.qatraining.api.ErrorResponse;
/*     */ import com.qatraining.dto.CategoryEditResponseDTO;
/*     */ import com.qatraining.dto.CategoryResponseDTO;
/*     */ import com.qatraining.dto.CategorySummaryDTO;
/*     */ import com.qatraining.dto.CategoryUpdateDTO;
/*     */ import com.qatraining.entity.Category;
/*     */ import com.qatraining.service.api.CategoryService;
/*     */ import io.swagger.v3.oas.annotations.Operation;
/*     */ import io.swagger.v3.oas.annotations.media.Content;
/*     */ import io.swagger.v3.oas.annotations.media.Schema;
/*     */ import io.swagger.v3.oas.annotations.responses.ApiResponse;
/*     */ import io.swagger.v3.oas.annotations.responses.ApiResponses;
/*     */ import jakarta.validation.Valid;
/*     */ import java.util.List;
/*     */ import org.springframework.data.domain.Page;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.security.access.prepost.PreAuthorize;
/*     */ import org.springframework.web.bind.annotation.DeleteMapping;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.PutMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.ResponseStatus;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ @Tag(name = "Categories", description = "Manage flower categories and sub-categories")
/*     */ @RestController
/*     */ @RequestMapping({"/api/categories"})
/*     */ public class CategoryController {
/*     */   public CategoryController(CategoryService service) {
/*  34 */     this.service = service;
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
/*     */ 
/*     */ 
/*     */   
/*     */   private final CategoryService service;
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
/*     */   @Operation(summary = "Create a category (main or sub category)")
/*     */   @ApiResponses({@ApiResponse(responseCode = "201", description = "Category created successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))}), @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "404", description = "Category not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @PostMapping
/*     */   @PreAuthorize("hasAnyRole('ADMIN')")
/*     */   @ResponseStatus(HttpStatus.CREATED)
/*     */   public Category create(@Valid @RequestBody Category category) {
/*  92 */     return this.service.save(category);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Operation(summary = "Get all categories")
/*     */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Category retrieved successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))}), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @GetMapping
/*     */   @PreAuthorize("hasAnyRole('ADMIN','USER')")
/*     */   public List<CategoryResponseDTO> getAll(@RequestParam(required = false) String name, @RequestParam(required = false) Long parentId) {
/* 128 */     return this.service.getFiltered(name, parentId);
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
/*     */ 
/*     */   
/*     */   @Operation(summary = "Get specific category by ID")
/*     */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Category retrieved successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))}), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "404", description = "Category not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @PreAuthorize("hasAnyRole('ADMIN','USER')")
/*     */   @GetMapping({"/{id}"})
/*     */   public CategoryEditResponseDTO getCategory(@PathVariable Long id) {
/* 171 */     Category category = this.service.getById(id);
/*     */     
/* 173 */     CategoryEditResponseDTO dto = new CategoryEditResponseDTO();
/* 174 */     dto.setId(category.getId());
/* 175 */     dto.setName(category.getName());
/*     */     
/* 177 */     if (category.getParent() != null) {
/* 178 */       dto.setParentId(category.getParent().getId());
/*     */     }
/*     */     
/* 181 */     return dto;
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
/*     */   
/*     */   @Operation(summary = "Get main categories only")
/*     */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Main category retrieved successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))}), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "404", description = "Category not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @PreAuthorize("hasAnyRole('ADMIN','USER')")
/*     */   @GetMapping({"/main"})
/*     */   public List<Category> getMainCategories() {
/* 223 */     return this.service.getMainCategories();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Operation(summary = "Delete category by ID")
/*     */   @ApiResponses({@ApiResponse(responseCode = "204", description = "Category deleted successfully", content = {@Content(mediaType = "application/json")}), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "404", description = "Category not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @PreAuthorize("hasAnyRole('ADMIN')")
/*     */   @DeleteMapping({"/{id}"})
/*     */   @ResponseStatus(HttpStatus.NO_CONTENT)
/*     */   public void delete(@PathVariable Long id) {
/* 272 */     this.service.deleteById(id);
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
/*     */   @Operation(summary = "Search by Name or Parent ID with Pagination and Sort")
/*     */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Category retrieved successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))}), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @PreAuthorize("hasAnyRole('ADMIN','USER')")
/*     */   @GetMapping({"/page"})
/*     */   public Page<CategoryResponseDTO> getCategoriesPaged(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String name, @RequestParam(required = false) Long parentId, @RequestParam(defaultValue = "id") String sortField, @RequestParam(defaultValue = "asc") String sortDir) {
/* 312 */     return this.service.getCategoriesPaged(page, size, name, parentId, sortField, sortDir);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Operation(summary = "Update category")
/*     */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Category updated successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))}), @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "404", description = "Category not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @PreAuthorize("hasAnyRole('ADMIN')")
/*     */   @PutMapping({"/{id}"})
/*     */   public Category update(@PathVariable Long id, @Valid @RequestBody CategoryUpdateDTO dto) {
/* 371 */     return this.service.update(id, dto);
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
/*     */   @Operation(summary = "Get Category summary")
/*     */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Category retrieved successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))}), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @GetMapping({"/summary"})
/*     */   @PreAuthorize("hasAnyRole('ADMIN','USER')")
/*     */   public CategorySummaryDTO getSummary() {
/* 412 */     return this.service.getSummary();
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
/*     */   @Operation(summary = "Get All Sub Categories")
/*     */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Sub Categories retrieved successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))}), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @GetMapping({"/sub-categories"})
/*     */   @PreAuthorize("hasAnyRole('ADMIN','USER')")
/*     */   public List<Category> getSubCategories() {
/* 453 */     return this.service.getSubCategories();
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/controller/api/CategoryController.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */