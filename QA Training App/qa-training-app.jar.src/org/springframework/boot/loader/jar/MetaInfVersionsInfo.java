/*    */ package org.springframework.boot.loader.jar;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import java.util.TreeSet;
/*    */ import java.util.function.IntFunction;
/*    */ import org.springframework.boot.loader.zip.ZipContent;
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
/*    */ final class MetaInfVersionsInfo
/*    */ {
/* 34 */   static final MetaInfVersionsInfo NONE = new MetaInfVersionsInfo(Collections.emptySet());
/*    */   
/*    */   private static final String META_INF_VERSIONS = "META-INF/versions/";
/*    */   
/*    */   private final int[] versions;
/*    */   
/*    */   private final String[] directories;
/*    */   
/*    */   private MetaInfVersionsInfo(Set<Integer> versions) {
/* 43 */     this.versions = versions.stream().mapToInt(Integer::intValue).toArray();
/* 44 */     this.directories = (String[])versions.stream().map(version -> "META-INF/versions/" + version + "/").toArray(x$0 -> new String[x$0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   int[] versions() {
/* 52 */     return this.versions;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   String[] directories() {
/* 60 */     return this.directories;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static MetaInfVersionsInfo get(ZipContent zipContent) {
/* 69 */     Objects.requireNonNull(zipContent); return get(zipContent.size(), zipContent::getEntry);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static MetaInfVersionsInfo get(int size, IntFunction<ZipContent.Entry> entries) {
/* 79 */     Set<Integer> versions = new TreeSet<>();
/* 80 */     for (int i = 0; i < size; i++) {
/* 81 */       ZipContent.Entry contentEntry = entries.apply(i);
/* 82 */       if (contentEntry.hasNameStartingWith("META-INF/versions/") && !contentEntry.isDirectory()) {
/* 83 */         String name = contentEntry.getName();
/* 84 */         int slash = name.indexOf('/', "META-INF/versions/".length());
/* 85 */         if (slash > -1) {
/* 86 */           String version = name.substring("META-INF/versions/".length(), slash);
/*    */           try {
/* 88 */             int versionNumber = Integer.parseInt(version);
/* 89 */             if (versionNumber >= NestedJarFile.BASE_VERSION) {
/* 90 */               versions.add(Integer.valueOf(versionNumber));
/*    */             }
/*    */           }
/* 93 */           catch (NumberFormatException numberFormatException) {}
/*    */         } 
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 99 */     return !versions.isEmpty() ? new MetaInfVersionsInfo(versions) : NONE;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/jar/MetaInfVersionsInfo.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */