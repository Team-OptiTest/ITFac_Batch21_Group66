/*     */ package org.springframework.boot.loader.net.util;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UrlDecoder
/*     */ {
/*     */   public static String decode(String string) {
/*  45 */     int length = string.length();
/*  46 */     if (length == 0 || string.indexOf('%') < 0) {
/*  47 */       return string;
/*     */     }
/*  49 */     StringBuilder result = new StringBuilder(length);
/*  50 */     ByteBuffer byteBuffer = ByteBuffer.allocate(length);
/*  51 */     CharBuffer charBuffer = CharBuffer.allocate(length);
/*     */ 
/*     */     
/*  54 */     CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
/*  55 */     int index = 0;
/*  56 */     while (index < length) {
/*  57 */       char ch = string.charAt(index);
/*  58 */       if (ch != '%') {
/*  59 */         result.append(ch);
/*  60 */         if (index + 1 >= length) {
/*  61 */           return result.toString();
/*     */         }
/*  63 */         index++;
/*     */         continue;
/*     */       } 
/*  66 */       index = fillByteBuffer(byteBuffer, string, index, length);
/*  67 */       decodeToCharBuffer(byteBuffer, charBuffer, decoder);
/*  68 */       result.append(charBuffer.flip());
/*     */     } 
/*     */     
/*  71 */     return result.toString();
/*     */   }
/*     */   
/*     */   private static int fillByteBuffer(ByteBuffer byteBuffer, String string, int index, int length) {
/*  75 */     byteBuffer.clear();
/*     */     do {
/*  77 */       byteBuffer.put(unescape(string, index));
/*  78 */       index += 3;
/*  79 */     } while (index < length && string.charAt(index) == '%');
/*     */ 
/*     */ 
/*     */     
/*  83 */     byteBuffer.flip();
/*  84 */     return index;
/*     */   }
/*     */   
/*     */   private static byte unescape(String string, int index) {
/*     */     try {
/*  89 */       return (byte)Integer.parseInt(string, index + 1, index + 3, 16);
/*     */     }
/*  91 */     catch (NumberFormatException ex) {
/*  92 */       throw new IllegalArgumentException();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void decodeToCharBuffer(ByteBuffer byteBuffer, CharBuffer charBuffer, CharsetDecoder decoder) {
/*  97 */     decoder.reset();
/*  98 */     charBuffer.clear();
/*  99 */     assertNoError(decoder.decode(byteBuffer, charBuffer, true));
/* 100 */     assertNoError(decoder.flush(charBuffer));
/*     */   }
/*     */   
/*     */   private static void assertNoError(CoderResult result) {
/* 104 */     if (result.isError())
/* 105 */       throw new IllegalArgumentException("Error decoding percent encoded characters"); 
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/util/UrlDecoder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */