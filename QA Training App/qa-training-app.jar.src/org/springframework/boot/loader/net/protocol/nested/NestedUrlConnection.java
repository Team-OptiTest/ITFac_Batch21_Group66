/*     */ package org.springframework.boot.loader.net.protocol.nested;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FilePermission;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.lang.ref.Cleaner;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.file.Files;
/*     */ import java.security.Permission;
/*     */ import java.time.Instant;
/*     */ import java.time.ZoneId;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.boot.loader.ref.Cleaner;
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
/*     */ class NestedUrlConnection
/*     */   extends URLConnection
/*     */ {
/*  51 */   private static final DateTimeFormatter RFC_1123_DATE_TIME = DateTimeFormatter.RFC_1123_DATE_TIME
/*  52 */     .withZone(ZoneId.of("GMT"));
/*     */   
/*     */   private static final String CONTENT_TYPE = "x-java/jar";
/*     */   
/*     */   private final NestedUrlConnectionResources resources;
/*     */   
/*     */   private final Cleaner.Cleanable cleanup;
/*     */   
/*  60 */   private long lastModified = -1L;
/*     */   
/*     */   private FilePermission permission;
/*     */   
/*     */   private Map<String, List<String>> headerFields;
/*     */   
/*     */   NestedUrlConnection(URL url) throws MalformedURLException {
/*  67 */     this(url, Cleaner.instance);
/*     */   }
/*     */   
/*     */   NestedUrlConnection(URL url, Cleaner cleaner) throws MalformedURLException {
/*  71 */     super(url);
/*  72 */     NestedLocation location = parseNestedLocation(url);
/*  73 */     this.resources = new NestedUrlConnectionResources(location);
/*  74 */     this.cleanup = cleaner.register(this, this.resources);
/*     */   }
/*     */   
/*     */   private NestedLocation parseNestedLocation(URL url) throws MalformedURLException {
/*     */     try {
/*  79 */       return NestedLocation.fromUrl(url);
/*     */     }
/*  81 */     catch (IllegalArgumentException ex) {
/*  82 */       throw new MalformedURLException(ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHeaderField(String name) {
/*  88 */     List<String> values = getHeaderFields().get(name);
/*  89 */     return (values != null && !values.isEmpty()) ? values.get(0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHeaderField(int n) {
/*  94 */     Map.Entry<String, List<String>> entry = getHeaderEntry(n);
/*  95 */     List<String> values = (entry != null) ? entry.getValue() : null;
/*  96 */     return (values != null && !values.isEmpty()) ? values.get(0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHeaderFieldKey(int n) {
/* 101 */     Map.Entry<String, List<String>> entry = getHeaderEntry(n);
/* 102 */     return (entry != null) ? entry.getKey() : null;
/*     */   }
/*     */   
/*     */   private Map.Entry<String, List<String>> getHeaderEntry(int n) {
/* 106 */     Iterator<Map.Entry<String, List<String>>> iterator = getHeaderFields().entrySet().iterator();
/* 107 */     Map.Entry<String, List<String>> entry = null;
/* 108 */     for (int i = 0; i < n; i++) {
/* 109 */       entry = !iterator.hasNext() ? null : iterator.next();
/*     */     }
/* 111 */     return entry;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, List<String>> getHeaderFields() {
/*     */     try {
/* 117 */       connect();
/*     */     }
/* 119 */     catch (IOException ex) {
/* 120 */       return Collections.emptyMap();
/*     */     } 
/* 122 */     Map<String, List<String>> headerFields = this.headerFields;
/* 123 */     if (headerFields == null) {
/* 124 */       headerFields = new LinkedHashMap<>();
/* 125 */       long contentLength = getContentLengthLong();
/* 126 */       long lastModified = getLastModified();
/* 127 */       if (contentLength > 0L) {
/* 128 */         headerFields.put("content-length", List.of(String.valueOf(contentLength)));
/*     */       }
/* 130 */       if (getLastModified() > 0L) {
/* 131 */         headerFields.put("last-modified", 
/* 132 */             List.of(RFC_1123_DATE_TIME.format(Instant.ofEpochMilli(lastModified))));
/*     */       }
/* 134 */       headerFields = Collections.unmodifiableMap(headerFields);
/* 135 */       this.headerFields = headerFields;
/*     */     } 
/* 137 */     return headerFields;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContentLength() {
/* 142 */     long contentLength = getContentLengthLong();
/* 143 */     return (contentLength <= 2147483647L) ? (int)contentLength : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLengthLong() {
/*     */     try {
/* 149 */       connect();
/* 150 */       return this.resources.getContentLength();
/*     */     }
/* 152 */     catch (IOException ex) {
/* 153 */       return -1L;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 159 */     return "x-java/jar";
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastModified() {
/* 164 */     if (this.lastModified == -1L) {
/*     */       try {
/* 166 */         this.lastModified = Files.getLastModifiedTime(this.resources.getLocation().path(), new java.nio.file.LinkOption[0]).toMillis();
/*     */       }
/* 168 */       catch (IOException ex) {
/* 169 */         this.lastModified = 0L;
/*     */       } 
/*     */     }
/* 172 */     return this.lastModified;
/*     */   }
/*     */ 
/*     */   
/*     */   public Permission getPermission() throws IOException {
/* 177 */     if (this.permission == null) {
/* 178 */       File file = this.resources.getLocation().path().toFile();
/* 179 */       this.permission = new FilePermission(file.getCanonicalPath(), "read");
/*     */     } 
/* 181 */     return this.permission;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 186 */     connect();
/* 187 */     return new ConnectionInputStream(this.resources.getInputStream());
/*     */   }
/*     */ 
/*     */   
/*     */   public void connect() throws IOException {
/* 192 */     if (this.connected) {
/*     */       return;
/*     */     }
/* 195 */     this.resources.connect();
/* 196 */     this.connected = true;
/*     */   }
/*     */ 
/*     */   
/*     */   class ConnectionInputStream
/*     */     extends FilterInputStream
/*     */   {
/*     */     private volatile boolean closing;
/*     */ 
/*     */     
/*     */     ConnectionInputStream(InputStream in) {
/* 207 */       super(in);
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 212 */       if (this.closing) {
/*     */         return;
/*     */       }
/* 215 */       this.closing = true;
/*     */       try {
/* 217 */         super.close();
/*     */       } finally {
/*     */         
/*     */         try {
/* 221 */           NestedUrlConnection.this.cleanup.clean();
/*     */         }
/* 223 */         catch (UncheckedIOException ex) {
/* 224 */           throw ex.getCause();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/nested/NestedUrlConnection.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */