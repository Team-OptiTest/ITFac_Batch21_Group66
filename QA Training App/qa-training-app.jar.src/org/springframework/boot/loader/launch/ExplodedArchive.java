/*     */ package org.springframework.boot.loader.launch;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ class ExplodedArchive
/*     */   implements Archive
/*     */ {
/*  42 */   private static final Object NO_MANIFEST = new Object();
/*     */   
/*  44 */   private static final Set<String> SKIPPED_NAMES = Set.of(".", "..");
/*     */   
/*  46 */   private static final Comparator<File> entryComparator = Comparator.comparing(File::getAbsolutePath);
/*     */ 
/*     */   
/*     */   private final File rootDirectory;
/*     */ 
/*     */   
/*     */   private final String rootUriPath;
/*     */ 
/*     */   
/*     */   private volatile Object manifest;
/*     */ 
/*     */   
/*     */   ExplodedArchive(File rootDirectory) {
/*  59 */     if (!rootDirectory.exists() || !rootDirectory.isDirectory()) {
/*  60 */       throw new IllegalArgumentException("Invalid source directory " + rootDirectory);
/*     */     }
/*  62 */     this.rootDirectory = rootDirectory;
/*  63 */     this.rootUriPath = this.rootDirectory.toURI().getPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public Manifest getManifest() throws IOException {
/*  68 */     Object manifest = this.manifest;
/*  69 */     if (manifest == null) {
/*  70 */       manifest = loadManifest();
/*  71 */       this.manifest = manifest;
/*     */     } 
/*  73 */     return (manifest != NO_MANIFEST) ? (Manifest)manifest : null;
/*     */   }
/*     */   
/*     */   private Object loadManifest() throws IOException {
/*  77 */     File file = new File(this.rootDirectory, "META-INF/MANIFEST.MF");
/*  78 */     if (!file.exists()) {
/*  79 */       return NO_MANIFEST;
/*     */     }
/*  81 */     FileInputStream inputStream = new FileInputStream(file); 
/*  82 */     try { Manifest manifest = new Manifest(inputStream);
/*  83 */       inputStream.close(); return manifest; }
/*     */     catch (Throwable throwable) { try {
/*     */         inputStream.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       }  throw throwable; }
/*  89 */      } public Set<URL> getClassPathUrls(Predicate<Archive.Entry> includeFilter, Predicate<Archive.Entry> directorySearchFilter) throws IOException { Set<URL> urls = new LinkedHashSet<>();
/*  90 */     LinkedList<File> files = new LinkedList<>(listFiles(this.rootDirectory));
/*  91 */     while (!files.isEmpty()) {
/*  92 */       File file = files.poll();
/*  93 */       if (SKIPPED_NAMES.contains(file.getName())) {
/*     */         continue;
/*     */       }
/*  96 */       String entryName = file.toURI().getPath().substring(this.rootUriPath.length());
/*  97 */       Archive.Entry entry = new FileArchiveEntry(entryName, file);
/*  98 */       if (entry.isDirectory() && directorySearchFilter.test(entry)) {
/*  99 */         files.addAll(0, listFiles(file));
/*     */       }
/* 101 */       if (includeFilter.test(entry)) {
/* 102 */         urls.add(file.toURI().toURL());
/*     */       }
/*     */     } 
/* 105 */     return urls; }
/*     */ 
/*     */   
/*     */   private List<File> listFiles(File file) {
/* 109 */     File[] files = file.listFiles();
/* 110 */     if (files == null) {
/* 111 */       return Collections.emptyList();
/*     */     }
/* 113 */     Arrays.sort(files, entryComparator);
/* 114 */     return Arrays.asList(files);
/*     */   }
/*     */ 
/*     */   
/*     */   public File getRootDirectory() {
/* 119 */     return this.rootDirectory;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 124 */     return this.rootDirectory.toString();
/*     */   } private static final class FileArchiveEntry extends Record implements Archive.Entry {
/*     */     private final String name; private final File file; public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lorg/springframework/boot/loader/launch/ExplodedArchive$FileArchiveEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #130	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lorg/springframework/boot/loader/launch/ExplodedArchive$FileArchiveEntry;
/*     */     } public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lorg/springframework/boot/loader/launch/ExplodedArchive$FileArchiveEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #130	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lorg/springframework/boot/loader/launch/ExplodedArchive$FileArchiveEntry;
/* 130 */     } private FileArchiveEntry(String name, File file) { this.name = name; this.file = file; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lorg/springframework/boot/loader/launch/ExplodedArchive$FileArchiveEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #130	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lorg/springframework/boot/loader/launch/ExplodedArchive$FileArchiveEntry;
/* 130 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public File file() { return this.file; }
/*     */ 
/*     */     
/*     */     public boolean isDirectory() {
/* 134 */       return this.file.isDirectory();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/launch/ExplodedArchive.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */