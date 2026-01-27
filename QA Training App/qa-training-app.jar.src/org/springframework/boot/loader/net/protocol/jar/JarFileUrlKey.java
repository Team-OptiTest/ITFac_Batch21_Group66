/*    */ package org.springframework.boot.loader.net.protocol.jar;
/*    */ 
/*    */ import java.lang.ref.SoftReference;
/*    */ import java.net.URL;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class JarFileUrlKey
/*    */ {
/*    */   private static volatile SoftReference<Map<URL, String>> cache;
/*    */   
/*    */   static String get(URL url) {
/* 43 */     Map<URL, String> cache = (JarFileUrlKey.cache != null) ? JarFileUrlKey.cache.get() : null;
/* 44 */     if (cache == null) {
/* 45 */       cache = new ConcurrentHashMap<>();
/* 46 */       JarFileUrlKey.cache = new SoftReference<>(cache);
/*    */     } 
/* 48 */     return cache.computeIfAbsent(url, JarFileUrlKey::create);
/*    */   }
/*    */   
/*    */   private static String create(URL url) {
/* 52 */     StringBuilder value = new StringBuilder();
/* 53 */     String protocol = url.getProtocol();
/* 54 */     String host = url.getHost();
/* 55 */     int port = (url.getPort() != -1) ? url.getPort() : url.getDefaultPort();
/* 56 */     String file = url.getFile();
/* 57 */     value.append(protocol.toLowerCase());
/* 58 */     value.append(":");
/* 59 */     if (host != null && !host.isEmpty()) {
/* 60 */       value.append(host.toLowerCase());
/* 61 */       value.append((port != -1) ? (":" + port) : "");
/*    */     } 
/* 63 */     value.append((file != null) ? file : "");
/* 64 */     if ("runtime".equals(url.getRef())) {
/* 65 */       value.append("#runtime");
/*    */     }
/* 67 */     return value.toString();
/*    */   }
/*    */   
/*    */   static void clearCache() {
/* 71 */     cache = null;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/JarFileUrlKey.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */