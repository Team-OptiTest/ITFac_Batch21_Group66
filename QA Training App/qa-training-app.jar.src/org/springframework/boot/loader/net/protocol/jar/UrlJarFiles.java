/*     */ package org.springframework.boot.loader.net.protocol.jar;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
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
/*     */ class UrlJarFiles
/*     */ {
/*     */   private final UrlJarFileFactory factory;
/*  39 */   private final Cache cache = new Cache();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   UrlJarFiles() {
/*  45 */     this(new UrlJarFileFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   UrlJarFiles(UrlJarFileFactory factory) {
/*  53 */     this.factory = factory;
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
/*     */   
/*     */   JarFile getOrCreate(boolean useCaches, URL jarFileUrl) throws IOException {
/*  66 */     if (useCaches) {
/*  67 */       JarFile cached = getCached(jarFileUrl);
/*  68 */       if (cached != null) {
/*  69 */         return cached;
/*     */       }
/*     */     } 
/*  72 */     return this.factory.createJarFile(jarFileUrl, this::onClose);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JarFile getCached(URL jarFileUrl) {
/*  81 */     return this.cache.get(jarFileUrl);
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
/*     */   boolean cacheIfAbsent(boolean useCaches, URL jarFileUrl, JarFile jarFile) {
/*  93 */     if (!useCaches) {
/*  94 */       return false;
/*     */     }
/*  96 */     return this.cache.putIfAbsent(jarFileUrl, jarFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void closeIfNotCached(URL jarFileUrl, JarFile jarFile) throws IOException {
/* 106 */     JarFile cached = getCached(jarFileUrl);
/* 107 */     if (cached != jarFile) {
/* 108 */       jarFile.close();
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
/*     */   URLConnection reconnect(JarFile jarFile, URLConnection existingConnection) throws IOException {
/* 121 */     Boolean useCaches = (existingConnection != null) ? Boolean.valueOf(existingConnection.getUseCaches()) : null;
/* 122 */     URLConnection connection = openConnection(jarFile);
/* 123 */     if (useCaches != null && connection != null) {
/* 124 */       connection.setUseCaches(useCaches.booleanValue());
/*     */     }
/* 126 */     return connection;
/*     */   }
/*     */   
/*     */   private URLConnection openConnection(JarFile jarFile) throws IOException {
/* 130 */     URL url = this.cache.get(jarFile);
/* 131 */     return (url != null) ? url.openConnection() : null;
/*     */   }
/*     */   
/*     */   private void onClose(JarFile jarFile) {
/* 135 */     this.cache.remove(jarFile);
/*     */   }
/*     */   
/*     */   void clearCache() {
/* 139 */     this.cache.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Cache
/*     */   {
/* 147 */     private final Map<String, JarFile> jarFileUrlToJarFile = new HashMap<>();
/*     */     
/* 149 */     private final Map<JarFile, URL> jarFileToJarFileUrl = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     JarFile get(URL jarFileUrl) {
/* 157 */       String urlKey = JarFileUrlKey.get(jarFileUrl);
/* 158 */       synchronized (this) {
/* 159 */         return this.jarFileUrlToJarFile.get(urlKey);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     URL get(JarFile jarFile) {
/* 169 */       synchronized (this) {
/* 170 */         return this.jarFileToJarFileUrl.get(jarFile);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean putIfAbsent(URL jarFileUrl, JarFile jarFile) {
/* 183 */       String urlKey = JarFileUrlKey.get(jarFileUrl);
/* 184 */       synchronized (this) {
/* 185 */         JarFile cached = this.jarFileUrlToJarFile.get(urlKey);
/* 186 */         if (cached == null) {
/* 187 */           this.jarFileUrlToJarFile.put(urlKey, jarFile);
/* 188 */           this.jarFileToJarFileUrl.put(jarFile, jarFileUrl);
/* 189 */           return true;
/*     */         } 
/* 191 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void remove(JarFile jarFile) {
/* 200 */       synchronized (this) {
/* 201 */         URL removedUrl = this.jarFileToJarFileUrl.remove(jarFile);
/* 202 */         if (removedUrl != null) {
/* 203 */           this.jarFileUrlToJarFile.remove(JarFileUrlKey.get(removedUrl));
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void clear() {
/* 209 */       synchronized (this) {
/* 210 */         this.jarFileToJarFileUrl.clear();
/* 211 */         this.jarFileUrlToJarFile.clear();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/UrlJarFiles.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */