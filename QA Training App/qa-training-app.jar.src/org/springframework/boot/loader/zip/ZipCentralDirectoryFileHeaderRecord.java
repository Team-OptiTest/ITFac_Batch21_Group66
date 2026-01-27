/*     */ package org.springframework.boot.loader.zip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.time.ZoneId;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.temporal.ChronoField;
/*     */ import java.time.temporal.ChronoUnit;
/*     */ import java.time.temporal.ValueRange;
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
/*     */ final class ZipCentralDirectoryFileHeaderRecord
/*     */   extends Record
/*     */ {
/*     */   private final short versionMadeBy;
/*     */   private final short versionNeededToExtract;
/*     */   private final short generalPurposeBitFlag;
/*     */   private final short compressionMethod;
/*     */   private final short lastModFileTime;
/*     */   private final short lastModFileDate;
/*     */   private final int crc32;
/*     */   private final int compressedSize;
/*     */   private final int uncompressedSize;
/*     */   private final short fileNameLength;
/*     */   private final short extraFieldLength;
/*     */   private final short fileCommentLength;
/*     */   private final short diskNumberStart;
/*     */   private final short internalFileAttributes;
/*     */   private final int externalFileAttributes;
/*     */   private final int offsetToLocalHeader;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lorg/springframework/boot/loader/zip/ZipCentralDirectoryFileHeaderRecord;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #54	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lorg/springframework/boot/loader/zip/ZipCentralDirectoryFileHeaderRecord;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lorg/springframework/boot/loader/zip/ZipCentralDirectoryFileHeaderRecord;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #54	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lorg/springframework/boot/loader/zip/ZipCentralDirectoryFileHeaderRecord;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lorg/springframework/boot/loader/zip/ZipCentralDirectoryFileHeaderRecord;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #54	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lorg/springframework/boot/loader/zip/ZipCentralDirectoryFileHeaderRecord;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   ZipCentralDirectoryFileHeaderRecord(short versionMadeBy, short versionNeededToExtract, short generalPurposeBitFlag, short compressionMethod, short lastModFileTime, short lastModFileDate, int crc32, int compressedSize, int uncompressedSize, short fileNameLength, short extraFieldLength, short fileCommentLength, short diskNumberStart, short internalFileAttributes, int externalFileAttributes, int offsetToLocalHeader) {
/*  54 */     this.versionMadeBy = versionMadeBy; this.versionNeededToExtract = versionNeededToExtract; this.generalPurposeBitFlag = generalPurposeBitFlag; this.compressionMethod = compressionMethod; this.lastModFileTime = lastModFileTime; this.lastModFileDate = lastModFileDate; this.crc32 = crc32; this.compressedSize = compressedSize; this.uncompressedSize = uncompressedSize; this.fileNameLength = fileNameLength; this.extraFieldLength = extraFieldLength; this.fileCommentLength = fileCommentLength; this.diskNumberStart = diskNumberStart; this.internalFileAttributes = internalFileAttributes; this.externalFileAttributes = externalFileAttributes; this.offsetToLocalHeader = offsetToLocalHeader; } public short versionMadeBy() { return this.versionMadeBy; } public short versionNeededToExtract() { return this.versionNeededToExtract; } public short generalPurposeBitFlag() { return this.generalPurposeBitFlag; } public short compressionMethod() { return this.compressionMethod; } public short lastModFileTime() { return this.lastModFileTime; } public short lastModFileDate() { return this.lastModFileDate; } public int crc32() { return this.crc32; } public int compressedSize() { return this.compressedSize; } public int uncompressedSize() { return this.uncompressedSize; } public short fileNameLength() { return this.fileNameLength; } public short extraFieldLength() { return this.extraFieldLength; } public short fileCommentLength() { return this.fileCommentLength; } public short diskNumberStart() { return this.diskNumberStart; } public short internalFileAttributes() { return this.internalFileAttributes; } public int externalFileAttributes() { return this.externalFileAttributes; } public int offsetToLocalHeader() { return this.offsetToLocalHeader; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private static final DebugLogger debug = DebugLogger.get(ZipCentralDirectoryFileHeaderRecord.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SIGNATURE = 33639248;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MINIMUM_SIZE = 46;
/*     */ 
/*     */   
/*     */   static final int FILE_NAME_OFFSET = 46;
/*     */ 
/*     */ 
/*     */   
/*     */   long size() {
/*  75 */     return (46 + fileNameLength() + extraFieldLength() + fileCommentLength());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void copyTo(DataBlock dataBlock, long pos, ZipEntry zipEntry) throws IOException {
/*  86 */     int fileNameLength = Short.toUnsignedInt(fileNameLength());
/*  87 */     int extraLength = Short.toUnsignedInt(extraFieldLength());
/*  88 */     int commentLength = Short.toUnsignedInt(fileCommentLength());
/*  89 */     zipEntry.setMethod(Short.toUnsignedInt(compressionMethod()));
/*  90 */     zipEntry.setTime(decodeMsDosFormatDateTime(lastModFileDate(), lastModFileTime()));
/*  91 */     zipEntry.setCrc(Integer.toUnsignedLong(crc32()));
/*  92 */     zipEntry.setCompressedSize(Integer.toUnsignedLong(compressedSize()));
/*  93 */     zipEntry.setSize(Integer.toUnsignedLong(uncompressedSize()));
/*  94 */     if (extraLength > 0) {
/*  95 */       long extraPos = pos + 46L + fileNameLength;
/*  96 */       ByteBuffer buffer = ByteBuffer.allocate(extraLength);
/*  97 */       dataBlock.readFully(buffer, extraPos);
/*  98 */       zipEntry.setExtra(buffer.array());
/*     */     } 
/* 100 */     if (commentLength > 0) {
/* 101 */       long commentPos = pos + 46L + fileNameLength + extraLength;
/* 102 */       zipEntry.setComment(ZipString.readString(dataBlock, commentPos, commentLength));
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
/*     */   private long decodeMsDosFormatDateTime(short date, short time) {
/* 115 */     int year = getChronoValue(((date >> 9 & 0x7F) + 1980), ChronoField.YEAR);
/* 116 */     int month = getChronoValue((date >> 5 & 0xF), ChronoField.MONTH_OF_YEAR);
/* 117 */     int day = getChronoValue((date & 0x1F), ChronoField.DAY_OF_MONTH);
/* 118 */     int hour = getChronoValue((time >> 11 & 0x1F), ChronoField.HOUR_OF_DAY);
/* 119 */     int minute = getChronoValue((time >> 5 & 0x3F), ChronoField.MINUTE_OF_HOUR);
/* 120 */     int second = getChronoValue((time << 1 & 0x3E), ChronoField.SECOND_OF_MINUTE);
/* 121 */     return ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.systemDefault())
/* 122 */       .toInstant()
/* 123 */       .truncatedTo(ChronoUnit.SECONDS)
/* 124 */       .toEpochMilli();
/*     */   }
/*     */   
/*     */   private static int getChronoValue(long value, ChronoField field) {
/* 128 */     ValueRange range = field.range();
/* 129 */     return Math.toIntExact(Math.min(Math.max(value, range.getMinimum()), range.getMaximum()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ZipCentralDirectoryFileHeaderRecord withFileNameLength(short fileNameLength) {
/* 139 */     return (this.fileNameLength != fileNameLength) ? new ZipCentralDirectoryFileHeaderRecord(this.versionMadeBy, this.versionNeededToExtract, this.generalPurposeBitFlag, this.compressionMethod, this.lastModFileTime, this.lastModFileDate, this.crc32, this.compressedSize, this.uncompressedSize, fileNameLength, this.extraFieldLength, this.fileCommentLength, this.diskNumberStart, this.internalFileAttributes, this.externalFileAttributes, this.offsetToLocalHeader) : 
/*     */ 
/*     */ 
/*     */       
/* 143 */       this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ZipCentralDirectoryFileHeaderRecord withOffsetToLocalHeader(int offsetToLocalHeader) {
/* 153 */     return (this.offsetToLocalHeader != offsetToLocalHeader) ? new ZipCentralDirectoryFileHeaderRecord(this.versionMadeBy, this.versionNeededToExtract, this.generalPurposeBitFlag, this.compressionMethod, this.lastModFileTime, this.lastModFileDate, this.crc32, this.compressedSize, this.uncompressedSize, this.fileNameLength, this.extraFieldLength, this.fileCommentLength, this.diskNumberStart, this.internalFileAttributes, this.externalFileAttributes, offsetToLocalHeader) : 
/*     */ 
/*     */ 
/*     */       
/* 157 */       this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] asByteArray() {
/* 165 */     ByteBuffer buffer = ByteBuffer.allocate(46);
/* 166 */     buffer.order(ByteOrder.LITTLE_ENDIAN);
/* 167 */     buffer.putInt(33639248);
/* 168 */     buffer.putShort(this.versionMadeBy);
/* 169 */     buffer.putShort(this.versionNeededToExtract);
/* 170 */     buffer.putShort(this.generalPurposeBitFlag);
/* 171 */     buffer.putShort(this.compressionMethod);
/* 172 */     buffer.putShort(this.lastModFileTime);
/* 173 */     buffer.putShort(this.lastModFileDate);
/* 174 */     buffer.putInt(this.crc32);
/* 175 */     buffer.putInt(this.compressedSize);
/* 176 */     buffer.putInt(this.uncompressedSize);
/* 177 */     buffer.putShort(this.fileNameLength);
/* 178 */     buffer.putShort(this.extraFieldLength);
/* 179 */     buffer.putShort(this.fileCommentLength);
/* 180 */     buffer.putShort(this.diskNumberStart);
/* 181 */     buffer.putShort(this.internalFileAttributes);
/* 182 */     buffer.putInt(this.externalFileAttributes);
/* 183 */     buffer.putInt(this.offsetToLocalHeader);
/* 184 */     return buffer.array();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ZipCentralDirectoryFileHeaderRecord load(DataBlock dataBlock, long pos) throws IOException {
/* 195 */     debug.log("Loading CentralDirectoryFileHeaderRecord from position %s", Long.valueOf(pos));
/* 196 */     ByteBuffer buffer = ByteBuffer.allocate(46);
/* 197 */     buffer.order(ByteOrder.LITTLE_ENDIAN);
/* 198 */     dataBlock.readFully(buffer, pos);
/* 199 */     buffer.rewind();
/* 200 */     int signature = buffer.getInt();
/* 201 */     if (signature != 33639248) {
/* 202 */       debug.log("Found incorrect CentralDirectoryFileHeaderRecord signature %s at position %s", Integer.valueOf(signature), Long.valueOf(pos));
/* 203 */       throw new IOException("Zip 'Central Directory File Header Record' not found at position " + pos);
/*     */     } 
/* 205 */     return new ZipCentralDirectoryFileHeaderRecord(buffer.getShort(), buffer.getShort(), buffer.getShort(), buffer
/* 206 */         .getShort(), buffer.getShort(), buffer.getShort(), buffer.getInt(), buffer.getInt(), buffer
/* 207 */         .getInt(), buffer.getShort(), buffer.getShort(), buffer.getShort(), buffer.getShort(), buffer
/* 208 */         .getShort(), buffer.getInt(), buffer.getInt());
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/ZipCentralDirectoryFileHeaderRecord.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */