/*    */ package org.springframework.boot.loader.net.protocol.jar;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class Optimizations
/*    */ {
/* 26 */   private static final ThreadLocal<Boolean> status = new ThreadLocal<>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void enable(boolean readContents) {
/* 32 */     status.set(Boolean.valueOf(readContents));
/*    */   }
/*    */   
/*    */   static void disable() {
/* 36 */     status.remove();
/*    */   }
/*    */   
/*    */   static boolean isEnabled() {
/* 40 */     return (status.get() != null);
/*    */   }
/*    */   
/*    */   static boolean isEnabled(boolean readContents) {
/* 44 */     return Boolean.valueOf(readContents).equals(status.get());
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/Optimizations.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */