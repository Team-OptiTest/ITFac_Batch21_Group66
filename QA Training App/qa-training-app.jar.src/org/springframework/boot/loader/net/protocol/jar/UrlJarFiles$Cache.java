/*     */ package org.springframework.boot.loader.net.protocol.jar;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.jar.JarFile;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Cache
/*     */ {
/* 147 */   private final Map<String, JarFile> jarFileUrlToJarFile = new HashMap<>();
/*     */   
/* 149 */   private final Map<JarFile, URL> jarFileToJarFileUrl = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JarFile get(URL jarFileUrl) {
/* 157 */     String urlKey = JarFileUrlKey.get(jarFileUrl);
/* 158 */     synchronized (this) {
/* 159 */       return this.jarFileUrlToJarFile.get(urlKey);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   URL get(JarFile jarFile) {
/* 169 */     synchronized (this) {
/* 170 */       return this.jarFileToJarFileUrl.get(jarFile);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean putIfAbsent(URL jarFileUrl, JarFile jarFile) {
/* 183 */     String urlKey = JarFileUrlKey.get(jarFileUrl);
/* 184 */     synchronized (this) {
/* 185 */       JarFile cached = this.jarFileUrlToJarFile.get(urlKey);
/* 186 */       if (cached == null) {
/* 187 */         this.jarFileUrlToJarFile.put(urlKey, jarFile);
/* 188 */         this.jarFileToJarFileUrl.put(jarFile, jarFileUrl);
/* 189 */         return true;
/*     */       } 
/* 191 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void remove(JarFile jarFile) {
/* 200 */     synchronized (this) {
/* 201 */       URL removedUrl = this.jarFileToJarFileUrl.remove(jarFile);
/* 202 */       if (removedUrl != null) {
/* 203 */         this.jarFileUrlToJarFile.remove(JarFileUrlKey.get(removedUrl));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   void clear() {
/* 209 */     synchronized (this) {
/* 210 */       this.jarFileToJarFileUrl.clear();
/* 211 */       this.jarFileUrlToJarFile.clear();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/UrlJarFiles$Cache.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */