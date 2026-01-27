/*    */ package org.springframework.boot.loader.zip;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.ByteOrder;
/*    */ import org.springframework.boot.loader.log.DebugLogger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class Zip64EndOfCentralDirectoryRecord
/*    */   extends Record
/*    */ {
/*    */   private final long size;
/*    */   private final long sizeOfZip64EndOfCentralDirectoryRecord;
/*    */   private final short versionMadeBy;
/*    */   private final short versionNeededToExtract;
/*    */   private final int numberOfThisDisk;
/*    */   private final int diskWhereCentralDirectoryStarts;
/*    */   private final long numberOfCentralDirectoryEntriesOnThisDisk;
/*    */   private final long totalNumberOfCentralDirectoryEntries;
/*    */   private final long sizeOfCentralDirectory;
/*    */   private final long offsetToStartOfCentralDirectory;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lorg/springframework/boot/loader/zip/Zip64EndOfCentralDirectoryRecord;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #46	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lorg/springframework/boot/loader/zip/Zip64EndOfCentralDirectoryRecord;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lorg/springframework/boot/loader/zip/Zip64EndOfCentralDirectoryRecord;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #46	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lorg/springframework/boot/loader/zip/Zip64EndOfCentralDirectoryRecord;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lorg/springframework/boot/loader/zip/Zip64EndOfCentralDirectoryRecord;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #46	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lorg/springframework/boot/loader/zip/Zip64EndOfCentralDirectoryRecord;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   Zip64EndOfCentralDirectoryRecord(long size, long sizeOfZip64EndOfCentralDirectoryRecord, short versionMadeBy, short versionNeededToExtract, int numberOfThisDisk, int diskWhereCentralDirectoryStarts, long numberOfCentralDirectoryEntriesOnThisDisk, long totalNumberOfCentralDirectoryEntries, long sizeOfCentralDirectory, long offsetToStartOfCentralDirectory) {
/* 46 */     this.size = size; this.sizeOfZip64EndOfCentralDirectoryRecord = sizeOfZip64EndOfCentralDirectoryRecord; this.versionMadeBy = versionMadeBy; this.versionNeededToExtract = versionNeededToExtract; this.numberOfThisDisk = numberOfThisDisk; this.diskWhereCentralDirectoryStarts = diskWhereCentralDirectoryStarts; this.numberOfCentralDirectoryEntriesOnThisDisk = numberOfCentralDirectoryEntriesOnThisDisk; this.totalNumberOfCentralDirectoryEntries = totalNumberOfCentralDirectoryEntries; this.sizeOfCentralDirectory = sizeOfCentralDirectory; this.offsetToStartOfCentralDirectory = offsetToStartOfCentralDirectory; } public long size() { return this.size; } public long sizeOfZip64EndOfCentralDirectoryRecord() { return this.sizeOfZip64EndOfCentralDirectoryRecord; } public short versionMadeBy() { return this.versionMadeBy; } public short versionNeededToExtract() { return this.versionNeededToExtract; } public int numberOfThisDisk() { return this.numberOfThisDisk; } public int diskWhereCentralDirectoryStarts() { return this.diskWhereCentralDirectoryStarts; } public long numberOfCentralDirectoryEntriesOnThisDisk() { return this.numberOfCentralDirectoryEntriesOnThisDisk; } public long totalNumberOfCentralDirectoryEntries() { return this.totalNumberOfCentralDirectoryEntries; } public long sizeOfCentralDirectory() { return this.sizeOfCentralDirectory; } public long offsetToStartOfCentralDirectory() { return this.offsetToStartOfCentralDirectory; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   private static final DebugLogger debug = DebugLogger.get(Zip64EndOfCentralDirectoryRecord.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final int SIGNATURE = 101075792;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final int MINIMUM_SIZE = 56;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static Zip64EndOfCentralDirectoryRecord load(DataBlock dataBlock, Zip64EndOfCentralDirectoryLocator locator) throws IOException {
/* 68 */     if (locator == null) {
/* 69 */       return null;
/*    */     }
/* 71 */     ByteBuffer buffer = ByteBuffer.allocate(56);
/* 72 */     buffer.order(ByteOrder.LITTLE_ENDIAN);
/* 73 */     long size = locator.pos() - locator.offsetToZip64EndOfCentralDirectoryRecord();
/* 74 */     long pos = locator.pos() - size;
/* 75 */     debug.log("Loading Zip64EndOfCentralDirectoryRecord from position %s size %s", Long.valueOf(pos), Long.valueOf(size));
/* 76 */     dataBlock.readFully(buffer, pos);
/* 77 */     buffer.rewind();
/* 78 */     int signature = buffer.getInt();
/* 79 */     if (signature != 101075792) {
/* 80 */       debug.log("Found incorrect Zip64EndOfCentralDirectoryRecord signature %s at position %s", Integer.valueOf(signature), Long.valueOf(pos));
/* 81 */       throw new IOException("Zip64 'End Of Central Directory Record' not found at position " + pos + ". Zip file is corrupt or includes prefixed bytes which are not supported with Zip64 files");
/*    */     } 
/*    */     
/* 84 */     return new Zip64EndOfCentralDirectoryRecord(size, buffer.getLong(), buffer.getShort(), buffer.getShort(), buffer
/* 85 */         .getInt(), buffer.getInt(), buffer.getLong(), buffer.getLong(), buffer.getLong(), buffer
/* 86 */         .getLong());
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/Zip64EndOfCentralDirectoryRecord.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */