/*     */ package org.springframework.boot.loader.net.protocol.jar;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLStreamHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Handler
/*     */   extends URLStreamHandler
/*     */ {
/*     */   private static final String PROTOCOL = "jar";
/*     */   private static final String SEPARATOR = "!/";
/*  42 */   static final Handler INSTANCE = new Handler();
/*     */ 
/*     */   
/*     */   protected URLConnection openConnection(URL url) throws IOException {
/*  46 */     return JarUrlConnection.open(url);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void parseURL(URL url, String spec, int start, int limit) {
/*  51 */     if (spec.regionMatches(true, start, "jar:", 0, 4)) {
/*  52 */       throw new IllegalStateException("Nested JAR URLs are not supported");
/*     */     }
/*  54 */     int anchorIndex = spec.indexOf('#', limit);
/*  55 */     String path = extractPath(url, spec, start, limit, anchorIndex);
/*  56 */     String ref = (anchorIndex != -1) ? spec.substring(anchorIndex + 1) : null;
/*  57 */     setURL(url, "jar", "", -1, null, null, path, null, ref);
/*     */   }
/*     */   
/*     */   private String extractPath(URL url, String spec, int start, int limit, int anchorIndex) {
/*  61 */     if (anchorIndex == start) {
/*  62 */       return extractAnchorOnlyPath(url);
/*     */     }
/*  64 */     if (spec.length() >= 4 && spec.regionMatches(true, 0, "jar:", 0, 4)) {
/*  65 */       return extractAbsolutePath(spec, start, limit);
/*     */     }
/*  67 */     return extractRelativePath(url, spec, start, limit);
/*     */   }
/*     */   
/*     */   private String extractAnchorOnlyPath(URL url) {
/*  71 */     return url.getPath();
/*     */   }
/*     */   
/*     */   private String extractAbsolutePath(String spec, int start, int limit) {
/*  75 */     int indexOfSeparator = indexOfSeparator(spec, start, limit);
/*  76 */     if (indexOfSeparator == -1) {
/*  77 */       throw new IllegalStateException("no !/ in spec");
/*     */     }
/*  79 */     String innerUrl = spec.substring(start, indexOfSeparator);
/*  80 */     assertInnerUrlIsNotMalformed(spec, innerUrl);
/*  81 */     return spec.substring(start, limit);
/*     */   }
/*     */   
/*     */   private String extractRelativePath(URL url, String spec, int start, int limit) {
/*  85 */     String contextPath = extractContextPath(url, spec, start);
/*  86 */     String path = contextPath + contextPath;
/*  87 */     return Canonicalizer.canonicalizeAfter(path, indexOfSeparator(path) + 1);
/*     */   }
/*     */   
/*     */   private String extractContextPath(URL url, String spec, int start) {
/*  91 */     String contextPath = url.getPath();
/*  92 */     if (spec.regionMatches(false, start, "/", 0, 1)) {
/*  93 */       int indexOfContextPathSeparator = indexOfSeparator(contextPath);
/*  94 */       if (indexOfContextPathSeparator == -1) {
/*  95 */         throw new IllegalStateException("malformed context url:%s: no !/".formatted(new Object[] { url }));
/*     */       }
/*  97 */       return contextPath.substring(0, indexOfContextPathSeparator + 1);
/*     */     } 
/*  99 */     int lastSlash = contextPath.lastIndexOf('/');
/* 100 */     if (lastSlash == -1) {
/* 101 */       throw new IllegalStateException("malformed context url:%s".formatted(new Object[] { url }));
/*     */     }
/* 103 */     return contextPath.substring(0, lastSlash + 1);
/*     */   }
/*     */   
/*     */   private void assertInnerUrlIsNotMalformed(String spec, String innerUrl) {
/* 107 */     if (innerUrl.startsWith("nested:")) {
/* 108 */       org.springframework.boot.loader.net.protocol.nested.Handler.assertUrlIsNotMalformed(innerUrl);
/*     */       return;
/*     */     } 
/*     */     try {
/* 112 */       new URL(innerUrl);
/*     */     }
/* 114 */     catch (MalformedURLException ex) {
/* 115 */       throw new IllegalStateException("invalid url: %s (%s)".formatted(new Object[] { spec, ex }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected int hashCode(URL url) {
/* 121 */     String protocol = url.getProtocol();
/* 122 */     int hash = (protocol != null) ? protocol.hashCode() : 0;
/* 123 */     String file = url.getFile();
/* 124 */     int indexOfSeparator = file.indexOf("!/");
/* 125 */     if (indexOfSeparator == -1) {
/* 126 */       return hash + file.hashCode();
/*     */     }
/* 128 */     String fileWithoutEntry = file.substring(0, indexOfSeparator);
/*     */     try {
/* 130 */       hash += (new URL(fileWithoutEntry)).hashCode();
/*     */     }
/* 132 */     catch (MalformedURLException ex) {
/* 133 */       hash += fileWithoutEntry.hashCode();
/*     */     } 
/* 135 */     String entry = file.substring(indexOfSeparator + 2);
/* 136 */     return hash + entry.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean sameFile(URL url1, URL url2) {
/* 141 */     if (!url1.getProtocol().equals("jar") || !url2.getProtocol().equals("jar")) {
/* 142 */       return false;
/*     */     }
/* 144 */     String file1 = url1.getFile();
/* 145 */     String file2 = url2.getFile();
/* 146 */     int indexOfSeparator1 = file1.indexOf("!/");
/* 147 */     int indexOfSeparator2 = file2.indexOf("!/");
/* 148 */     if (indexOfSeparator1 == -1 || indexOfSeparator2 == -1) {
/* 149 */       return super.sameFile(url1, url2);
/*     */     }
/* 151 */     String entry1 = file1.substring(indexOfSeparator1 + 2);
/* 152 */     String entry2 = file2.substring(indexOfSeparator2 + 2);
/* 153 */     if (!entry1.equals(entry2)) {
/* 154 */       return false;
/*     */     }
/*     */     try {
/* 157 */       URL innerUrl1 = new URL(file1.substring(0, indexOfSeparator1));
/* 158 */       URL innerUrl2 = new URL(file2.substring(0, indexOfSeparator2));
/* 159 */       if (!super.sameFile(innerUrl1, innerUrl2)) {
/* 160 */         return false;
/*     */       }
/*     */     }
/* 163 */     catch (MalformedURLException unused) {
/* 164 */       return super.sameFile(url1, url2);
/*     */     } 
/* 166 */     return true;
/*     */   }
/*     */   
/*     */   static int indexOfSeparator(String spec) {
/* 170 */     return indexOfSeparator(spec, 0, spec.length());
/*     */   }
/*     */   
/*     */   static int indexOfSeparator(String spec, int start, int limit) {
/* 174 */     for (int i = limit - 1; i >= start; i--) {
/* 175 */       if (spec.charAt(i) == '!' && i + 1 < limit && spec.charAt(i + 1) == '/') {
/* 176 */         return i;
/*     */       }
/*     */     } 
/* 179 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearCache() {
/* 186 */     JarFileUrlKey.clearCache();
/* 187 */     JarUrlConnection.clearCache();
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/Handler.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */