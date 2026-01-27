/*     */ package org.springframework.boot.loader.net.protocol.jar;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.jar.JarFile;
/*     */ import org.springframework.boot.loader.net.protocol.nested.NestedLocation;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class UrlJarFileFactory
/*     */ {
/*     */   JarFile createJarFile(URL jarFileUrl, Consumer<JarFile> closeAction) throws IOException {
/*  50 */     Runtime.Version version = getVersion(jarFileUrl);
/*  51 */     if (isLocalFileUrl(jarFileUrl)) {
/*  52 */       return createJarFileForLocalFile(jarFileUrl, version, closeAction);
/*     */     }
/*  54 */     if (isNestedUrl(jarFileUrl)) {
/*  55 */       return createJarFileForNested(jarFileUrl, version, closeAction);
/*     */     }
/*  57 */     return createJarFileForStream(jarFileUrl, version, closeAction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Runtime.Version getVersion(URL url) {
/*  66 */     return "base".equals(url.getRef()) ? JarFile.baseVersion() : JarFile.runtimeVersion();
/*     */   }
/*     */   
/*     */   private boolean isLocalFileUrl(URL url) {
/*  70 */     return (url.getProtocol().equalsIgnoreCase("file") && isLocal(url.getHost()));
/*     */   }
/*     */   
/*     */   private boolean isLocal(String host) {
/*  74 */     return (host == null || host.isEmpty() || host.equals("~") || host.equalsIgnoreCase("localhost"));
/*     */   }
/*     */ 
/*     */   
/*     */   private JarFile createJarFileForLocalFile(URL url, Runtime.Version version, Consumer<JarFile> closeAction) throws IOException {
/*  79 */     String path = UrlDecoder.decode(url.getPath());
/*  80 */     return new UrlJarFile(new File(path), version, closeAction);
/*     */   }
/*     */ 
/*     */   
/*     */   private JarFile createJarFileForNested(URL url, Runtime.Version version, Consumer<JarFile> closeAction) throws IOException {
/*  85 */     NestedLocation location = NestedLocation.fromUrl(url);
/*  86 */     return (JarFile)new UrlNestedJarFile(location.path().toFile(), location.nestedEntryName(), version, closeAction);
/*     */   }
/*     */   
/*     */   private JarFile createJarFileForStream(URL url, Runtime.Version version, Consumer<JarFile> closeAction) throws IOException {
/*  90 */     InputStream in = url.openStream(); 
/*  91 */     try { JarFile jarFile = createJarFileForStream(in, version, closeAction);
/*  92 */       if (in != null) in.close();  return jarFile; }
/*     */     catch (Throwable throwable) { if (in != null)
/*     */         try { in.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  97 */      } private JarFile createJarFileForStream(InputStream in, Runtime.Version version, Consumer<JarFile> closeAction) throws IOException { Path local = Files.createTempFile("jar_cache", null, (FileAttribute<?>[])new FileAttribute[0]);
/*     */     try {
/*  99 */       Files.copy(in, local, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 100 */       JarFile jarFile = new UrlJarFile(local.toFile(), version, closeAction);
/* 101 */       local.toFile().deleteOnExit();
/* 102 */       return jarFile;
/*     */     }
/* 104 */     catch (Throwable ex) {
/* 105 */       deleteIfPossible(local, ex);
/* 106 */       throw ex;
/*     */     }  }
/*     */ 
/*     */   
/*     */   private void deleteIfPossible(Path local, Throwable cause) {
/*     */     try {
/* 112 */       Files.delete(local);
/*     */     }
/* 114 */     catch (IOException ex) {
/* 115 */       cause.addSuppressed(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   static boolean isNestedUrl(URL url) {
/* 120 */     return url.getProtocol().equalsIgnoreCase("nested");
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/UrlJarFileFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */