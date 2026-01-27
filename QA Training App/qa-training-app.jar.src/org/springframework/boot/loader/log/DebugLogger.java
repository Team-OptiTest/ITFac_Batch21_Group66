/*     */ package org.springframework.boot.loader.log;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DebugLogger
/*     */ {
/*     */   private static final String ENABLED_PROPERTY = "loader.debug";
/*  31 */   private static final DebugLogger disabled = Boolean.getBoolean("loader.debug") ? null : new DisabledDebugLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void log(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void log(String paramString, Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void log(String paramString, Object paramObject1, Object paramObject2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void log(String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void log(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DebugLogger get(Class<?> sourceClass) {
/*  80 */     return (disabled != null) ? disabled : new SystemErrDebugLogger(sourceClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class DisabledDebugLogger
/*     */     extends DebugLogger
/*     */   {
/*     */     public void log(String message) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void log(String message, Object arg1) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void log(String message, Object arg1, Object arg2) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void log(String message, Object arg1, Object arg2, Object arg3) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void log(String message, Object arg1, Object arg2, Object arg3, Object arg4) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class SystemErrDebugLogger
/*     */     extends DebugLogger
/*     */   {
/*     */     private final String prefix;
/*     */ 
/*     */ 
/*     */     
/*     */     SystemErrDebugLogger(Class<?> sourceClass) {
/* 118 */       this.prefix = "LOADER: " + sourceClass + " : ";
/*     */     }
/*     */ 
/*     */     
/*     */     public void log(String message) {
/* 123 */       print(message);
/*     */     }
/*     */ 
/*     */     
/*     */     public void log(String message, Object arg1) {
/* 128 */       print(message.formatted(new Object[] { arg1 }));
/*     */     }
/*     */ 
/*     */     
/*     */     public void log(String message, Object arg1, Object arg2) {
/* 133 */       print(message.formatted(new Object[] { arg1, arg2 }));
/*     */     }
/*     */ 
/*     */     
/*     */     public void log(String message, Object arg1, Object arg2, Object arg3) {
/* 138 */       print(message.formatted(new Object[] { arg1, arg2, arg3 }));
/*     */     }
/*     */ 
/*     */     
/*     */     public void log(String message, Object arg1, Object arg2, Object arg3, Object arg4) {
/* 143 */       print(message.formatted(new Object[] { arg1, arg2, arg3, arg4 }));
/*     */     }
/*     */     
/*     */     private void print(String message) {
/* 147 */       System.err.println(this.prefix + this.prefix);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/log/DebugLogger.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */