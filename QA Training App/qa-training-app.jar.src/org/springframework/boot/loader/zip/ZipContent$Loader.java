/*     */ package org.springframework.boot.loader.zip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Loader
/*     */ {
/* 448 */   private final ByteBuffer buffer = ByteBuffer.allocate(256);
/*     */   
/*     */   private final ZipContent.Source source;
/*     */   
/*     */   private final FileDataBlock data;
/*     */   
/*     */   private final long centralDirectoryPos;
/*     */   
/*     */   private final int[] index;
/*     */   
/*     */   private int[] nameHashLookups;
/*     */   
/*     */   private int[] relativeCentralDirectoryOffsetLookups;
/*     */   
/*     */   private final NameOffsetLookups nameOffsetLookups;
/*     */   
/*     */   private int cursor;
/*     */   
/*     */   private Loader(ZipContent.Source source, ZipContent.Entry directoryEntry, FileDataBlock data, long centralDirectoryPos, int maxSize) {
/* 467 */     this.source = source;
/* 468 */     this.data = data;
/* 469 */     this.centralDirectoryPos = centralDirectoryPos;
/* 470 */     this.index = new int[maxSize];
/* 471 */     this.nameHashLookups = new int[maxSize];
/* 472 */     this.relativeCentralDirectoryOffsetLookups = new int[maxSize];
/* 473 */     this
/* 474 */       .nameOffsetLookups = (directoryEntry != null) ? new NameOffsetLookups(directoryEntry.getName().length(), maxSize) : NameOffsetLookups.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   private void add(ZipCentralDirectoryFileHeaderRecord centralRecord, long pos, boolean enableNameOffset) throws IOException {
/* 479 */     int nameOffset = this.nameOffsetLookups.enable(this.cursor, enableNameOffset);
/* 480 */     int hash = ZipString.hash(this.buffer, this.data, pos + 46L + nameOffset, centralRecord
/*     */         
/* 482 */         .fileNameLength() - nameOffset, true);
/* 483 */     this.nameHashLookups[this.cursor] = hash;
/* 484 */     this.relativeCentralDirectoryOffsetLookups[this.cursor] = (int)(pos - this.centralDirectoryPos);
/* 485 */     this.index[this.cursor] = this.cursor;
/* 486 */     this.cursor++;
/*     */   }
/*     */   
/*     */   private ZipContent finish(ZipContent.Kind kind, long commentPos, long commentLength, boolean hasJarSignatureFile) {
/* 490 */     if (this.cursor != this.nameHashLookups.length) {
/* 491 */       this.nameHashLookups = Arrays.copyOf(this.nameHashLookups, this.cursor);
/* 492 */       this.relativeCentralDirectoryOffsetLookups = Arrays.copyOf(this.relativeCentralDirectoryOffsetLookups, this.cursor);
/*     */     } 
/*     */     
/* 495 */     int size = this.nameHashLookups.length;
/* 496 */     sort(0, size - 1);
/* 497 */     int[] lookupIndexes = new int[size];
/* 498 */     for (int i = 0; i < size; i++) {
/* 499 */       lookupIndexes[this.index[i]] = i;
/*     */     }
/* 501 */     return new ZipContent(this.source, kind, this.data, this.centralDirectoryPos, commentPos, commentLength, lookupIndexes, this.nameHashLookups, this.relativeCentralDirectoryOffsetLookups, this.nameOffsetLookups, hasJarSignatureFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sort(int left, int right) {
/* 508 */     if (left < right) {
/* 509 */       int pivot = this.nameHashLookups[left + (right - left) / 2];
/* 510 */       int i = left;
/* 511 */       int j = right;
/* 512 */       while (i <= j) {
/* 513 */         while (this.nameHashLookups[i] < pivot) {
/* 514 */           i++;
/*     */         }
/* 516 */         while (this.nameHashLookups[j] > pivot) {
/* 517 */           j--;
/*     */         }
/* 519 */         if (i <= j) {
/* 520 */           swap(i, j);
/* 521 */           i++;
/* 522 */           j--;
/*     */         } 
/*     */       } 
/* 525 */       if (left < j) {
/* 526 */         sort(left, j);
/*     */       }
/* 528 */       if (right > i) {
/* 529 */         sort(i, right);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void swap(int i, int j) {
/* 535 */     swap(this.index, i, j);
/* 536 */     swap(this.nameHashLookups, i, j);
/* 537 */     swap(this.relativeCentralDirectoryOffsetLookups, i, j);
/* 538 */     this.nameOffsetLookups.swap(i, j);
/*     */   }
/*     */   
/*     */   private static void swap(int[] array, int i, int j) {
/* 542 */     int temp = array[i];
/* 543 */     array[i] = array[j];
/* 544 */     array[j] = temp;
/*     */   }
/*     */   
/*     */   static ZipContent load(ZipContent.Source source) throws IOException {
/* 548 */     if (!source.isNested()) {
/* 549 */       return loadNonNested(source);
/*     */     }
/* 551 */     ZipContent zip = ZipContent.open(source.path()); 
/* 552 */     try { ZipContent.Entry entry = zip.getEntry(source.nestedEntryName());
/* 553 */       if (entry == null) {
/* 554 */         throw new IOException("Nested entry '%s' not found in container zip '%s'"
/* 555 */             .formatted(new Object[] { source.nestedEntryName(), source.path() }));
/*     */       }
/* 557 */       ZipContent zipContent = !entry.isDirectory() ? loadNestedZip(source, entry) : loadNestedDirectory(source, zip, entry);
/* 558 */       if (zip != null) zip.close();  return zipContent; } catch (Throwable throwable) { if (zip != null)
/*     */         try { zip.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 562 */      } private static ZipContent loadNonNested(ZipContent.Source source) throws IOException { ZipContent.debug.log("Loading non-nested zip '%s'", source.path());
/* 563 */     return openAndLoad(source, ZipContent.Kind.ZIP, new FileDataBlock(source.path())); }
/*     */ 
/*     */   
/*     */   private static ZipContent loadNestedZip(ZipContent.Source source, ZipContent.Entry entry) throws IOException {
/* 567 */     if (entry.centralRecord.compressionMethod() != 0) {
/* 568 */       throw new IOException("Nested entry '%s' in container zip '%s' must not be compressed"
/* 569 */           .formatted(new Object[] { source.nestedEntryName(), source.path() }));
/*     */     }
/* 571 */     ZipContent.debug.log("Loading nested zip entry '%s' from '%s'", source.nestedEntryName(), source.path());
/* 572 */     return openAndLoad(source, ZipContent.Kind.NESTED_ZIP, entry.getContent());
/*     */   }
/*     */   
/*     */   private static ZipContent openAndLoad(ZipContent.Source source, ZipContent.Kind kind, FileDataBlock data) throws IOException {
/*     */     try {
/* 577 */       data.open();
/* 578 */       return loadContent(source, kind, data);
/*     */     }
/* 580 */     catch (IOException|RuntimeException ex) {
/* 581 */       data.close();
/* 582 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ZipContent loadContent(ZipContent.Source source, ZipContent.Kind kind, FileDataBlock data) throws IOException {
/* 587 */     ZipEndOfCentralDirectoryRecord.Located locatedEocd = ZipEndOfCentralDirectoryRecord.load(data);
/* 588 */     ZipEndOfCentralDirectoryRecord eocd = locatedEocd.endOfCentralDirectoryRecord();
/* 589 */     long eocdPos = locatedEocd.pos();
/* 590 */     Zip64EndOfCentralDirectoryLocator zip64Locator = Zip64EndOfCentralDirectoryLocator.find(data, eocdPos);
/* 591 */     Zip64EndOfCentralDirectoryRecord zip64Eocd = Zip64EndOfCentralDirectoryRecord.load(data, zip64Locator);
/* 592 */     data = data.slice(getStartOfZipContent(data, eocd, zip64Eocd));
/*     */     
/* 594 */     long centralDirectoryPos = (zip64Eocd != null) ? zip64Eocd.offsetToStartOfCentralDirectory() : Integer.toUnsignedLong(eocd.offsetToStartOfCentralDirectory());
/*     */     
/* 596 */     long numberOfEntries = (zip64Eocd != null) ? zip64Eocd.totalNumberOfCentralDirectoryEntries() : Short.toUnsignedInt(eocd.totalNumberOfCentralDirectoryEntries());
/* 597 */     if (numberOfEntries < 0L) {
/* 598 */       throw new IllegalStateException("Invalid number of zip entries in " + source);
/*     */     }
/* 600 */     if (numberOfEntries > 2147483647L) {
/* 601 */       throw new IllegalStateException("Too many zip entries in " + source);
/*     */     }
/* 603 */     Loader loader = new Loader(source, null, data, centralDirectoryPos, (int)numberOfEntries);
/* 604 */     ByteBuffer signatureNameSuffixBuffer = ByteBuffer.allocate(ZipContent.SIGNATURE_SUFFIX.length);
/* 605 */     boolean hasJarSignatureFile = false;
/* 606 */     long pos = centralDirectoryPos;
/* 607 */     for (int i = 0; i < numberOfEntries; i++) {
/* 608 */       ZipCentralDirectoryFileHeaderRecord centralRecord = ZipCentralDirectoryFileHeaderRecord.load(data, pos);
/* 609 */       if (!hasJarSignatureFile) {
/* 610 */         long filenamePos = pos + 46L;
/* 611 */         if (centralRecord.fileNameLength() > ZipContent.SIGNATURE_SUFFIX.length && ZipString.startsWith(loader.buffer, data, filenamePos, centralRecord
/* 612 */             .fileNameLength(), "META-INF/") >= 0) {
/* 613 */           signatureNameSuffixBuffer.clear();
/* 614 */           data.readFully(signatureNameSuffixBuffer, filenamePos + centralRecord
/* 615 */               .fileNameLength() - ZipContent.SIGNATURE_SUFFIX.length);
/* 616 */           hasJarSignatureFile = Arrays.equals(ZipContent.SIGNATURE_SUFFIX, signatureNameSuffixBuffer.array());
/*     */         } 
/*     */       } 
/* 619 */       loader.add(centralRecord, pos, false);
/* 620 */       pos += centralRecord.size();
/*     */     } 
/* 622 */     long commentPos = locatedEocd.pos() + 22L;
/* 623 */     return loader.finish(kind, commentPos, eocd.commentLength(), hasJarSignatureFile);
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
/*     */   private static long getStartOfZipContent(FileDataBlock data, ZipEndOfCentralDirectoryRecord eocd, Zip64EndOfCentralDirectoryRecord zip64Eocd) throws IOException {
/* 640 */     long specifiedOffsetToStartOfCentralDirectory = (zip64Eocd != null) ? zip64Eocd.offsetToStartOfCentralDirectory() : Integer.toUnsignedLong(eocd.offsetToStartOfCentralDirectory());
/* 641 */     long sizeOfCentralDirectoryAndEndRecords = getSizeOfCentralDirectoryAndEndRecords(eocd, zip64Eocd);
/* 642 */     long actualOffsetToStartOfCentralDirectory = data.size() - sizeOfCentralDirectoryAndEndRecords;
/* 643 */     return actualOffsetToStartOfCentralDirectory - specifiedOffsetToStartOfCentralDirectory;
/*     */   }
/*     */ 
/*     */   
/*     */   private static long getSizeOfCentralDirectoryAndEndRecords(ZipEndOfCentralDirectoryRecord eocd, Zip64EndOfCentralDirectoryRecord zip64Eocd) {
/* 648 */     long result = 0L;
/* 649 */     result += eocd.size();
/* 650 */     if (zip64Eocd != null) {
/* 651 */       result += 20L;
/* 652 */       result += zip64Eocd.size();
/*     */     } 
/* 654 */     result += (zip64Eocd != null) ? zip64Eocd.sizeOfCentralDirectory() : 
/* 655 */       Integer.toUnsignedLong(eocd.sizeOfCentralDirectory());
/* 656 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ZipContent loadNestedDirectory(ZipContent.Source source, ZipContent zip, ZipContent.Entry directoryEntry) throws IOException {
/* 661 */     ZipContent.debug.log("Loading nested directory entry '%s' from '%s'", source.nestedEntryName(), source.path());
/* 662 */     if (!source.nestedEntryName().endsWith("/")) {
/* 663 */       throw new IllegalArgumentException("Nested entry name must end with '/'");
/*     */     }
/* 665 */     String directoryName = directoryEntry.getName();
/* 666 */     zip.data.open();
/*     */     try {
/* 668 */       Loader loader = new Loader(source, directoryEntry, zip.data, zip.centralDirectoryPos, zip.size());
/* 669 */       for (int cursor = 0; cursor < zip.size(); cursor++) {
/* 670 */         int index = zip.lookupIndexes[cursor];
/* 671 */         if (index != directoryEntry.getLookupIndex()) {
/* 672 */           long pos = zip.getCentralDirectoryFileHeaderRecordPos(index);
/*     */           
/* 674 */           ZipCentralDirectoryFileHeaderRecord centralRecord = ZipCentralDirectoryFileHeaderRecord.load(zip.data, pos);
/* 675 */           long namePos = pos + 46L;
/* 676 */           short nameLen = centralRecord.fileNameLength();
/* 677 */           if (ZipString.startsWith(loader.buffer, zip.data, namePos, nameLen, directoryName) != -1) {
/* 678 */             loader.add(centralRecord, pos, true);
/*     */           }
/*     */         } 
/*     */       } 
/* 682 */       return loader.finish(ZipContent.Kind.NESTED_DIRECTORY, zip.commentPos, zip.commentLength, zip.hasJarSignatureFile);
/*     */     }
/* 684 */     catch (IOException|RuntimeException ex) {
/* 685 */       zip.data.close();
/* 686 */       throw ex;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/ZipContent$Loader.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */