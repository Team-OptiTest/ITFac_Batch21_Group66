/*     */ package org.springframework.boot.loader.nio.file;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.nio.file.ClosedFileSystemException;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystemNotFoundException;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.nio.file.WatchService;
/*     */ import java.nio.file.attribute.UserPrincipalLookupService;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
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
/*     */ class NestedFileSystem
/*     */   extends FileSystem
/*     */ {
/*  46 */   private static final Set<String> SUPPORTED_FILE_ATTRIBUTE_VIEWS = Set.of("basic");
/*     */   
/*  48 */   private static final String FILE_SYSTEMS_CLASS_NAME = FileSystems.class.getName();
/*     */   
/*  50 */   private static final Object EXISTING_FILE_SYSTEM = new Object();
/*     */   
/*     */   private final NestedFileSystemProvider provider;
/*     */   
/*     */   private final Path jarPath;
/*     */   
/*     */   private volatile boolean closed;
/*     */   
/*  58 */   private final Map<String, Object> zipFileSystems = new HashMap<>();
/*     */   
/*     */   NestedFileSystem(NestedFileSystemProvider provider, Path jarPath) {
/*  61 */     if (provider == null || jarPath == null) {
/*  62 */       throw new IllegalArgumentException("Provider and JarPath must not be null");
/*     */     }
/*  64 */     this.provider = provider;
/*  65 */     this.jarPath = jarPath;
/*     */   }
/*     */   
/*     */   void installZipFileSystemIfNecessary(String nestedEntryName) {
/*     */     try {
/*     */       boolean seen;
/*  71 */       synchronized (this.zipFileSystems) {
/*  72 */         seen = (this.zipFileSystems.putIfAbsent(nestedEntryName, EXISTING_FILE_SYSTEM) != null);
/*     */       } 
/*  74 */       if (!seen) {
/*  75 */         URI uri = new URI("jar:nested:" + this.jarPath.toUri().getPath() + "/!" + nestedEntryName);
/*  76 */         if (!hasFileSystem(uri)) {
/*  77 */           FileSystem zipFileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
/*  78 */           synchronized (this.zipFileSystems) {
/*  79 */             this.zipFileSystems.put(nestedEntryName, zipFileSystem);
/*     */           }
/*     */         
/*     */         } 
/*     */       } 
/*  84 */     } catch (Exception seen) {
/*     */       Exception exception;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasFileSystem(URI uri) {
/*     */     try {
/*  91 */       FileSystems.getFileSystem(uri);
/*  92 */       return true;
/*     */     }
/*  94 */     catch (FileSystemNotFoundException ex) {
/*  95 */       return isCreatingNewFileSystem();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isCreatingNewFileSystem() {
/* 100 */     StackTraceElement[] stack = Thread.currentThread().getStackTrace();
/* 101 */     if (stack != null) {
/* 102 */       for (StackTraceElement element : stack) {
/* 103 */         if (FILE_SYSTEMS_CLASS_NAME.equals(element.getClassName())) {
/* 104 */           return "newFileSystem".equals(element.getMethodName());
/*     */         }
/*     */       } 
/*     */     }
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileSystemProvider provider() {
/* 113 */     return this.provider;
/*     */   }
/*     */   
/*     */   Path getJarPath() {
/* 117 */     return this.jarPath;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 122 */     if (this.closed) {
/*     */       return;
/*     */     }
/* 125 */     this.closed = true;
/* 126 */     synchronized (this.zipFileSystems) {
/*     */ 
/*     */       
/* 129 */       Objects.requireNonNull(FileSystem.class);
/* 130 */       Objects.requireNonNull(FileSystem.class); this.zipFileSystems.values().stream().filter(FileSystem.class::isInstance).map(FileSystem.class::cast)
/* 131 */         .forEach(this::closeZipFileSystem);
/*     */     } 
/* 133 */     this.provider.removeFileSystem(this);
/*     */   }
/*     */   
/*     */   private void closeZipFileSystem(FileSystem zipFileSystem) {
/*     */     try {
/* 138 */       zipFileSystem.close();
/*     */     }
/* 140 */     catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 147 */     return !this.closed;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadOnly() {
/* 152 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSeparator() {
/* 157 */     return "/!";
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterable<Path> getRootDirectories() {
/* 162 */     assertNotClosed();
/* 163 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterable<FileStore> getFileStores() {
/* 168 */     assertNotClosed();
/* 169 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> supportedFileAttributeViews() {
/* 174 */     assertNotClosed();
/* 175 */     return SUPPORTED_FILE_ATTRIBUTE_VIEWS;
/*     */   }
/*     */ 
/*     */   
/*     */   public Path getPath(String first, String... more) {
/* 180 */     assertNotClosed();
/* 181 */     if (more.length != 0) {
/* 182 */       throw new IllegalArgumentException("Nested paths must contain a single element");
/*     */     }
/* 184 */     return new NestedPath(this, first);
/*     */   }
/*     */ 
/*     */   
/*     */   public PathMatcher getPathMatcher(String syntaxAndPattern) {
/* 189 */     throw new UnsupportedOperationException("Nested paths do not support path matchers");
/*     */   }
/*     */ 
/*     */   
/*     */   public UserPrincipalLookupService getUserPrincipalLookupService() {
/* 194 */     throw new UnsupportedOperationException("Nested paths do not have a user principal lookup service");
/*     */   }
/*     */ 
/*     */   
/*     */   public WatchService newWatchService() throws IOException {
/* 199 */     throw new UnsupportedOperationException("Nested paths do not support the WatchService");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 204 */     if (this == obj) {
/* 205 */       return true;
/*     */     }
/* 207 */     if (obj == null || getClass() != obj.getClass()) {
/* 208 */       return false;
/*     */     }
/* 210 */     NestedFileSystem other = (NestedFileSystem)obj;
/* 211 */     return this.jarPath.equals(other.jarPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 216 */     return this.jarPath.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 221 */     return this.jarPath.toAbsolutePath().toString();
/*     */   }
/*     */   
/*     */   private void assertNotClosed() {
/* 225 */     if (this.closed)
/* 226 */       throw new ClosedFileSystemException(); 
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/nio/file/NestedFileSystem.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */