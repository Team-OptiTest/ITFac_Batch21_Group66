/*     */ package org.springframework.boot.loader.jar;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.lang.ref.Cleaner;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.security.CodeSigner;
/*     */ import java.security.cert.Certificate;
/*     */ import java.time.LocalDateTime;
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import java.util.zip.Inflater;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipException;
/*     */ import org.springframework.boot.loader.log.DebugLogger;
/*     */ import org.springframework.boot.loader.ref.Cleaner;
/*     */ import org.springframework.boot.loader.zip.CloseableDataBlock;
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
/*     */ public class NestedJarFile
/*     */   extends JarFile
/*     */ {
/*     */   private static final int DECIMAL = 10;
/*     */   private static final String META_INF = "META-INF/";
/*     */   static final String META_INF_VERSIONS = "META-INF/versions/";
/*  68 */   static final int BASE_VERSION = baseVersion().feature();
/*     */   
/*  70 */   private static final DebugLogger debug = DebugLogger.get(NestedJarFile.class);
/*     */ 
/*     */   
/*     */   private final Cleaner cleaner;
/*     */ 
/*     */   
/*     */   private final NestedJarFileResources resources;
/*     */ 
/*     */   
/*     */   private final Cleaner.Cleanable cleanup;
/*     */ 
/*     */   
/*     */   private final String name;
/*     */ 
/*     */   
/*     */   private final int version;
/*     */   
/*     */   private volatile NestedJarEntry lastEntry;
/*     */   
/*     */   private volatile boolean closed;
/*     */   
/*     */   private volatile ManifestInfo manifestInfo;
/*     */   
/*     */   private volatile MetaInfVersionsInfo metaInfVersionsInfo;
/*     */ 
/*     */   
/*     */   NestedJarFile(File file) throws IOException {
/*  97 */     this(file, null, null, false, Cleaner.instance);
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
/*     */   public NestedJarFile(File file, String nestedEntryName) throws IOException {
/* 110 */     this(file, nestedEntryName, null, true, Cleaner.instance);
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
/*     */   
/*     */   public NestedJarFile(File file, String nestedEntryName, Runtime.Version version) throws IOException {
/* 124 */     this(file, nestedEntryName, version, true, Cleaner.instance);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   NestedJarFile(File file, String nestedEntryName, Runtime.Version version, boolean onlyNestedJars, Cleaner cleaner) throws IOException {
/* 141 */     super(file);
/* 142 */     if (onlyNestedJars && (nestedEntryName == null || nestedEntryName.isEmpty())) {
/* 143 */       throw new IllegalArgumentException("nestedEntryName must not be empty");
/*     */     }
/* 145 */     debug.log("Created nested jar file (%s, %s, %s)", file, nestedEntryName, version);
/* 146 */     this.cleaner = cleaner;
/* 147 */     this.resources = new NestedJarFileResources(file, nestedEntryName);
/* 148 */     this.cleanup = cleaner.register(this, this.resources);
/* 149 */     this.name = file.getPath() + file.getPath();
/* 150 */     this.version = (version != null) ? version.feature() : baseVersion().feature();
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getRawZipDataInputStream() throws IOException {
/* 155 */     RawZipDataInputStream inputStream = new RawZipDataInputStream(this.resources.zipContent().openRawZipData().asInputStream());
/* 156 */     this.resources.addInputStream(inputStream);
/* 157 */     return inputStream;
/*     */   }
/*     */ 
/*     */   
/*     */   public Manifest getManifest() throws IOException {
/*     */     try {
/* 163 */       return ((ManifestInfo)this.resources.zipContentForManifest()
/* 164 */         .getInfo(ManifestInfo.class, this::getManifestInfo))
/* 165 */         .getManifest();
/*     */     }
/* 167 */     catch (UncheckedIOException ex) {
/* 168 */       throw ex.getCause();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration<JarEntry> entries() {
/* 174 */     synchronized (this) {
/* 175 */       ensureOpen();
/* 176 */       return new JarEntriesEnumeration(this.resources.zipContent());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<JarEntry> stream() {
/* 182 */     synchronized (this) {
/* 183 */       ensureOpen();
/* 184 */       return streamContentEntries().map(x$0 -> new NestedJarEntry(x$0));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<JarEntry> versionedStream() {
/* 190 */     synchronized (this) {
/* 191 */       ensureOpen();
/* 192 */       return streamContentEntries().map(this::getBaseName)
/* 193 */         .filter(Objects::nonNull)
/* 194 */         .distinct()
/* 195 */         .map(this::getJarEntry)
/* 196 */         .filter(Objects::nonNull);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Stream<ZipContent.Entry> streamContentEntries() {
/* 201 */     ZipContentEntriesSpliterator spliterator = new ZipContentEntriesSpliterator(this.resources.zipContent());
/* 202 */     return StreamSupport.stream(spliterator, false);
/*     */   }
/*     */   
/*     */   private String getBaseName(ZipContent.Entry contentEntry) {
/* 206 */     String name = contentEntry.getName();
/* 207 */     if (!name.startsWith("META-INF/versions/")) {
/* 208 */       return name;
/*     */     }
/* 210 */     int versionNumberStartIndex = "META-INF/versions/".length();
/* 211 */     int versionNumberEndIndex = (versionNumberStartIndex != -1) ? name.indexOf('/', versionNumberStartIndex) : -1;
/* 212 */     if (versionNumberEndIndex == -1 || versionNumberEndIndex == name.length() - 1) {
/* 213 */       return null;
/*     */     }
/*     */     try {
/* 216 */       int versionNumber = Integer.parseInt(name, versionNumberStartIndex, versionNumberEndIndex, 10);
/* 217 */       if (versionNumber > this.version) {
/* 218 */         return null;
/*     */       }
/*     */     }
/* 221 */     catch (NumberFormatException ex) {
/* 222 */       return null;
/*     */     } 
/* 224 */     return name.substring(versionNumberEndIndex + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public JarEntry getJarEntry(String name) {
/* 229 */     return getNestedJarEntry(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public JarEntry getEntry(String name) {
/* 234 */     return getNestedJarEntry(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasEntry(String name) {
/* 243 */     NestedJarEntry lastEntry = this.lastEntry;
/* 244 */     if (lastEntry != null && name.equals(lastEntry.getName())) {
/* 245 */       return true;
/*     */     }
/* 247 */     ZipContent.Entry entry = getVersionedContentEntry(name);
/* 248 */     if (entry != null) {
/* 249 */       return true;
/*     */     }
/* 251 */     synchronized (this) {
/* 252 */       ensureOpen();
/* 253 */       return this.resources.zipContent().hasEntry(null, name);
/*     */     } 
/*     */   }
/*     */   
/*     */   private NestedJarEntry getNestedJarEntry(String name) {
/* 258 */     Objects.requireNonNull(name, "name");
/* 259 */     NestedJarEntry lastEntry = this.lastEntry;
/* 260 */     if (lastEntry != null && name.equals(lastEntry.getName())) {
/* 261 */       return lastEntry;
/*     */     }
/* 263 */     ZipContent.Entry entry = getVersionedContentEntry(name);
/* 264 */     entry = (entry != null) ? entry : getContentEntry(null, name);
/* 265 */     if (entry == null) {
/* 266 */       return null;
/*     */     }
/* 268 */     NestedJarEntry nestedJarEntry = new NestedJarEntry(entry, name);
/* 269 */     this.lastEntry = nestedJarEntry;
/* 270 */     return nestedJarEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ZipContent.Entry getVersionedContentEntry(String name) {
/* 276 */     if (BASE_VERSION >= this.version || name.startsWith("META-INF/") || !getManifestInfo().isMultiRelease()) {
/* 277 */       return null;
/*     */     }
/* 279 */     MetaInfVersionsInfo metaInfVersionsInfo = getMetaInfVersionsInfo();
/* 280 */     int[] versions = metaInfVersionsInfo.versions();
/* 281 */     String[] directories = metaInfVersionsInfo.directories();
/* 282 */     for (int i = versions.length - 1; i >= 0; i--) {
/* 283 */       if (versions[i] <= this.version) {
/* 284 */         ZipContent.Entry entry = getContentEntry(directories[i], name);
/* 285 */         if (entry != null) {
/* 286 */           return entry;
/*     */         }
/*     */       } 
/*     */     } 
/* 290 */     return null;
/*     */   }
/*     */   
/*     */   private ZipContent.Entry getContentEntry(String namePrefix, String name) {
/* 294 */     synchronized (this) {
/* 295 */       ensureOpen();
/* 296 */       return this.resources.zipContent().getEntry(namePrefix, name);
/*     */     } 
/*     */   }
/*     */   
/*     */   private ManifestInfo getManifestInfo() {
/* 301 */     ManifestInfo manifestInfo = this.manifestInfo;
/* 302 */     if (manifestInfo != null) {
/* 303 */       return manifestInfo;
/*     */     }
/* 305 */     synchronized (this) {
/* 306 */       ensureOpen();
/* 307 */       manifestInfo = (ManifestInfo)this.resources.zipContent().getInfo(ManifestInfo.class, this::getManifestInfo);
/*     */     } 
/* 309 */     this.manifestInfo = manifestInfo;
/* 310 */     return manifestInfo;
/*     */   }
/*     */   
/*     */   private ManifestInfo getManifestInfo(ZipContent zipContent) {
/* 314 */     ZipContent.Entry contentEntry = zipContent.getEntry("META-INF/MANIFEST.MF");
/* 315 */     if (contentEntry == null) {
/* 316 */       return ManifestInfo.NONE;
/*     */     }
/*     */     try {
/* 319 */       InputStream inputStream = getInputStream(contentEntry); 
/* 320 */       try { Manifest manifest = new Manifest(inputStream);
/* 321 */         ManifestInfo manifestInfo = new ManifestInfo(manifest);
/* 322 */         if (inputStream != null) inputStream.close();  return manifestInfo; } catch (Throwable throwable) { if (inputStream != null)
/*     */           try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; } 
/* 324 */     } catch (IOException ex) {
/* 325 */       throw new UncheckedIOException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private MetaInfVersionsInfo getMetaInfVersionsInfo() {
/* 330 */     MetaInfVersionsInfo metaInfVersionsInfo = this.metaInfVersionsInfo;
/* 331 */     if (metaInfVersionsInfo != null) {
/* 332 */       return metaInfVersionsInfo;
/*     */     }
/* 334 */     synchronized (this) {
/* 335 */       ensureOpen();
/*     */       
/* 337 */       metaInfVersionsInfo = (MetaInfVersionsInfo)this.resources.zipContent().getInfo(MetaInfVersionsInfo.class, MetaInfVersionsInfo::get);
/*     */     } 
/* 339 */     this.metaInfVersionsInfo = metaInfVersionsInfo;
/* 340 */     return metaInfVersionsInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getInputStream(ZipEntry entry) throws IOException {
/* 345 */     Objects.requireNonNull(entry, "entry");
/* 346 */     if (entry instanceof NestedJarEntry) { NestedJarEntry nestedJarEntry = (NestedJarEntry)entry; if (nestedJarEntry.isOwnedBy(this))
/* 347 */         return getInputStream(nestedJarEntry.contentEntry());  }
/*     */     
/* 349 */     return getInputStream(getNestedJarEntry(entry.getName()).contentEntry());
/*     */   }
/*     */   
/*     */   private InputStream getInputStream(ZipContent.Entry contentEntry) throws IOException {
/* 353 */     int compression = contentEntry.getCompressionMethod();
/* 354 */     if (compression != 0 && compression != 8) {
/* 355 */       throw new ZipException("invalid compression method");
/*     */     }
/* 357 */     synchronized (this) {
/* 358 */       ensureOpen();
/* 359 */       InputStream inputStream = new JarEntryInputStream(contentEntry);
/*     */       try {
/* 361 */         if (compression == 8) {
/* 362 */           inputStream = new JarEntryInflaterInputStream((JarEntryInputStream)inputStream, this.resources);
/*     */         }
/* 364 */         this.resources.addInputStream(inputStream);
/* 365 */         return inputStream;
/*     */       }
/* 367 */       catch (RuntimeException ex) {
/* 368 */         inputStream.close();
/* 369 */         throw ex;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getComment() {
/* 376 */     synchronized (this) {
/* 377 */       ensureOpen();
/* 378 */       return this.resources.zipContent().getComment();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 384 */     synchronized (this) {
/* 385 */       ensureOpen();
/* 386 */       return this.resources.zipContent().size();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 392 */     super.close();
/* 393 */     if (this.closed) {
/*     */       return;
/*     */     }
/* 396 */     this.closed = true;
/* 397 */     synchronized (this) {
/*     */       try {
/* 399 */         this.cleanup.clean();
/*     */       }
/* 401 */       catch (UncheckedIOException ex) {
/* 402 */         throw ex.getCause();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 409 */     return this.name;
/*     */   }
/*     */   
/*     */   private void ensureOpen() {
/* 413 */     if (this.closed) {
/* 414 */       throw new IllegalStateException("Zip file closed");
/*     */     }
/* 416 */     if (this.resources.zipContent() == null) {
/* 417 */       throw new IllegalStateException("The object is not initialized.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearCache() {
/* 425 */     synchronized (this) {
/* 426 */       this.lastEntry = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class NestedJarEntry
/*     */     extends JarEntry
/*     */   {
/* 435 */     private static final IllegalStateException CANNOT_BE_MODIFIED_EXCEPTION = new IllegalStateException("Neste jar entries cannot be modified");
/*     */     
/*     */     private final ZipContent.Entry contentEntry;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private volatile boolean populated;
/*     */ 
/*     */     
/*     */     NestedJarEntry(ZipContent.Entry contentEntry) {
/* 445 */       this(contentEntry, contentEntry.getName());
/*     */     }
/*     */     
/*     */     NestedJarEntry(ZipContent.Entry contentEntry, String name) {
/* 449 */       super(contentEntry.getName());
/* 450 */       this.contentEntry = contentEntry;
/* 451 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getTime() {
/* 456 */       populate();
/* 457 */       return super.getTime();
/*     */     }
/*     */ 
/*     */     
/*     */     public LocalDateTime getTimeLocal() {
/* 462 */       populate();
/* 463 */       return super.getTimeLocal();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setTime(long time) {
/* 468 */       throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setTimeLocal(LocalDateTime time) {
/* 473 */       throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */     }
/*     */ 
/*     */     
/*     */     public FileTime getLastModifiedTime() {
/* 478 */       populate();
/* 479 */       return super.getLastModifiedTime();
/*     */     }
/*     */ 
/*     */     
/*     */     public ZipEntry setLastModifiedTime(FileTime time) {
/* 484 */       throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */     }
/*     */ 
/*     */     
/*     */     public FileTime getLastAccessTime() {
/* 489 */       populate();
/* 490 */       return super.getLastAccessTime();
/*     */     }
/*     */ 
/*     */     
/*     */     public ZipEntry setLastAccessTime(FileTime time) {
/* 495 */       throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */     }
/*     */ 
/*     */     
/*     */     public FileTime getCreationTime() {
/* 500 */       populate();
/* 501 */       return super.getCreationTime();
/*     */     }
/*     */ 
/*     */     
/*     */     public ZipEntry setCreationTime(FileTime time) {
/* 506 */       throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getSize() {
/* 511 */       return this.contentEntry.getUncompressedSize() & 0xFFFFFFFFL;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSize(long size) {
/* 516 */       throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getCompressedSize() {
/* 521 */       populate();
/* 522 */       return super.getCompressedSize();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCompressedSize(long csize) {
/* 527 */       throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getCrc() {
/* 532 */       populate();
/* 533 */       return super.getCrc();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCrc(long crc) {
/* 538 */       throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMethod() {
/* 543 */       populate();
/* 544 */       return super.getMethod();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setMethod(int method) {
/* 549 */       throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getExtra() {
/* 554 */       populate();
/* 555 */       return super.getExtra();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setExtra(byte[] extra) {
/* 560 */       throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getComment() {
/* 565 */       populate();
/* 566 */       return super.getComment();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setComment(String comment) {
/* 571 */       throw CANNOT_BE_MODIFIED_EXCEPTION;
/*     */     }
/*     */     
/*     */     boolean isOwnedBy(NestedJarFile nestedJarFile) {
/* 575 */       return (NestedJarFile.this == nestedJarFile);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getRealName() {
/* 580 */       return super.getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 585 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public Attributes getAttributes() throws IOException {
/* 590 */       Manifest manifest = NestedJarFile.this.getManifest();
/* 591 */       return (manifest != null) ? manifest.getAttributes(getName()) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Certificate[] getCertificates() {
/* 596 */       return getSecurityInfo().getCertificates(contentEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     public CodeSigner[] getCodeSigners() {
/* 601 */       return getSecurityInfo().getCodeSigners(contentEntry());
/*     */     }
/*     */     
/*     */     private SecurityInfo getSecurityInfo() {
/* 605 */       return (SecurityInfo)NestedJarFile.this.resources.zipContent().getInfo(SecurityInfo.class, SecurityInfo::get);
/*     */     }
/*     */     
/*     */     ZipContent.Entry contentEntry() {
/* 609 */       return this.contentEntry;
/*     */     }
/*     */     
/*     */     private void populate() {
/* 613 */       boolean populated = this.populated;
/* 614 */       if (!populated) {
/* 615 */         ZipEntry entry = this.contentEntry.as(ZipEntry::new);
/* 616 */         super.setMethod(entry.getMethod());
/* 617 */         super.setTime(entry.getTime());
/* 618 */         super.setCrc(entry.getCrc());
/* 619 */         super.setCompressedSize(entry.getCompressedSize());
/* 620 */         super.setSize(entry.getSize());
/* 621 */         super.setExtra(entry.getExtra());
/* 622 */         super.setComment(entry.getComment());
/* 623 */         this.populated = true;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class JarEntriesEnumeration
/*     */     implements Enumeration<JarEntry>
/*     */   {
/*     */     private final ZipContent zipContent;
/*     */     
/*     */     private int cursor;
/*     */ 
/*     */     
/*     */     JarEntriesEnumeration(ZipContent zipContent) {
/* 639 */       this.zipContent = zipContent;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasMoreElements() {
/* 644 */       return (this.cursor < this.zipContent.size());
/*     */     }
/*     */ 
/*     */     
/*     */     public NestedJarFile.NestedJarEntry nextElement() {
/* 649 */       if (!hasMoreElements()) {
/* 650 */         throw new NoSuchElementException();
/*     */       }
/* 652 */       synchronized (NestedJarFile.this) {
/* 653 */         NestedJarFile.this.ensureOpen();
/* 654 */         return new NestedJarFile.NestedJarEntry(this.zipContent.getEntry(this.cursor++));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ZipContentEntriesSpliterator
/*     */     extends Spliterators.AbstractSpliterator<ZipContent.Entry>
/*     */   {
/*     */     private static final int ADDITIONAL_CHARACTERISTICS = 1297;
/*     */ 
/*     */     
/*     */     private final ZipContent zipContent;
/*     */     
/*     */     private int cursor;
/*     */ 
/*     */     
/*     */     ZipContentEntriesSpliterator(ZipContent zipContent) {
/* 673 */       super(zipContent.size(), 1297);
/* 674 */       this.zipContent = zipContent;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryAdvance(Consumer<? super ZipContent.Entry> action) {
/* 679 */       if (this.cursor < this.zipContent.size()) {
/* 680 */         synchronized (NestedJarFile.this) {
/* 681 */           NestedJarFile.this.ensureOpen();
/* 682 */           action.accept(this.zipContent.getEntry(this.cursor++));
/*     */         } 
/* 684 */         return true;
/*     */       } 
/* 686 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class JarEntryInputStream
/*     */     extends InputStream
/*     */   {
/*     */     private final int uncompressedSize;
/*     */     
/*     */     private final CloseableDataBlock content;
/*     */     
/*     */     private long pos;
/*     */     
/*     */     private long remaining;
/*     */     
/*     */     private volatile boolean closed;
/*     */ 
/*     */     
/*     */     JarEntryInputStream(ZipContent.Entry entry) throws IOException {
/* 707 */       this.uncompressedSize = entry.getUncompressedSize();
/* 708 */       this.content = entry.openContent();
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() throws IOException {
/* 713 */       byte[] b = new byte[1];
/* 714 */       return (read(b, 0, 1) == 1) ? (b[0] & 0xFF) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(byte[] b, int off, int len) throws IOException {
/*     */       int result;
/* 720 */       synchronized (NestedJarFile.this) {
/* 721 */         ensureOpen();
/* 722 */         ByteBuffer dst = ByteBuffer.wrap(b, off, len);
/* 723 */         int count = this.content.read(dst, this.pos);
/* 724 */         if (count > 0) {
/* 725 */           this.pos += count;
/* 726 */           this.remaining -= count;
/*     */         } 
/* 728 */         result = count;
/*     */       } 
/* 730 */       if (this.remaining == 0L) {
/* 731 */         close();
/*     */       }
/* 733 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public long skip(long n) throws IOException {
/*     */       long result;
/* 739 */       synchronized (NestedJarFile.this) {
/* 740 */         result = (n > 0L) ? maxForwardSkip(n) : maxBackwardSkip(n);
/* 741 */         this.pos += result;
/* 742 */         this.remaining -= result;
/*     */       } 
/* 744 */       if (this.remaining == 0L) {
/* 745 */         close();
/*     */       }
/* 747 */       return result;
/*     */     }
/*     */     
/*     */     private long maxForwardSkip(long n) {
/* 751 */       boolean willCauseOverflow = (this.pos + n < 0L);
/* 752 */       return (willCauseOverflow || n > this.remaining) ? this.remaining : n;
/*     */     }
/*     */     
/*     */     private long maxBackwardSkip(long n) {
/* 756 */       return Math.max(-this.pos, n);
/*     */     }
/*     */ 
/*     */     
/*     */     public int available() {
/* 761 */       return (this.remaining < 2147483647L) ? (int)this.remaining : Integer.MAX_VALUE;
/*     */     }
/*     */     
/*     */     private void ensureOpen() throws ZipException {
/* 765 */       if (NestedJarFile.this.closed || this.closed) {
/* 766 */         throw new ZipException("ZipFile closed");
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 772 */       if (this.closed) {
/*     */         return;
/*     */       }
/* 775 */       this.closed = true;
/* 776 */       this.content.close();
/* 777 */       NestedJarFile.this.resources.removeInputStream(this);
/*     */     }
/*     */     
/*     */     int getUncompressedSize() {
/* 781 */       return this.uncompressedSize;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class JarEntryInflaterInputStream
/*     */     extends ZipInflaterInputStream
/*     */   {
/*     */     private final Cleaner.Cleanable cleanup;
/*     */     
/*     */     private volatile boolean closed;
/*     */ 
/*     */     
/*     */     JarEntryInflaterInputStream(NestedJarFile.JarEntryInputStream inputStream, NestedJarFileResources resources) {
/* 796 */       this(inputStream, resources, resources.getOrCreateInflater());
/*     */     }
/*     */ 
/*     */     
/*     */     private JarEntryInflaterInputStream(NestedJarFile.JarEntryInputStream inputStream, NestedJarFileResources resources, Inflater inflater) {
/* 801 */       super(inputStream, inflater, inputStream.getUncompressedSize());
/* 802 */       this.cleanup = NestedJarFile.this.cleaner.register(this, resources.createInflatorCleanupAction(inflater));
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 807 */       if (this.closed) {
/*     */         return;
/*     */       }
/* 810 */       this.closed = true;
/* 811 */       super.close();
/* 812 */       NestedJarFile.this.resources.removeInputStream(this);
/* 813 */       this.cleanup.clean();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class RawZipDataInputStream
/*     */     extends FilterInputStream
/*     */   {
/*     */     private volatile boolean closed;
/*     */ 
/*     */     
/*     */     RawZipDataInputStream(InputStream in) {
/* 826 */       super(in);
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 831 */       if (this.closed) {
/*     */         return;
/*     */       }
/* 834 */       this.closed = true;
/* 835 */       super.close();
/* 836 */       NestedJarFile.this.resources.removeInputStream(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/jar/NestedJarFile.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */