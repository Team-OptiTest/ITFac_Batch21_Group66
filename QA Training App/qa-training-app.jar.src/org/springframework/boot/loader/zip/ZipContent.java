/*     */ package org.springframework.boot.loader.zip;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.zip.ZipEntry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ZipContent
/*     */   implements Closeable
/*     */ {
/*     */   private static final String META_INF = "META-INF/";
/*  67 */   private static final byte[] SIGNATURE_SUFFIX = ".DSA".getBytes(StandardCharsets.UTF_8);
/*     */   
/*  69 */   private static final DebugLogger debug = DebugLogger.get(ZipContent.class);
/*     */   
/*  71 */   private static final Map<Source, ZipContent> cache = new ConcurrentHashMap<>();
/*     */ 
/*     */   
/*     */   private final Source source;
/*     */   
/*     */   private final Kind kind;
/*     */   
/*     */   private final FileDataBlock data;
/*     */   
/*     */   private final long centralDirectoryPos;
/*     */   
/*     */   private final long commentPos;
/*     */   
/*     */   private final long commentLength;
/*     */   
/*     */   private final int[] lookupIndexes;
/*     */   
/*     */   private final int[] nameHashLookups;
/*     */   
/*     */   private final int[] relativeCentralDirectoryOffsetLookups;
/*     */   
/*     */   private final NameOffsetLookups nameOffsetLookups;
/*     */   
/*     */   private final boolean hasJarSignatureFile;
/*     */   
/*     */   private SoftReference<CloseableDataBlock> virtualData;
/*     */   
/*     */   private SoftReference<Map<Class<?>, Object>> info;
/*     */ 
/*     */   
/*     */   private ZipContent(Source source, Kind kind, FileDataBlock data, long centralDirectoryPos, long commentPos, long commentLength, int[] lookupIndexes, int[] nameHashLookups, int[] relativeCentralDirectoryOffsetLookups, NameOffsetLookups nameOffsetLookups, boolean hasJarSignatureFile) {
/* 102 */     this.source = source;
/* 103 */     this.kind = kind;
/* 104 */     this.data = data;
/* 105 */     this.centralDirectoryPos = centralDirectoryPos;
/* 106 */     this.commentPos = commentPos;
/* 107 */     this.commentLength = commentLength;
/* 108 */     this.lookupIndexes = lookupIndexes;
/* 109 */     this.nameHashLookups = nameHashLookups;
/* 110 */     this.relativeCentralDirectoryOffsetLookups = relativeCentralDirectoryOffsetLookups;
/* 111 */     this.nameOffsetLookups = nameOffsetLookups;
/* 112 */     this.hasJarSignatureFile = hasJarSignatureFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Kind getKind() {
/* 121 */     return this.kind;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CloseableDataBlock openRawZipData() throws IOException {
/* 142 */     this.data.open();
/* 143 */     return !this.nameOffsetLookups.hasAnyEnabled() ? this.data : getVirtualData();
/*     */   }
/*     */   
/*     */   private CloseableDataBlock getVirtualData() throws IOException {
/* 147 */     CloseableDataBlock virtualData = (this.virtualData != null) ? this.virtualData.get() : null;
/* 148 */     if (virtualData != null) {
/* 149 */       return virtualData;
/*     */     }
/* 151 */     virtualData = createVirtualData();
/* 152 */     this.virtualData = new SoftReference<>(virtualData);
/* 153 */     return virtualData;
/*     */   }
/*     */   
/*     */   private CloseableDataBlock createVirtualData() throws IOException {
/* 157 */     int size = size();
/* 158 */     NameOffsetLookups nameOffsetLookups = this.nameOffsetLookups.emptyCopy();
/* 159 */     ZipCentralDirectoryFileHeaderRecord[] centralRecords = new ZipCentralDirectoryFileHeaderRecord[size];
/* 160 */     long[] centralRecordPositions = new long[size];
/* 161 */     for (int i = 0; i < size; i++) {
/* 162 */       int lookupIndex = this.lookupIndexes[i];
/* 163 */       long pos = getCentralDirectoryFileHeaderRecordPos(lookupIndex);
/* 164 */       nameOffsetLookups.enable(i, this.nameOffsetLookups.isEnabled(lookupIndex));
/* 165 */       centralRecords[i] = ZipCentralDirectoryFileHeaderRecord.load(this.data, pos);
/* 166 */       centralRecordPositions[i] = pos;
/*     */     } 
/* 168 */     return new VirtualZipDataBlock(this.data, nameOffsetLookups, centralRecords, centralRecordPositions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 176 */     return this.lookupIndexes.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getComment() {
/*     */     try {
/* 185 */       return ZipString.readString(this.data, this.commentPos, this.commentLength);
/*     */     }
/* 187 */     catch (UncheckedIOException ex) {
/* 188 */       if (ex.getCause() instanceof java.nio.channels.ClosedChannelException) {
/* 189 */         throw new IllegalStateException("Zip content closed", ex);
/*     */       }
/* 191 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entry getEntry(CharSequence name) {
/* 201 */     return getEntry(null, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entry getEntry(CharSequence namePrefix, CharSequence name) {
/* 211 */     int nameHash = nameHash(namePrefix, name);
/* 212 */     int lookupIndex = getFirstLookupIndex(nameHash);
/* 213 */     int size = size();
/* 214 */     while (lookupIndex >= 0 && lookupIndex < size && this.nameHashLookups[lookupIndex] == nameHash) {
/* 215 */       long pos = getCentralDirectoryFileHeaderRecordPos(lookupIndex);
/* 216 */       ZipCentralDirectoryFileHeaderRecord centralRecord = loadZipCentralDirectoryFileHeaderRecord(pos);
/* 217 */       if (hasName(lookupIndex, centralRecord, pos, namePrefix, name)) {
/* 218 */         return new Entry(lookupIndex, centralRecord);
/*     */       }
/* 220 */       lookupIndex++;
/*     */     } 
/* 222 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasEntry(CharSequence namePrefix, CharSequence name) {
/* 232 */     int nameHash = nameHash(namePrefix, name);
/* 233 */     int lookupIndex = getFirstLookupIndex(nameHash);
/* 234 */     int size = size();
/* 235 */     while (lookupIndex >= 0 && lookupIndex < size && this.nameHashLookups[lookupIndex] == nameHash) {
/* 236 */       long pos = getCentralDirectoryFileHeaderRecordPos(lookupIndex);
/* 237 */       ZipCentralDirectoryFileHeaderRecord centralRecord = loadZipCentralDirectoryFileHeaderRecord(pos);
/* 238 */       if (hasName(lookupIndex, centralRecord, pos, namePrefix, name)) {
/* 239 */         return true;
/*     */       }
/* 241 */       lookupIndex++;
/*     */     } 
/* 243 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entry getEntry(int index) {
/* 253 */     int lookupIndex = this.lookupIndexes[index];
/* 254 */     long pos = getCentralDirectoryFileHeaderRecordPos(lookupIndex);
/* 255 */     ZipCentralDirectoryFileHeaderRecord centralRecord = loadZipCentralDirectoryFileHeaderRecord(pos);
/* 256 */     return new Entry(lookupIndex, centralRecord);
/*     */   }
/*     */   
/*     */   private ZipCentralDirectoryFileHeaderRecord loadZipCentralDirectoryFileHeaderRecord(long pos) {
/*     */     try {
/* 261 */       return ZipCentralDirectoryFileHeaderRecord.load(this.data, pos);
/*     */     }
/* 263 */     catch (IOException ex) {
/* 264 */       if (ex instanceof java.nio.channels.ClosedChannelException) {
/* 265 */         throw new IllegalStateException("Zip content closed", ex);
/*     */       }
/* 267 */       throw new UncheckedIOException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int nameHash(CharSequence namePrefix, CharSequence name) {
/* 272 */     int nameHash = 0;
/* 273 */     nameHash = (namePrefix != null) ? ZipString.hash(nameHash, namePrefix, false) : nameHash;
/* 274 */     nameHash = ZipString.hash(nameHash, name, true);
/* 275 */     return nameHash;
/*     */   }
/*     */   
/*     */   private int getFirstLookupIndex(int nameHash) {
/* 279 */     int lookupIndex = Arrays.binarySearch(this.nameHashLookups, 0, this.nameHashLookups.length, nameHash);
/* 280 */     if (lookupIndex < 0) {
/* 281 */       return -1;
/*     */     }
/* 283 */     while (lookupIndex > 0 && this.nameHashLookups[lookupIndex - 1] == nameHash) {
/* 284 */       lookupIndex--;
/*     */     }
/* 286 */     return lookupIndex;
/*     */   }
/*     */   
/*     */   private long getCentralDirectoryFileHeaderRecordPos(int lookupIndex) {
/* 290 */     return this.centralDirectoryPos + this.relativeCentralDirectoryOffsetLookups[lookupIndex];
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasName(int lookupIndex, ZipCentralDirectoryFileHeaderRecord centralRecord, long pos, CharSequence namePrefix, CharSequence name) {
/* 295 */     int offset = this.nameOffsetLookups.get(lookupIndex);
/* 296 */     pos += (46 + offset);
/* 297 */     int len = centralRecord.fileNameLength() - offset;
/* 298 */     ByteBuffer buffer = ByteBuffer.allocate(256);
/* 299 */     if (namePrefix != null) {
/* 300 */       int startsWithNamePrefix = ZipString.startsWith(buffer, this.data, pos, len, namePrefix);
/* 301 */       if (startsWithNamePrefix == -1) {
/* 302 */         return false;
/*     */       }
/* 304 */       pos += startsWithNamePrefix;
/* 305 */       len -= startsWithNamePrefix;
/*     */     } 
/* 307 */     return ZipString.matches(buffer, this.data, pos, len, name, true);
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
/*     */   public <I> I getInfo(Class<I> type, Function<ZipContent, I> function) {
/* 319 */     Map<Class<?>, Object> info = (this.info != null) ? this.info.get() : null;
/* 320 */     if (info == null) {
/* 321 */       info = new ConcurrentHashMap<>();
/* 322 */       this.info = new SoftReference<>(info);
/*     */     } 
/* 324 */     return (I)info.computeIfAbsent(type, key -> {
/*     */           debug.log("Getting %s info from zip '%s'", type.getName(), this);
/*     */           return function.apply(this);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasJarSignatureFile() {
/* 336 */     return this.hasJarSignatureFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 345 */     this.data.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 350 */     return this.source.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ZipContent open(Path path) throws IOException {
/* 361 */     return open(new Source(path.toAbsolutePath(), null));
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
/*     */   public static ZipContent open(Path path, String nestedEntryName) throws IOException {
/* 373 */     return open(new Source(path.toAbsolutePath(), nestedEntryName));
/*     */   }
/*     */   
/*     */   private static ZipContent open(Source source) throws IOException {
/* 377 */     ZipContent zipContent = cache.get(source);
/* 378 */     if (zipContent != null) {
/* 379 */       debug.log("Opening existing cached zip content for %s", zipContent);
/* 380 */       zipContent.data.open();
/* 381 */       return zipContent;
/*     */     } 
/* 383 */     debug.log("Loading zip content from %s", source);
/* 384 */     zipContent = Loader.load(source);
/* 385 */     ZipContent previouslyCached = cache.putIfAbsent(source, zipContent);
/* 386 */     if (previouslyCached != null) {
/* 387 */       debug.log("Closing zip content from %s since cache was populated from another thread", source);
/* 388 */       zipContent.close();
/* 389 */       previouslyCached.data.open();
/* 390 */       return previouslyCached;
/*     */     } 
/* 392 */     return zipContent;
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
/*     */   public enum Kind
/*     */   {
/* 405 */     ZIP,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 410 */     NESTED_ZIP,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 415 */     NESTED_DIRECTORY;
/*     */   }
/*     */   
/*     */   private static final class Source extends Record { private final Path path;
/*     */     private final String nestedEntryName;
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lorg/springframework/boot/loader/zip/ZipContent$Source;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #425	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lorg/springframework/boot/loader/zip/ZipContent$Source;
/*     */     }
/*     */     
/* 425 */     private Source(Path path, String nestedEntryName) { this.path = path; this.nestedEntryName = nestedEntryName; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lorg/springframework/boot/loader/zip/ZipContent$Source;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #425	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lorg/springframework/boot/loader/zip/ZipContent$Source;
/* 425 */       //   0	8	1	o	Ljava/lang/Object; } public Path path() { return this.path; } public String nestedEntryName() { return this.nestedEntryName; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNested() {
/* 432 */       return (this.nestedEntryName != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 437 */       return !isNested() ? path().toString() : ("" + path() + "[" + path() + "]");
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Loader
/*     */   {
/* 448 */     private final ByteBuffer buffer = ByteBuffer.allocate(256);
/*     */     
/*     */     private final ZipContent.Source source;
/*     */     
/*     */     private final FileDataBlock data;
/*     */     
/*     */     private final long centralDirectoryPos;
/*     */     
/*     */     private final int[] index;
/*     */     
/*     */     private int[] nameHashLookups;
/*     */     
/*     */     private int[] relativeCentralDirectoryOffsetLookups;
/*     */     
/*     */     private final NameOffsetLookups nameOffsetLookups;
/*     */     
/*     */     private int cursor;
/*     */     
/*     */     private Loader(ZipContent.Source source, ZipContent.Entry directoryEntry, FileDataBlock data, long centralDirectoryPos, int maxSize) {
/* 467 */       this.source = source;
/* 468 */       this.data = data;
/* 469 */       this.centralDirectoryPos = centralDirectoryPos;
/* 470 */       this.index = new int[maxSize];
/* 471 */       this.nameHashLookups = new int[maxSize];
/* 472 */       this.relativeCentralDirectoryOffsetLookups = new int[maxSize];
/* 473 */       this
/* 474 */         .nameOffsetLookups = (directoryEntry != null) ? new NameOffsetLookups(directoryEntry.getName().length(), maxSize) : NameOffsetLookups.NONE;
/*     */     }
/*     */ 
/*     */     
/*     */     private void add(ZipCentralDirectoryFileHeaderRecord centralRecord, long pos, boolean enableNameOffset) throws IOException {
/* 479 */       int nameOffset = this.nameOffsetLookups.enable(this.cursor, enableNameOffset);
/* 480 */       int hash = ZipString.hash(this.buffer, this.data, pos + 46L + nameOffset, centralRecord
/*     */           
/* 482 */           .fileNameLength() - nameOffset, true);
/* 483 */       this.nameHashLookups[this.cursor] = hash;
/* 484 */       this.relativeCentralDirectoryOffsetLookups[this.cursor] = (int)(pos - this.centralDirectoryPos);
/* 485 */       this.index[this.cursor] = this.cursor;
/* 486 */       this.cursor++;
/*     */     }
/*     */     
/*     */     private ZipContent finish(ZipContent.Kind kind, long commentPos, long commentLength, boolean hasJarSignatureFile) {
/* 490 */       if (this.cursor != this.nameHashLookups.length) {
/* 491 */         this.nameHashLookups = Arrays.copyOf(this.nameHashLookups, this.cursor);
/* 492 */         this.relativeCentralDirectoryOffsetLookups = Arrays.copyOf(this.relativeCentralDirectoryOffsetLookups, this.cursor);
/*     */       } 
/*     */       
/* 495 */       int size = this.nameHashLookups.length;
/* 496 */       sort(0, size - 1);
/* 497 */       int[] lookupIndexes = new int[size];
/* 498 */       for (int i = 0; i < size; i++) {
/* 499 */         lookupIndexes[this.index[i]] = i;
/*     */       }
/* 501 */       return new ZipContent(this.source, kind, this.data, this.centralDirectoryPos, commentPos, commentLength, lookupIndexes, this.nameHashLookups, this.relativeCentralDirectoryOffsetLookups, this.nameOffsetLookups, hasJarSignatureFile);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void sort(int left, int right) {
/* 508 */       if (left < right) {
/* 509 */         int pivot = this.nameHashLookups[left + (right - left) / 2];
/* 510 */         int i = left;
/* 511 */         int j = right;
/* 512 */         while (i <= j) {
/* 513 */           while (this.nameHashLookups[i] < pivot) {
/* 514 */             i++;
/*     */           }
/* 516 */           while (this.nameHashLookups[j] > pivot) {
/* 517 */             j--;
/*     */           }
/* 519 */           if (i <= j) {
/* 520 */             swap(i, j);
/* 521 */             i++;
/* 522 */             j--;
/*     */           } 
/*     */         } 
/* 525 */         if (left < j) {
/* 526 */           sort(left, j);
/*     */         }
/* 528 */         if (right > i) {
/* 529 */           sort(i, right);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     private void swap(int i, int j) {
/* 535 */       swap(this.index, i, j);
/* 536 */       swap(this.nameHashLookups, i, j);
/* 537 */       swap(this.relativeCentralDirectoryOffsetLookups, i, j);
/* 538 */       this.nameOffsetLookups.swap(i, j);
/*     */     }
/*     */     
/*     */     private static void swap(int[] array, int i, int j) {
/* 542 */       int temp = array[i];
/* 543 */       array[i] = array[j];
/* 544 */       array[j] = temp;
/*     */     }
/*     */     
/*     */     static ZipContent load(ZipContent.Source source) throws IOException {
/* 548 */       if (!source.isNested()) {
/* 549 */         return loadNonNested(source);
/*     */       }
/* 551 */       ZipContent zip = ZipContent.open(source.path()); 
/* 552 */       try { ZipContent.Entry entry = zip.getEntry(source.nestedEntryName());
/* 553 */         if (entry == null) {
/* 554 */           throw new IOException("Nested entry '%s' not found in container zip '%s'"
/* 555 */               .formatted(new Object[] { source.nestedEntryName(), source.path() }));
/*     */         }
/* 557 */         ZipContent zipContent = !entry.isDirectory() ? loadNestedZip(source, entry) : loadNestedDirectory(source, zip, entry);
/* 558 */         if (zip != null) zip.close();  return zipContent; } catch (Throwable throwable) { if (zip != null)
/*     */           try { zip.close(); }
/*     */           catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */             throw throwable; }
/* 562 */        } private static ZipContent loadNonNested(ZipContent.Source source) throws IOException { ZipContent.debug.log("Loading non-nested zip '%s'", source.path());
/* 563 */       return openAndLoad(source, ZipContent.Kind.ZIP, new FileDataBlock(source.path())); }
/*     */ 
/*     */     
/*     */     private static ZipContent loadNestedZip(ZipContent.Source source, ZipContent.Entry entry) throws IOException {
/* 567 */       if (entry.centralRecord.compressionMethod() != 0) {
/* 568 */         throw new IOException("Nested entry '%s' in container zip '%s' must not be compressed"
/* 569 */             .formatted(new Object[] { source.nestedEntryName(), source.path() }));
/*     */       }
/* 571 */       ZipContent.debug.log("Loading nested zip entry '%s' from '%s'", source.nestedEntryName(), source.path());
/* 572 */       return openAndLoad(source, ZipContent.Kind.NESTED_ZIP, entry.getContent());
/*     */     }
/*     */     
/*     */     private static ZipContent openAndLoad(ZipContent.Source source, ZipContent.Kind kind, FileDataBlock data) throws IOException {
/*     */       try {
/* 577 */         data.open();
/* 578 */         return loadContent(source, kind, data);
/*     */       }
/* 580 */       catch (IOException|RuntimeException ex) {
/* 581 */         data.close();
/* 582 */         throw ex;
/*     */       } 
/*     */     }
/*     */     
/*     */     private static ZipContent loadContent(ZipContent.Source source, ZipContent.Kind kind, FileDataBlock data) throws IOException {
/* 587 */       ZipEndOfCentralDirectoryRecord.Located locatedEocd = ZipEndOfCentralDirectoryRecord.load(data);
/* 588 */       ZipEndOfCentralDirectoryRecord eocd = locatedEocd.endOfCentralDirectoryRecord();
/* 589 */       long eocdPos = locatedEocd.pos();
/* 590 */       Zip64EndOfCentralDirectoryLocator zip64Locator = Zip64EndOfCentralDirectoryLocator.find(data, eocdPos);
/* 591 */       Zip64EndOfCentralDirectoryRecord zip64Eocd = Zip64EndOfCentralDirectoryRecord.load(data, zip64Locator);
/* 592 */       data = data.slice(getStartOfZipContent(data, eocd, zip64Eocd));
/*     */       
/* 594 */       long centralDirectoryPos = (zip64Eocd != null) ? zip64Eocd.offsetToStartOfCentralDirectory() : Integer.toUnsignedLong(eocd.offsetToStartOfCentralDirectory());
/*     */       
/* 596 */       long numberOfEntries = (zip64Eocd != null) ? zip64Eocd.totalNumberOfCentralDirectoryEntries() : Short.toUnsignedInt(eocd.totalNumberOfCentralDirectoryEntries());
/* 597 */       if (numberOfEntries < 0L) {
/* 598 */         throw new IllegalStateException("Invalid number of zip entries in " + source);
/*     */       }
/* 600 */       if (numberOfEntries > 2147483647L) {
/* 601 */         throw new IllegalStateException("Too many zip entries in " + source);
/*     */       }
/* 603 */       Loader loader = new Loader(source, null, data, centralDirectoryPos, (int)numberOfEntries);
/* 604 */       ByteBuffer signatureNameSuffixBuffer = ByteBuffer.allocate(ZipContent.SIGNATURE_SUFFIX.length);
/* 605 */       boolean hasJarSignatureFile = false;
/* 606 */       long pos = centralDirectoryPos;
/* 607 */       for (int i = 0; i < numberOfEntries; i++) {
/* 608 */         ZipCentralDirectoryFileHeaderRecord centralRecord = ZipCentralDirectoryFileHeaderRecord.load(data, pos);
/* 609 */         if (!hasJarSignatureFile) {
/* 610 */           long filenamePos = pos + 46L;
/* 611 */           if (centralRecord.fileNameLength() > ZipContent.SIGNATURE_SUFFIX.length && ZipString.startsWith(loader.buffer, data, filenamePos, centralRecord
/* 612 */               .fileNameLength(), "META-INF/") >= 0) {
/* 613 */             signatureNameSuffixBuffer.clear();
/* 614 */             data.readFully(signatureNameSuffixBuffer, filenamePos + centralRecord
/* 615 */                 .fileNameLength() - ZipContent.SIGNATURE_SUFFIX.length);
/* 616 */             hasJarSignatureFile = Arrays.equals(ZipContent.SIGNATURE_SUFFIX, signatureNameSuffixBuffer.array());
/*     */           } 
/*     */         } 
/* 619 */         loader.add(centralRecord, pos, false);
/* 620 */         pos += centralRecord.size();
/*     */       } 
/* 622 */       long commentPos = locatedEocd.pos() + 22L;
/* 623 */       return loader.finish(kind, commentPos, eocd.commentLength(), hasJarSignatureFile);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static long getStartOfZipContent(FileDataBlock data, ZipEndOfCentralDirectoryRecord eocd, Zip64EndOfCentralDirectoryRecord zip64Eocd) throws IOException {
/* 640 */       long specifiedOffsetToStartOfCentralDirectory = (zip64Eocd != null) ? zip64Eocd.offsetToStartOfCentralDirectory() : Integer.toUnsignedLong(eocd.offsetToStartOfCentralDirectory());
/* 641 */       long sizeOfCentralDirectoryAndEndRecords = getSizeOfCentralDirectoryAndEndRecords(eocd, zip64Eocd);
/* 642 */       long actualOffsetToStartOfCentralDirectory = data.size() - sizeOfCentralDirectoryAndEndRecords;
/* 643 */       return actualOffsetToStartOfCentralDirectory - specifiedOffsetToStartOfCentralDirectory;
/*     */     }
/*     */ 
/*     */     
/*     */     private static long getSizeOfCentralDirectoryAndEndRecords(ZipEndOfCentralDirectoryRecord eocd, Zip64EndOfCentralDirectoryRecord zip64Eocd) {
/* 648 */       long result = 0L;
/* 649 */       result += eocd.size();
/* 650 */       if (zip64Eocd != null) {
/* 651 */         result += 20L;
/* 652 */         result += zip64Eocd.size();
/*     */       } 
/* 654 */       result += (zip64Eocd != null) ? zip64Eocd.sizeOfCentralDirectory() : 
/* 655 */         Integer.toUnsignedLong(eocd.sizeOfCentralDirectory());
/* 656 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     private static ZipContent loadNestedDirectory(ZipContent.Source source, ZipContent zip, ZipContent.Entry directoryEntry) throws IOException {
/* 661 */       ZipContent.debug.log("Loading nested directory entry '%s' from '%s'", source.nestedEntryName(), source.path());
/* 662 */       if (!source.nestedEntryName().endsWith("/")) {
/* 663 */         throw new IllegalArgumentException("Nested entry name must end with '/'");
/*     */       }
/* 665 */       String directoryName = directoryEntry.getName();
/* 666 */       zip.data.open();
/*     */       try {
/* 668 */         Loader loader = new Loader(source, directoryEntry, zip.data, zip.centralDirectoryPos, zip.size());
/* 669 */         for (int cursor = 0; cursor < zip.size(); cursor++) {
/* 670 */           int index = zip.lookupIndexes[cursor];
/* 671 */           if (index != directoryEntry.getLookupIndex()) {
/* 672 */             long pos = zip.getCentralDirectoryFileHeaderRecordPos(index);
/*     */             
/* 674 */             ZipCentralDirectoryFileHeaderRecord centralRecord = ZipCentralDirectoryFileHeaderRecord.load(zip.data, pos);
/* 675 */             long namePos = pos + 46L;
/* 676 */             short nameLen = centralRecord.fileNameLength();
/* 677 */             if (ZipString.startsWith(loader.buffer, zip.data, namePos, nameLen, directoryName) != -1) {
/* 678 */               loader.add(centralRecord, pos, true);
/*     */             }
/*     */           } 
/*     */         } 
/* 682 */         return loader.finish(ZipContent.Kind.NESTED_DIRECTORY, zip.commentPos, zip.commentLength, zip.hasJarSignatureFile);
/*     */       }
/* 684 */       catch (IOException|RuntimeException ex) {
/* 685 */         zip.data.close();
/* 686 */         throw ex;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class Entry
/*     */   {
/*     */     private final int lookupIndex;
/*     */ 
/*     */ 
/*     */     
/*     */     private final ZipCentralDirectoryFileHeaderRecord centralRecord;
/*     */ 
/*     */ 
/*     */     
/*     */     private volatile String name;
/*     */ 
/*     */     
/*     */     private volatile FileDataBlock content;
/*     */ 
/*     */ 
/*     */     
/*     */     Entry(int lookupIndex, ZipCentralDirectoryFileHeaderRecord centralRecord) {
/* 712 */       this.lookupIndex = lookupIndex;
/* 713 */       this.centralRecord = centralRecord;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getLookupIndex() {
/* 722 */       return this.lookupIndex;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isDirectory() {
/* 730 */       return getName().endsWith("/");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNameStartingWith(CharSequence prefix) {
/* 739 */       String name = this.name;
/* 740 */       if (name != null) {
/* 741 */         return name.startsWith(prefix.toString());
/*     */       }
/* 743 */       long pos = ZipContent.this.getCentralDirectoryFileHeaderRecordPos(this.lookupIndex) + 46L;
/*     */       
/* 745 */       return (ZipString.startsWith(null, ZipContent.this.data, pos, this.centralRecord.fileNameLength(), prefix) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 754 */       String name = this.name;
/* 755 */       if (name == null) {
/* 756 */         int offset = ZipContent.this.nameOffsetLookups.get(this.lookupIndex);
/* 757 */         long pos = ZipContent.this.getCentralDirectoryFileHeaderRecordPos(this.lookupIndex) + 46L + offset;
/*     */         
/* 759 */         name = ZipString.readString(ZipContent.this.data, pos, (this.centralRecord.fileNameLength() - offset));
/* 760 */         this.name = name;
/*     */       } 
/* 762 */       return name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getCompressionMethod() {
/* 772 */       return this.centralRecord.compressionMethod();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getUncompressedSize() {
/* 780 */       return this.centralRecord.uncompressedSize();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CloseableDataBlock openContent() throws IOException {
/* 793 */       FileDataBlock content = getContent();
/* 794 */       content.open();
/* 795 */       return content;
/*     */     }
/*     */     
/*     */     private FileDataBlock getContent() throws IOException {
/* 799 */       FileDataBlock content = this.content;
/* 800 */       if (content == null) {
/* 801 */         long pos = Integer.toUnsignedLong(this.centralRecord.offsetToLocalHeader());
/* 802 */         checkNotZip64Extended(pos);
/* 803 */         ZipLocalFileHeaderRecord localHeader = ZipLocalFileHeaderRecord.load(ZipContent.this.data, pos);
/* 804 */         long size = Integer.toUnsignedLong(this.centralRecord.compressedSize());
/* 805 */         checkNotZip64Extended(size);
/* 806 */         content = ZipContent.this.data.slice(pos + localHeader.size(), size);
/* 807 */         this.content = content;
/*     */       } 
/* 809 */       return content;
/*     */     }
/*     */     
/*     */     private void checkNotZip64Extended(long value) throws IOException {
/* 813 */       if (value == -1L) {
/* 814 */         throw new IOException("Zip64 extended information extra fields are not supported");
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <E extends ZipEntry> E as(Function<String, E> factory) {
/* 825 */       return as((entry, name) -> (ZipEntry)factory.apply(name));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <E extends ZipEntry> E as(BiFunction<Entry, String, E> factory) {
/*     */       try {
/* 836 */         ZipEntry zipEntry = (ZipEntry)factory.apply(this, getName());
/* 837 */         long pos = ZipContent.this.getCentralDirectoryFileHeaderRecordPos(this.lookupIndex);
/* 838 */         this.centralRecord.copyTo(ZipContent.this.data, pos, zipEntry);
/* 839 */         return (E)zipEntry;
/*     */       }
/* 841 */       catch (IOException ex) {
/* 842 */         throw new UncheckedIOException(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/ZipContent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */