/*     */ package org.springframework.boot.loader.nio.file;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.lang.ref.Cleaner;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.NonWritableChannelException;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.Path;
/*     */ import org.springframework.boot.loader.ref.Cleaner;
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
/*     */ class NestedByteChannel
/*     */   implements SeekableByteChannel
/*     */ {
/*     */   private long position;
/*     */   private final Resources resources;
/*     */   private final Cleaner.Cleanable cleanup;
/*     */   private final long size;
/*     */   private volatile boolean closed;
/*     */   
/*     */   NestedByteChannel(Path path, String nestedEntryName) throws IOException {
/*  53 */     this(path, nestedEntryName, Cleaner.instance);
/*     */   }
/*     */   
/*     */   NestedByteChannel(Path path, String nestedEntryName, Cleaner cleaner) throws IOException {
/*  57 */     this.resources = new Resources(path, nestedEntryName);
/*  58 */     this.cleanup = cleaner.register(this, this.resources);
/*  59 */     this.size = this.resources.getData().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/*  64 */     return !this.closed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  69 */     if (this.closed) {
/*     */       return;
/*     */     }
/*  72 */     this.closed = true;
/*     */     try {
/*  74 */       this.cleanup.clean();
/*     */     }
/*  76 */     catch (UncheckedIOException ex) {
/*  77 */       throw ex.getCause();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst) throws IOException {
/*  83 */     assertNotClosed();
/*  84 */     int total = 0;
/*  85 */     while (dst.remaining() > 0) {
/*  86 */       int count = this.resources.getData().read(dst, this.position);
/*  87 */       if (count <= 0) {
/*  88 */         return (total != 0) ? 0 : count;
/*     */       }
/*  90 */       total += count;
/*  91 */       this.position += count;
/*     */     } 
/*  93 */     return total;
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(ByteBuffer src) throws IOException {
/*  98 */     throw new NonWritableChannelException();
/*     */   }
/*     */ 
/*     */   
/*     */   public long position() throws IOException {
/* 103 */     assertNotClosed();
/* 104 */     return this.position;
/*     */   }
/*     */ 
/*     */   
/*     */   public SeekableByteChannel position(long position) throws IOException {
/* 109 */     assertNotClosed();
/* 110 */     if (position < 0L || position >= this.size) {
/* 111 */       throw new IllegalArgumentException("Position must be in bounds");
/*     */     }
/* 113 */     this.position = position;
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public long size() throws IOException {
/* 119 */     assertNotClosed();
/* 120 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public SeekableByteChannel truncate(long size) throws IOException {
/* 125 */     throw new NonWritableChannelException();
/*     */   }
/*     */   
/*     */   private void assertNotClosed() throws ClosedChannelException {
/* 129 */     if (this.closed) {
/* 130 */       throw new ClosedChannelException();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class Resources
/*     */     implements Runnable
/*     */   {
/*     */     private final ZipContent zipContent;
/*     */     
/*     */     private final CloseableDataBlock data;
/*     */ 
/*     */     
/*     */     Resources(Path path, String nestedEntryName) throws IOException {
/* 144 */       this.zipContent = ZipContent.open(path, nestedEntryName);
/* 145 */       this.data = this.zipContent.openRawZipData();
/*     */     }
/*     */     
/*     */     DataBlock getData() {
/* 149 */       return (DataBlock)this.data;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 154 */       releaseAll();
/*     */     }
/*     */     
/*     */     private void releaseAll() {
/* 158 */       IOException exception = null;
/*     */       try {
/* 160 */         this.data.close();
/*     */       }
/* 162 */       catch (IOException ex) {
/* 163 */         exception = ex;
/*     */       } 
/*     */       try {
/* 166 */         this.zipContent.close();
/*     */       }
/* 168 */       catch (IOException ex) {
/* 169 */         if (exception != null) {
/* 170 */           ex.addSuppressed(exception);
/*     */         }
/* 172 */         exception = ex;
/*     */       } 
/* 174 */       if (exception != null)
/* 175 */         throw new UncheckedIOException(exception); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/nio/file/NestedByteChannel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */