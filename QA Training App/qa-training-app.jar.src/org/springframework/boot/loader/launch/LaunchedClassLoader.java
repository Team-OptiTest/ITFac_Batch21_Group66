/*     */ package org.springframework.boot.loader.launch;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.jar.Manifest;
/*     */ import org.springframework.boot.loader.net.protocol.jar.JarUrlClassLoader;
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
/*     */ public class LaunchedClassLoader
/*     */   extends JarUrlClassLoader
/*     */ {
/*     */   private static final String JAR_MODE_PACKAGE_PREFIX = "org.springframework.boot.loader.jarmode.";
/*  40 */   private static final String JAR_MODE_RUNNER_CLASS_NAME = JarModeRunner.class.getName(); private final boolean exploded; private final Archive rootArchive;
/*     */   
/*     */   static {
/*  43 */     ClassLoader.registerAsParallelCapable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   private final Object definePackageLock = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile DefinePackageCallType definePackageCallType;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LaunchedClassLoader(boolean exploded, URL[] urls, ClassLoader parent) {
/*  61 */     this(exploded, (Archive)null, urls, parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LaunchedClassLoader(boolean exploded, Archive rootArchive, URL[] urls, ClassLoader parent) {
/*  72 */     super(urls, parent);
/*  73 */     this.exploded = exploded;
/*  74 */     this.rootArchive = rootArchive;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
/*  79 */     if (name.startsWith("org.springframework.boot.loader.jarmode.") || name.equals(JAR_MODE_RUNNER_CLASS_NAME)) {
/*     */       try {
/*  81 */         Class<?> result = loadClassInLaunchedClassLoader(name);
/*  82 */         if (resolve) {
/*  83 */           resolveClass(result);
/*     */         }
/*  85 */         return result;
/*     */       }
/*  87 */       catch (ClassNotFoundException classNotFoundException) {}
/*     */     }
/*     */ 
/*     */     
/*  91 */     return super.loadClass(name, resolve);
/*     */   }
/*     */   
/*     */   private Class<?> loadClassInLaunchedClassLoader(String name) throws ClassNotFoundException {
/*     */     try {
/*  96 */       String internalName = name.replace('.', '/') + ".class";
/*  97 */       InputStream inputStream = getParent().getResourceAsStream(internalName); 
/*  98 */       try { ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
/*  99 */         try { if (inputStream == null) {
/* 100 */             throw new ClassNotFoundException(name);
/*     */           }
/* 102 */           inputStream.transferTo(outputStream);
/* 103 */           byte[] bytes = outputStream.toByteArray();
/* 104 */           Class<?> definedClass = defineClass(name, bytes, 0, bytes.length);
/* 105 */           definePackageIfNecessary(name);
/* 106 */           Class<?> clazz1 = definedClass;
/* 107 */           outputStream.close(); if (inputStream != null) inputStream.close();  return clazz1; } catch (Throwable throwable) { try { outputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable throwable) { if (inputStream != null)
/*     */           try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; } 
/* 109 */     } catch (IOException ex) {
/* 110 */       throw new ClassNotFoundException("Cannot load resource for class [" + name + "]", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Package definePackage(String name, Manifest man, URL url) throws IllegalArgumentException {
/* 116 */     return !this.exploded ? super.definePackage(name, man, url) : definePackageForExploded(name, man, url);
/*     */   }
/*     */   
/*     */   private Package definePackageForExploded(String name, Manifest man, URL url) {
/* 120 */     synchronized (this.definePackageLock) {
/* 121 */       return definePackage(DefinePackageCallType.MANIFEST, () -> super.definePackage(name, man, url));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Package definePackage(String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException {
/* 128 */     if (!this.exploded) {
/* 129 */       return super.definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
/*     */     }
/*     */     
/* 132 */     return definePackageForExploded(name, sealBase, () -> super.definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase));
/*     */   }
/*     */ 
/*     */   
/*     */   private Package definePackageForExploded(String name, URL sealBase, Supplier<Package> call) {
/* 137 */     synchronized (this.definePackageLock) {
/* 138 */       if (this.definePackageCallType == null) {
/*     */ 
/*     */ 
/*     */         
/* 142 */         Manifest manifest = getManifest(this.rootArchive);
/* 143 */         if (manifest != null) {
/* 144 */           return definePackage(name, manifest, sealBase);
/*     */         }
/*     */       } 
/* 147 */       return definePackage(DefinePackageCallType.ATTRIBUTES, call);
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> T definePackage(DefinePackageCallType type, Supplier<T> call) {
/* 152 */     DefinePackageCallType existingType = this.definePackageCallType;
/*     */     try {
/* 154 */       this.definePackageCallType = type;
/* 155 */       return call.get();
/*     */     } finally {
/*     */       
/* 158 */       this.definePackageCallType = existingType;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Manifest getManifest(Archive archive) {
/*     */     try {
/* 164 */       return (archive != null) ? archive.getManifest() : null;
/*     */     }
/* 166 */     catch (IOException ex) {
/* 167 */       return null;
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
/*     */   private enum DefinePackageCallType
/*     */   {
/* 180 */     MANIFEST,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     ATTRIBUTES;
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/launch/LaunchedClassLoader.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */