/*    */ package org.springframework.boot.loader.net.protocol.jar;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import java.util.jar.JarEntry;
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
/*    */ public final class JarUrl
/*    */ {
/*    */   public static URL create(File file) {
/* 41 */     return create(file, (String)null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static URL create(File file, JarEntry nestedEntry) {
/* 51 */     return create(file, (nestedEntry != null) ? nestedEntry.getName() : null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static URL create(File file, String nestedEntryName) {
/* 61 */     return create(file, nestedEntryName, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static URL create(File file, String nestedEntryName, String path) {
/*    */     try {
/* 73 */       path = (path != null) ? path : "";
/* 74 */       return new URL(null, "jar:" + getJarReference(file, nestedEntryName) + "!/" + path, Handler.INSTANCE);
/*    */     }
/* 76 */     catch (MalformedURLException ex) {
/* 77 */       throw new IllegalStateException("Unable to create JarFileArchive URL", ex);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static String getJarReference(File file, String nestedEntryName) {
/* 82 */     String jarFilePath = file.toURI().getRawPath().replace("!", "%21");
/* 83 */     return (nestedEntryName != null) ? ("nested:" + jarFilePath + "/!" + nestedEntryName) : ("file:" + jarFilePath);
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/JarUrl.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */