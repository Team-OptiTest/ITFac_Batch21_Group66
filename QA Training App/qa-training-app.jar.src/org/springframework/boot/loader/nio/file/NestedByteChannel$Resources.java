/*     */ package org.springframework.boot.loader.nio.file;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.file.Path;
/*     */ import org.springframework.boot.loader.zip.CloseableDataBlock;
/*     */ import org.springframework.boot.loader.zip.DataBlock;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Resources
/*     */   implements Runnable
/*     */ {
/*     */   private final ZipContent zipContent;
/*     */   private final CloseableDataBlock data;
/*     */   
/*     */   Resources(Path path, String nestedEntryName) throws IOException {
/* 144 */     this.zipContent = ZipContent.open(path, nestedEntryName);
/* 145 */     this.data = this.zipContent.openRawZipData();
/*     */   }
/*     */   
/*     */   DataBlock getData() {
/* 149 */     return (DataBlock)this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/* 154 */     releaseAll();
/*     */   }
/*     */   
/*     */   private void releaseAll() {
/* 158 */     IOException exception = null;
/*     */     try {
/* 160 */       this.data.close();
/*     */     }
/* 162 */     catch (IOException ex) {
/* 163 */       exception = ex;
/*     */     } 
/*     */     try {
/* 166 */       this.zipContent.close();
/*     */     }
/* 168 */     catch (IOException ex) {
/* 169 */       if (exception != null) {
/* 170 */         ex.addSuppressed(exception);
/*     */       }
/* 172 */       exception = ex;
/*     */     } 
/* 174 */     if (exception != null)
/* 175 */       throw new UncheckedIOException(exception); 
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/nio/file/NestedByteChannel$Resources.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */