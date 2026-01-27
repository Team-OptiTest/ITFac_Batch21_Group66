/*    */ package org.springframework.boot.loader.jar;
/*    */ 
/*    */ import java.io.EOFException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.zip.Inflater;
/*    */ import java.util.zip.InflaterInputStream;
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
/*    */ class ZipInflaterInputStream
/*    */   extends InflaterInputStream
/*    */ {
/*    */   private int available;
/*    */   private boolean extraBytesWritten;
/*    */   
/*    */   ZipInflaterInputStream(InputStream inputStream, Inflater inflater, int size) {
/* 39 */     super(inputStream, inflater, getInflaterBufferSize(size));
/* 40 */     this.available = size;
/*    */   }
/*    */   
/*    */   private static int getInflaterBufferSize(long size) {
/* 44 */     size += 2L;
/* 45 */     size = (size > 65536L) ? 8192L : size;
/* 46 */     size = (size <= 0L) ? 4096L : size;
/* 47 */     return (int)size;
/*    */   }
/*    */ 
/*    */   
/*    */   public int available() throws IOException {
/* 52 */     return (this.available >= 0) ? this.available : super.available();
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/* 57 */     int result = super.read(b, off, len);
/* 58 */     if (result != -1) {
/* 59 */       this.available -= result;
/*    */     }
/* 61 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void fill() throws IOException {
/*    */     try {
/* 67 */       super.fill();
/*    */     }
/* 69 */     catch (EOFException ex) {
/* 70 */       if (this.extraBytesWritten) {
/* 71 */         throw ex;
/*    */       }
/* 73 */       this.len = 1;
/* 74 */       this.buf[0] = 0;
/* 75 */       this.extraBytesWritten = true;
/* 76 */       this.inf.setInput(this.buf, 0, this.len);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/jar/ZipInflaterInputStream.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */