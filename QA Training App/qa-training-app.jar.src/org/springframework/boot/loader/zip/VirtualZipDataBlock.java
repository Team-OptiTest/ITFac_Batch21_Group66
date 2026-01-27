/*     */ package org.springframework.boot.loader.zip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class VirtualZipDataBlock
/*     */   extends VirtualDataBlock
/*     */   implements CloseableDataBlock
/*     */ {
/*     */   private final CloseableDataBlock data;
/*     */   
/*     */   VirtualZipDataBlock(CloseableDataBlock data, NameOffsetLookups nameOffsetLookups, ZipCentralDirectoryFileHeaderRecord[] centralRecords, long[] centralRecordPositions) throws IOException {
/*  45 */     this.data = data;
/*  46 */     List<DataBlock> parts = new ArrayList<>();
/*  47 */     List<DataBlock> centralParts = new ArrayList<>();
/*  48 */     long offset = 0L;
/*  49 */     long sizeOfCentralDirectory = 0L;
/*  50 */     for (int i = 0; i < centralRecords.length; i++) {
/*  51 */       ZipCentralDirectoryFileHeaderRecord centralRecord = centralRecords[i];
/*  52 */       int nameOffset = nameOffsetLookups.get(i);
/*  53 */       long centralRecordPos = centralRecordPositions[i];
/*     */ 
/*     */       
/*  56 */       DataBlock name = new DataPart(centralRecordPos + 46L + nameOffset, Short.toUnsignedLong(centralRecord.fileNameLength()) - nameOffset);
/*  57 */       long localRecordPos = Integer.toUnsignedLong(centralRecord.offsetToLocalHeader());
/*  58 */       ZipLocalFileHeaderRecord localRecord = ZipLocalFileHeaderRecord.load(this.data, localRecordPos);
/*  59 */       DataBlock content = new DataPart(localRecordPos + localRecord.size(), centralRecord.compressedSize());
/*  60 */       boolean hasDescriptorRecord = ZipDataDescriptorRecord.isPresentBasedOnFlag(centralRecord);
/*     */       
/*  62 */       ZipDataDescriptorRecord dataDescriptorRecord = !hasDescriptorRecord ? null : ZipDataDescriptorRecord.load(data, localRecordPos + localRecord.size() + content.size());
/*  63 */       sizeOfCentralDirectory += addToCentral(centralParts, centralRecord, centralRecordPos, name, (int)offset);
/*  64 */       offset += addToLocal(parts, centralRecord, localRecord, dataDescriptorRecord, name, content);
/*     */     } 
/*  66 */     parts.addAll(centralParts);
/*  67 */     ZipEndOfCentralDirectoryRecord eocd = new ZipEndOfCentralDirectoryRecord((short)centralRecords.length, (int)sizeOfCentralDirectory, (int)offset);
/*     */     
/*  69 */     parts.add(new ByteArrayDataBlock(eocd.asByteArray()));
/*  70 */     setParts(parts);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long addToCentral(List<DataBlock> parts, ZipCentralDirectoryFileHeaderRecord originalRecord, long originalRecordPos, DataBlock name, int offsetToLocalHeader) throws IOException {
/*  76 */     ZipCentralDirectoryFileHeaderRecord record = originalRecord.withFileNameLength((short)(int)(name.size() & 0xFFFFL)).withOffsetToLocalHeader(offsetToLocalHeader);
/*  77 */     int originalExtraFieldLength = Short.toUnsignedInt(originalRecord.extraFieldLength());
/*  78 */     int originalFileCommentLength = Short.toUnsignedInt(originalRecord.fileCommentLength());
/*  79 */     int extraFieldAndCommentSize = originalExtraFieldLength + originalFileCommentLength;
/*  80 */     parts.add(new ByteArrayDataBlock(record.asByteArray()));
/*  81 */     parts.add(name);
/*  82 */     if (extraFieldAndCommentSize > 0) {
/*  83 */       parts.add(new DataPart(originalRecordPos + originalRecord.size() - extraFieldAndCommentSize, extraFieldAndCommentSize));
/*     */     }
/*     */     
/*  86 */     return record.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long addToLocal(List<DataBlock> parts, ZipCentralDirectoryFileHeaderRecord centralRecord, ZipLocalFileHeaderRecord originalRecord, ZipDataDescriptorRecord dataDescriptorRecord, DataBlock name, DataBlock content) throws IOException {
/*  92 */     ZipLocalFileHeaderRecord record = originalRecord.withFileNameLength((short)(int)(name.size() & 0xFFFFL));
/*  93 */     long originalRecordPos = Integer.toUnsignedLong(centralRecord.offsetToLocalHeader());
/*  94 */     int extraFieldLength = Short.toUnsignedInt(originalRecord.extraFieldLength());
/*  95 */     parts.add(new ByteArrayDataBlock(record.asByteArray()));
/*  96 */     parts.add(name);
/*  97 */     if (extraFieldLength > 0) {
/*  98 */       parts.add(new DataPart(originalRecordPos + originalRecord.size() - extraFieldLength, extraFieldLength));
/*     */     }
/* 100 */     parts.add(content);
/* 101 */     if (dataDescriptorRecord != null) {
/* 102 */       parts.add(new ByteArrayDataBlock(dataDescriptorRecord.asByteArray()));
/*     */     }
/* 104 */     return record.size() + content.size() + ((dataDescriptorRecord != null) ? dataDescriptorRecord.size() : 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 109 */     this.data.close();
/*     */   }
/*     */ 
/*     */   
/*     */   final class DataPart
/*     */     implements DataBlock
/*     */   {
/*     */     private final long offset;
/*     */     
/*     */     private final long size;
/*     */ 
/*     */     
/*     */     DataPart(long offset, long size) {
/* 122 */       this.offset = offset;
/* 123 */       this.size = size;
/*     */     }
/*     */ 
/*     */     
/*     */     public long size() throws IOException {
/* 128 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(ByteBuffer dst, long pos) throws IOException {
/* 133 */       int remaining = (int)(this.size - pos);
/* 134 */       if (remaining <= 0) {
/* 135 */         return -1;
/*     */       }
/* 137 */       int originalLimit = -1;
/* 138 */       if (dst.remaining() > remaining) {
/* 139 */         originalLimit = dst.limit();
/* 140 */         dst.limit(dst.position() + remaining);
/*     */       } 
/* 142 */       int result = VirtualZipDataBlock.this.data.read(dst, this.offset + pos);
/* 143 */       if (originalLimit != -1) {
/* 144 */         dst.limit(originalLimit);
/*     */       }
/* 146 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/VirtualZipDataBlock.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */