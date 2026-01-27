/*     */ package org.springframework.boot.loader.zip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class VirtualDataBlock
/*     */   implements DataBlock
/*     */ {
/*     */   private DataBlock[] parts;
/*     */   private long[] offsets;
/*     */   private long size;
/*  37 */   private volatile int lastReadPart = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   VirtualDataBlock(Collection<? extends DataBlock> parts) throws IOException {
/*  52 */     setParts(parts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setParts(Collection<? extends DataBlock> parts) throws IOException {
/*  61 */     this.parts = parts.<DataBlock>toArray(x$0 -> new DataBlock[x$0]);
/*  62 */     this.offsets = new long[parts.size()];
/*  63 */     long size = 0L;
/*  64 */     int i = 0;
/*  65 */     for (DataBlock part : parts) {
/*  66 */       this.offsets[i++] = size;
/*  67 */       size += part.size();
/*     */     } 
/*  69 */     this.size = size;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long size() throws IOException {
/*  75 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst, long pos) throws IOException {
/*  80 */     if (pos < 0L || pos >= this.size) {
/*  81 */       return -1;
/*     */     }
/*  83 */     int lastReadPart = this.lastReadPart;
/*  84 */     int partIndex = 0;
/*  85 */     long offset = 0L;
/*  86 */     int result = 0;
/*  87 */     if (pos >= this.offsets[lastReadPart]) {
/*  88 */       partIndex = lastReadPart;
/*  89 */       offset = this.offsets[lastReadPart];
/*     */     } 
/*  91 */     while (partIndex < this.parts.length) {
/*  92 */       DataBlock part = this.parts[partIndex];
/*  93 */       while (pos >= offset && pos < offset + part.size()) {
/*  94 */         int count = part.read(dst, pos - offset);
/*  95 */         result += Math.max(count, 0);
/*  96 */         if (count <= 0 || !dst.hasRemaining()) {
/*  97 */           this.lastReadPart = partIndex;
/*  98 */           return result;
/*     */         } 
/* 100 */         pos += count;
/*     */       } 
/* 102 */       offset += part.size();
/* 103 */       partIndex++;
/*     */     } 
/* 105 */     return result;
/*     */   }
/*     */   
/*     */   protected VirtualDataBlock() {}
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/VirtualDataBlock.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */