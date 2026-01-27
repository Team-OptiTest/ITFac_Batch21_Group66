/*     */ package org.springframework.boot.loader.net.protocol.jar;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class LazyDelegatingInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private volatile InputStream in;
/*     */   
/*     */   public int read() throws IOException {
/*  33 */     return in().read();
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/*  38 */     return in().read(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  43 */     return in().read(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/*  48 */     return in().skip(n);
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/*  53 */     return in().available();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/*     */     try {
/*  59 */       return in().markSupported();
/*     */     }
/*  61 */     catch (IOException ex) {
/*  62 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void mark(int readlimit) {
/*     */     try {
/*  69 */       in().mark(readlimit);
/*     */     }
/*  71 */     catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/*  78 */     in().reset();
/*     */   }
/*     */   
/*     */   private InputStream in() throws IOException {
/*  82 */     InputStream in = this.in;
/*  83 */     if (in == null) {
/*  84 */       synchronized (this) {
/*  85 */         in = this.in;
/*  86 */         if (in == null) {
/*  87 */           in = getDelegateInputStream();
/*  88 */           this.in = in;
/*     */         } 
/*     */       } 
/*     */     }
/*  92 */     return in;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  97 */     InputStream in = this.in;
/*  98 */     if (in != null)
/*  99 */       synchronized (this) {
/* 100 */         in = this.in;
/* 101 */         if (in != null)
/* 102 */           in.close(); 
/*     */       }  
/*     */   }
/*     */   
/*     */   protected abstract InputStream getDelegateInputStream() throws IOException;
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/LazyDelegatingInputStream.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */