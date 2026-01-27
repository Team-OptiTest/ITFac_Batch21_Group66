/*    */ package BOOT-INF.classes.com.qatraining.dto;
/*    */ 
/*    */ public class ApiErrorResponse {
/*    */   private int status;
/*    */   private String error;
/*    */   private String message;
/*    */   private String timestamp;
/*    */   
/*    */   public String getTimestamp() {
/* 10 */     return this.timestamp;
/*    */   }
/*    */   
/*    */   public void setTimestamp(String timestamp) {
/* 14 */     this.timestamp = timestamp;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getStatus() {
/* 20 */     return this.status;
/*    */   }
/*    */   
/*    */   public void setStatus(int status) {
/* 24 */     this.status = status;
/*    */   }
/*    */   
/*    */   public String getError() {
/* 28 */     return this.error;
/*    */   }
/*    */   
/*    */   public void setError(String error) {
/* 32 */     this.error = error;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 36 */     return this.message;
/*    */   }
/*    */   
/*    */   public void setMessage(String message) {
/* 40 */     this.message = message;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/dto/ApiErrorResponse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */