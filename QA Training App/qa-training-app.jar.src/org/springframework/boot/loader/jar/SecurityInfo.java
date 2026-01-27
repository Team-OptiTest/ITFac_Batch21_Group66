/*     */ package org.springframework.boot.loader.jar;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.security.CodeSigner;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.jar.JarEntry;
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
/*     */ final class SecurityInfo
/*     */ {
/*  36 */   static final SecurityInfo NONE = new SecurityInfo(null, null);
/*     */   
/*     */   private final Certificate[][] certificateLookups;
/*     */   
/*     */   private final CodeSigner[][] codeSignerLookups;
/*     */   
/*     */   private SecurityInfo(Certificate[][] entryCertificates, CodeSigner[][] entryCodeSigners) {
/*  43 */     this.certificateLookups = entryCertificates;
/*  44 */     this.codeSignerLookups = entryCodeSigners;
/*     */   }
/*     */   
/*     */   Certificate[] getCertificates(ZipContent.Entry contentEntry) {
/*  48 */     return (this.certificateLookups != null) ? clone(this.certificateLookups[contentEntry.getLookupIndex()]) : null;
/*     */   }
/*     */   
/*     */   CodeSigner[] getCodeSigners(ZipContent.Entry contentEntry) {
/*  52 */     return (this.codeSignerLookups != null) ? clone(this.codeSignerLookups[contentEntry.getLookupIndex()]) : null;
/*     */   }
/*     */   
/*     */   private <T> T[] clone(T[] array) {
/*  56 */     return (array != null) ? (T[])array.clone() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static SecurityInfo get(ZipContent content) {
/*  65 */     if (!content.hasJarSignatureFile()) {
/*  66 */       return NONE;
/*     */     }
/*     */     try {
/*  69 */       return load(content);
/*     */     }
/*  71 */     catch (IOException ex) {
/*  72 */       throw new UncheckedIOException(ex);
/*     */     } 
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
/*     */   
/*     */   private static SecurityInfo load(ZipContent content) throws IOException {
/*  86 */     int size = content.size();
/*  87 */     boolean hasSecurityInfo = false;
/*  88 */     Certificate[][] entryCertificates = new Certificate[size][];
/*  89 */     CodeSigner[][] entryCodeSigners = new CodeSigner[size][];
/*  90 */     JarEntriesStream entries = new JarEntriesStream(content.openRawZipData().asInputStream()); 
/*  91 */     try { JarEntry entry = entries.getNextEntry();
/*  92 */       while (entry != null) {
/*  93 */         ZipContent.Entry relatedEntry = content.getEntry(entry.getName());
/*  94 */         if (relatedEntry != null && entries.matches(relatedEntry.isDirectory(), relatedEntry
/*  95 */             .getUncompressedSize(), relatedEntry.getCompressionMethod(), () -> relatedEntry.openContent().asInputStream())) {
/*     */           
/*  97 */           Certificate[] certificates = entry.getCertificates();
/*  98 */           CodeSigner[] codeSigners = entry.getCodeSigners();
/*  99 */           if (certificates != null || codeSigners != null) {
/* 100 */             hasSecurityInfo = true;
/* 101 */             entryCertificates[relatedEntry.getLookupIndex()] = certificates;
/* 102 */             entryCodeSigners[relatedEntry.getLookupIndex()] = codeSigners;
/*     */           } 
/*     */         } 
/* 105 */         entry = entries.getNextEntry();
/*     */       } 
/* 107 */       entries.close(); } catch (Throwable throwable) { try { entries.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 108 */      return !hasSecurityInfo ? NONE : new SecurityInfo(entryCertificates, entryCodeSigners);
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/jar/SecurityInfo.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */