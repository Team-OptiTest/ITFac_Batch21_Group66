/*     */ package org.springframework.boot.loader.zip;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ class DataBlockInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private final DataBlock dataBlock;
/*     */   private long pos;
/*     */   private long remaining;
/*     */   private volatile boolean closed;
/*     */   
/*     */   DataBlockInputStream(DataBlock dataBlock) throws IOException {
/*  40 */     this.dataBlock = dataBlock;
/*  41 */     this.remaining = dataBlock.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  46 */     byte[] b = new byte[1];
/*  47 */     return (read(b, 0, 1) == 1) ? (b[0] & 0xFF) : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  52 */     ensureOpen();
/*  53 */     ByteBuffer dst = ByteBuffer.wrap(b, off, len);
/*  54 */     int count = this.dataBlock.read(dst, this.pos);
/*  55 */     if (count > 0) {
/*  56 */       this.pos += count;
/*  57 */       this.remaining -= count;
/*     */     } 
/*  59 */     return count;
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/*  64 */     long count = (n > 0L) ? maxForwardSkip(n) : maxBackwardSkip(n);
/*  65 */     this.pos += count;
/*  66 */     this.remaining -= count;
/*  67 */     return count;
/*     */   }
/*     */   
/*     */   private long maxForwardSkip(long n) {
/*  71 */     boolean willCauseOverflow = (this.pos + n < 0L);
/*  72 */     return (willCauseOverflow || n > this.remaining) ? this.remaining : n;
/*     */   }
/*     */   
/*     */   private long maxBackwardSkip(long n) {
/*  76 */     return Math.max(-this.pos, n);
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/*  81 */     if (this.closed) {
/*  82 */       return 0;
/*     */     }
/*  84 */     return (this.remaining < 2147483647L) ? (int)this.remaining : Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */   private void ensureOpen() throws IOException {
/*  88 */     if (this.closed) {
/*  89 */       throw new IOException("InputStream closed");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  95 */     if (this.closed) {
/*     */       return;
/*     */     }
/*  98 */     this.closed = true;
/*  99 */     DataBlock dataBlock = this.dataBlock; if (dataBlock instanceof Closeable) { Closeable closeable = (Closeable)dataBlock;
/* 100 */       closeable.close(); }
/*     */   
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/DataBlockInputStream.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */