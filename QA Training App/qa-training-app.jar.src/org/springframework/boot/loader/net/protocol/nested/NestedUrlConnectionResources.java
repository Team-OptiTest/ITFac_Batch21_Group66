/*     */ package org.springframework.boot.loader.net.protocol.nested;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UncheckedIOException;
/*     */ import org.springframework.boot.loader.zip.CloseableDataBlock;
/*     */ import org.springframework.boot.loader.zip.ZipContent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class NestedUrlConnectionResources
/*     */   implements Runnable
/*     */ {
/*     */   private final NestedLocation location;
/*     */   private volatile ZipContent zipContent;
/*  39 */   private volatile long size = -1L;
/*     */   
/*     */   private volatile InputStream inputStream;
/*     */   
/*     */   NestedUrlConnectionResources(NestedLocation location) {
/*  44 */     this.location = location;
/*     */   }
/*     */   
/*     */   NestedLocation getLocation() {
/*  48 */     return this.location;
/*     */   }
/*     */   
/*     */   void connect() throws IOException {
/*  52 */     synchronized (this) {
/*  53 */       if (this.zipContent == null) {
/*  54 */         this.zipContent = ZipContent.open(this.location.path(), this.location.nestedEntryName());
/*     */         try {
/*  56 */           connectData();
/*     */         }
/*  58 */         catch (IOException|RuntimeException ex) {
/*  59 */           this.zipContent.close();
/*  60 */           this.zipContent = null;
/*  61 */           throw ex;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void connectData() throws IOException {
/*  68 */     CloseableDataBlock data = this.zipContent.openRawZipData();
/*     */     try {
/*  70 */       this.size = data.size();
/*  71 */       this.inputStream = data.asInputStream();
/*     */     }
/*  73 */     catch (IOException|RuntimeException ex) {
/*  74 */       data.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   InputStream getInputStream() throws IOException {
/*  79 */     synchronized (this) {
/*  80 */       if (this.inputStream == null) {
/*  81 */         throw new IOException("Nested location not found " + this.location);
/*     */       }
/*  83 */       return this.inputStream;
/*     */     } 
/*     */   }
/*     */   
/*     */   long getContentLength() {
/*  88 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  93 */     releaseAll();
/*     */   }
/*     */   
/*     */   private void releaseAll() {
/*  97 */     synchronized (this) {
/*  98 */       if (this.zipContent != null) {
/*  99 */         IOException exceptionChain = null;
/*     */         try {
/* 101 */           this.inputStream.close();
/*     */         }
/* 103 */         catch (IOException ex) {
/* 104 */           exceptionChain = addToExceptionChain(exceptionChain, ex);
/*     */         } 
/*     */         try {
/* 107 */           this.zipContent.close();
/*     */         }
/* 109 */         catch (IOException ex) {
/* 110 */           exceptionChain = addToExceptionChain(exceptionChain, ex);
/*     */         } 
/* 112 */         this.size = -1L;
/* 113 */         if (exceptionChain != null) {
/* 114 */           throw new UncheckedIOException(exceptionChain);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private IOException addToExceptionChain(IOException exceptionChain, IOException ex) {
/* 121 */     if (exceptionChain != null) {
/* 122 */       exceptionChain.addSuppressed(ex);
/* 123 */       return exceptionChain;
/*     */     } 
/* 125 */     return ex;
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/nested/NestedUrlConnectionResources.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */