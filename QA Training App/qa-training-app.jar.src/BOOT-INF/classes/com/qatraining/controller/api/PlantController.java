/*     */ package BOOT-INF.classes.com.qatraining.controller.api;
/*     */ import com.qatraining.api.ErrorResponse;
/*     */ import com.qatraining.dto.PlantEditResponseDTO;
/*     */ import com.qatraining.dto.PlantSummaryDTO;
/*     */ import com.qatraining.entity.Plant;
/*     */ import com.qatraining.service.api.PlantService;
/*     */ import io.swagger.v3.oas.annotations.Operation;
/*     */ import io.swagger.v3.oas.annotations.media.Content;
/*     */ import io.swagger.v3.oas.annotations.media.Schema;
/*     */ import io.swagger.v3.oas.annotations.responses.ApiResponse;
/*     */ import io.swagger.v3.oas.annotations.responses.ApiResponses;
/*     */ import jakarta.validation.Valid;
/*     */ import java.util.List;
/*     */ import org.springframework.data.domain.Page;
/*     */ import org.springframework.data.domain.Pageable;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.security.access.prepost.PreAuthorize;
/*     */ import org.springframework.web.bind.annotation.DeleteMapping;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.PutMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.ResponseStatus;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ @Tag(name = "Plants", description = "Manage plants under sub-categories")
/*     */ @RestController
/*     */ @RequestMapping({"/api/plants"})
/*     */ public class PlantController {
/*     */   public PlantController(PlantService plantService) {
/*  32 */     this.plantService = plantService;
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
/*     */   private final PlantService plantService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Operation(summary = "Create a plant under a sub-category")
/*     */   @ApiResponses({@ApiResponse(responseCode = "201", description = "Plant created successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Plant.class))}), @ApiResponse(responseCode = "400", description = "Invalid request / Duplicate plant", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "404", description = "Category not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @PreAuthorize("hasAnyRole('ADMIN')")
/*     */   @PostMapping({"/category/{categoryId}"})
/*     */   @ResponseStatus(HttpStatus.CREATED)
/*     */   public Plant addPlant(@Valid @PathVariable Long categoryId, @Valid @RequestBody Plant plant) {
/*  95 */     return this.plantService.addPlant(categoryId, plant);
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
/*     */   @Operation(summary = "Get all plants")
/*     */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Plants retrieved successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Plant.class))}), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @PreAuthorize("hasAnyRole('ADMIN','USER')")
/*     */   @GetMapping
/*     */   public List<Plant> getAllPlants() {
/* 138 */     return this.plantService.getAllPlants();
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
/*     */   @Operation(summary = "Get plants by category")
/*     */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Plants retrieved successfully"), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "404", description = "Category not found", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @PreAuthorize("hasAnyRole('ADMIN','USER')")
/*     */   @GetMapping({"/category/{categoryId}"})
/*     */   public List<Plant> getPlantsByCategory(@PathVariable Long categoryId) {
/* 180 */     return this.plantService.getPlantsByCategory(categoryId);
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
/*     */   @Operation(summary = "Get plant by ID")
/*     */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Plant retrieved successfully"), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "404", description = "Plant not found", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @PreAuthorize("hasAnyRole('ADMIN','USER')")
/*     */   @GetMapping({"/{id}"})
/*     */   public PlantEditResponseDTO getPlant(@PathVariable Long id) {
/* 223 */     Plant plant = this.plantService.getById(id);
/*     */     
/* 225 */     PlantEditResponseDTO dto = new PlantEditResponseDTO();
/* 226 */     dto.setId(plant.getId());
/* 227 */     dto.setName(plant.getName());
/* 228 */     dto.setPrice(plant.getPrice());
/* 229 */     dto.setQuantity(plant.getQuantity());
/* 230 */     dto.setCategoryId(plant.getCategory().getId());
/*     */     
/* 232 */     return dto;
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
/*     */   @Operation(summary = "Update plant details")
/*     */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Plant updated successfully"), @ApiResponse(responseCode = "400", description = "Invalid request", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "404", description = "Plant not found", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @PreAuthorize("hasAnyRole('ADMIN')")
/*     */   @PutMapping({"/{id}"})
/*     */   public Plant updatePlant(@PathVariable Long id, @Valid @RequestBody Plant plant) {
/* 287 */     return this.plantService.updatePlant(id, plant);
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
/*     */   @Operation(summary = "Delete plant by ID")
/*     */   @ApiResponses({@ApiResponse(responseCode = "204", description = "Plant deleted successfully"), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "404", description = "Plant not found", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @PreAuthorize("hasAnyRole('ADMIN')")
/*     */   @DeleteMapping({"/{id}"})
/*     */   @ResponseStatus(HttpStatus.NO_CONTENT)
/*     */   public void delete(@PathVariable Long id) {
/* 335 */     this.plantService.deletePlant(id);
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
/*     */   @Operation(summary = "Search plants with pagination and sorting")
/*     */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Plants retrieved successfully"), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "404", description = "Plant not found", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @PreAuthorize("hasAnyRole('ADMIN','USER')")
/*     */   @GetMapping({"/paged"})
/*     */   public Page<Plant> getPlantsPaged(@RequestParam(required = false) String name, @RequestParam(required = false) Long categoryId, Pageable pageable) {
/* 385 */     return this.plantService.getPlantsPaged(name, categoryId, pageable);
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
/*     */   @Operation(summary = "Get plant summary")
/*     */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Plant summary retrieved successfully"), @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "404", description = "Plant not found", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}), @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
/*     */   @PreAuthorize("hasAnyRole('ADMIN','USER')")
/*     */   @GetMapping({"/summary"})
/*     */   public PlantSummaryDTO getPlantSummary() {
/* 434 */     return this.plantService.getPlantSummary();
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/controller/api/PlantController.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */