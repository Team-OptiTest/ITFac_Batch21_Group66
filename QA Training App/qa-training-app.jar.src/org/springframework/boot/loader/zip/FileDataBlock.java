/*     */ package org.springframework.boot.loader.zip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ClosedByInterruptException;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.function.Supplier;
/*     */ import org.springframework.boot.loader.log.DebugLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FileDataBlock
/*     */   implements CloseableDataBlock
/*     */ {
/*  41 */   private static final DebugLogger debug = DebugLogger.get(FileDataBlock.class);
/*     */   
/*  43 */   static Tracker tracker = Tracker.NONE;
/*     */   
/*     */   private final FileAccess fileAccess;
/*     */   
/*     */   private final long offset;
/*     */   
/*     */   private final long size;
/*     */   
/*     */   FileDataBlock(Path path) throws IOException {
/*  52 */     this.fileAccess = new FileAccess(path);
/*  53 */     this.offset = 0L;
/*  54 */     this.size = Files.size(path);
/*     */   }
/*     */   
/*     */   FileDataBlock(FileAccess fileAccess, long offset, long size) {
/*  58 */     this.fileAccess = fileAccess;
/*  59 */     this.offset = offset;
/*  60 */     this.size = size;
/*     */   }
/*     */ 
/*     */   
/*     */   public long size() throws IOException {
/*  65 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst, long pos) throws IOException {
/*  70 */     if (pos < 0L) {
/*  71 */       throw new IllegalArgumentException("Position must not be negative");
/*     */     }
/*  73 */     ensureOpen(java.nio.channels.ClosedChannelException::new);
/*  74 */     long remaining = this.size - pos;
/*  75 */     if (remaining <= 0L) {
/*  76 */       return -1;
/*     */     }
/*  78 */     int originalDestinationLimit = -1;
/*  79 */     if (dst.remaining() > remaining) {
/*  80 */       originalDestinationLimit = dst.limit();
/*  81 */       long updatedLimit = dst.position() + remaining;
/*  82 */       dst.limit((updatedLimit > 2147483647L) ? Integer.MAX_VALUE : (int)updatedLimit);
/*     */     } 
/*  84 */     int result = this.fileAccess.read(dst, this.offset + pos);
/*  85 */     if (originalDestinationLimit != -1) {
/*  86 */       dst.limit(originalDestinationLimit);
/*     */     }
/*  88 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void open() throws IOException {
/*  97 */     this.fileAccess.open();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 107 */     this.fileAccess.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   <E extends Exception> void ensureOpen(Supplier<E> exceptionSupplier) throws E {
/* 117 */     this.fileAccess.ensureOpen(exceptionSupplier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   FileDataBlock slice(long offset) throws IOException {
/* 129 */     return slice(offset, this.size - offset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   FileDataBlock slice(long offset, long size) {
/* 141 */     if (offset == 0L && size == this.size) {
/* 142 */       return this;
/*     */     }
/* 144 */     if (offset < 0L) {
/* 145 */       throw new IllegalArgumentException("Offset must not be negative");
/*     */     }
/* 147 */     if (size < 0L || offset + size > this.size) {
/* 148 */       throw new IllegalArgumentException("Size must not be negative and must be within bounds");
/*     */     }
/* 150 */     debug.log("Slicing %s at %s with size %s", this.fileAccess, Long.valueOf(offset), Long.valueOf(size));
/* 151 */     return new FileDataBlock(this.fileAccess, this.offset + offset, size);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class FileAccess
/*     */   {
/*     */     static final int BUFFER_SIZE = 10240;
/*     */ 
/*     */     
/*     */     private final Path path;
/*     */     
/*     */     private int referenceCount;
/*     */     
/*     */     private FileChannel fileChannel;
/*     */     
/*     */     private boolean fileChannelInterrupted;
/*     */     
/*     */     private RandomAccessFile randomAccessFile;
/*     */     
/*     */     private ByteBuffer buffer;
/*     */     
/* 173 */     private long bufferPosition = -1L;
/*     */     
/*     */     private int bufferSize;
/*     */     
/* 177 */     private final Object lock = new Object();
/*     */     
/*     */     FileAccess(Path path) {
/* 180 */       if (!Files.isRegularFile(path, new java.nio.file.LinkOption[0])) {
/* 181 */         throw new IllegalArgumentException("" + path + " must be a regular file");
/*     */       }
/* 183 */       this.path = path;
/*     */     }
/*     */     
/*     */     int read(ByteBuffer dst, long position) throws IOException {
/* 187 */       synchronized (this.lock) {
/* 188 */         if (position < this.bufferPosition || position >= this.bufferPosition + this.bufferSize) {
/* 189 */           fillBuffer(position);
/*     */         }
/* 191 */         if (this.bufferSize <= 0) {
/* 192 */           return this.bufferSize;
/*     */         }
/* 194 */         int offset = (int)(position - this.bufferPosition);
/* 195 */         int length = Math.min(this.bufferSize - offset, dst.remaining());
/* 196 */         dst.put(dst.position(), this.buffer, offset, length);
/* 197 */         dst.position(dst.position() + length);
/* 198 */         return length;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void fillBuffer(long position) throws IOException {
/* 203 */       if (Thread.currentThread().isInterrupted()) {
/* 204 */         fillBufferUsingRandomAccessFile(position);
/*     */         return;
/*     */       } 
/*     */       try {
/* 208 */         if (this.fileChannelInterrupted) {
/* 209 */           repairFileChannel();
/* 210 */           this.fileChannelInterrupted = false;
/*     */         } 
/* 212 */         this.buffer.clear();
/* 213 */         this.bufferSize = this.fileChannel.read(this.buffer, position);
/* 214 */         this.bufferPosition = position;
/*     */       }
/* 216 */       catch (ClosedByInterruptException ex) {
/* 217 */         this.fileChannelInterrupted = true;
/* 218 */         fillBufferUsingRandomAccessFile(position);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void fillBufferUsingRandomAccessFile(long position) throws IOException {
/* 223 */       if (this.randomAccessFile == null) {
/* 224 */         this.randomAccessFile = new RandomAccessFile(this.path.toFile(), "r");
/* 225 */         FileDataBlock.tracker.openedFileChannel(this.path);
/*     */       } 
/* 227 */       byte[] bytes = new byte[10240];
/* 228 */       this.randomAccessFile.seek(position);
/* 229 */       int len = this.randomAccessFile.read(bytes);
/* 230 */       this.buffer.clear();
/* 231 */       if (len > 0) {
/* 232 */         this.buffer.put(bytes, 0, len);
/*     */       }
/* 234 */       this.bufferSize = len;
/* 235 */       this.bufferPosition = position;
/*     */     }
/*     */     
/*     */     private void repairFileChannel() throws IOException {
/* 239 */       FileDataBlock.tracker.closedFileChannel(this.path);
/* 240 */       this.fileChannel = FileChannel.open(this.path, new OpenOption[] { StandardOpenOption.READ });
/* 241 */       FileDataBlock.tracker.openedFileChannel(this.path);
/*     */     }
/*     */     
/*     */     void open() throws IOException {
/* 245 */       synchronized (this.lock) {
/* 246 */         if (this.referenceCount == 0) {
/* 247 */           FileDataBlock.debug.log("Opening '%s'", this.path);
/* 248 */           this.fileChannel = FileChannel.open(this.path, new OpenOption[] { StandardOpenOption.READ });
/* 249 */           this.buffer = ByteBuffer.allocateDirect(10240);
/* 250 */           FileDataBlock.tracker.openedFileChannel(this.path);
/*     */         } 
/* 252 */         this.referenceCount++;
/* 253 */         FileDataBlock.debug.log("Reference count for '%s' incremented to %s", this.path, Integer.valueOf(this.referenceCount));
/*     */       } 
/*     */     }
/*     */     
/*     */     void close() throws IOException {
/* 258 */       synchronized (this.lock) {
/* 259 */         if (this.referenceCount == 0) {
/*     */           return;
/*     */         }
/* 262 */         this.referenceCount--;
/* 263 */         if (this.referenceCount == 0) {
/* 264 */           FileDataBlock.debug.log("Closing '%s'", this.path);
/* 265 */           this.buffer = null;
/* 266 */           this.bufferPosition = -1L;
/* 267 */           this.bufferSize = 0;
/* 268 */           this.fileChannel.close();
/* 269 */           FileDataBlock.tracker.closedFileChannel(this.path);
/* 270 */           this.fileChannel = null;
/* 271 */           if (this.randomAccessFile != null) {
/* 272 */             this.randomAccessFile.close();
/* 273 */             FileDataBlock.tracker.closedFileChannel(this.path);
/* 274 */             this.randomAccessFile = null;
/*     */           } 
/*     */         } 
/* 277 */         FileDataBlock.debug.log("Reference count for '%s' decremented to %s", this.path, Integer.valueOf(this.referenceCount));
/*     */       } 
/*     */     }
/*     */     
/*     */     <E extends Exception> void ensureOpen(Supplier<E> exceptionSupplier) throws E {
/* 282 */       synchronized (this.lock) {
/* 283 */         if (this.referenceCount == 0) {
/* 284 */           throw (E)exceptionSupplier.get();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 291 */       return this.path.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static interface Tracker
/*     */   {
/* 301 */     public static final Tracker NONE = new Tracker() {
/*     */         public void openedFileChannel(Path path) {}
/*     */         
/*     */         public void closedFileChannel(Path path) {}
/*     */       };
/*     */     
/*     */     void openedFileChannel(Path param1Path);
/*     */     
/*     */     void closedFileChannel(Path param1Path);
/*     */   }
/*     */   
/*     */   class null implements Tracker {
/*     */     public void openedFileChannel(Path path) {}
/*     */     
/*     */     public void closedFileChannel(Path path) {}
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/FileDataBlock.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */