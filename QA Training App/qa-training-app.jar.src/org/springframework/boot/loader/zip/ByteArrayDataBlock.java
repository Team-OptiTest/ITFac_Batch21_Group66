/*    */ package org.springframework.boot.loader.zip;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ByteArrayDataBlock
/*    */   implements CloseableDataBlock
/*    */ {
/*    */   private final byte[] bytes;
/*    */   private final int maxReadSize;
/*    */   
/*    */   ByteArrayDataBlock(byte... bytes) {
/* 38 */     this(bytes, -1);
/*    */   }
/*    */   
/*    */   ByteArrayDataBlock(byte[] bytes, int maxReadSize) {
/* 42 */     this.bytes = bytes;
/* 43 */     this.maxReadSize = maxReadSize;
/*    */   }
/*    */ 
/*    */   
/*    */   public long size() throws IOException {
/* 48 */     return this.bytes.length;
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(ByteBuffer dst, long pos) throws IOException {
/* 53 */     return read(dst, (int)pos);
/*    */   }
/*    */   
/*    */   private int read(ByteBuffer dst, int pos) {
/* 57 */     int remaining = dst.remaining();
/* 58 */     int length = Math.min(this.bytes.length - pos, remaining);
/* 59 */     if (this.maxReadSize > 0 && length > this.maxReadSize) {
/* 60 */       length = this.maxReadSize;
/*    */     }
/* 62 */     dst.put(this.bytes, pos, length);
/* 63 */     return length;
/*    */   }
/*    */   
/*    */   public void close() throws IOException {}
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/ByteArrayDataBlock.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */