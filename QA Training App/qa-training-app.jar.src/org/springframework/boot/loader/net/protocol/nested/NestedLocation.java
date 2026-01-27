/*     */ package org.springframework.boot.loader.net.protocol.nested;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.boot.loader.net.util.UrlDecoder;
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
/*     */ public final class NestedLocation
/*     */   extends Record
/*     */ {
/*     */   private final Path path;
/*     */   private final String nestedEntryName;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lorg/springframework/boot/loader/net/protocol/nested/NestedLocation;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #54	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lorg/springframework/boot/loader/net/protocol/nested/NestedLocation;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lorg/springframework/boot/loader/net/protocol/nested/NestedLocation;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #54	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lorg/springframework/boot/loader/net/protocol/nested/NestedLocation;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lorg/springframework/boot/loader/net/protocol/nested/NestedLocation;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #54	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lorg/springframework/boot/loader/net/protocol/nested/NestedLocation;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Path path() {
/*  54 */     return this.path; } public String nestedEntryName() { return this.nestedEntryName; }
/*     */   
/*  56 */   private static final Map<String, NestedLocation> locationCache = new ConcurrentHashMap<>();
/*     */   
/*  58 */   private static final Map<String, Path> pathCache = new ConcurrentHashMap<>();
/*     */   
/*     */   public NestedLocation(Path path, String nestedEntryName) {
/*  61 */     if (path == null) {
/*  62 */       throw new IllegalArgumentException("'path' must not be null");
/*     */     }
/*  64 */     this.path = path;
/*  65 */     this.nestedEntryName = (nestedEntryName != null && !nestedEntryName.isEmpty()) ? nestedEntryName : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NestedLocation fromUrl(URL url) {
/*  75 */     if (url == null || !"nested".equalsIgnoreCase(url.getProtocol())) {
/*  76 */       throw new IllegalArgumentException("'url' must not be null and must use 'nested' protocol");
/*     */     }
/*  78 */     return parse(UrlDecoder.decode(url.toString().substring(7)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NestedLocation fromUri(URI uri) {
/*  88 */     if (uri == null || !"nested".equalsIgnoreCase(uri.getScheme())) {
/*  89 */       throw new IllegalArgumentException("'uri' must not be null and must use 'nested' scheme");
/*     */     }
/*  91 */     return parse(uri.getSchemeSpecificPart());
/*     */   }
/*     */   
/*     */   static NestedLocation parse(String location) {
/*  95 */     if (location == null || location.isEmpty()) {
/*  96 */       throw new IllegalArgumentException("'location' must not be empty");
/*     */     }
/*  98 */     return locationCache.computeIfAbsent(location, key -> create(location));
/*     */   }
/*     */   
/*     */   private static NestedLocation create(String location) {
/* 102 */     int index = location.lastIndexOf("/!");
/* 103 */     String locationPath = (index != -1) ? location.substring(0, index) : location;
/* 104 */     String nestedEntryName = (index != -1) ? location.substring(index + 2) : null;
/* 105 */     return new NestedLocation(!locationPath.isEmpty() ? asPath(locationPath) : null, nestedEntryName);
/*     */   }
/*     */   
/*     */   private static Path asPath(String locationPath) {
/* 109 */     return pathCache.computeIfAbsent(locationPath, key -> Path.of(!isWindows() ? locationPath : fixWindowsLocationPath(locationPath), new String[0]));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isWindows() {
/* 114 */     return (File.separatorChar == '\\');
/*     */   }
/*     */ 
/*     */   
/*     */   private static String fixWindowsLocationPath(String locationPath) {
/* 119 */     if (locationPath.length() > 2 && locationPath.charAt(2) == ':') {
/* 120 */       return locationPath.substring(1);
/*     */     }
/*     */     
/* 123 */     if (locationPath.startsWith("///") && locationPath.charAt(4) == ':') {
/* 124 */       return locationPath.substring(3);
/*     */     }
/* 126 */     return locationPath;
/*     */   }
/*     */   
/*     */   static void clearCache() {
/* 130 */     locationCache.clear();
/* 131 */     pathCache.clear();
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/nested/NestedLocation.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */