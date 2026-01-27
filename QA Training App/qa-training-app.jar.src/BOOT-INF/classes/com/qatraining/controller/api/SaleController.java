/*    */ package BOOT-INF.classes.com.qatraining.controller.api;
/*    */ 
/*    */ import com.qatraining.entity.Sale;
/*    */ import com.qatraining.service.api.SaleService;
/*    */ import io.swagger.v3.oas.annotations.Operation;
/*    */ import io.swagger.v3.oas.annotations.responses.ApiResponse;
/*    */ import io.swagger.v3.oas.annotations.responses.ApiResponses;
/*    */ import io.swagger.v3.oas.annotations.tags.Tag;
/*    */ import jakarta.validation.constraints.Min;
/*    */ import java.util.List;
/*    */ import org.springframework.data.domain.Page;
/*    */ import org.springframework.data.domain.Pageable;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.validation.annotation.Validated;
/*    */ import org.springframework.web.bind.annotation.DeleteMapping;
/*    */ import org.springframework.web.bind.annotation.GetMapping;
/*    */ import org.springframework.web.bind.annotation.PathVariable;
/*    */ import org.springframework.web.bind.annotation.PostMapping;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RequestParam;
/*    */ import org.springframework.web.bind.annotation.ResponseStatus;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ 
/*    */ @Tag(name = "Sales", description = "Sell plants and automatically update inventory (ADMIN only)")
/*    */ @Validated
/*    */ @RestController
/*    */ @RequestMapping({"/api/sales"})
/*    */ public class SaleController
/*    */ {
/*    */   private final SaleService saleService;
/*    */   
/*    */   public SaleController(SaleService saleService) {
/* 34 */     this.saleService = saleService;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Operation(summary = "Sell plant")
/*    */   @ApiResponses({@ApiResponse(responseCode = "201", description = "Sale completed"), @ApiResponse(responseCode = "404", description = "Plant not found"), @ApiResponse(responseCode = "400", description = "Invalid quantity"), @ApiResponse(responseCode = "500", description = "Internal server error")})
/*    */   @PostMapping({"/plant/{plantId}"})
/*    */   @ResponseStatus(HttpStatus.CREATED)
/*    */   public Sale sellPlant(@PathVariable Long plantId, @RequestParam @Min(value = 1L, message = "Quantity must be greater than 0") int quantity) {
/* 48 */     return this.saleService.sellPlant(plantId, quantity);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Operation(summary = "Get all sales")
/*    */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Sales retrieved"), @ApiResponse(responseCode = "500", description = "Internal server error")})
/*    */   @GetMapping
/*    */   public List<Sale> getAllSales() {
/* 58 */     return this.saleService.getAllSales();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Operation(summary = "Get sale by ID")
/*    */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Sale retrieved"), @ApiResponse(responseCode = "404", description = "Sale not found"), @ApiResponse(responseCode = "401", description = "Unauthorized"), @ApiResponse(responseCode = "403", description = "Forbidden"), @ApiResponse(responseCode = "500", description = "Internal server error")})
/*    */   @GetMapping({"/{id}"})
/*    */   public Sale getById(@PathVariable Long id) {
/* 71 */     return this.saleService.getSaleById(id);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Operation(summary = "Delete sale")
/*    */   @ApiResponses({@ApiResponse(responseCode = "204", description = "Sale deleted"), @ApiResponse(responseCode = "404", description = "Sale not found"), @ApiResponse(responseCode = "500", description = "Internal server error")})
/*    */   @DeleteMapping({"/{id}"})
/*    */   @ResponseStatus(HttpStatus.NO_CONTENT)
/*    */   public void delete(@PathVariable Long id) {
/* 83 */     this.saleService.deleteSale(id);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Operation(summary = "Get sales with pagination and sorting")
/*    */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Sales retrieved"), @ApiResponse(responseCode = "500", description = "Internal server error")})
/*    */   @GetMapping({"/page"})
/*    */   public Page<Sale> getSalesPaged(Pageable pageable) {
/* 93 */     return this.saleService.getSalesPaged(pageable);
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/controller/api/SaleController.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */