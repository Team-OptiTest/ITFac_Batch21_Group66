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
/*     */ final class ZipDataDescriptorRecord
/*     */   extends Record
/*     */ {
/*     */   private final boolean includeSignature;
/*     */   private final int crc32;
/*     */   private final int compressedSize;
/*     */   private final int uncompressedSize;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lorg/springframework/boot/loader/zip/ZipDataDescriptorRecord;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #36	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lorg/springframework/boot/loader/zip/ZipDataDescriptorRecord;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lorg/springframework/boot/loader/zip/ZipDataDescriptorRecord;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #36	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lorg/springframework/boot/loader/zip/ZipDataDescriptorRecord;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lorg/springframework/boot/loader/zip/ZipDataDescriptorRecord;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #36	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lorg/springframework/boot/loader/zip/ZipDataDescriptorRecord;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   ZipDataDescriptorRecord(boolean includeSignature, int crc32, int compressedSize, int uncompressedSize) {
/*  36 */     this.includeSignature = includeSignature; this.crc32 = crc32; this.compressedSize = compressedSize; this.uncompressedSize = uncompressedSize; } public boolean includeSignature() { return this.includeSignature; } public int crc32() { return this.crc32; } public int compressedSize() { return this.compressedSize; } public int uncompressedSize() { return this.uncompressedSize; }
/*     */   
/*  38 */   private static final DebugLogger debug = DebugLogger.get(ZipDataDescriptorRecord.class);
/*     */   
/*     */   private static final int SIGNATURE = 134695760;
/*     */   
/*     */   private static final int DATA_SIZE = 12;
/*     */   
/*     */   private static final int SIGNATURE_SIZE = 4;
/*     */   
/*     */   long size() {
/*  47 */     return !includeSignature() ? 12L : 16L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] asByteArray() {
/*  55 */     ByteBuffer buffer = ByteBuffer.allocate((int)size());
/*  56 */     buffer.order(ByteOrder.LITTLE_ENDIAN);
/*  57 */     if (this.includeSignature) {
/*  58 */       buffer.putInt(134695760);
/*     */     }
/*  60 */     buffer.putInt(this.crc32);
/*  61 */     buffer.putInt(this.compressedSize);
/*  62 */     buffer.putInt(this.uncompressedSize);
/*  63 */     return buffer.array();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ZipDataDescriptorRecord load(DataBlock dataBlock, long pos) throws IOException {
/*  74 */     debug.log("Loading ZipDataDescriptorRecord from position %s", Long.valueOf(pos));
/*  75 */     ByteBuffer buffer = ByteBuffer.allocate(16);
/*  76 */     buffer.order(ByteOrder.LITTLE_ENDIAN);
/*  77 */     buffer.limit(4);
/*  78 */     dataBlock.readFully(buffer, pos);
/*  79 */     buffer.rewind();
/*  80 */     int signatureOrCrc = buffer.getInt();
/*  81 */     boolean hasSignature = (signatureOrCrc == 134695760);
/*  82 */     buffer.rewind();
/*  83 */     buffer.limit(!hasSignature ? 8 : 12);
/*  84 */     dataBlock.readFully(buffer, pos + 4L);
/*  85 */     buffer.rewind();
/*  86 */     return new ZipDataDescriptorRecord(hasSignature, !hasSignature ? signatureOrCrc : buffer.getInt(), buffer
/*  87 */         .getInt(), buffer.getInt());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isPresentBasedOnFlag(ZipLocalFileHeaderRecord localRecord) {
/*  97 */     return isPresentBasedOnFlag(localRecord.generalPurposeBitFlag());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isPresentBasedOnFlag(ZipCentralDirectoryFileHeaderRecord centralRecord) {
/* 107 */     return isPresentBasedOnFlag(centralRecord.generalPurposeBitFlag());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isPresentBasedOnFlag(int generalPurposeBitFlag) {
/* 117 */     return ((generalPurposeBitFlag & 0x8) != 0);
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/ZipDataDescriptorRecord.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */