/*    */ package org.springframework.boot.loader.zip;
/*    */ 
/*    */ import java.io.EOFException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
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
/*    */ public interface DataBlock
/*    */ {
/*    */   long size() throws IOException;
/*    */   
/*    */   int read(ByteBuffer paramByteBuffer, long paramLong) throws IOException;
/*    */   
/*    */   default void readFully(ByteBuffer dst, long pos) throws IOException {
/*    */     do {
/* 64 */       int count = read(dst, pos);
/* 65 */       if (count <= 0) {
/* 66 */         throw new EOFException();
/*    */       }
/* 68 */       pos += count;
/*    */     }
/* 70 */     while (dst.hasRemaining());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default InputStream asInputStream() throws IOException {
/* 79 */     return new DataBlockInputStream(this);
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/DataBlock.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */