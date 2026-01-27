/*     */ package org.springframework.boot.loader.zip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class DataPart
/*     */   implements DataBlock
/*     */ {
/*     */   private final long offset;
/*     */   private final long size;
/*     */   
/*     */   DataPart(long offset, long size) {
/* 122 */     this.offset = offset;
/* 123 */     this.size = size;
/*     */   }
/*     */ 
/*     */   
/*     */   public long size() throws IOException {
/* 128 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst, long pos) throws IOException {
/* 133 */     int remaining = (int)(this.size - pos);
/* 134 */     if (remaining <= 0) {
/* 135 */       return -1;
/*     */     }
/* 137 */     int originalLimit = -1;
/* 138 */     if (dst.remaining() > remaining) {
/* 139 */       originalLimit = dst.limit();
/* 140 */       dst.limit(dst.position() + remaining);
/*     */     } 
/* 142 */     int result = VirtualZipDataBlock.this.data.read(dst, this.offset + pos);
/* 143 */     if (originalLimit != -1) {
/* 144 */       dst.limit(originalLimit);
/*     */     }
/* 146 */     return result;
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/VirtualZipDataBlock$DataPart.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */