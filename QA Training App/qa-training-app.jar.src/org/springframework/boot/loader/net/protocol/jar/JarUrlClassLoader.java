/*     */ package org.springframework.boot.loader.net.protocol.jar;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.jar.JarFile;
/*     */ import org.springframework.boot.loader.jar.NestedJarFile;
/*     */ import org.springframework.boot.loader.net.protocol.nested.Handler;
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
/*     */ public abstract class JarUrlClassLoader
/*     */   extends URLClassLoader
/*     */ {
/*     */   private final URL[] urls;
/*     */   private final boolean hasJarUrls;
/*     */   
/*     */   static {
/*  44 */     ClassLoader.registerAsParallelCapable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   private final Map<URL, JarFile> jarFiles = new ConcurrentHashMap<>();
/*     */   
/*  53 */   private final Set<String> undefinablePackages = ConcurrentHashMap.newKeySet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JarUrlClassLoader(URL[] urls, ClassLoader parent) {
/*  61 */     super(urls, parent);
/*  62 */     this.urls = urls;
/*  63 */     this.hasJarUrls = Arrays.<URL>stream(urls).anyMatch(this::isJarUrl);
/*     */   }
/*     */ 
/*     */   
/*     */   public URL findResource(String name) {
/*  68 */     if (!this.hasJarUrls) {
/*  69 */       return super.findResource(name);
/*     */     }
/*  71 */     Optimizations.enable(false);
/*     */     try {
/*  73 */       return super.findResource(name);
/*     */     } finally {
/*     */       
/*  76 */       Optimizations.disable();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration<URL> findResources(String name) throws IOException {
/*  82 */     if (!this.hasJarUrls) {
/*  83 */       return super.findResources(name);
/*     */     }
/*  85 */     Optimizations.enable(false);
/*     */     try {
/*  87 */       return new OptimizedEnumeration(super.findResources(name));
/*     */     } finally {
/*     */       
/*  90 */       Optimizations.disable();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
/*  96 */     if (!this.hasJarUrls) {
/*  97 */       return super.loadClass(name, resolve);
/*     */     }
/*  99 */     Optimizations.enable(true);
/*     */     try {
/*     */       try {
/* 102 */         definePackageIfNecessary(name);
/*     */       }
/* 104 */       catch (IllegalArgumentException ex) {
/* 105 */         tolerateRaceConditionDueToBeingParallelCapable(ex, name);
/*     */       } 
/* 107 */       return super.loadClass(name, resolve);
/*     */     } finally {
/*     */       
/* 110 */       Optimizations.disable();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void definePackageIfNecessary(String className) {
/* 121 */     if (className.startsWith("java.")) {
/*     */       return;
/*     */     }
/* 124 */     int lastDot = className.lastIndexOf('.');
/* 125 */     if (lastDot >= 0) {
/* 126 */       String packageName = className.substring(0, lastDot);
/* 127 */       if (getDefinedPackage(packageName) == null) {
/*     */         try {
/* 129 */           definePackage(className, packageName);
/*     */         }
/* 131 */         catch (IllegalArgumentException ex) {
/* 132 */           tolerateRaceConditionDueToBeingParallelCapable(ex, packageName);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void definePackage(String className, String packageName) {
/* 139 */     if (this.undefinablePackages.contains(packageName)) {
/*     */       return;
/*     */     }
/* 142 */     String packageEntryName = packageName.replace('.', '/') + "/";
/* 143 */     String classEntryName = className.replace('.', '/') + ".class";
/* 144 */     for (URL url : this.urls) {
/*     */       try {
/* 146 */         JarFile jarFile = getJarFile(url);
/* 147 */         if (jarFile != null && 
/* 148 */           hasEntry(jarFile, classEntryName) && hasEntry(jarFile, packageEntryName) && jarFile
/* 149 */           .getManifest() != null) {
/* 150 */           definePackage(packageName, jarFile.getManifest(), url);
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/* 155 */       } catch (IOException iOException) {}
/*     */     } 
/*     */ 
/*     */     
/* 159 */     this.undefinablePackages.add(packageName);
/*     */   }
/*     */ 
/*     */   
/*     */   private void tolerateRaceConditionDueToBeingParallelCapable(IllegalArgumentException ex, String packageName) throws AssertionError {
/* 164 */     if (getDefinedPackage(packageName) == null)
/*     */     {
/*     */ 
/*     */       
/* 168 */       throw new AssertionError("Package %s has already been defined but it could not be found"
/* 169 */           .formatted(new Object[] { packageName }, ), ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasEntry(JarFile jarFile, String name) {
/* 174 */     NestedJarFile nestedJarFile = (NestedJarFile)jarFile; return (jarFile instanceof NestedJarFile) ? nestedJarFile.hasEntry(name) : (
/* 175 */       (jarFile.getEntry(name) != null));
/*     */   }
/*     */   
/*     */   private JarFile getJarFile(URL url) throws IOException {
/* 179 */     JarFile jarFile = this.jarFiles.get(url);
/* 180 */     if (jarFile != null) {
/* 181 */       return jarFile;
/*     */     }
/* 183 */     URLConnection connection = url.openConnection();
/* 184 */     if (!(connection instanceof JarURLConnection)) {
/* 185 */       return null;
/*     */     }
/* 187 */     connection.setUseCaches(false);
/* 188 */     jarFile = ((JarURLConnection)connection).getJarFile();
/* 189 */     synchronized (this.jarFiles) {
/* 190 */       JarFile previous = this.jarFiles.putIfAbsent(url, jarFile);
/* 191 */       if (previous != null) {
/* 192 */         jarFile.close();
/* 193 */         jarFile = previous;
/*     */       } 
/*     */     } 
/* 196 */     return jarFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearCache() {
/* 204 */     Handler.clearCache();
/* 205 */     Handler.clearCache();
/*     */     try {
/* 207 */       clearJarFiles();
/*     */     }
/* 209 */     catch (IOException iOException) {}
/*     */ 
/*     */     
/* 212 */     for (URL url : this.urls) {
/* 213 */       if (isJarUrl(url)) {
/* 214 */         clearCache(url);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clearCache(URL url) {
/*     */     try {
/* 221 */       URLConnection connection = url.openConnection();
/* 222 */       if (connection instanceof JarURLConnection) { JarURLConnection jarUrlConnection = (JarURLConnection)connection;
/* 223 */         clearCache(jarUrlConnection); }
/*     */ 
/*     */     
/* 226 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void clearCache(JarURLConnection connection) throws IOException {
/* 232 */     JarFile jarFile = connection.getJarFile();
/* 233 */     if (jarFile instanceof NestedJarFile) { NestedJarFile nestedJarFile = (NestedJarFile)jarFile;
/* 234 */       nestedJarFile.clearCache(); }
/*     */   
/*     */   }
/*     */   
/*     */   private boolean isJarUrl(URL url) {
/* 239 */     return "jar".equals(url.getProtocol());
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 244 */     super.close();
/* 245 */     clearJarFiles();
/*     */   }
/*     */   
/*     */   private void clearJarFiles() throws IOException {
/* 249 */     synchronized (this.jarFiles) {
/* 250 */       for (JarFile jarFile : this.jarFiles.values()) {
/* 251 */         jarFile.close();
/*     */       }
/* 253 */       this.jarFiles.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class OptimizedEnumeration
/*     */     implements Enumeration<URL>
/*     */   {
/*     */     private final Enumeration<URL> delegate;
/*     */ 
/*     */     
/*     */     OptimizedEnumeration(Enumeration<URL> delegate) {
/* 265 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasMoreElements() {
/* 270 */       Optimizations.enable(false);
/*     */       try {
/* 272 */         return this.delegate.hasMoreElements();
/*     */       } finally {
/*     */         
/* 275 */         Optimizations.disable();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public URL nextElement() {
/* 282 */       Optimizations.enable(false);
/*     */       try {
/* 284 */         return this.delegate.nextElement();
/*     */       } finally {
/*     */         
/* 287 */         Optimizations.disable();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/JarUrlClassLoader.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */