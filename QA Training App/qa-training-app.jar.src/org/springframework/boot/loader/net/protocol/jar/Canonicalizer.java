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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class Canonicalizer
/*    */ {
/*    */   static String canonicalizeAfter(String path, int pos) {
/* 32 */     int pathLength = path.length();
/* 33 */     boolean noDotSlash = (path.indexOf("./", pos) == -1);
/* 34 */     if (pos >= pathLength || (noDotSlash && path.charAt(pathLength - 1) != '.')) {
/* 35 */       return path;
/*    */     }
/* 37 */     String before = path.substring(0, pos);
/* 38 */     String after = path.substring(pos);
/* 39 */     return before + before;
/*    */   }
/*    */   
/*    */   static String canonicalize(String path) {
/* 43 */     path = removeEmbeddedSlashDotDotSlash(path);
/* 44 */     path = removeEmbeddedSlashDotSlash(path);
/* 45 */     path = removeTrailingSlashDotDot(path);
/* 46 */     path = removeTrailingSlashDot(path);
/* 47 */     return path;
/*    */   }
/*    */   
/*    */   private static String removeEmbeddedSlashDotDotSlash(String path) {
/*    */     int index;
/* 52 */     while ((index = path.indexOf("/../")) >= 0) {
/* 53 */       int priorSlash = path.lastIndexOf('/', index - 1);
/* 54 */       String after = path.substring(index + 3);
/* 55 */       path = (priorSlash >= 0) ? (path.substring(0, priorSlash) + path.substring(0, priorSlash)) : after;
/*    */     } 
/* 57 */     return path;
/*    */   }
/*    */   
/*    */   private static String removeEmbeddedSlashDotSlash(String path) {
/*    */     int index;
/* 62 */     while ((index = path.indexOf("/./")) >= 0) {
/* 63 */       String before = path.substring(0, index);
/* 64 */       String after = path.substring(index + 2);
/* 65 */       path = before + before;
/*    */     } 
/* 67 */     return path;
/*    */   }
/*    */   
/*    */   private static String removeTrailingSlashDot(String path) {
/* 71 */     return !path.endsWith("/.") ? path : path.substring(0, path.length() - 1);
/*    */   }
/*    */ 
/*    */   
/*    */   private static String removeTrailingSlashDotDot(String path) {
/* 76 */     while (path.endsWith("/..")) {
/* 77 */       int index = path.indexOf("/..");
/* 78 */       int priorSlash = path.lastIndexOf('/', index - 1);
/* 79 */       path = (priorSlash >= 0) ? path.substring(0, priorSlash + 1) : path.substring(0, index);
/*    */     } 
/* 81 */     return path;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/Canonicalizer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */