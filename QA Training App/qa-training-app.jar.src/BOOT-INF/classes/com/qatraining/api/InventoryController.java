/*    */ package BOOT-INF.classes.com.qatraining.api;
/*    */ 
/*    */ import com.qatraining.entity.Inventory;
/*    */ import com.qatraining.service.api.InventoryService;
/*    */ import io.swagger.v3.oas.annotations.Hidden;
/*    */ import io.swagger.v3.oas.annotations.Operation;
/*    */ import io.swagger.v3.oas.annotations.responses.ApiResponse;
/*    */ import io.swagger.v3.oas.annotations.responses.ApiResponses;
/*    */ import io.swagger.v3.oas.annotations.tags.Tag;
/*    */ import jakarta.validation.Valid;
/*    */ import java.util.List;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.web.bind.annotation.DeleteMapping;
/*    */ import org.springframework.web.bind.annotation.GetMapping;
/*    */ import org.springframework.web.bind.annotation.PathVariable;
/*    */ import org.springframework.web.bind.annotation.PostMapping;
/*    */ import org.springframework.web.bind.annotation.RequestBody;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.ResponseStatus;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ @Tag(name = "Inventory", description = "Track stock IN / OUT movements (ADMIN only)")
/*    */ @Hidden
/*    */ @RestController
/*    */ @RequestMapping({"/api/inventory"})
/*    */ public class InventoryController {
/*    */   public InventoryController(InventoryService inventoryService) {
/* 28 */     this.inventoryService = inventoryService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private final InventoryService inventoryService;
/*    */ 
/*    */ 
/*    */   
/*    */   @Operation(summary = "Adjust stock (IN / OUT)")
/*    */   @ApiResponses({@ApiResponse(responseCode = "201", description = "Inventory updated"), @ApiResponse(responseCode = "404", description = "Plant not found"), @ApiResponse(responseCode = "400", description = "Invalid request"), @ApiResponse(responseCode = "500", description = "Internal server error")})
/*    */   @PostMapping({"/plant/{plantId}"})
/*    */   @ResponseStatus(HttpStatus.CREATED)
/*    */   public Inventory adjustStock(@Valid @PathVariable Long plantId, @RequestBody Inventory inventory) {
/* 42 */     return this.inventoryService.adjustStock(plantId, inventory);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Operation(summary = "Get inventory history for a plant")
/*    */   @ApiResponses({@ApiResponse(responseCode = "200", description = "Inventory retrieved"), @ApiResponse(responseCode = "404", description = "Plant not found"), @ApiResponse(responseCode = "500", description = "Internal server error")})
/*    */   @GetMapping({"/plant/{plantId}"})
/*    */   public List<Inventory> getInventory(@PathVariable Long plantId) {
/* 53 */     return this.inventoryService.getInventoryByPlant(plantId);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Operation(summary = "Delete inventory record")
/*    */   @ApiResponses({@ApiResponse(responseCode = "204", description = "Inventory deleted"), @ApiResponse(responseCode = "404", description = "Inventory not found"), @ApiResponse(responseCode = "500", description = "Internal server error")})
/*    */   @DeleteMapping({"/{id}"})
/*    */   @ResponseStatus(HttpStatus.NO_CONTENT)
/*    */   public void delete(@PathVariable Long id) {
/* 65 */     this.inventoryService.delete(id);
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/api/InventoryController.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */