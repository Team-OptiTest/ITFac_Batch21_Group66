/*     */ package BOOT-INF.classes.com.qatraining.controller.api;
/*     */ 
/*     */ import com.qatraining.api.ErrorResponse;
/*     */ import com.qatraining.exception.DuplicateResourceException;
/*     */ import jakarta.servlet.http.HttpServletRequest;
/*     */ import jakarta.validation.ConstraintViolation;
/*     */ import jakarta.validation.ConstraintViolationException;
/*     */ import java.time.LocalDateTime;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.HttpStatusCode;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.validation.FieldError;
/*     */ import org.springframework.web.bind.MethodArgumentNotValidException;
/*     */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*     */ import org.springframework.web.bind.annotation.ResponseStatus;
/*     */ import org.springframework.web.bind.annotation.RestControllerAdvice;
/*     */ import org.springframework.web.server.ResponseStatusException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @RestControllerAdvice
/*     */ public class GlobalExceptionHandler
/*     */ {
/*     */   @ExceptionHandler({NoSuchElementException.class})
/*     */   public ResponseEntity<ErrorResponse> handleNotFound(NoSuchElementException ex) {
/*  30 */     ErrorResponse error = new ErrorResponse(404, "NOT_FOUND", ex.getMessage());
/*     */ 
/*     */     
/*  33 */     return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(error);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ExceptionHandler({ResponseStatusException.class})
/*     */   public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex) {
/*  43 */     ErrorResponse error = new ErrorResponse(ex.getStatusCode().value(), ex.getStatusCode().toString(), ex.getReason());
/*     */ 
/*     */     
/*  46 */     return ResponseEntity.status(ex.getStatusCode()).body(error);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ExceptionHandler({MethodArgumentNotValidException.class})
/*     */   public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
/*  55 */     Map<String, String> errors = new HashMap<>();
/*     */     
/*  57 */     for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
/*  58 */       errors.put(fieldError.getField(), fieldError.getDefaultMessage());
/*     */     }
/*     */     
/*  61 */     Map<String, Object> body = new HashMap<>();
/*  62 */     body.put("status", Integer.valueOf(400));
/*  63 */     body.put("error", "BAD_REQUEST");
/*  64 */     body.put("message", "Validation failed");
/*  65 */     body.put("details", errors);
/*  66 */     body.put("timestamp", LocalDateTime.now());
/*     */     
/*  68 */     return ResponseEntity.badRequest().body(body);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ExceptionHandler({IllegalArgumentException.class})
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   public ErrorResponse handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
/*  77 */     return new ErrorResponse(HttpStatus.BAD_REQUEST
/*  78 */         .value(), "BAD_REQUEST", ex
/*     */         
/*  80 */         .getMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ExceptionHandler({Exception.class})
/*     */   public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) throws Exception {
/*  88 */     String uri = request.getRequestURI();
/*     */     
/*  90 */     if (ex instanceof org.springframework.security.authorization.AuthorizationDeniedException || ex instanceof org.springframework.security.access.AccessDeniedException || ex instanceof org.springframework.security.core.AuthenticationException)
/*     */     {
/*     */       
/*  93 */       throw ex;
/*     */     }
/*     */ 
/*     */     
/*  97 */     if (uri.startsWith("/ui") || uri
/*  98 */       .startsWith("/css") || uri
/*  99 */       .startsWith("/js") || uri
/* 100 */       .startsWith("/images") || uri
/* 101 */       .equals("/favicon.ico") || uri
/* 102 */       .startsWith("/.well-known"))
/*     */     {
/* 104 */       throw ex;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR", ex.getMessage());
/*     */ 
/*     */     
/* 113 */     return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(error);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ExceptionHandler({DuplicateResourceException.class})
/*     */   public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException ex) {
/* 125 */     ErrorResponse error = new ErrorResponse(400, "DUPLICATE_RESOURCE", ex.getMessage());
/*     */ 
/*     */     
/* 128 */     return ResponseEntity.status((HttpStatusCode)HttpStatus.BAD_REQUEST).body(error);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ExceptionHandler({ConstraintViolationException.class})
/*     */   public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
/* 135 */     Map<String, Object> body = new HashMap<>();
/* 136 */     body.put("status", Integer.valueOf(400));
/* 137 */     body.put("error", "BAD_REQUEST");
/* 138 */     body.put("message", ((ConstraintViolation)ex.getConstraintViolations()
/* 139 */         .iterator().next()).getMessage());
/* 140 */     body.put("timestamp", LocalDateTime.now());
/*     */     
/* 142 */     return ResponseEntity.badRequest().body(body);
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/controller/api/GlobalExceptionHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */