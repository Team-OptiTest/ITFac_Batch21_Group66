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
/*    */ final class Zip64EndOfCentralDirectoryLocator
/*    */   extends Record
/*    */ {
/*    */   private final long pos;
/*    */   private final int numberOfThisDisk;
/*    */   private final long offsetToZip64EndOfCentralDirectoryRecord;
/*    */   private final int totalNumberOfDisks;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lorg/springframework/boot/loader/zip/Zip64EndOfCentralDirectoryLocator;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #39	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lorg/springframework/boot/loader/zip/Zip64EndOfCentralDirectoryLocator;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lorg/springframework/boot/loader/zip/Zip64EndOfCentralDirectoryLocator;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #39	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lorg/springframework/boot/loader/zip/Zip64EndOfCentralDirectoryLocator;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lorg/springframework/boot/loader/zip/Zip64EndOfCentralDirectoryLocator;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #39	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lorg/springframework/boot/loader/zip/Zip64EndOfCentralDirectoryLocator;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   Zip64EndOfCentralDirectoryLocator(long pos, int numberOfThisDisk, long offsetToZip64EndOfCentralDirectoryRecord, int totalNumberOfDisks) {
/* 39 */     this.pos = pos; this.numberOfThisDisk = numberOfThisDisk; this.offsetToZip64EndOfCentralDirectoryRecord = offsetToZip64EndOfCentralDirectoryRecord; this.totalNumberOfDisks = totalNumberOfDisks; } public long pos() { return this.pos; } public int numberOfThisDisk() { return this.numberOfThisDisk; } public long offsetToZip64EndOfCentralDirectoryRecord() { return this.offsetToZip64EndOfCentralDirectoryRecord; } public int totalNumberOfDisks() { return this.totalNumberOfDisks; }
/*    */ 
/*    */   
/* 42 */   private static final DebugLogger debug = DebugLogger.get(Zip64EndOfCentralDirectoryLocator.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final int SIGNATURE = 117853008;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static final int SIZE = 20;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static Zip64EndOfCentralDirectoryLocator find(DataBlock dataBlock, long endOfCentralDirectoryPos) throws IOException {
/* 61 */     debug.log("Finding Zip64EndOfCentralDirectoryLocator from EOCD at %s", Long.valueOf(endOfCentralDirectoryPos));
/* 62 */     long pos = endOfCentralDirectoryPos - 20L;
/* 63 */     if (pos < 0L) {
/* 64 */       debug.log("No Zip64EndOfCentralDirectoryLocator due to negative position %s", Long.valueOf(pos));
/* 65 */       return null;
/*    */     } 
/* 67 */     ByteBuffer buffer = ByteBuffer.allocate(20);
/* 68 */     buffer.order(ByteOrder.LITTLE_ENDIAN);
/* 69 */     dataBlock.read(buffer, pos);
/* 70 */     buffer.rewind();
/* 71 */     int signature = buffer.getInt();
/* 72 */     if (signature != 117853008) {
/* 73 */       debug.log("Found incorrect Zip64EndOfCentralDirectoryLocator signature %s at position %s", Integer.valueOf(signature), Long.valueOf(pos));
/* 74 */       return null;
/*    */     } 
/* 76 */     debug.log("Found Zip64EndOfCentralDirectoryLocator at position %s", Long.valueOf(pos));
/* 77 */     return new Zip64EndOfCentralDirectoryLocator(pos, buffer.getInt(), buffer.getLong(), buffer.getInt());
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/Zip64EndOfCentralDirectoryLocator.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */