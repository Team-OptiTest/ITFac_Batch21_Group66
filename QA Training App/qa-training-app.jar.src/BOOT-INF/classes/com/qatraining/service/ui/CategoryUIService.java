/*     */ package BOOT-INF.classes.com.qatraining.service.ui;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.qatraining.dto.ApiErrorResponse;
/*     */ import com.qatraining.dto.CategoryCreateDTO;
/*     */ import com.qatraining.dto.CategoryDTO;
/*     */ import com.qatraining.dto.CategoryEditResponseDTO;
/*     */ import com.qatraining.dto.CategoryResponseDTO;
/*     */ import com.qatraining.dto.CategorySummaryDTO;
/*     */ import com.qatraining.dto.PageResponse;
/*     */ import jakarta.servlet.http.HttpSession;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.core.ParameterizedTypeReference;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.stereotype.Service;
/*     */ import org.springframework.web.client.HttpClientErrorException;
/*     */ import org.springframework.web.client.HttpStatusCodeException;
/*     */ import org.springframework.web.client.RestTemplate;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class CategoryUIService
/*     */ {
/*     */   private final RestTemplate restTemplate;
/*     */   private final String apiBaseUrl;
/*     */   private final HttpSession session;
/*     */   
/*     */   public CategoryUIService(RestTemplate restTemplate, @Value("${api.base-url}") String apiBaseUrl, HttpSession session) {
/*  34 */     this.restTemplate = restTemplate;
/*  35 */     this.apiBaseUrl = apiBaseUrl;
/*  36 */     this.session = session;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<CategoryResponseDTO> getParentCategories() {
/*  41 */     String url = this.apiBaseUrl + "/api/categories/main";
/*     */ 
/*     */     
/*  44 */     return (List<CategoryResponseDTO>)this.restTemplate.exchange(url, HttpMethod.GET, null, (ParameterizedTypeReference)new Object(this), new Object[0])
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  49 */       .getBody();
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
/*     */   public PageResponse<CategoryResponseDTO> getCategoriesPaged(int page, int size, String name, Long parentId, String sortField, String sortDir) {
/*  66 */     UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.apiBaseUrl + "/api/categories/page").queryParam("page", new Object[] { Integer.valueOf(page) }).queryParam("size", new Object[] { Integer.valueOf(size) }).queryParam("sortField", new Object[] { sortField }).queryParam("sortDir", new Object[] { sortDir });
/*     */     
/*  68 */     if (name != null && !name.isBlank()) {
/*  69 */       builder.queryParam("name", new Object[] { name });
/*     */     }
/*     */     
/*  72 */     if (parentId != null) {
/*  73 */       builder.queryParam("parentId", new Object[] { parentId });
/*     */     }
/*     */ 
/*     */     
/*  77 */     return (PageResponse<CategoryResponseDTO>)this.restTemplate.exchange(builder
/*  78 */         .toUriString(), HttpMethod.GET, null, (ParameterizedTypeReference)new Object(this), new Object[0])
/*     */ 
/*     */ 
/*     */       
/*  82 */       .getBody();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void createCategory(CategoryCreateDTO dto) {
/*  88 */     CategoryDTO categoryParentDTO = new CategoryDTO();
/*  89 */     categoryParentDTO.setId(dto.getParentId());
/*     */     
/*  91 */     CategoryDTO categoryDTO = new CategoryDTO();
/*     */ 
/*     */     
/*  94 */     categoryDTO.setName(dto.getName());
/*  95 */     categoryDTO.setParent(categoryParentDTO);
/*     */     
/*     */     try {
/*  98 */       this.restTemplate.postForEntity(this.apiBaseUrl + "/api/categories", categoryDTO, Void.class, new Object[0]);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 103 */     catch (org.springframework.web.client.HttpClientErrorException.BadRequest ex) {
/* 104 */       throw extractApiException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CategoryEditResponseDTO getCategoryById(Long id) {
/* 109 */     return (CategoryEditResponseDTO)this.restTemplate.getForObject(this.apiBaseUrl + "/api/categories/" + this.apiBaseUrl, CategoryEditResponseDTO.class, new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCategory(Long id, CategoryCreateDTO dto) {
/*     */     try {
/* 118 */       this.restTemplate.put(this.apiBaseUrl + "/api/categories/" + this.apiBaseUrl, dto, new Object[0]);
/*     */ 
/*     */     
/*     */     }
/* 122 */     catch (org.springframework.web.client.HttpClientErrorException.BadRequest ex) {
/* 123 */       throw extractApiException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void deleteCategory(Long id) {
/*     */     try {
/* 129 */       this.restTemplate.exchange(this.apiBaseUrl + "/api/categories/" + this.apiBaseUrl, HttpMethod.DELETE, null, Void.class, new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 135 */     catch (HttpStatusCodeException ex) {
/* 136 */       throw extractApiException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CategorySummaryDTO getCategorySummary() {
/* 141 */     return (CategorySummaryDTO)this.restTemplate.getForObject(this.apiBaseUrl + "/api/categories/summary", CategorySummaryDTO.class, new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HttpHeaders authHeaders() {
/* 148 */     String token = (String)this.session.getAttribute("JWT_TOKEN");
/*     */     
/* 150 */     HttpHeaders headers = new HttpHeaders();
/* 151 */     headers.setBearerAuth(token);
/*     */     
/* 153 */     return headers;
/*     */   }
/*     */ 
/*     */   
/*     */   private RuntimeException extractApiException(HttpStatusCodeException ex) {
/*     */     try {
/* 159 */       ApiErrorResponse apiError = (ApiErrorResponse)(new ObjectMapper()).readValue(ex
/* 160 */           .getResponseBodyAsString(), ApiErrorResponse.class);
/*     */ 
/*     */ 
/*     */       
/* 164 */       return new RuntimeException(apiError.getMessage());
/*     */     }
/* 166 */     catch (Exception e) {
/* 167 */       return new RuntimeException("Unexpected error occurred");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<CategoryDTO> getSubCategoriesOnly() {
/* 174 */     CategoryDTO[] categories = (CategoryDTO[])this.restTemplate.getForObject(this.apiBaseUrl + "/api/categories/sub-categories", CategoryDTO[].class, new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     return Arrays.asList(categories);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<CategoryResponseDTO> getAllCategories() {
/* 185 */     UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.apiBaseUrl + "/api/categories");
/*     */     
/* 187 */     return (List<CategoryResponseDTO>)this.restTemplate.exchange(builder
/* 188 */         .toUriString(), HttpMethod.GET, null, (ParameterizedTypeReference)new Object(this), new Object[0])
/*     */ 
/*     */ 
/*     */       
/* 192 */       .getBody();
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/service/ui/CategoryUIService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */