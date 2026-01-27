/*     */ package org.springframework.boot.loader.launch;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Path;
/*     */ import java.security.CodeSource;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.jar.Manifest;
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
/*     */ public interface Archive
/*     */   extends AutoCloseable
/*     */ {
/*     */   public static final Predicate<Entry> ALL_ENTRIES = entry -> true;
/*     */   
/*     */   default Set<URL> getClassPathUrls(Predicate<Entry> includeFilter) throws IOException {
/*  57 */     return getClassPathUrls(includeFilter, ALL_ENTRIES);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean isExploded() {
/*  77 */     return (getRootDirectory() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default File getRootDirectory() {
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void close() throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Archive create(Class<?> target) throws Exception {
/* 105 */     return create(target.getProtectionDomain());
/*     */   }
/*     */   
/*     */   static Archive create(ProtectionDomain protectionDomain) throws Exception {
/* 109 */     CodeSource codeSource = protectionDomain.getCodeSource();
/* 110 */     URI location = (codeSource != null) ? codeSource.getLocation().toURI() : null;
/* 111 */     if (location == null) {
/* 112 */       throw new IllegalStateException("Unable to determine code source archive");
/*     */     }
/* 114 */     return create(Path.of(location).toFile());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Archive create(File target) throws Exception {
/* 125 */     if (!target.exists()) {
/* 126 */       throw new IllegalStateException("Unable to determine code source archive from " + target);
/*     */     }
/* 128 */     return target.isDirectory() ? new ExplodedArchive(target) : new JarFileArchive(target);
/*     */   }
/*     */   
/*     */   Manifest getManifest() throws IOException;
/*     */   
/*     */   Set<URL> getClassPathUrls(Predicate<Entry> paramPredicate1, Predicate<Entry> paramPredicate2) throws IOException;
/*     */   
/*     */   public static interface Entry {
/*     */     String name();
/*     */     
/*     */     boolean isDirectory();
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/launch/Archive.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */