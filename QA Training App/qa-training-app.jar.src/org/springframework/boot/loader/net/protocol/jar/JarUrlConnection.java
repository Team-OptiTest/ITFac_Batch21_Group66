/*     */ package org.springframework.boot.loader.net.protocol.jar;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLStreamHandler;
/*     */ import java.security.Permission;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import org.springframework.boot.loader.jar.NestedJarFile;
/*     */ import org.springframework.boot.loader.net.util.UrlDecoder;
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
/*     */ final class JarUrlConnection
/*     */   extends JarURLConnection
/*     */ {
/*  51 */   static final UrlJarFiles jarFiles = new UrlJarFiles();
/*     */   
/*  53 */   static final InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
/*     */   
/*  55 */   static final FileNotFoundException FILE_NOT_FOUND_EXCEPTION = new FileNotFoundException("Jar file or entry not found"); private static final URL NOT_FOUND_URL; static final JarUrlConnection NOT_FOUND_CONNECTION; private final String entryName; private final Supplier<FileNotFoundException> notFound;
/*     */   private JarFile jarFile;
/*     */   private URLConnection jarFileConnection;
/*     */   private JarEntry jarEntry;
/*     */   private String contentType;
/*     */   
/*     */   static {
/*     */     try {
/*  63 */       NOT_FOUND_URL = new URL("jar:", null, 0, "nested:!/", new EmptyUrlStreamHandler());
/*  64 */       NOT_FOUND_CONNECTION = new JarUrlConnection(() -> FILE_NOT_FOUND_EXCEPTION);
/*     */     }
/*  66 */     catch (IOException ex) {
/*  67 */       throw new IllegalStateException(ex);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JarUrlConnection(URL url) throws IOException {
/*  84 */     super(url);
/*  85 */     this.entryName = getEntryName();
/*  86 */     this.notFound = null;
/*  87 */     this.jarFileConnection = getJarFileURL().openConnection();
/*  88 */     this.jarFileConnection.setUseCaches(this.useCaches);
/*     */   }
/*     */   
/*     */   private JarUrlConnection(Supplier<FileNotFoundException> notFound) throws IOException {
/*  92 */     super(NOT_FOUND_URL);
/*  93 */     this.entryName = null;
/*  94 */     this.notFound = notFound;
/*     */   }
/*     */ 
/*     */   
/*     */   public JarFile getJarFile() throws IOException {
/*  99 */     connect();
/* 100 */     return this.jarFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public JarEntry getJarEntry() throws IOException {
/* 105 */     connect();
/* 106 */     return this.jarEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContentLength() {
/* 111 */     long contentLength = getContentLengthLong();
/* 112 */     return (contentLength <= 2147483647L) ? (int)contentLength : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLengthLong() {
/*     */     try {
/* 118 */       connect();
/* 119 */       return (this.jarEntry != null) ? this.jarEntry.getSize() : this.jarFileConnection.getContentLengthLong();
/*     */     }
/* 121 */     catch (IOException ex) {
/* 122 */       return -1L;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 128 */     if (this.contentType == null) {
/* 129 */       this.contentType = deduceContentType();
/*     */     }
/* 131 */     return this.contentType;
/*     */   }
/*     */   
/*     */   private String deduceContentType() {
/* 135 */     String type = (this.entryName != null) ? null : "x-java/jar";
/* 136 */     type = (type != null) ? type : deduceContentTypeFromStream();
/* 137 */     type = (type != null) ? type : deduceContentTypeFromEntryName();
/* 138 */     return (type != null) ? type : "content/unknown";
/*     */   }
/*     */   
/*     */   private String deduceContentTypeFromStream() {
/*     */     try {
/* 143 */       connect();
/* 144 */       InputStream in = this.jarFile.getInputStream(this.jarEntry); 
/* 145 */       try { String str = guessContentTypeFromStream(new BufferedInputStream(in));
/* 146 */         if (in != null) in.close();  return str; } catch (Throwable throwable) { if (in != null)
/*     */           try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; } 
/* 148 */     } catch (IOException ex) {
/* 149 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private String deduceContentTypeFromEntryName() {
/* 154 */     return guessContentTypeFromName(this.entryName);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastModified() {
/* 159 */     return (this.jarFileConnection != null) ? this.jarFileConnection.getLastModified() : super.getLastModified();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHeaderField(String name) {
/* 164 */     return (this.jarFileConnection != null) ? this.jarFileConnection.getHeaderField(name) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getContent() throws IOException {
/* 169 */     connect();
/* 170 */     return (this.entryName != null) ? super.getContent() : this.jarFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public Permission getPermission() throws IOException {
/* 175 */     return (this.jarFileConnection != null) ? this.jarFileConnection.getPermission() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 180 */     if (this.notFound != null) {
/* 181 */       throwFileNotFound();
/*     */     }
/* 183 */     URL jarFileURL = getJarFileURL();
/* 184 */     if (this.entryName == null && !UrlJarFileFactory.isNestedUrl(jarFileURL)) {
/* 185 */       throw new IOException("no entry name specified");
/*     */     }
/* 187 */     if (!getUseCaches() && Optimizations.isEnabled(false) && this.entryName != null) {
/* 188 */       JarFile cached = jarFiles.getCached(jarFileURL);
/* 189 */       if (cached != null && 
/* 190 */         cached.getEntry(this.entryName) != null) {
/* 191 */         return emptyInputStream;
/*     */       }
/*     */     } 
/*     */     
/* 195 */     connect();
/* 196 */     if (this.jarEntry == null) {
/* 197 */       JarFile jarFile = this.jarFile; if (jarFile instanceof NestedJarFile) { NestedJarFile nestedJarFile = (NestedJarFile)jarFile;
/*     */ 
/*     */ 
/*     */         
/* 201 */         return nestedJarFile.getRawZipDataInputStream(); }
/*     */       
/* 203 */       throwFileNotFound();
/*     */     } 
/* 205 */     return new ConnectionInputStream();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getAllowUserInteraction() {
/* 210 */     return (this.jarFileConnection != null && this.jarFileConnection.getAllowUserInteraction());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAllowUserInteraction(boolean allowuserinteraction) {
/* 215 */     if (this.jarFileConnection != null) {
/* 216 */       this.jarFileConnection.setAllowUserInteraction(allowuserinteraction);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getUseCaches() {
/* 222 */     return (this.jarFileConnection == null || this.jarFileConnection.getUseCaches());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUseCaches(boolean usecaches) {
/* 227 */     if (this.jarFileConnection != null) {
/* 228 */       this.jarFileConnection.setUseCaches(usecaches);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getDefaultUseCaches() {
/* 234 */     return (this.jarFileConnection == null || this.jarFileConnection.getDefaultUseCaches());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultUseCaches(boolean defaultusecaches) {
/* 239 */     if (this.jarFileConnection != null) {
/* 240 */       this.jarFileConnection.setDefaultUseCaches(defaultusecaches);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIfModifiedSince(long ifModifiedSince) {
/* 246 */     if (this.jarFileConnection != null) {
/* 247 */       this.jarFileConnection.setIfModifiedSince(ifModifiedSince);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRequestProperty(String key) {
/* 253 */     return (this.jarFileConnection != null) ? this.jarFileConnection.getRequestProperty(key) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRequestProperty(String key, String value) {
/* 258 */     if (this.jarFileConnection != null) {
/* 259 */       this.jarFileConnection.setRequestProperty(key, value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addRequestProperty(String key, String value) {
/* 265 */     if (this.jarFileConnection != null) {
/* 266 */       this.jarFileConnection.addRequestProperty(key, value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, List<String>> getRequestProperties() {
/* 272 */     return (this.jarFileConnection != null) ? this.jarFileConnection.getRequestProperties() : 
/* 273 */       Collections.<String, List<String>>emptyMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public void connect() throws IOException {
/* 278 */     if (this.connected) {
/*     */       return;
/*     */     }
/* 281 */     if (this.notFound != null) {
/* 282 */       throwFileNotFound();
/*     */     }
/* 284 */     boolean useCaches = getUseCaches();
/* 285 */     URL jarFileURL = getJarFileURL();
/* 286 */     if (this.entryName != null && Optimizations.isEnabled()) {
/* 287 */       assertCachedJarFileHasEntry(jarFileURL, this.entryName);
/*     */     }
/* 289 */     this.jarFile = jarFiles.getOrCreate(useCaches, jarFileURL);
/* 290 */     this.jarEntry = getJarEntry(jarFileURL);
/* 291 */     boolean addedToCache = jarFiles.cacheIfAbsent(useCaches, jarFileURL, this.jarFile);
/* 292 */     if (addedToCache) {
/* 293 */       this.jarFileConnection = jarFiles.reconnect(this.jarFile, this.jarFileConnection);
/*     */     }
/* 295 */     this.connected = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void assertCachedJarFileHasEntry(URL jarFileURL, String entryName) throws FileNotFoundException {
/* 306 */     JarFile cachedJarFile = jarFiles.getCached(jarFileURL);
/* 307 */     if (cachedJarFile != null && cachedJarFile.getJarEntry(entryName) == null) {
/* 308 */       throw FILE_NOT_FOUND_EXCEPTION;
/*     */     }
/*     */   }
/*     */   
/*     */   private JarEntry getJarEntry(URL jarFileUrl) throws IOException {
/* 313 */     if (this.entryName == null) {
/* 314 */       return null;
/*     */     }
/* 316 */     JarEntry jarEntry = this.jarFile.getJarEntry(this.entryName);
/* 317 */     if (jarEntry == null) {
/* 318 */       jarFiles.closeIfNotCached(jarFileUrl, this.jarFile);
/* 319 */       throwFileNotFound();
/*     */     } 
/* 321 */     return jarEntry;
/*     */   }
/*     */   
/*     */   private void throwFileNotFound() throws FileNotFoundException {
/* 325 */     if (Optimizations.isEnabled()) {
/* 326 */       throw FILE_NOT_FOUND_EXCEPTION;
/*     */     }
/* 328 */     if (this.notFound != null) {
/* 329 */       throw (FileNotFoundException)this.notFound.get();
/*     */     }
/* 331 */     throw new FileNotFoundException("JAR entry " + this.entryName + " not found in " + this.jarFile.getName());
/*     */   }
/*     */   
/*     */   static JarUrlConnection open(URL url) throws IOException {
/* 335 */     String spec = url.getFile();
/* 336 */     if (spec.startsWith("nested:")) {
/* 337 */       int separator = spec.indexOf("!/");
/* 338 */       boolean specHasEntry = (separator != -1 && separator + 2 != spec.length());
/* 339 */       if (specHasEntry) {
/* 340 */         URL jarFileUrl = new URL(spec.substring(0, separator));
/* 341 */         if ("runtime".equals(url.getRef())) {
/* 342 */           jarFileUrl = new URL(jarFileUrl, "#runtime");
/*     */         }
/* 344 */         String entryName = UrlDecoder.decode(spec.substring(separator + 2));
/* 345 */         JarFile jarFile = jarFiles.getOrCreate(true, jarFileUrl);
/* 346 */         jarFiles.cacheIfAbsent(true, jarFileUrl, jarFile);
/* 347 */         if (!hasEntry(jarFile, entryName)) {
/* 348 */           return notFoundConnection(jarFile.getName(), entryName);
/*     */         }
/*     */       } 
/*     */     } 
/* 352 */     return new JarUrlConnection(url);
/*     */   }
/*     */   
/*     */   private static boolean hasEntry(JarFile jarFile, String name) {
/* 356 */     NestedJarFile nestedJarFile = (NestedJarFile)jarFile; return (jarFile instanceof NestedJarFile) ? nestedJarFile.hasEntry(name) : (
/* 357 */       (jarFile.getEntry(name) != null));
/*     */   }
/*     */   
/*     */   private static JarUrlConnection notFoundConnection(String jarFileName, String entryName) throws IOException {
/* 361 */     if (Optimizations.isEnabled()) {
/* 362 */       return NOT_FOUND_CONNECTION;
/*     */     }
/* 364 */     return new JarUrlConnection(() -> new FileNotFoundException("JAR entry " + entryName + " not found in " + jarFileName));
/*     */   }
/*     */ 
/*     */   
/*     */   static void clearCache() {
/* 369 */     jarFiles.clearCache();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class ConnectionInputStream
/*     */     extends LazyDelegatingInputStream
/*     */   {
/*     */     public void close() throws IOException {
/*     */       try {
/* 382 */         super.close();
/*     */       } finally {
/*     */         
/* 385 */         if (!JarUrlConnection.this.getUseCaches()) {
/* 386 */           JarUrlConnection.this.jarFile.close();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected InputStream getDelegateInputStream() throws IOException {
/* 393 */       return JarUrlConnection.this.jarFile.getInputStream(JarUrlConnection.this.jarEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class EmptyUrlStreamHandler
/*     */     extends URLStreamHandler
/*     */   {
/*     */     protected URLConnection openConnection(URL url) {
/* 406 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/JarUrlConnection.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */