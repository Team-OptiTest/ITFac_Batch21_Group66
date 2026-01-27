/*     */ package org.springframework.boot.loader.launch;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.net.URL;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.nio.file.attribute.PosixFilePermissions;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.boot.loader.net.protocol.jar.JarUrl;
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
/*     */ class JarFileArchive
/*     */   implements Archive
/*     */ {
/*     */   private static final String UNPACK_MARKER = "UNPACK:";
/*  53 */   private static final FileAttribute<?>[] NO_FILE_ATTRIBUTES = (FileAttribute<?>[])new FileAttribute[0];
/*     */   
/*  55 */   private static final FileAttribute<?>[] DIRECTORY_PERMISSION_ATTRIBUTES = asFileAttributes(new PosixFilePermission[] { PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE });
/*     */ 
/*     */   
/*  58 */   private static final FileAttribute<?>[] FILE_PERMISSION_ATTRIBUTES = asFileAttributes(new PosixFilePermission[] { PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE });
/*     */ 
/*     */   
/*  61 */   private static final Path TEMP = Paths.get(System.getProperty("java.io.tmpdir"), new String[0]);
/*     */   
/*     */   private final File file;
/*     */   
/*     */   private final JarFile jarFile;
/*     */   
/*     */   private volatile Path tempUnpackDirectory;
/*     */   
/*     */   JarFileArchive(File file) throws IOException {
/*  70 */     this(file, new JarFile(file));
/*     */   }
/*     */   
/*     */   private JarFileArchive(File file, JarFile jarFile) {
/*  74 */     this.file = file;
/*  75 */     this.jarFile = jarFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public Manifest getManifest() throws IOException {
/*  80 */     return this.jarFile.getManifest();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<URL> getClassPathUrls(Predicate<Archive.Entry> includeFilter, Predicate<Archive.Entry> directorySearchFilter) throws IOException {
/*  86 */     return (Set<URL>)this.jarFile.stream()
/*  87 */       .<Archive.Entry>map(JarArchiveEntry::new)
/*  88 */       .filter(includeFilter)
/*  89 */       .map(this::getNestedJarUrl)
/*  90 */       .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
/*     */   }
/*     */   
/*     */   private URL getNestedJarUrl(JarArchiveEntry archiveEntry) {
/*     */     try {
/*  95 */       JarEntry jarEntry = archiveEntry.jarEntry();
/*  96 */       String comment = jarEntry.getComment();
/*  97 */       if (comment != null && comment.startsWith("UNPACK:")) {
/*  98 */         return getUnpackedNestedJarUrl(jarEntry);
/*     */       }
/* 100 */       return JarUrl.create(this.file, jarEntry);
/*     */     }
/* 102 */     catch (IOException ex) {
/* 103 */       throw new UncheckedIOException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private URL getUnpackedNestedJarUrl(JarEntry jarEntry) throws IOException {
/* 108 */     String name = jarEntry.getName();
/* 109 */     if (name.lastIndexOf('/') != -1) {
/* 110 */       name = name.substring(name.lastIndexOf('/') + 1);
/*     */     }
/* 112 */     Path path = getTempUnpackDirectory().resolve(name);
/* 113 */     if (!Files.exists(path, new java.nio.file.LinkOption[0]) || Files.size(path) != jarEntry.getSize()) {
/* 114 */       unpack(jarEntry, path);
/*     */     }
/* 116 */     return path.toUri().toURL();
/*     */   }
/*     */   
/*     */   private Path getTempUnpackDirectory() {
/* 120 */     Path tempUnpackDirectory = this.tempUnpackDirectory;
/* 121 */     if (tempUnpackDirectory != null) {
/* 122 */       return tempUnpackDirectory;
/*     */     }
/* 124 */     synchronized (TEMP) {
/* 125 */       tempUnpackDirectory = this.tempUnpackDirectory;
/* 126 */       if (tempUnpackDirectory == null) {
/* 127 */         tempUnpackDirectory = createUnpackDirectory(TEMP);
/* 128 */         this.tempUnpackDirectory = tempUnpackDirectory;
/*     */       } 
/*     */     } 
/* 131 */     return tempUnpackDirectory;
/*     */   }
/*     */   
/*     */   private Path createUnpackDirectory(Path parent) {
/* 135 */     int attempts = 0;
/* 136 */     String fileName = Paths.get(this.jarFile.getName(), new String[0]).getFileName().toString();
/* 137 */     while (attempts++ < 100) {
/* 138 */       Path unpackDirectory = parent.resolve(fileName + "-spring-boot-libs-" + fileName);
/*     */       try {
/* 140 */         createDirectory(unpackDirectory);
/* 141 */         return unpackDirectory;
/*     */       }
/* 143 */       catch (IOException iOException) {}
/*     */     } 
/*     */ 
/*     */     
/* 147 */     throw new IllegalStateException("Failed to create unpack directory in directory '" + parent + "'");
/*     */   }
/*     */   
/*     */   private void createDirectory(Path path) throws IOException {
/* 151 */     Files.createDirectory(path, getFileAttributes(path, DIRECTORY_PERMISSION_ATTRIBUTES));
/*     */   }
/*     */   
/*     */   private void unpack(JarEntry entry, Path path) throws IOException {
/* 155 */     createFile(path);
/* 156 */     path.toFile().deleteOnExit();
/* 157 */     InputStream in = this.jarFile.getInputStream(entry); 
/* 158 */     try { Files.copy(in, path, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 159 */       if (in != null) in.close();  } catch (Throwable throwable) { if (in != null)
/*     */         try { in.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 163 */      } private void createFile(Path path) throws IOException { Files.createFile(path, getFileAttributes(path, FILE_PERMISSION_ATTRIBUTES)); }
/*     */ 
/*     */   
/*     */   private FileAttribute<?>[] getFileAttributes(Path path, FileAttribute<?>[] permissionAttributes) {
/* 167 */     return !supportsPosix(path.getFileSystem()) ? NO_FILE_ATTRIBUTES : permissionAttributes;
/*     */   }
/*     */   
/*     */   private boolean supportsPosix(FileSystem fileSystem) {
/* 171 */     return fileSystem.supportedFileAttributeViews().contains("posix");
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 176 */     this.jarFile.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 181 */     return this.file.toString();
/*     */   }
/*     */   
/*     */   private static FileAttribute<?>[] asFileAttributes(PosixFilePermission... permissions) {
/* 185 */     return (FileAttribute<?>[])new FileAttribute[] { PosixFilePermissions.asFileAttribute(Set.of(permissions)) };
/*     */   }
/*     */   
/*     */   private static final class JarArchiveEntry extends Record implements Archive.Entry {
/*     */     private final JarEntry jarEntry;
/*     */     
/* 191 */     private JarArchiveEntry(JarEntry jarEntry) { this.jarEntry = jarEntry; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lorg/springframework/boot/loader/launch/JarFileArchive$JarArchiveEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #191	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 191 */       //   0	7	0	this	Lorg/springframework/boot/loader/launch/JarFileArchive$JarArchiveEntry; } public JarEntry jarEntry() { return this.jarEntry; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lorg/springframework/boot/loader/launch/JarFileArchive$JarArchiveEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #191	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lorg/springframework/boot/loader/launch/JarFileArchive$JarArchiveEntry; } public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lorg/springframework/boot/loader/launch/JarFileArchive$JarArchiveEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #191	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lorg/springframework/boot/loader/launch/JarFileArchive$JarArchiveEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     } public String name() {
/* 195 */       return this.jarEntry.getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDirectory() {
/* 200 */       return this.jarEntry.isDirectory();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/launch/JarFileArchive.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */