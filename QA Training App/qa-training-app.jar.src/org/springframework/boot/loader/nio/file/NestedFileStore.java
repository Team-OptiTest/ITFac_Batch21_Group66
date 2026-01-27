/*     */ package org.springframework.boot.loader.nio.file;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.attribute.FileAttributeView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class NestedFileStore
/*     */   extends FileStore
/*     */ {
/*     */   private final NestedFileSystem fileSystem;
/*     */   
/*     */   NestedFileStore(NestedFileSystem fileSystem) {
/*  39 */     this.fileSystem = fileSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/*  44 */     return this.fileSystem.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String type() {
/*  49 */     return "nestedfs";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadOnly() {
/*  54 */     return this.fileSystem.isReadOnly();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTotalSpace() throws IOException {
/*  59 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getUsableSpace() throws IOException {
/*  64 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getUnallocatedSpace() throws IOException {
/*  69 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) {
/*  74 */     return getJarPathFileStore().supportsFileAttributeView(type);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsFileAttributeView(String name) {
/*  79 */     return getJarPathFileStore().supportsFileAttributeView(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public <V extends java.nio.file.attribute.FileStoreAttributeView> V getFileStoreAttributeView(Class<V> type) {
/*  84 */     return getJarPathFileStore().getFileStoreAttributeView(type);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAttribute(String attribute) throws IOException {
/*     */     try {
/*  90 */       return getJarPathFileStore().getAttribute(attribute);
/*     */     }
/*  92 */     catch (UncheckedIOException ex) {
/*  93 */       throw ex.getCause();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected FileStore getJarPathFileStore() {
/*     */     try {
/*  99 */       return Files.getFileStore(this.fileSystem.getJarPath());
/*     */     }
/* 101 */     catch (IOException ex) {
/* 102 */       throw new UncheckedIOException(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/nio/file/NestedFileStore.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */