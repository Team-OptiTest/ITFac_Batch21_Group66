/*    */ package org.springframework.boot.loader.net.protocol;
/*    */ 
/*    */ import java.net.URL;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Handlers
/*    */ {
/*    */   private static final String PROTOCOL_HANDLER_PACKAGES = "java.protocol.handler.pkgs";
/* 33 */   private static final String PACKAGE = Handlers.class.getPackageName();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void register() {
/* 43 */     String packages = System.getProperty("java.protocol.handler.pkgs", "");
/* 44 */     packages = (!packages.isEmpty() && !packages.contains(PACKAGE)) ? (packages + "|" + packages) : PACKAGE;
/* 45 */     System.setProperty("java.protocol.handler.pkgs", packages);
/* 46 */     resetCachedUrlHandlers();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void resetCachedUrlHandlers() {
/*    */     try {
/* 56 */       URL.setURLStreamHandlerFactory(null);
/*    */     }
/* 58 */     catch (Error error) {}
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/Handlers.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */