/*    */ package org.springframework.boot.loader.nio.file;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.nio.charset.StandardCharsets;
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
/*    */ final class UriPathEncoder
/*    */ {
/* 31 */   private static char[] ALLOWED = "/:@-._~!$&'()*+,;=".toCharArray();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static String encode(String path) {
/* 37 */     byte[] bytes = path.getBytes(StandardCharsets.UTF_8);
/* 38 */     for (byte b : bytes) {
/* 39 */       if (!isAllowed(b)) {
/* 40 */         return encode(bytes);
/*    */       }
/*    */     } 
/* 43 */     return path;
/*    */   }
/*    */   
/*    */   private static String encode(byte[] bytes) {
/* 47 */     ByteArrayOutputStream result = new ByteArrayOutputStream(bytes.length);
/* 48 */     for (byte b : bytes) {
/* 49 */       if (isAllowed(b)) {
/* 50 */         result.write(b);
/*    */       } else {
/*    */         
/* 53 */         result.write(37);
/* 54 */         result.write(Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16)));
/* 55 */         result.write(Character.toUpperCase(Character.forDigit(b & 0xF, 16)));
/*    */       } 
/*    */     } 
/* 58 */     return result.toString(StandardCharsets.UTF_8);
/*    */   }
/*    */   
/*    */   private static boolean isAllowed(int ch) {
/* 62 */     for (char allowed : ALLOWED) {
/* 63 */       if (ch == allowed) {
/* 64 */         return true;
/*    */       }
/*    */     } 
/* 67 */     return (isAlpha(ch) || isDigit(ch));
/*    */   }
/*    */   
/*    */   private static boolean isAlpha(int ch) {
/* 71 */     return ((ch >= 97 && ch <= 122) || (ch >= 65 && ch <= 90));
/*    */   }
/*    */   
/*    */   private static boolean isDigit(int ch) {
/* 75 */     return (ch >= 48 && ch <= 57);
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/nio/file/UriPathEncoder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */