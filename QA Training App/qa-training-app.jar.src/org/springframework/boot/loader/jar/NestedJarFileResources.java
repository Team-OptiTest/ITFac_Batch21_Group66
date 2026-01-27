/*     */ package org.springframework.boot.loader.jar;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collections;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.zip.Inflater;
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
/*     */ class NestedJarFileResources
/*     */   implements Runnable
/*     */ {
/*     */   private static final int INFLATER_CACHE_LIMIT = 20;
/*     */   private ZipContent zipContent;
/*     */   private ZipContent zipContentForManifest;
/*  49 */   private final Set<InputStream> inputStreams = Collections.newSetFromMap(new WeakHashMap<>());
/*     */   
/*  51 */   private Deque<Inflater> inflaterCache = new ArrayDeque<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   NestedJarFileResources(File file, String nestedEntryName) throws IOException {
/*  60 */     this.zipContent = ZipContent.open(file.toPath(), nestedEntryName);
/*  61 */     this
/*  62 */       .zipContentForManifest = (this.zipContent.getKind() != ZipContent.Kind.NESTED_DIRECTORY) ? null : ZipContent.open(file.toPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ZipContent zipContent() {
/*  70 */     return this.zipContent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ZipContent zipContentForManifest() {
/*  79 */     return (this.zipContentForManifest != null) ? this.zipContentForManifest : this.zipContent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addInputStream(InputStream inputStream) {
/*  87 */     synchronized (this.inputStreams) {
/*  88 */       this.inputStreams.add(inputStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void removeInputStream(InputStream inputStream) {
/*  97 */     synchronized (this.inputStreams) {
/*  98 */       this.inputStreams.remove(inputStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Runnable createInflatorCleanupAction(Inflater inflater) {
/* 108 */     return () -> endOrCacheInflater(inflater);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Inflater getOrCreateInflater() {
/* 116 */     Deque<Inflater> inflaterCache = this.inflaterCache;
/* 117 */     if (inflaterCache != null) {
/* 118 */       synchronized (inflaterCache) {
/* 119 */         Inflater inflater = this.inflaterCache.poll();
/* 120 */         if (inflater != null) {
/* 121 */           return inflater;
/*     */         }
/*     */       } 
/*     */     }
/* 125 */     return new Inflater(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void endOrCacheInflater(Inflater inflater) {
/* 134 */     Deque<Inflater> inflaterCache = this.inflaterCache;
/* 135 */     if (inflaterCache != null) {
/* 136 */       synchronized (inflaterCache) {
/* 137 */         if (this.inflaterCache == inflaterCache && inflaterCache.size() < 20) {
/* 138 */           inflater.reset();
/* 139 */           this.inflaterCache.add(inflater);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/* 144 */     inflater.end();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 153 */     releaseAll();
/*     */   }
/*     */   
/*     */   private void releaseAll() {
/* 157 */     IOException exceptionChain = null;
/* 158 */     exceptionChain = releaseInflators(exceptionChain);
/* 159 */     exceptionChain = releaseInputStreams(exceptionChain);
/* 160 */     exceptionChain = releaseZipContent(exceptionChain);
/* 161 */     exceptionChain = releaseZipContentForManifest(exceptionChain);
/* 162 */     if (exceptionChain != null) {
/* 163 */       throw new UncheckedIOException(exceptionChain);
/*     */     }
/*     */   }
/*     */   
/*     */   private IOException releaseInflators(IOException exceptionChain) {
/* 168 */     Deque<Inflater> inflaterCache = this.inflaterCache;
/* 169 */     if (inflaterCache != null) {
/*     */       try {
/* 171 */         synchronized (inflaterCache) {
/* 172 */           inflaterCache.forEach(Inflater::end);
/*     */         } 
/*     */       } finally {
/*     */         
/* 176 */         this.inflaterCache = null;
/*     */       } 
/*     */     }
/* 179 */     return exceptionChain;
/*     */   }
/*     */   
/*     */   private IOException releaseInputStreams(IOException exceptionChain) {
/* 183 */     synchronized (this.inputStreams) {
/* 184 */       for (InputStream inputStream : List.<InputStream>copyOf(this.inputStreams)) {
/*     */         try {
/* 186 */           inputStream.close();
/*     */         }
/* 188 */         catch (IOException ex) {
/* 189 */           exceptionChain = addToExceptionChain(exceptionChain, ex);
/*     */         } 
/*     */       } 
/* 192 */       this.inputStreams.clear();
/*     */     } 
/* 194 */     return exceptionChain;
/*     */   }
/*     */   
/*     */   private IOException releaseZipContent(IOException exceptionChain) {
/* 198 */     ZipContent zipContent = this.zipContent;
/* 199 */     if (zipContent != null) {
/*     */       try {
/* 201 */         zipContent.close();
/*     */       }
/* 203 */       catch (IOException ex) {
/* 204 */         exceptionChain = addToExceptionChain(exceptionChain, ex);
/*     */       } finally {
/*     */         
/* 207 */         this.zipContent = null;
/*     */       } 
/*     */     }
/* 210 */     return exceptionChain;
/*     */   }
/*     */   
/*     */   private IOException releaseZipContentForManifest(IOException exceptionChain) {
/* 214 */     ZipContent zipContentForManifest = this.zipContentForManifest;
/* 215 */     if (zipContentForManifest != null) {
/*     */       try {
/* 217 */         zipContentForManifest.close();
/*     */       }
/* 219 */       catch (IOException ex) {
/* 220 */         exceptionChain = addToExceptionChain(exceptionChain, ex);
/*     */       } finally {
/*     */         
/* 223 */         this.zipContentForManifest = null;
/*     */       } 
/*     */     }
/* 226 */     return exceptionChain;
/*     */   }
/*     */   
/*     */   private IOException addToExceptionChain(IOException exceptionChain, IOException ex) {
/* 230 */     if (exceptionChain != null) {
/* 231 */       exceptionChain.addSuppressed(ex);
/* 232 */       return exceptionChain;
/*     */     } 
/* 234 */     return ex;
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/jar/NestedJarFileResources.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */