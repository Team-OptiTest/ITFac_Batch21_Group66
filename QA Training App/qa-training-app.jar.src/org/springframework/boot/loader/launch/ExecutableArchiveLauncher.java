/*    */ package org.springframework.boot.loader.launch;
/*    */ 
/*    */ import java.net.URL;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Set;
/*    */ import java.util.jar.Manifest;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ExecutableArchiveLauncher
/*    */   extends Launcher
/*    */ {
/*    */   private static final String START_CLASS_ATTRIBUTE = "Start-Class";
/*    */   private final Archive archive;
/*    */   
/*    */   public ExecutableArchiveLauncher() throws Exception {
/* 43 */     this(Archive.create(Launcher.class));
/*    */   }
/*    */   
/*    */   protected ExecutableArchiveLauncher(Archive archive) throws Exception {
/* 47 */     this.archive = archive;
/* 48 */     this.classPathIndex = getClassPathIndex(this.archive);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ClassLoader createClassLoader(Collection<URL> urls) throws Exception {
/* 53 */     if (this.classPathIndex != null) {
/* 54 */       urls = new ArrayList<>(urls);
/* 55 */       urls.addAll(this.classPathIndex.getUrls());
/*    */     } 
/* 57 */     return super.createClassLoader(urls);
/*    */   }
/*    */ 
/*    */   
/*    */   protected final Archive getArchive() {
/* 62 */     return this.archive;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getMainClass() throws Exception {
/* 67 */     Manifest manifest = this.archive.getManifest();
/* 68 */     String mainClass = (manifest != null) ? manifest.getMainAttributes().getValue("Start-Class") : null;
/* 69 */     if (mainClass == null) {
/* 70 */       throw new IllegalStateException("No 'Start-Class' manifest entry specified in " + this);
/*    */     }
/* 72 */     return mainClass;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Set<URL> getClassPathUrls() throws Exception {
/* 77 */     return this.archive.getClassPathUrls(this::isIncludedOnClassPathAndNotIndexed, this::isSearchedDirectory);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isSearchedDirectory(Archive.Entry entry) {
/* 86 */     return ((getEntryPathPrefix() == null || entry.name().startsWith(getEntryPathPrefix())) && 
/* 87 */       !isIncludedOnClassPath(entry));
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/launch/ExecutableArchiveLauncher.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */