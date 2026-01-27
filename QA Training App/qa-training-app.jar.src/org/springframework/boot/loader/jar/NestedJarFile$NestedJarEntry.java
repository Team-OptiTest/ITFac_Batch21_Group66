/*     */ package org.springframework.boot.loader.jar;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.security.CodeSigner;
/*     */ import java.security.cert.Certificate;
/*     */ import java.time.LocalDateTime;
/*     */ import java.util.function.Function;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.zip.ZipEntry;
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
/*     */ class NestedJarEntry
/*     */   extends JarEntry
/*     */ {
/* 435 */   private static final IllegalStateException CANNOT_BE_MODIFIED_EXCEPTION = new IllegalStateException("Neste jar entries cannot be modified");
/*     */   
/*     */   private final ZipContent.Entry contentEntry;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private volatile boolean populated;
/*     */ 
/*     */   
/*     */   NestedJarEntry(NestedJarFile paramNestedJarFile, ZipContent.Entry contentEntry) {
/* 445 */     this(contentEntry, contentEntry.getName());
/*     */   }
/*     */   
/*     */   NestedJarEntry(ZipContent.Entry contentEntry, String name) {
/* 449 */     super(contentEntry.getName());
/* 450 */     this.contentEntry = contentEntry;
/* 451 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTime() {
/* 456 */     populate();
/* 457 */     return super.getTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDateTime getTimeLocal() {
/* 462 */     populate();
/* 463 */     return super.getTimeLocal();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTime(long time) {
/* 468 */     throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTimeLocal(LocalDateTime time) {
/* 473 */     throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileTime getLastModifiedTime() {
/* 478 */     populate();
/* 479 */     return super.getLastModifiedTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public ZipEntry setLastModifiedTime(FileTime time) {
/* 484 */     throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileTime getLastAccessTime() {
/* 489 */     populate();
/* 490 */     return super.getLastAccessTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public ZipEntry setLastAccessTime(FileTime time) {
/* 495 */     throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileTime getCreationTime() {
/* 500 */     populate();
/* 501 */     return super.getCreationTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public ZipEntry setCreationTime(FileTime time) {
/* 506 */     throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getSize() {
/* 511 */     return this.contentEntry.getUncompressedSize() & 0xFFFFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSize(long size) {
/* 516 */     throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getCompressedSize() {
/* 521 */     populate();
/* 522 */     return super.getCompressedSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCompressedSize(long csize) {
/* 527 */     throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getCrc() {
/* 532 */     populate();
/* 533 */     return super.getCrc();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCrc(long crc) {
/* 538 */     throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMethod() {
/* 543 */     populate();
/* 544 */     return super.getMethod();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMethod(int method) {
/* 549 */     throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getExtra() {
/* 554 */     populate();
/* 555 */     return super.getExtra();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setExtra(byte[] extra) {
/* 560 */     throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getComment() {
/* 565 */     populate();
/* 566 */     return super.getComment();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setComment(String comment) {
/* 571 */     throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */   }
/*     */   
/*     */   boolean isOwnedBy(NestedJarFile nestedJarFile) {
/* 575 */     return (NestedJarFile.this == nestedJarFile);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRealName() {
/* 580 */     return super.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 585 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public Attributes getAttributes() throws IOException {
/* 590 */     Manifest manifest = NestedJarFile.this.getManifest();
/* 591 */     return (manifest != null) ? manifest.getAttributes(getName()) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Certificate[] getCertificates() {
/* 596 */     return getSecurityInfo().getCertificates(contentEntry());
/*     */   }
/*     */ 
/*     */   
/*     */   public CodeSigner[] getCodeSigners() {
/* 601 */     return getSecurityInfo().getCodeSigners(contentEntry());
/*     */   }
/*     */   
/*     */   private SecurityInfo getSecurityInfo() {
/* 605 */     return (SecurityInfo)NestedJarFile.this.resources.zipContent().getInfo(SecurityInfo.class, SecurityInfo::get);
/*     */   }
/*     */   
/*     */   ZipContent.Entry contentEntry() {
/* 609 */     return this.contentEntry;
/*     */   }
/*     */   
/*     */   private void populate() {
/* 613 */     boolean populated = this.populated;
/* 614 */     if (!populated) {
/* 615 */       ZipEntry entry = this.contentEntry.as(ZipEntry::new);
/* 616 */       super.setMethod(entry.getMethod());
/* 617 */       super.setTime(entry.getTime());
/* 618 */       super.setCrc(entry.getCrc());
/* 619 */       super.setCompressedSize(entry.getCompressedSize());
/* 620 */       super.setSize(entry.getSize());
/* 621 */       super.setExtra(entry.getExtra());
/* 622 */       super.setComment(entry.getComment());
/* 623 */       this.populated = true;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/jar/NestedJarFile$NestedJarEntry.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */