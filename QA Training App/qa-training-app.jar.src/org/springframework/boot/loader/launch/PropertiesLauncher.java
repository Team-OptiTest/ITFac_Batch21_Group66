/*     */ package org.springframework.boot.loader.launch;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLDecoder;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.boot.loader.log.DebugLogger;
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
/*     */ public class PropertiesLauncher
/*     */   extends Launcher
/*     */ {
/*     */   public static final String MAIN = "loader.main";
/*     */   public static final String PATH = "loader.path";
/*     */   public static final String HOME = "loader.home";
/*     */   public static final String ARGS = "loader.args";
/*     */   public static final String CONFIG_NAME = "loader.config.name";
/*     */   public static final String CONFIG_LOCATION = "loader.config.location";
/*     */   public static final String SET_SYSTEM_PROPERTIES = "loader.system";
/* 125 */   private static final URL[] NO_URLS = new URL[0];
/*     */   
/* 127 */   private static final Pattern WORD_SEPARATOR = Pattern.compile("\\W+");
/*     */   
/* 129 */   private static final String NESTED_ARCHIVE_SEPARATOR = "!" + File.separator;
/*     */   
/*     */   private static final String JAR_FILE_PREFIX = "jar:file:";
/*     */   
/* 133 */   private static final DebugLogger debug = DebugLogger.get(PropertiesLauncher.class);
/*     */   
/*     */   private final Archive archive;
/*     */   
/*     */   private final File homeDirectory;
/*     */   
/*     */   private final List<String> paths;
/*     */   
/* 141 */   private final Properties properties = new Properties();
/*     */   
/*     */   public PropertiesLauncher() throws Exception {
/* 144 */     this(Archive.create(Launcher.class));
/*     */   }
/*     */   
/*     */   PropertiesLauncher(Archive archive) throws Exception {
/* 148 */     this.archive = archive;
/* 149 */     this.homeDirectory = getHomeDirectory();
/* 150 */     initializeProperties();
/* 151 */     this.paths = getPaths();
/* 152 */     this.classPathIndex = getClassPathIndex(this.archive);
/*     */   }
/*     */   
/*     */   protected File getHomeDirectory() throws Exception {
/* 156 */     return new File(getPropertyWithDefault("loader.home", "${user.dir}"));
/*     */   }
/*     */   
/*     */   private void initializeProperties() throws Exception {
/* 160 */     List<String> configs = new ArrayList<>();
/* 161 */     if (getProperty("loader.config.location") != null) {
/* 162 */       configs.add(getProperty("loader.config.location"));
/*     */     } else {
/*     */       
/* 165 */       String[] names = getPropertyWithDefault("loader.config.name", "loader").split(",");
/* 166 */       for (String name : names) {
/* 167 */         String propertiesFile = name + ".properties";
/* 168 */         configs.add("file:" + this.homeDirectory + "/" + propertiesFile);
/* 169 */         configs.add("classpath:" + propertiesFile);
/* 170 */         configs.add("classpath:BOOT-INF/classes/" + propertiesFile);
/*     */       } 
/*     */     } 
/* 173 */     for (String config : configs) {
/* 174 */       InputStream resource = getResource(config); 
/* 175 */       try { if (resource == null)
/* 176 */         { debug.log("Not found: %s", config);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 182 */           if (resource != null) resource.close();  continue; }  debug.log("Found: %s", config); loadResource(resource); if (resource != null) resource.close();  return; } catch (Throwable throwable) { if (resource != null)
/*     */           try { resource.close(); }
/*     */           catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */             throw throwable; }
/*     */     
/* 187 */     }  } private InputStream getResource(String config) throws Exception { if (config.startsWith("classpath:")) {
/* 188 */       return getClasspathResource(config.substring("classpath:".length()));
/*     */     }
/* 190 */     config = handleUrl(config);
/* 191 */     if (isUrl(config)) {
/* 192 */       return getURLResource(config);
/*     */     }
/* 194 */     return getFileResource(config); }
/*     */ 
/*     */   
/*     */   private InputStream getClasspathResource(String config) {
/* 198 */     config = stripLeadingSlashes(config);
/* 199 */     config = "/" + config;
/* 200 */     debug.log("Trying classpath: %s", config);
/* 201 */     return getClass().getResourceAsStream(config);
/*     */   }
/*     */   
/*     */   private String handleUrl(String path) {
/* 205 */     if (path.startsWith("jar:file:") || path.startsWith("file:")) {
/* 206 */       path = URLDecoder.decode(path, StandardCharsets.UTF_8);
/* 207 */       if (path.startsWith("file:")) {
/* 208 */         path = path.substring("file:".length());
/* 209 */         if (path.startsWith("//")) {
/* 210 */           path = path.substring(2);
/*     */         }
/*     */       } 
/*     */     } 
/* 214 */     return path;
/*     */   }
/*     */   
/*     */   private boolean isUrl(String config) {
/* 218 */     return config.contains("://");
/*     */   }
/*     */   
/*     */   private InputStream getURLResource(String config) throws Exception {
/* 222 */     URL url = new URL(config);
/* 223 */     if (exists(url)) {
/* 224 */       URLConnection connection = url.openConnection();
/*     */       try {
/* 226 */         return connection.getInputStream();
/*     */       }
/* 228 */       catch (IOException ex) {
/* 229 */         disconnect(connection);
/* 230 */         throw ex;
/*     */       } 
/*     */     } 
/* 233 */     return null;
/*     */   }
/*     */   
/*     */   private boolean exists(URL url) throws IOException {
/* 237 */     URLConnection connection = url.openConnection();
/*     */     try {
/* 239 */       connection.setUseCaches(connection.getClass().getSimpleName().startsWith("JNLP"));
/* 240 */       if (connection instanceof HttpURLConnection) { HttpURLConnection httpConnection = (HttpURLConnection)connection;
/* 241 */         httpConnection.setRequestMethod("HEAD");
/* 242 */         int responseCode = httpConnection.getResponseCode();
/* 243 */         if (responseCode == 200) {
/* 244 */           return true;
/*     */         }
/* 246 */         if (responseCode == 404) {
/* 247 */           return false;
/*     */         } }
/*     */       
/* 250 */       return (connection.getContentLength() >= 0);
/*     */     } finally {
/*     */       
/* 253 */       disconnect(connection);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void disconnect(URLConnection connection) {
/* 258 */     if (connection instanceof HttpURLConnection) { HttpURLConnection httpConnection = (HttpURLConnection)connection;
/* 259 */       httpConnection.disconnect(); }
/*     */   
/*     */   }
/*     */   
/*     */   private InputStream getFileResource(String config) throws Exception {
/* 264 */     File file = new File(config);
/* 265 */     debug.log("Trying file: %s", config);
/* 266 */     return !file.canRead() ? null : new FileInputStream(file);
/*     */   }
/*     */   
/*     */   private void loadResource(InputStream resource) throws Exception {
/* 270 */     this.properties.load(resource);
/* 271 */     resolvePropertyPlaceholders();
/* 272 */     if ("true".equalsIgnoreCase(getProperty("loader.system"))) {
/* 273 */       addToSystemProperties();
/*     */     }
/*     */   }
/*     */   
/*     */   private void resolvePropertyPlaceholders() {
/* 278 */     for (String name : this.properties.stringPropertyNames()) {
/* 279 */       String value = this.properties.getProperty(name);
/* 280 */       String resolved = SystemPropertyUtils.resolvePlaceholders(this.properties, value);
/* 281 */       if (resolved != null) {
/* 282 */         this.properties.put(name, resolved);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addToSystemProperties() {
/* 288 */     debug.log("Adding resolved properties to System properties");
/* 289 */     for (String name : this.properties.stringPropertyNames()) {
/* 290 */       String value = this.properties.getProperty(name);
/* 291 */       System.setProperty(name, value);
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<String> getPaths() throws Exception {
/* 296 */     String path = getProperty("loader.path");
/* 297 */     List<String> paths = (path != null) ? parsePathsProperty(path) : Collections.<String>emptyList();
/* 298 */     debug.log("Nested archive paths: %s", this.paths);
/* 299 */     return paths;
/*     */   }
/*     */   
/*     */   private List<String> parsePathsProperty(String commaSeparatedPaths) {
/* 303 */     List<String> paths = new ArrayList<>();
/* 304 */     for (String path : commaSeparatedPaths.split(",")) {
/* 305 */       path = cleanupPath(path);
/*     */       
/* 307 */       path = path.isEmpty() ? "/" : path;
/* 308 */       paths.add(path);
/*     */     } 
/* 310 */     if (paths.isEmpty()) {
/* 311 */       paths.add("lib");
/*     */     }
/* 313 */     return paths;
/*     */   }
/*     */   
/*     */   private String cleanupPath(String path) {
/* 317 */     path = path.trim();
/*     */     
/* 319 */     if (path.startsWith("./")) {
/* 320 */       path = path.substring(2);
/*     */     }
/* 322 */     if (isArchive(path)) {
/* 323 */       return path;
/*     */     }
/* 325 */     if (path.endsWith("/*")) {
/* 326 */       return path.substring(0, path.length() - 1);
/*     */     }
/*     */     
/* 329 */     return (!path.endsWith("/") && !path.equals(".")) ? (path + "/") : path;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClassLoader createClassLoader(Collection<URL> urls) throws Exception {
/* 334 */     String loaderClassName = getProperty("loader.classLoader");
/* 335 */     if (this.classPathIndex != null) {
/* 336 */       urls = new ArrayList<>(urls);
/* 337 */       urls.addAll(this.classPathIndex.getUrls());
/*     */     } 
/* 339 */     if (loaderClassName == null) {
/* 340 */       return super.createClassLoader(urls);
/*     */     }
/* 342 */     ClassLoader parent = getClass().getClassLoader();
/* 343 */     LaunchedClassLoader launchedClassLoader = new LaunchedClassLoader(false, urls.<URL>toArray(new URL[0]), parent);
/* 344 */     debug.log("Classpath for custom loader: %s", urls);
/* 345 */     ClassLoader classLoader1 = wrapWithCustomClassLoader((ClassLoader)launchedClassLoader, loaderClassName);
/* 346 */     debug.log("Using custom class loader: %s", loaderClassName);
/* 347 */     return classLoader1;
/*     */   }
/*     */   
/*     */   private ClassLoader wrapWithCustomClassLoader(ClassLoader parent, String loaderClassName) throws Exception {
/* 351 */     Instantiator<ClassLoader> instantiator = new Instantiator<>(parent, loaderClassName);
/* 352 */     ClassLoader loader = instantiator.declaredConstructor(new Class[] { ClassLoader.class }).newInstance(new Object[] { parent });
/*     */     
/* 354 */     loader = (loader != null) ? loader : instantiator.declaredConstructor(new Class[] { URL[].class, ClassLoader.class }).newInstance(new Object[] { NO_URLS, parent });
/* 355 */     loader = (loader != null) ? loader : instantiator.constructWithoutParameters();
/* 356 */     if (loader != null) {
/* 357 */       return loader;
/*     */     }
/* 359 */     throw new IllegalStateException("Unable to create class loader for " + loaderClassName);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Archive getArchive() {
/* 364 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getMainClass() throws Exception {
/* 369 */     String mainClass = getProperty("loader.main", "Start-Class");
/* 370 */     if (mainClass == null) {
/* 371 */       throw new IllegalStateException("No '%s' or 'Start-Class' specified".formatted(new Object[] { "loader.main" }));
/*     */     }
/* 373 */     return mainClass;
/*     */   }
/*     */   
/*     */   protected String[] getArgs(String... args) throws Exception {
/* 377 */     String loaderArgs = getProperty("loader.args");
/* 378 */     return (loaderArgs != null) ? merge(loaderArgs.split("\\s+"), args) : args;
/*     */   }
/*     */   
/*     */   private String[] merge(String[] a1, String[] a2) {
/* 382 */     String[] result = new String[a1.length + a2.length];
/* 383 */     System.arraycopy(a1, 0, result, 0, a1.length);
/* 384 */     System.arraycopy(a2, 0, result, a1.length, a2.length);
/* 385 */     return result;
/*     */   }
/*     */   
/*     */   private String getProperty(String name) throws Exception {
/* 389 */     return getProperty(name, (String)null, (String)null);
/*     */   }
/*     */   
/*     */   private String getProperty(String name, String manifestKey) throws Exception {
/* 393 */     return getProperty(name, manifestKey, (String)null);
/*     */   }
/*     */   
/*     */   private String getPropertyWithDefault(String name, String defaultValue) throws Exception {
/* 397 */     return getProperty(name, (String)null, defaultValue);
/*     */   }
/*     */   
/*     */   private String getProperty(String name, String manifestKey, String defaultValue) throws Exception {
/* 401 */     manifestKey = (manifestKey != null) ? manifestKey : toCamelCase(name.replace('.', '-'));
/* 402 */     String value = SystemPropertyUtils.getProperty(name);
/* 403 */     if (value != null) {
/* 404 */       return getResolvedProperty(name, manifestKey, value, "environment");
/*     */     }
/* 406 */     if (this.properties.containsKey(name)) {
/* 407 */       value = this.properties.getProperty(name);
/* 408 */       return getResolvedProperty(name, manifestKey, value, "properties");
/*     */     } 
/*     */     
/* 411 */     if (this.homeDirectory != null) {
/*     */       try {
/* 413 */         ExplodedArchive explodedArchive = new ExplodedArchive(this.homeDirectory); 
/* 414 */         try { value = getManifestValue(explodedArchive, manifestKey);
/* 415 */           if (value != null)
/* 416 */           { String str = getResolvedProperty(name, manifestKey, value, "home directory manifest");
/*     */             
/* 418 */             explodedArchive.close(); return str; }  explodedArchive.close(); } catch (Throwable throwable) { try { explodedArchive.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/*     */       
/* 420 */       } catch (IllegalStateException illegalStateException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 425 */     value = getManifestValue(this.archive, manifestKey);
/* 426 */     if (value != null) {
/* 427 */       return getResolvedProperty(name, manifestKey, value, "manifest");
/*     */     }
/* 429 */     return SystemPropertyUtils.resolvePlaceholders(this.properties, defaultValue);
/*     */   }
/*     */   
/*     */   String getManifestValue(Archive archive, String manifestKey) throws Exception {
/* 433 */     Manifest manifest = archive.getManifest();
/* 434 */     return (manifest != null) ? manifest.getMainAttributes().getValue(manifestKey) : null;
/*     */   }
/*     */   
/*     */   private String getResolvedProperty(String name, String manifestKey, String value, String from) {
/* 438 */     value = SystemPropertyUtils.resolvePlaceholders(this.properties, value);
/* 439 */     String altName = (manifestKey != null && !manifestKey.equals(name)) ? "[%s] ".formatted(new Object[] { manifestKey }) : "";
/* 440 */     debug.log("Property '%s'%s from %s: %s", name, altName, from, value);
/* 441 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   void close() throws Exception {
/* 446 */     if (this.archive != null) {
/* 447 */       this.archive.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public static String toCamelCase(CharSequence string) {
/* 452 */     if (string == null) {
/* 453 */       return null;
/*     */     }
/* 455 */     StringBuilder result = new StringBuilder();
/* 456 */     Matcher matcher = WORD_SEPARATOR.matcher(string);
/* 457 */     int pos = 0;
/* 458 */     while (matcher.find()) {
/* 459 */       result.append(capitalize(string.subSequence(pos, matcher.end()).toString()));
/* 460 */       pos = matcher.end();
/*     */     } 
/* 462 */     result.append(capitalize(string.subSequence(pos, string.length()).toString()));
/* 463 */     return result.toString();
/*     */   }
/*     */   
/*     */   private static String capitalize(String str) {
/* 467 */     return "" + Character.toUpperCase(str.charAt(0)) + Character.toUpperCase(str.charAt(0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Set<URL> getClassPathUrls() throws Exception {
/* 472 */     Set<URL> urls = new LinkedHashSet<>();
/* 473 */     for (String path : getPaths()) {
/* 474 */       path = cleanupPath(handleUrl(path));
/* 475 */       urls.addAll(getClassPathUrlsForPath(path));
/*     */     } 
/* 477 */     urls.addAll(getClassPathUrlsForRoot());
/* 478 */     debug.log("Using class path URLs %s", urls);
/* 479 */     return urls;
/*     */   }
/*     */   
/*     */   private Set<URL> getClassPathUrlsForPath(String path) throws Exception {
/* 483 */     File file = !isAbsolutePath(path) ? new File(this.homeDirectory, path) : new File(path);
/* 484 */     Set<URL> urls = new LinkedHashSet<>();
/* 485 */     if (!"/".equals(path) && 
/* 486 */       file.isDirectory()) {
/* 487 */       ExplodedArchive explodedArchive = new ExplodedArchive(file); 
/* 488 */       try { debug.log("Adding classpath entries from directory %s", file);
/* 489 */         urls.add(file.toURI().toURL());
/* 490 */         urls.addAll(explodedArchive.getClassPathUrls(this::isArchive));
/* 491 */         explodedArchive.close(); } catch (Throwable throwable) { try { explodedArchive.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */          throw throwable; }
/*     */     
/* 494 */     }  if (!file.getPath().contains(NESTED_ARCHIVE_SEPARATOR) && isArchive(file.getName())) {
/* 495 */       debug.log("Adding classpath entries from jar/zip archive %s", path);
/* 496 */       urls.add(file.toURI().toURL());
/*     */     } 
/* 498 */     Set<URL> nested = getClassPathUrlsForNested(path);
/* 499 */     if (!nested.isEmpty()) {
/* 500 */       debug.log("Adding classpath entries from nested %s", path);
/* 501 */       urls.addAll(nested);
/*     */     } 
/* 503 */     return urls;
/*     */   }
/*     */   
/*     */   private Set<URL> getClassPathUrlsForNested(String path) throws Exception {
/* 507 */     boolean isJustArchive = isArchive(path);
/* 508 */     if ((!path.equals("/") && path.startsWith("/")) || (this.archive
/* 509 */       .isExploded() && this.archive.getRootDirectory().equals(this.homeDirectory))) {
/* 510 */       return Collections.emptySet();
/*     */     }
/* 512 */     File file = null;
/* 513 */     if (isJustArchive) {
/* 514 */       File candidate = new File(this.homeDirectory, path);
/* 515 */       if (candidate.exists()) {
/* 516 */         file = candidate;
/* 517 */         path = "";
/*     */       } 
/*     */     } 
/* 520 */     int separatorIndex = path.indexOf('!');
/* 521 */     if (separatorIndex != -1) {
/*     */       
/* 523 */       file = !path.startsWith("jar:file:") ? new File(this.homeDirectory, path.substring(0, separatorIndex)) : new File(path.substring("jar:file:".length(), separatorIndex));
/* 524 */       path = path.substring(separatorIndex + 1);
/* 525 */       path = stripLeadingSlashes(path);
/*     */     } 
/* 527 */     if (path.equals("/") || path.equals("./") || path.equals("."))
/*     */     {
/* 529 */       path = "";
/*     */     }
/* 531 */     Archive archive = (file != null) ? new JarFileArchive(file) : this.archive;
/*     */     try {
/* 533 */       Set<URL> urls = new LinkedHashSet<>(archive.getClassPathUrls(includeByPrefix(path)));
/* 534 */       if (!isJustArchive && file != null && path.isEmpty()) {
/* 535 */         urls.add(JarUrl.create(file));
/*     */       }
/* 537 */       return urls;
/*     */     } finally {
/*     */       
/* 540 */       if (archive != this.archive) {
/* 541 */         archive.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private Set<URL> getClassPathUrlsForRoot() throws Exception {
/* 547 */     debug.log("Adding classpath entries from root archive %s", this.archive);
/* 548 */     return this.archive.getClassPathUrls(this::isIncludedOnClassPathAndNotIndexed, Archive.ALL_ENTRIES);
/*     */   }
/*     */   
/*     */   private Predicate<Archive.Entry> includeByPrefix(String prefix) {
/* 552 */     return entry -> ((entry.isDirectory() && entry.name().equals(prefix)) || (isArchive(entry) && entry.name().startsWith(prefix)));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isArchive(Archive.Entry entry) {
/* 557 */     return isArchive(entry.name());
/*     */   }
/*     */   
/*     */   private boolean isArchive(String name) {
/* 561 */     name = name.toLowerCase(Locale.ENGLISH);
/* 562 */     return (name.endsWith(".jar") || name.endsWith(".zip"));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isAbsolutePath(String root) {
/* 567 */     return (root.contains(":") || root.startsWith("/"));
/*     */   }
/*     */   
/*     */   private String stripLeadingSlashes(String string) {
/* 571 */     while (string.startsWith("/")) {
/* 572 */       string = string.substring(1);
/*     */     }
/* 574 */     return string;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 578 */     PropertiesLauncher launcher = new PropertiesLauncher();
/* 579 */     args = launcher.getArgs(args);
/* 580 */     launcher.launch(args);
/*     */   } private static final class Instantiator<T> extends Record {
/*     */     private final ClassLoader parent; private final Class<?> type; public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #586	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator<TT;>;
/*     */     } public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #586	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator<TT;>;
/* 586 */     } private Instantiator(ClassLoader parent, Class<?> type) { this.parent = parent; this.type = type; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #586	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 586 */       //   0	8	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator<TT;>; } public ClassLoader parent() { return this.parent; } public Class<?> type() { return this.type; }
/*     */     
/*     */     Instantiator(ClassLoader parent, String className) throws ClassNotFoundException {
/* 589 */       this(parent, Class.forName(className, true, parent));
/*     */     }
/*     */     
/*     */     T constructWithoutParameters() throws Exception {
/* 593 */       return declaredConstructor(new Class[0]).newInstance(new Object[0]);
/*     */     }
/*     */     
/*     */     Using<T> declaredConstructor(Class<?>... parameterTypes) {
/* 597 */       return new Using<>(this, parameterTypes);
/*     */     }
/*     */     private static final class Using<T> extends Record { private final PropertiesLauncher.Instantiator<T> instantiator; private final Class<?>[] parameterTypes;
/* 600 */       private Using(PropertiesLauncher.Instantiator<T> instantiator, Class<?>... parameterTypes) { this.instantiator = instantiator; this.parameterTypes = parameterTypes; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #600	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using;
/*     */         // Local variable type table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	7	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using<TT;>; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #600	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using;
/*     */         // Local variable type table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	7	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #600	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */         // Local variable type table:
/*     */         //   start	length	slot	name	signature
/* 600 */         //   0	8	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using<TT;>; } public PropertiesLauncher.Instantiator<T> instantiator() { return this.instantiator; } public Class<?>[] parameterTypes() { return this.parameterTypes; }
/*     */ 
/*     */       
/*     */       T newInstance(Object... initargs) throws Exception {
/*     */         
/* 605 */         try { Constructor<?> constructor = this.instantiator.type().getDeclaredConstructor(this.parameterTypes);
/* 606 */           constructor.setAccessible(true);
/* 607 */           return (T)constructor.newInstance(initargs); }
/*     */         
/* 609 */         catch (NoSuchMethodException ex)
/* 610 */         { return null; }  } } } private static final class Using<T> extends Record { private final PropertiesLauncher.Instantiator<T> instantiator; private final Class<?>[] parameterTypes; T newInstance(Object... initargs) throws Exception { try { Constructor<?> constructor = this.instantiator.type().getDeclaredConstructor(this.parameterTypes); constructor.setAccessible(true); return (T)constructor.newInstance(initargs); } catch (NoSuchMethodException ex) { return null; }
/*     */        }
/*     */ 
/*     */     
/*     */     private Using(PropertiesLauncher.Instantiator<T> instantiator, Class<?>... parameterTypes) {
/*     */       this.instantiator = instantiator;
/*     */       this.parameterTypes = parameterTypes;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #600	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #600	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #600	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lorg/springframework/boot/loader/launch/PropertiesLauncher$Instantiator$Using<TT;>;
/*     */     }
/*     */     
/*     */     public PropertiesLauncher.Instantiator<T> instantiator() {
/*     */       return this.instantiator;
/*     */     }
/*     */     
/*     */     public Class<?>[] parameterTypes() {
/*     */       return this.parameterTypes;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/launch/PropertiesLauncher.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */