/*    */ package BOOT-INF.classes.com.qatraining.api;
/*    */ 
/*    */ import java.time.LocalDateTime;
/*    */ 
/*    */ public class ErrorResponse
/*    */ {
/*    */   private int status;
/*    */   private String error;
/*    */   private String message;
/*    */   private LocalDateTime timestamp;
/*    */   
/*    */   public ErrorResponse(int status, String error, String message) {
/* 13 */     this.status = status;
/* 14 */     this.error = error;
/* 15 */     this.message = message;
/* 16 */     this.timestamp = LocalDateTime.now();
/*    */   }
/*    */   
/*    */   public int getStatus() {
/* 20 */     return this.status;
/*    */   }
/*    */   
/*    */   public String getError() {
/* 24 */     return this.error;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 28 */     return this.message;
/*    */   }
/*    */   
/*    */   public LocalDateTime getTimestamp() {
/* 32 */     return this.timestamp;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/api/ErrorResponse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */