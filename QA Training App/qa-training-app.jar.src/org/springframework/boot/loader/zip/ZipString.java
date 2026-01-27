/*     */ package org.springframework.boot.loader.zip;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.charset.StandardCharsets;
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
/*     */ final class ZipString
/*     */ {
/*  37 */   private static final DebugLogger debug = DebugLogger.get(ZipString.class);
/*     */   
/*     */   static final int BUFFER_SIZE = 256;
/*     */   
/*  41 */   private static final int[] INITIAL_BYTE_BITMASK = new int[] { 127, 31, 15, 7 };
/*     */   
/*     */   private static final int SUBSEQUENT_BYTE_BITMASK = 63;
/*     */   
/*  45 */   private static final int EMPTY_HASH = "".hashCode();
/*     */   
/*  47 */   private static final int EMPTY_SLASH_HASH = "/".hashCode();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int hash(CharSequence charSequence, boolean addEndSlash) {
/*  60 */     return hash(0, charSequence, addEndSlash);
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
/*     */   static int hash(int initialHash, CharSequence charSequence, boolean addEndSlash) {
/*  72 */     if (charSequence == null || charSequence.isEmpty()) {
/*  73 */       return !addEndSlash ? EMPTY_HASH : EMPTY_SLASH_HASH;
/*     */     }
/*  75 */     boolean endsWithSlash = (charSequence.charAt(charSequence.length() - 1) == '/');
/*  76 */     int hash = initialHash;
/*  77 */     if (charSequence instanceof String && initialHash == 0) {
/*     */       
/*  79 */       hash = charSequence.hashCode();
/*     */     } else {
/*     */       
/*  82 */       for (int i = 0; i < charSequence.length(); i++) {
/*  83 */         char ch = charSequence.charAt(i);
/*  84 */         hash = 31 * hash + ch;
/*     */       } 
/*     */     } 
/*  87 */     hash = (addEndSlash && !endsWithSlash) ? (31 * hash + 47) : hash;
/*  88 */     debug.log("%s calculated for charsequence '%s' (addEndSlash=%s)", Integer.valueOf(hash), charSequence, Boolean.valueOf(endsWithSlash));
/*  89 */     return hash;
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
/*     */   static int hash(ByteBuffer buffer, DataBlock dataBlock, long pos, int len, boolean addEndSlash) throws IOException {
/* 104 */     if (len == 0) {
/* 105 */       return !addEndSlash ? EMPTY_HASH : EMPTY_SLASH_HASH;
/*     */     }
/* 107 */     buffer = (buffer != null) ? buffer : ByteBuffer.allocate(256);
/* 108 */     byte[] bytes = buffer.array();
/* 109 */     int hash = 0;
/* 110 */     char lastChar = Character.MIN_VALUE;
/* 111 */     int codePointSize = 1;
/* 112 */     while (len > 0) {
/* 113 */       int count = readInBuffer(dataBlock, pos, buffer, len, codePointSize);
/* 114 */       for (int byteIndex = 0; byteIndex < count; ) {
/* 115 */         codePointSize = getCodePointSize(bytes, byteIndex);
/* 116 */         if (!hasEnoughBytes(byteIndex, codePointSize, count)) {
/*     */           break;
/*     */         }
/* 119 */         int codePoint = getCodePoint(bytes, byteIndex, codePointSize);
/* 120 */         if (codePoint <= 65535) {
/* 121 */           lastChar = (char)(codePoint & 0xFFFF);
/* 122 */           hash = 31 * hash + lastChar;
/*     */         } else {
/*     */           
/* 125 */           lastChar = Character.MIN_VALUE;
/* 126 */           hash = 31 * hash + Character.highSurrogate(codePoint);
/* 127 */           hash = 31 * hash + Character.lowSurrogate(codePoint);
/*     */         } 
/* 129 */         byteIndex += codePointSize;
/* 130 */         pos += codePointSize;
/* 131 */         len -= codePointSize;
/* 132 */         codePointSize = 1;
/*     */       } 
/*     */     } 
/* 135 */     hash = (addEndSlash && lastChar != '/') ? (31 * hash + 47) : hash;
/* 136 */     debug.log("%08X calculated for datablock position %s size %s (addEndSlash=%s)", Integer.valueOf(hash), Long.valueOf(pos), Integer.valueOf(len), Boolean.valueOf(addEndSlash));
/* 137 */     return hash;
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
/*     */   static boolean matches(ByteBuffer buffer, DataBlock dataBlock, long pos, int len, CharSequence charSequence, boolean addSlash) {
/* 154 */     if (charSequence.isEmpty()) {
/* 155 */       return true;
/*     */     }
/* 157 */     buffer = (buffer != null) ? buffer : ByteBuffer.allocate(256);
/*     */     try {
/* 159 */       return (compare(buffer, dataBlock, pos, len, charSequence, 
/* 160 */           !addSlash ? CompareType.MATCHES : CompareType.MATCHES_ADDING_SLASH) != -1);
/*     */     }
/* 162 */     catch (IOException ex) {
/* 163 */       throw new UncheckedIOException(ex);
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
/*     */ 
/*     */ 
/*     */   
/*     */   static int startsWith(ByteBuffer buffer, DataBlock dataBlock, long pos, int len, CharSequence charSequence) {
/* 179 */     if (charSequence.isEmpty()) {
/* 180 */       return 0;
/*     */     }
/* 182 */     buffer = (buffer != null) ? buffer : ByteBuffer.allocate(256);
/*     */     try {
/* 184 */       return compare(buffer, dataBlock, pos, len, charSequence, CompareType.STARTS_WITH);
/*     */     }
/* 186 */     catch (IOException ex) {
/* 187 */       throw new UncheckedIOException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static int compare(ByteBuffer buffer, DataBlock dataBlock, long pos, int len, CharSequence charSequence, CompareType compareType) throws IOException {
/* 193 */     if (charSequence.isEmpty()) {
/* 194 */       return 0;
/*     */     }
/* 196 */     boolean addSlash = (compareType == CompareType.MATCHES_ADDING_SLASH && !endsWith(charSequence, '/'));
/* 197 */     int charSequenceIndex = 0;
/* 198 */     int maxCharSequenceLength = !addSlash ? charSequence.length() : (charSequence.length() + 1);
/* 199 */     int result = 0;
/* 200 */     byte[] bytes = buffer.array();
/* 201 */     int codePointSize = 1;
/* 202 */     while (len > 0) {
/* 203 */       int count = readInBuffer(dataBlock, pos, buffer, len, codePointSize);
/* 204 */       for (int byteIndex = 0; byteIndex < count; ) {
/* 205 */         codePointSize = getCodePointSize(bytes, byteIndex);
/* 206 */         if (!hasEnoughBytes(byteIndex, codePointSize, count)) {
/*     */           break;
/*     */         }
/* 209 */         int codePoint = getCodePoint(bytes, byteIndex, codePointSize);
/* 210 */         if (codePoint <= 65535) {
/* 211 */           char ch = (char)(codePoint & 0xFFFF);
/* 212 */           if (charSequenceIndex >= maxCharSequenceLength || 
/* 213 */             getChar(charSequence, charSequenceIndex++) != ch) {
/* 214 */             return -1;
/*     */           }
/*     */         } else {
/*     */           
/* 218 */           char ch = Character.highSurrogate(codePoint);
/* 219 */           if (charSequenceIndex >= maxCharSequenceLength || 
/* 220 */             getChar(charSequence, charSequenceIndex++) != ch) {
/* 221 */             return -1;
/*     */           }
/* 223 */           ch = Character.lowSurrogate(codePoint);
/* 224 */           if (charSequenceIndex >= charSequence.length() || 
/* 225 */             getChar(charSequence, charSequenceIndex++) != ch) {
/* 226 */             return -1;
/*     */           }
/*     */         } 
/* 229 */         byteIndex += codePointSize;
/* 230 */         pos += codePointSize;
/* 231 */         len -= codePointSize;
/* 232 */         result += codePointSize;
/* 233 */         codePointSize = 1;
/* 234 */         if (compareType == CompareType.STARTS_WITH && charSequenceIndex >= charSequence.length()) {
/* 235 */           return result;
/*     */         }
/*     */       } 
/*     */     } 
/* 239 */     return (charSequenceIndex >= charSequence.length()) ? result : -1;
/*     */   }
/*     */   
/*     */   private static boolean hasEnoughBytes(int byteIndex, int codePointSize, int count) {
/* 243 */     return (byteIndex + codePointSize - 1 < count);
/*     */   }
/*     */   
/*     */   private static boolean endsWith(CharSequence charSequence, char ch) {
/* 247 */     return (!charSequence.isEmpty() && charSequence.charAt(charSequence.length() - 1) == ch);
/*     */   }
/*     */   
/*     */   private static char getChar(CharSequence charSequence, int index) {
/* 251 */     return (index != charSequence.length()) ? charSequence.charAt(index) : '/';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String readString(DataBlock data, long pos, long len) {
/*     */     try {
/* 263 */       if (len > 2147483647L) {
/* 264 */         throw new IllegalStateException("String is too long to read");
/*     */       }
/* 266 */       ByteBuffer buffer = ByteBuffer.allocate((int)len);
/* 267 */       buffer.order(ByteOrder.LITTLE_ENDIAN);
/* 268 */       data.readFully(buffer, pos);
/* 269 */       return new String(buffer.array(), StandardCharsets.UTF_8);
/*     */     }
/* 271 */     catch (IOException ex) {
/* 272 */       throw new UncheckedIOException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static int readInBuffer(DataBlock dataBlock, long pos, ByteBuffer buffer, int maxLen, int minLen) throws IOException {
/* 278 */     buffer.clear();
/* 279 */     if (buffer.remaining() > maxLen) {
/* 280 */       buffer.limit(maxLen);
/*     */     }
/* 282 */     int result = 0;
/* 283 */     while (result < minLen) {
/* 284 */       int count = dataBlock.read(buffer, pos);
/* 285 */       if (count <= 0) {
/* 286 */         throw new EOFException();
/*     */       }
/* 288 */       result += count;
/* 289 */       pos += count;
/*     */     } 
/* 291 */     return result;
/*     */   }
/*     */   
/*     */   private static int getCodePointSize(byte[] bytes, int i) {
/* 295 */     int b = Byte.toUnsignedInt(bytes[i]);
/* 296 */     if ((b & 0x80) == 0) {
/* 297 */       return 1;
/*     */     }
/* 299 */     if ((b & 0xE0) == 192) {
/* 300 */       return 2;
/*     */     }
/* 302 */     if ((b & 0xF0) == 224) {
/* 303 */       return 3;
/*     */     }
/* 305 */     return 4;
/*     */   }
/*     */   
/*     */   private static int getCodePoint(byte[] bytes, int i, int codePointSize) {
/* 309 */     int codePoint = Byte.toUnsignedInt(bytes[i]);
/* 310 */     codePoint &= INITIAL_BYTE_BITMASK[codePointSize - 1];
/* 311 */     for (int j = 1; j < codePointSize; j++) {
/* 312 */       codePoint = (codePoint << 6) + (bytes[i + j] & 0x3F);
/*     */     }
/* 314 */     return codePoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum CompareType
/*     */   {
/* 322 */     MATCHES, MATCHES_ADDING_SLASH, STARTS_WITH;
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/ZipString.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */