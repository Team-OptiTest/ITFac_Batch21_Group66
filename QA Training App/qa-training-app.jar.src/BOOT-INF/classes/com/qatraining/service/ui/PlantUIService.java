/*     */ package BOOT-INF.classes.com.qatraining.service.ui;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.qatraining.dto.ApiErrorResponse;
/*     */ import com.qatraining.dto.CategoryResponseDTO;
/*     */ import com.qatraining.dto.PageResponse;
/*     */ import com.qatraining.dto.PlantCreateDTO;
/*     */ import com.qatraining.dto.PlantDTO;
/*     */ import com.qatraining.dto.PlantEditResponseDTO;
/*     */ import com.qatraining.dto.PlantSummaryDTO;
/*     */ import com.qatraining.entity.Plant;
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
/*     */ import org.springframework.data.domain.Sort;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.stereotype.Service;
/*     */ import org.springframework.web.client.HttpClientErrorException;
/*     */ import org.springframework.web.client.HttpStatusCodeException;
/*     */ import org.springframework.web.client.RestTemplate;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ 
/*     */ @Service
/*     */ public class PlantUIService {
/*     */   private final RestTemplate restTemplate;
/*     */   private final String apiBaseUrl;
/*     */   
/*     */   public PlantUIService(RestTemplate restTemplate, @Value("${api.base-url}") String apiBaseUrl, HttpSession session, CategoryUIService categoryUIService) {
/*  38 */     this.restTemplate = restTemplate;
/*  39 */     this.apiBaseUrl = apiBaseUrl;
/*  40 */     this.session = session;
/*  41 */     this.categoryUIService = categoryUIService;
/*     */   }
/*     */   
/*     */   private final HttpSession session;
/*     */   
/*     */   public List<PlantDTO> getAllPlants() {
/*  47 */     Plant[] plants = (Plant[])this.restTemplate.getForObject(this.apiBaseUrl + "/api/plants", Plant[].class, new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     return Arrays.<Plant>stream(plants)
/*  53 */       .map(this::toDTO)
/*  54 */       .toList();
/*     */   }
/*     */   private final CategoryUIService categoryUIService;
/*     */   public void createPlant(PlantCreateDTO dto) {
/*     */     try {
/*  59 */       this.restTemplate.postForObject(this.apiBaseUrl + "/api/plants/category/" + this.apiBaseUrl, 
/*  60 */           dto, Void.class, new Object[0]);
/*     */ 
/*     */     
/*     */     }
/*  64 */     catch (HttpClientErrorException ex) {
/*  65 */       throw extractApiException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public PlantEditResponseDTO getPlantById(Long id) {
/*  70 */     return (PlantEditResponseDTO)this.restTemplate.getForObject(this.apiBaseUrl + "/api/plants/" + this.apiBaseUrl, PlantEditResponseDTO.class, new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<CategoryResponseDTO> getSubCategoriesOnly() {
/*  77 */     return getCategories()
/*  78 */       .stream()
/*  79 */       .filter(c -> !"-".equals(c.getParentName()))
/*  80 */       .toList();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updatePlant(Long id, PlantCreateDTO dto) {
/*     */     try {
/*  86 */       this.restTemplate.put(this.apiBaseUrl + "/api/plants/" + this.apiBaseUrl, dto, new Object[0]);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*  91 */     catch (HttpClientErrorException ex) {
/*  92 */       throw extractApiException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<CategoryResponseDTO> getCategories() {
/*  97 */     return this.categoryUIService.getAllCategories();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void deletePlant(Long id) {
/* 103 */     this.restTemplate.delete(this.apiBaseUrl + "/api/plants/" + this.apiBaseUrl, new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PlantDTO> getPlantsByCategory(Long categoryId) {
/* 108 */     Plant[] plants = (Plant[])this.restTemplate.getForObject(this.apiBaseUrl + "/api/plants/category/" + this.apiBaseUrl, Plant[].class, new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     return Arrays.<Plant>stream(plants)
/* 114 */       .map(this::toDTO)
/* 115 */       .toList();
/*     */   }
/*     */   
/*     */   private HttpHeaders authHeaders() {
/* 119 */     String token = (String)this.session.getAttribute("JWT_TOKEN");
/*     */     
/* 121 */     HttpHeaders headers = new HttpHeaders();
/* 122 */     headers.setBearerAuth(token);
/*     */     
/* 124 */     return headers;
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
/*     */   public Page<PlantDTO> getPlantsPaged(int page, int size, String sortField, String sortDir, String name, Long categoryId) {
/* 138 */     Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(new String[] { sortField }).ascending() : Sort.by(new String[] { sortField }).descending();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     String url = UriComponentsBuilder.fromHttpUrl(this.apiBaseUrl + "/api/plants/paged").queryParam("page", new Object[] { Integer.valueOf(page) }).queryParam("size", new Object[] { Integer.valueOf(size) }).queryParam("sort", new Object[] { sortField + "," + sortField }).queryParam("name", new Object[] { name }).queryParam("categoryId", new Object[] { categoryId }).toUriString();
/*     */ 
/*     */     
/* 150 */     ResponseEntity<PageResponse<Plant>> response = this.restTemplate.exchange(url, HttpMethod.GET, null, (ParameterizedTypeReference)new Object(this), new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     PageResponse<Plant> pageResponse = (PageResponse<Plant>)response.getBody();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     List<PlantDTO> dtoList = pageResponse.getContent().stream().map(this::toDTO).toList();
/*     */     
/* 164 */     return (Page<PlantDTO>)new PageImpl(dtoList, 
/*     */         
/* 166 */         (Pageable)PageRequest.of(page, size, sort), pageResponse
/* 167 */         .getTotalElements());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PlantDTO toDTO(Plant plant) {
/* 176 */     PlantDTO dto = new PlantDTO();
/* 177 */     dto.setId(plant.getId());
/* 178 */     dto.setName(plant.getName());
/* 179 */     dto.setPrice(plant.getPrice());
/* 180 */     dto.setQuantity(plant.getQuantity());
/* 181 */     dto.setCategoryName(
/* 182 */         (plant.getCategory() != null) ? 
/* 183 */         plant.getCategory().getName() : 
/* 184 */         "-");
/*     */     
/* 186 */     return dto;
/*     */   }
/*     */ 
/*     */   
/*     */   private RuntimeException extractApiException(HttpStatusCodeException ex) {
/*     */     try {
/* 192 */       ApiErrorResponse apiError = (ApiErrorResponse)(new ObjectMapper()).readValue(ex
/* 193 */           .getResponseBodyAsString(), ApiErrorResponse.class);
/*     */ 
/*     */ 
/*     */       
/* 197 */       return new RuntimeException(apiError.getMessage());
/*     */     }
/* 199 */     catch (Exception e) {
/* 200 */       return new RuntimeException("Unexpected error occurred");
/*     */     } 
/*     */   }
/*     */   
/*     */   public PlantSummaryDTO getPlantSummary() {
/* 205 */     return (PlantSummaryDTO)this.restTemplate.getForObject(this.apiBaseUrl + "/api/plants/summary", PlantSummaryDTO.class, new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Plant> getAvailablePlants() {
/* 213 */     Plant[] plants = (Plant[])this.restTemplate.getForObject(this.apiBaseUrl + "/api/plants", Plant[].class, new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 218 */     return Arrays.<Plant>stream(plants)
/* 219 */       .filter(p -> (p.getQuantity().intValue() > 0))
/* 220 */       .toList();
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/service/ui/PlantUIService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */