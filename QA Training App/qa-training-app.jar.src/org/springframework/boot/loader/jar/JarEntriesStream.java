/*     */ package org.springframework.boot.loader.jar;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarInputStream;
/*     */ import java.util.zip.Inflater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class JarEntriesStream
/*     */   implements Closeable
/*     */ {
/*     */   private static final int BUFFER_SIZE = 4096;
/*     */   private final JarInputStream in;
/*  43 */   private final byte[] inBuffer = new byte[4096];
/*     */   
/*  45 */   private final byte[] compareBuffer = new byte[4096];
/*     */   
/*  47 */   private final Inflater inflater = new Inflater(true);
/*     */   
/*     */   private JarEntry entry;
/*     */   
/*     */   JarEntriesStream(InputStream in) throws IOException {
/*  52 */     this.in = new JarInputStream(in);
/*     */   }
/*     */   
/*     */   JarEntry getNextEntry() throws IOException {
/*  56 */     this.entry = this.in.getNextJarEntry();
/*  57 */     if (this.entry != null) {
/*  58 */       this.entry.getSize();
/*     */     }
/*  60 */     this.inflater.reset();
/*  61 */     return this.entry;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean matches(boolean directory, int size, int compressionMethod, InputStreamSupplier streamSupplier) throws IOException {
/*  66 */     if (this.entry.isDirectory() != directory) {
/*  67 */       fail("directory");
/*     */     }
/*  69 */     if (this.entry.getMethod() != compressionMethod) {
/*  70 */       fail("compression method");
/*     */     }
/*  72 */     if (this.entry.isDirectory()) {
/*  73 */       this.in.closeEntry();
/*  74 */       return true;
/*     */     } 
/*  76 */     DataInputStream expected = new DataInputStream(getInputStream(size, streamSupplier)); 
/*  77 */     try { assertSameContent(expected);
/*  78 */       expected.close(); } catch (Throwable throwable) { try { expected.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/*  79 */      return true;
/*     */   }
/*     */   
/*     */   private InputStream getInputStream(int size, InputStreamSupplier streamSupplier) throws IOException {
/*  83 */     InputStream inputStream = streamSupplier.get();
/*  84 */     return (this.entry.getMethod() != 8) ? inputStream : 
/*  85 */       new ZipInflaterInputStream(inputStream, this.inflater, size);
/*     */   }
/*     */   
/*     */   private void assertSameContent(DataInputStream expected) throws IOException {
/*     */     int len;
/*  90 */     while ((len = this.in.read(this.inBuffer)) > 0) {
/*     */       try {
/*  92 */         expected.readFully(this.compareBuffer, 0, len);
/*  93 */         if (Arrays.equals(this.inBuffer, 0, len, this.compareBuffer, 0, len)) {
/*     */           continue;
/*     */         }
/*     */       }
/*  97 */       catch (EOFException eOFException) {}
/*     */ 
/*     */       
/* 100 */       fail("content");
/*     */     } 
/* 102 */     if (expected.read() != -1) {
/* 103 */       fail("content");
/*     */     }
/*     */   }
/*     */   
/*     */   private void fail(String check) {
/* 108 */     throw new IllegalStateException("Content mismatch when reading security info for entry '%s' (%s check)"
/* 109 */         .formatted(new Object[] { this.entry.getName(), check }));
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 114 */     this.inflater.end();
/* 115 */     this.in.close();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   static interface InputStreamSupplier {
/*     */     InputStream get() throws IOException;
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/jar/JarEntriesStream.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */