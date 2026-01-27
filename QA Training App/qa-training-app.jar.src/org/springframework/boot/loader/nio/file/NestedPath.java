/*     */ package org.springframework.boot.loader.nio.file;
/*     */ 
/*     */ import java.io.IOError;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.ProviderMismatchException;
/*     */ import java.nio.file.WatchEvent;
/*     */ import java.nio.file.WatchKey;
/*     */ import java.nio.file.WatchService;
/*     */ import java.util.Objects;
/*     */ import org.springframework.boot.loader.zip.ZipContent;
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
/*     */ final class NestedPath
/*     */   implements Path
/*     */ {
/*     */   private final NestedFileSystem fileSystem;
/*     */   private final String nestedEntryName;
/*     */   private volatile Boolean entryExists;
/*     */   
/*     */   NestedPath(NestedFileSystem fileSystem, String nestedEntryName) {
/*  52 */     if (fileSystem == null) {
/*  53 */       throw new IllegalArgumentException("'filesSystem' must not be null");
/*     */     }
/*  55 */     this.fileSystem = fileSystem;
/*  56 */     this.nestedEntryName = (nestedEntryName != null && !nestedEntryName.isBlank()) ? nestedEntryName : null;
/*     */   }
/*     */   
/*     */   Path getJarPath() {
/*  60 */     return this.fileSystem.getJarPath();
/*     */   }
/*     */   
/*     */   String getNestedEntryName() {
/*  64 */     return this.nestedEntryName;
/*     */   }
/*     */ 
/*     */   
/*     */   public NestedFileSystem getFileSystem() {
/*  69 */     return this.fileSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbsolute() {
/*  74 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Path getRoot() {
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Path getFileName() {
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Path getParent() {
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNameCount() {
/*  94 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Path getName(int index) {
/*  99 */     if (index != 0) {
/* 100 */       throw new IllegalArgumentException("Nested paths only have a single element");
/*     */     }
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Path subpath(int beginIndex, int endIndex) {
/* 107 */     if (beginIndex != 0 || endIndex != 1) {
/* 108 */       throw new IllegalArgumentException("Nested paths only have a single element");
/*     */     }
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean startsWith(Path other) {
/* 115 */     return equals(other);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean endsWith(Path other) {
/* 120 */     return equals(other);
/*     */   }
/*     */ 
/*     */   
/*     */   public Path normalize() {
/* 125 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Path resolve(Path other) {
/* 130 */     throw new UnsupportedOperationException("Unable to resolve nested path");
/*     */   }
/*     */ 
/*     */   
/*     */   public Path relativize(Path other) {
/* 135 */     throw new UnsupportedOperationException("Unable to relativize nested path");
/*     */   }
/*     */ 
/*     */   
/*     */   public URI toUri() {
/*     */     try {
/* 141 */       String uri = "nested:" + this.fileSystem.getJarPath().toUri().getRawPath();
/* 142 */       if (this.nestedEntryName != null) {
/* 143 */         uri = uri + "/!" + uri;
/*     */       }
/* 145 */       return new URI(uri);
/*     */     }
/* 147 */     catch (URISyntaxException ex) {
/* 148 */       throw new IOError(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Path toAbsolutePath() {
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Path toRealPath(LinkOption... options) throws IOException {
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
/* 164 */     throw new UnsupportedOperationException("Nested paths cannot be watched");
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Path other) {
/* 169 */     NestedPath otherNestedPath = cast(other);
/* 170 */     return this.nestedEntryName.compareTo(otherNestedPath.nestedEntryName);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 175 */     if (this == obj) {
/* 176 */       return true;
/*     */     }
/* 178 */     if (obj == null || getClass() != obj.getClass()) {
/* 179 */       return false;
/*     */     }
/* 181 */     NestedPath other = (NestedPath)obj;
/* 182 */     return (Objects.equals(this.fileSystem, other.fileSystem) && 
/* 183 */       Objects.equals(this.nestedEntryName, other.nestedEntryName));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 188 */     return Objects.hash(new Object[] { this.fileSystem, this.nestedEntryName });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 193 */     String string = this.fileSystem.getJarPath().toString();
/* 194 */     if (this.nestedEntryName != null) {
/* 195 */       string = string + string + this.fileSystem.getSeparator();
/*     */     }
/* 197 */     return string;
/*     */   }
/*     */   
/*     */   void assertExists() throws NoSuchFileException {
/* 201 */     if (!Files.isRegularFile(getJarPath(), new LinkOption[0])) {
/* 202 */       throw new NoSuchFileException(toString());
/*     */     }
/* 204 */     Boolean entryExists = this.entryExists;
/* 205 */     if (entryExists == null) {
/*     */       try {
/* 207 */         ZipContent content = ZipContent.open(getJarPath(), this.nestedEntryName); 
/* 208 */         try { entryExists = Boolean.valueOf(true);
/* 209 */           if (content != null) content.close();  } catch (Throwable throwable) { if (content != null)
/*     */             try { content.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; } 
/* 211 */       } catch (IOException ex) {
/* 212 */         entryExists = Boolean.valueOf(false);
/*     */       } 
/* 214 */       this.entryExists = entryExists;
/*     */     } 
/* 216 */     if (!entryExists.booleanValue()) {
/* 217 */       throw new NoSuchFileException(toString());
/*     */     }
/*     */   }
/*     */   
/*     */   static NestedPath cast(Path path) {
/* 222 */     if (path instanceof NestedPath) { NestedPath nestedPath = (NestedPath)path;
/* 223 */       return nestedPath; }
/*     */     
/* 225 */     throw new ProviderMismatchException();
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/nio/file/NestedPath.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */