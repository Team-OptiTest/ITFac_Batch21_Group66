/*     */ package org.springframework.boot.loader.nio.file;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.AccessMode;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystemAlreadyExistsException;
/*     */ import java.nio.file.FileSystemNotFoundException;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NotDirectoryException;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.ReadOnlyFileSystemException;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.boot.loader.net.protocol.nested.NestedLocation;
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
/*     */ public class NestedFileSystemProvider
/*     */   extends FileSystemProvider
/*     */ {
/*  53 */   private Map<Path, NestedFileSystem> fileSystems = new HashMap<>();
/*     */ 
/*     */   
/*     */   public String getScheme() {
/*  57 */     return "nested";
/*     */   }
/*     */ 
/*     */   
/*     */   public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
/*  62 */     NestedLocation location = NestedLocation.fromUri(uri);
/*  63 */     Path jarPath = location.path();
/*  64 */     synchronized (this.fileSystems) {
/*  65 */       if (this.fileSystems.containsKey(jarPath)) {
/*  66 */         throw new FileSystemAlreadyExistsException();
/*     */       }
/*  68 */       NestedFileSystem fileSystem = new NestedFileSystem(this, location.path());
/*  69 */       this.fileSystems.put(location.path(), fileSystem);
/*  70 */       return fileSystem;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public FileSystem getFileSystem(URI uri) {
/*  76 */     NestedLocation location = NestedLocation.fromUri(uri);
/*  77 */     synchronized (this.fileSystems) {
/*  78 */       NestedFileSystem fileSystem = this.fileSystems.get(location.path());
/*  79 */       if (fileSystem == null) {
/*  80 */         throw new FileSystemNotFoundException();
/*     */       }
/*  82 */       return fileSystem;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Path getPath(URI uri) {
/*  88 */     NestedLocation location = NestedLocation.fromUri(uri);
/*  89 */     synchronized (this.fileSystems) {
/*  90 */       NestedFileSystem fileSystem = this.fileSystems.computeIfAbsent(location.path(), path -> new NestedFileSystem(this, path));
/*     */       
/*  92 */       fileSystem.installZipFileSystemIfNecessary(location.nestedEntryName());
/*  93 */       return fileSystem.getPath(location.nestedEntryName(), new String[0]);
/*     */     } 
/*     */   }
/*     */   
/*     */   void removeFileSystem(NestedFileSystem fileSystem) {
/*  98 */     synchronized (this.fileSystems) {
/*  99 */       this.fileSystems.remove(fileSystem.getJarPath());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
/* 106 */     NestedPath nestedPath = NestedPath.cast(path);
/* 107 */     return new NestedByteChannel(nestedPath.getJarPath(), nestedPath.getNestedEntryName());
/*     */   }
/*     */ 
/*     */   
/*     */   public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
/* 112 */     throw new NotDirectoryException(NestedPath.cast(dir).toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
/* 117 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete(Path path) throws IOException {
/* 122 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void copy(Path source, Path target, CopyOption... options) throws IOException {
/* 127 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(Path source, Path target, CopyOption... options) throws IOException {
/* 132 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSameFile(Path path, Path path2) throws IOException {
/* 137 */     return path.equals(path2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHidden(Path path) throws IOException {
/* 142 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileStore getFileStore(Path path) throws IOException {
/* 147 */     NestedPath nestedPath = NestedPath.cast(path);
/* 148 */     nestedPath.assertExists();
/* 149 */     return new NestedFileStore(nestedPath.getFileSystem());
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkAccess(Path path, AccessMode... modes) throws IOException {
/* 154 */     Path jarPath = getJarPath(path);
/* 155 */     jarPath.getFileSystem().provider().checkAccess(jarPath, modes);
/*     */   }
/*     */ 
/*     */   
/*     */   public <V extends java.nio.file.attribute.FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
/* 160 */     Path jarPath = getJarPath(path);
/* 161 */     return jarPath.getFileSystem().provider().getFileAttributeView(jarPath, type, options);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends java.nio.file.attribute.BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
/* 167 */     Path jarPath = getJarPath(path);
/* 168 */     return jarPath.getFileSystem().provider().readAttributes(jarPath, type, options);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
/* 173 */     Path jarPath = getJarPath(path);
/* 174 */     return jarPath.getFileSystem().provider().readAttributes(jarPath, attributes, options);
/*     */   }
/*     */   
/*     */   protected Path getJarPath(Path path) {
/* 178 */     return NestedPath.cast(path).getJarPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
/* 183 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/nio/file/NestedFileSystemProvider.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */