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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ZipEndOfCentralDirectoryRecord
/*     */   extends Record
/*     */ {
/*     */   private final short numberOfThisDisk;
/*     */   private final short diskWhereCentralDirectoryStarts;
/*     */   private final short numberOfCentralDirectoryEntriesOnThisDisk;
/*     */   private final short totalNumberOfCentralDirectoryEntries;
/*     */   private final int sizeOfCentralDirectory;
/*     */   private final int offsetToStartOfCentralDirectory;
/*     */   private final short commentLength;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lorg/springframework/boot/loader/zip/ZipEndOfCentralDirectoryRecord;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #44	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lorg/springframework/boot/loader/zip/ZipEndOfCentralDirectoryRecord;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lorg/springframework/boot/loader/zip/ZipEndOfCentralDirectoryRecord;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #44	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lorg/springframework/boot/loader/zip/ZipEndOfCentralDirectoryRecord;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lorg/springframework/boot/loader/zip/ZipEndOfCentralDirectoryRecord;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #44	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lorg/springframework/boot/loader/zip/ZipEndOfCentralDirectoryRecord;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   ZipEndOfCentralDirectoryRecord(short numberOfThisDisk, short diskWhereCentralDirectoryStarts, short numberOfCentralDirectoryEntriesOnThisDisk, short totalNumberOfCentralDirectoryEntries, int sizeOfCentralDirectory, int offsetToStartOfCentralDirectory, short commentLength) {
/*  44 */     this.numberOfThisDisk = numberOfThisDisk; this.diskWhereCentralDirectoryStarts = diskWhereCentralDirectoryStarts; this.numberOfCentralDirectoryEntriesOnThisDisk = numberOfCentralDirectoryEntriesOnThisDisk; this.totalNumberOfCentralDirectoryEntries = totalNumberOfCentralDirectoryEntries; this.sizeOfCentralDirectory = sizeOfCentralDirectory; this.offsetToStartOfCentralDirectory = offsetToStartOfCentralDirectory; this.commentLength = commentLength; } public short numberOfThisDisk() { return this.numberOfThisDisk; } public short diskWhereCentralDirectoryStarts() { return this.diskWhereCentralDirectoryStarts; } public short numberOfCentralDirectoryEntriesOnThisDisk() { return this.numberOfCentralDirectoryEntriesOnThisDisk; } public short totalNumberOfCentralDirectoryEntries() { return this.totalNumberOfCentralDirectoryEntries; } public int sizeOfCentralDirectory() { return this.sizeOfCentralDirectory; } public int offsetToStartOfCentralDirectory() { return this.offsetToStartOfCentralDirectory; } public short commentLength() { return this.commentLength; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ZipEndOfCentralDirectoryRecord(short totalNumberOfCentralDirectoryEntries, int sizeOfCentralDirectory, int offsetToStartOfCentralDirectory) {
/*  50 */     this((short)0, (short)0, totalNumberOfCentralDirectoryEntries, totalNumberOfCentralDirectoryEntries, sizeOfCentralDirectory, offsetToStartOfCentralDirectory, (short)0);
/*     */   }
/*     */ 
/*     */   
/*  54 */   private static final DebugLogger debug = DebugLogger.get(ZipEndOfCentralDirectoryRecord.class);
/*     */ 
/*     */   
/*     */   private static final int SIGNATURE = 101010256;
/*     */ 
/*     */   
/*     */   private static final int MAXIMUM_COMMENT_LENGTH = 65535;
/*     */ 
/*     */   
/*     */   private static final int MINIMUM_SIZE = 22;
/*     */ 
/*     */   
/*     */   private static final int MAXIMUM_SIZE = 65557;
/*     */ 
/*     */   
/*     */   static final int BUFFER_SIZE = 256;
/*     */ 
/*     */   
/*     */   static final int COMMENT_OFFSET = 22;
/*     */ 
/*     */   
/*     */   long size() {
/*  76 */     return (22 + this.commentLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] asByteArray() {
/*  84 */     ByteBuffer buffer = ByteBuffer.allocate(22);
/*  85 */     buffer.order(ByteOrder.LITTLE_ENDIAN);
/*  86 */     buffer.putInt(101010256);
/*  87 */     buffer.putShort(this.numberOfThisDisk);
/*  88 */     buffer.putShort(this.diskWhereCentralDirectoryStarts);
/*  89 */     buffer.putShort(this.numberOfCentralDirectoryEntriesOnThisDisk);
/*  90 */     buffer.putShort(this.totalNumberOfCentralDirectoryEntries);
/*  91 */     buffer.putInt(this.sizeOfCentralDirectory);
/*  92 */     buffer.putInt(this.offsetToStartOfCentralDirectory);
/*  93 */     buffer.putShort(this.commentLength);
/*  94 */     return buffer.array();
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
/*     */   static Located load(DataBlock dataBlock) throws IOException {
/* 106 */     ByteBuffer buffer = ByteBuffer.allocate(256);
/* 107 */     buffer.order(ByteOrder.LITTLE_ENDIAN);
/* 108 */     long pos = locate(dataBlock, buffer);
/* 109 */     return new Located(pos, new ZipEndOfCentralDirectoryRecord(buffer.getShort(), buffer.getShort(), buffer
/* 110 */           .getShort(), buffer.getShort(), buffer.getInt(), buffer.getInt(), buffer.getShort()));
/*     */   }
/*     */   
/*     */   private static long locate(DataBlock dataBlock, ByteBuffer buffer) throws IOException {
/* 114 */     long endPos = dataBlock.size();
/* 115 */     debug.log("Finding EndOfCentralDirectoryRecord starting at end position %s", Long.valueOf(endPos));
/* 116 */     while (endPos > 0L) {
/* 117 */       buffer.clear();
/* 118 */       long totalRead = dataBlock.size() - endPos;
/* 119 */       if (totalRead > 65557L) {
/* 120 */         throw new IOException("Zip 'End Of Central Directory Record' not found after reading " + totalRead + " bytes");
/*     */       }
/*     */       
/* 123 */       long startPos = endPos - buffer.limit();
/* 124 */       if (startPos < 0L) {
/* 125 */         buffer.limit((int)startPos + buffer.limit());
/* 126 */         startPos = 0L;
/*     */       } 
/* 128 */       debug.log("Finding EndOfCentralDirectoryRecord from %s with limit %s", Long.valueOf(startPos), Integer.valueOf(buffer.limit()));
/* 129 */       dataBlock.readFully(buffer, startPos);
/* 130 */       int offset = findInBuffer(buffer);
/* 131 */       if (offset >= 0) {
/* 132 */         debug.log("Found EndOfCentralDirectoryRecord at %s + %s", Long.valueOf(startPos), Integer.valueOf(offset));
/* 133 */         return startPos + offset;
/*     */       } 
/* 135 */       endPos = endPos - 256L + 22L;
/*     */     } 
/* 137 */     throw new IOException("Zip 'End Of Central Directory Record' not found after reading entire data block");
/*     */   }
/*     */   
/*     */   private static int findInBuffer(ByteBuffer buffer) {
/* 141 */     for (int pos = buffer.limit() - 4; pos >= 0; pos--) {
/* 142 */       buffer.position(pos);
/* 143 */       if (buffer.getInt() == 101010256) {
/* 144 */         return pos;
/*     */       }
/*     */     } 
/* 147 */     return -1;
/*     */   }
/*     */   static final class Located extends Record { private final long pos; private final ZipEndOfCentralDirectoryRecord endOfCentralDirectoryRecord;
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lorg/springframework/boot/loader/zip/ZipEndOfCentralDirectoryRecord$Located;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #156	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lorg/springframework/boot/loader/zip/ZipEndOfCentralDirectoryRecord$Located;
/*     */     }
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lorg/springframework/boot/loader/zip/ZipEndOfCentralDirectoryRecord$Located;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #156	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lorg/springframework/boot/loader/zip/ZipEndOfCentralDirectoryRecord$Located;
/*     */     }
/* 156 */     Located(long pos, ZipEndOfCentralDirectoryRecord endOfCentralDirectoryRecord) { this.pos = pos; this.endOfCentralDirectoryRecord = endOfCentralDirectoryRecord; } public long pos() { return this.pos; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lorg/springframework/boot/loader/zip/ZipEndOfCentralDirectoryRecord$Located;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #156	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lorg/springframework/boot/loader/zip/ZipEndOfCentralDirectoryRecord$Located;
/* 156 */       //   0	8	1	o	Ljava/lang/Object; } public ZipEndOfCentralDirectoryRecord endOfCentralDirectoryRecord() { return this.endOfCentralDirectoryRecord; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/ZipEndOfCentralDirectoryRecord.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */