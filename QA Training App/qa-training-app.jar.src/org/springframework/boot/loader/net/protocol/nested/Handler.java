/*    */ package org.springframework.boot.loader.net.protocol.nested;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import java.net.URLStreamHandler;
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
/*    */ 
/*    */ public class Handler
/*    */   extends URLStreamHandler
/*    */ {
/*    */   private static final String PREFIX = "nested:";
/*    */   
/*    */   protected URLConnection openConnection(URL url) throws IOException {
/* 40 */     return new NestedUrlConnection(url);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void assertUrlIsNotMalformed(String url) {
/* 48 */     if (url == null || !url.startsWith("nested:")) {
/* 49 */       throw new IllegalArgumentException("'url' must not be null and must use 'nested' protocol");
/*    */     }
/* 51 */     NestedLocation.parse(url.substring("nested:".length()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void clearCache() {
/* 58 */     NestedLocation.clearCache();
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/nested/Handler.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */