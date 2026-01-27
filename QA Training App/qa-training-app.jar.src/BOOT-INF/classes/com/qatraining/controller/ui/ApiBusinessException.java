/*    */ package BOOT-INF.classes.com.qatraining.controller.ui;
/*    */ 
/*    */ public class ApiBusinessException
/*    */   extends RuntimeException {
/*    */   private final String errorCode;
/*    */   private final String message;
/*    */   
/*    */   public ApiBusinessException(String errorCode, String message) {
/*  9 */     super(message);
/* 10 */     this.errorCode = errorCode;
/* 11 */     this.message = message;
/*    */   }
/*    */   
/*    */   public String getErrorCode() {
/* 15 */     return this.errorCode;
/*    */   }
/*    */   
/*    */   public String getErrorMessage() {
/* 19 */     return this.message;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/controller/ui/ApiBusinessException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */