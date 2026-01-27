/*     */ package BOOT-INF.classes.com.qatraining.service.ui;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.qatraining.dto.ApiErrorResponse;
/*     */ import com.qatraining.dto.PageResponse;
/*     */ import com.qatraining.entity.Sale;
/*     */ import com.qatraining.service.ui.CategoryUIService;
/*     */ import jakarta.servlet.http.HttpSession;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.core.ParameterizedTypeReference;
/*     */ import org.springframework.data.domain.Page;
/*     */ import org.springframework.data.domain.PageImpl;
/*     */ import org.springframework.data.domain.PageRequest;
/*     */ import org.springframework.data.domain.Pageable;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.stereotype.Service;
/*     */ import org.springframework.web.client.HttpClientErrorException;
/*     */ import org.springframework.web.client.HttpStatusCodeException;
/*     */ import org.springframework.web.client.RestTemplate;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class SaleUIService
/*     */ {
/*     */   private final RestTemplate restTemplate;
/*     */   private final String apiBaseUrl;
/*     */   private final HttpSession session;
/*     */   private final CategoryUIService categoryUIService;
/*     */   
/*     */   public SaleUIService(RestTemplate restTemplate, @Value("${api.base-url}") String apiBaseUrl, HttpSession session, CategoryUIService categoryUIService) {
/*  35 */     this.restTemplate = restTemplate;
/*  36 */     this.apiBaseUrl = apiBaseUrl;
/*  37 */     this.session = session;
/*  38 */     this.categoryUIService = categoryUIService;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Sale> getAllSales() {
/*  43 */     Sale[] sales = (Sale[])this.restTemplate.getForObject(this.apiBaseUrl + "/api/sales", Sale[].class, new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  48 */     return Arrays.asList(sales);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sellPlant(Long plantId, int quantity) {
/*     */     try {
/*  54 */       this.restTemplate.postForObject(this.apiBaseUrl + "/api/sales/plant/" + this.apiBaseUrl + "?quantity=" + plantId, null, Void.class, new Object[0]);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*  59 */     catch (HttpClientErrorException ex) {
/*  60 */       throw extractApiException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private RuntimeException extractApiException(HttpStatusCodeException ex) {
/*     */     try {
/*  67 */       ApiErrorResponse apiError = (ApiErrorResponse)(new ObjectMapper()).readValue(ex
/*  68 */           .getResponseBodyAsString(), ApiErrorResponse.class);
/*     */ 
/*     */ 
/*     */       
/*  72 */       return new RuntimeException(apiError.getMessage());
/*     */     }
/*  74 */     catch (Exception e) {
/*  75 */       return new RuntimeException("Unexpected error occurred");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Page<Sale> getSalesPaged(int page, int size, String sortField, String sortDir) {
/*  86 */     String url = this.apiBaseUrl + "/api/sales/page?page=" + this.apiBaseUrl + "&size=" + page + "&sort=" + size + "," + sortField;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     ResponseEntity<PageResponse<Sale>> response = this.restTemplate.exchange(url, HttpMethod.GET, null, (ParameterizedTypeReference)new Object(this), new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     PageResponse<Sale> dto = (PageResponse<Sale>)response.getBody();
/*     */     
/* 101 */     return (Page<Sale>)new PageImpl(dto
/* 102 */         .getContent(), 
/* 103 */         (Pageable)PageRequest.of(dto.getNumber(), dto.getSize()), dto
/* 104 */         .getTotalElements());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteSale(Long saleId) {
/*     */     try {
/* 111 */       this.restTemplate.delete(this.apiBaseUrl + "/api/sales/" + this.apiBaseUrl, new Object[0]);
/*     */     
/*     */     }
/* 114 */     catch (HttpClientErrorException ex) {
/* 115 */       throw extractApiException(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/service/ui/SaleUIService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */