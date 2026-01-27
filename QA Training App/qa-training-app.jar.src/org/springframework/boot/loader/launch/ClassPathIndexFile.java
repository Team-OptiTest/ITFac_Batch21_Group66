/*    */ package org.springframework.boot.loader.launch;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import java.nio.file.Files;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Collectors;
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
/*    */ 
/*    */ final class ClassPathIndexFile
/*    */ {
/*    */   private final File root;
/*    */   private final Set<String> lines;
/*    */   
/*    */   private ClassPathIndexFile(File root, List<String> lines) {
/* 42 */     this.root = root;
/* 43 */     this.lines = (Set<String>)lines.stream().map(this::extractName).collect(Collectors.toCollection(java.util.LinkedHashSet::new));
/*    */   }
/*    */   
/*    */   private String extractName(String line) {
/* 47 */     if (line.startsWith("- \"") && line.endsWith("\"")) {
/* 48 */       return line.substring(3, line.length() - 1);
/*    */     }
/* 50 */     throw new IllegalStateException("Malformed classpath index line [" + line + "]");
/*    */   }
/*    */   
/*    */   int size() {
/* 54 */     return this.lines.size();
/*    */   }
/*    */   
/*    */   boolean containsEntry(String name) {
/* 58 */     if (name == null || name.isEmpty()) {
/* 59 */       return false;
/*    */     }
/* 61 */     return this.lines.contains(name);
/*    */   }
/*    */   
/*    */   List<URL> getUrls() {
/* 65 */     return this.lines.stream().map(this::asUrl).toList();
/*    */   }
/*    */   
/*    */   private URL asUrl(String line) {
/*    */     try {
/* 70 */       return (new File(this.root, line)).toURI().toURL();
/*    */     }
/* 72 */     catch (MalformedURLException ex) {
/* 73 */       throw new IllegalStateException(ex);
/*    */     } 
/*    */   }
/*    */   
/*    */   static ClassPathIndexFile loadIfPossible(File root, String location) throws IOException {
/* 78 */     return loadIfPossible(root, new File(root, location));
/*    */   }
/*    */   
/*    */   private static ClassPathIndexFile loadIfPossible(File root, File indexFile) throws IOException {
/* 82 */     if (indexFile.exists() && indexFile.isFile()) {
/*    */ 
/*    */ 
/*    */       
/* 86 */       List<String> lines = Files.readAllLines(indexFile.toPath()).stream().filter(ClassPathIndexFile::lineHasText).toList();
/* 87 */       return new ClassPathIndexFile(root, lines);
/*    */     } 
/* 89 */     return null;
/*    */   }
/*    */   
/*    */   private static boolean lineHasText(String line) {
/* 93 */     return !line.trim().isEmpty();
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/launch/ClassPathIndexFile.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */