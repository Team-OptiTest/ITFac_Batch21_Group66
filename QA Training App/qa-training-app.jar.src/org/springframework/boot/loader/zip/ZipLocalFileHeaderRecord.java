/*     */ package org.springframework.boot.loader.zip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import org.springframework.boot.loader.log.DebugLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ZipLocalFileHeaderRecord
/*     */   extends Record
/*     */ {
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
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lorg/springframework/boot/loader/zip/ZipLocalFileHeaderRecord;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #42	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lorg/springframework/boot/loader/zip/ZipLocalFileHeaderRecord;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lorg/springframework/boot/loader/zip/ZipLocalFileHeaderRecord;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #42	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lorg/springframework/boot/loader/zip/ZipLocalFileHeaderRecord;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lorg/springframework/boot/loader/zip/ZipLocalFileHeaderRecord;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #42	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lorg/springframework/boot/loader/zip/ZipLocalFileHeaderRecord;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   ZipLocalFileHeaderRecord(short versionNeededToExtract, short generalPurposeBitFlag, short compressionMethod, short lastModFileTime, short lastModFileDate, int crc32, int compressedSize, int uncompressedSize, short fileNameLength, short extraFieldLength) {
/*  42 */     this.versionNeededToExtract = versionNeededToExtract; this.generalPurposeBitFlag = generalPurposeBitFlag; this.compressionMethod = compressionMethod; this.lastModFileTime = lastModFileTime; this.lastModFileDate = lastModFileDate; this.crc32 = crc32; this.compressedSize = compressedSize; this.uncompressedSize = uncompressedSize; this.fileNameLength = fileNameLength; this.extraFieldLength = extraFieldLength; } public short versionNeededToExtract() { return this.versionNeededToExtract; } public short generalPurposeBitFlag() { return this.generalPurposeBitFlag; } public short compressionMethod() { return this.compressionMethod; } public short lastModFileTime() { return this.lastModFileTime; } public short lastModFileDate() { return this.lastModFileDate; } public int crc32() { return this.crc32; } public int compressedSize() { return this.compressedSize; } public int uncompressedSize() { return this.uncompressedSize; } public short fileNameLength() { return this.fileNameLength; } public short extraFieldLength() { return this.extraFieldLength; }
/*     */ 
/*     */ 
/*     */   
/*  46 */   private static final DebugLogger debug = DebugLogger.get(ZipLocalFileHeaderRecord.class);
/*     */ 
/*     */   
/*     */   private static final int SIGNATURE = 67324752;
/*     */ 
/*     */   
/*     */   private static final int MINIMUM_SIZE = 30;
/*     */ 
/*     */ 
/*     */   
/*     */   long size() {
/*  57 */     return (30 + fileNameLength() + extraFieldLength());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ZipLocalFileHeaderRecord withExtraFieldLength(short extraFieldLength) {
/*  67 */     return new ZipLocalFileHeaderRecord(this.versionNeededToExtract, this.generalPurposeBitFlag, this.compressionMethod, this.lastModFileTime, this.lastModFileDate, this.crc32, this.compressedSize, this.uncompressedSize, this.fileNameLength, extraFieldLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ZipLocalFileHeaderRecord withFileNameLength(short fileNameLength) {
/*  78 */     return new ZipLocalFileHeaderRecord(this.versionNeededToExtract, this.generalPurposeBitFlag, this.compressionMethod, this.lastModFileTime, this.lastModFileDate, this.crc32, this.compressedSize, this.uncompressedSize, fileNameLength, this.extraFieldLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] asByteArray() {
/*  88 */     ByteBuffer buffer = ByteBuffer.allocate(30);
/*  89 */     buffer.order(ByteOrder.LITTLE_ENDIAN);
/*  90 */     buffer.putInt(67324752);
/*  91 */     buffer.putShort(this.versionNeededToExtract);
/*  92 */     buffer.putShort(this.generalPurposeBitFlag);
/*  93 */     buffer.putShort(this.compressionMethod);
/*  94 */     buffer.putShort(this.lastModFileTime);
/*  95 */     buffer.putShort(this.lastModFileDate);
/*  96 */     buffer.putInt(this.crc32);
/*  97 */     buffer.putInt(this.compressedSize);
/*  98 */     buffer.putInt(this.uncompressedSize);
/*  99 */     buffer.putShort(this.fileNameLength);
/* 100 */     buffer.putShort(this.extraFieldLength);
/* 101 */     return buffer.array();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ZipLocalFileHeaderRecord load(DataBlock dataBlock, long pos) throws IOException {
/* 112 */     debug.log("Loading LocalFileHeaderRecord from position %s", Long.valueOf(pos));
/* 113 */     ByteBuffer buffer = ByteBuffer.allocate(30);
/* 114 */     buffer.order(ByteOrder.LITTLE_ENDIAN);
/* 115 */     dataBlock.readFully(buffer, pos);
/* 116 */     buffer.rewind();
/* 117 */     if (buffer.getInt() != 67324752) {
/* 118 */       throw new IOException("Zip 'Local File Header Record' not found at position " + pos);
/*     */     }
/* 120 */     return new ZipLocalFileHeaderRecord(buffer.getShort(), buffer.getShort(), buffer.getShort(), buffer.getShort(), buffer
/* 121 */         .getShort(), buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getShort(), buffer
/* 122 */         .getShort());
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/ZipLocalFileHeaderRecord.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */