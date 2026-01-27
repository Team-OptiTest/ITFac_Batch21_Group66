/*    */ package org.springframework.boot.loader.net.protocol.jar;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.jar.JarEntry;
/*    */ import java.util.jar.JarFile;
/*    */ import java.util.jar.Manifest;
/*    */ import java.util.zip.ZipEntry;
/*    */ import org.springframework.boot.loader.jar.NestedJarFile;
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
/*    */ class UrlNestedJarFile
/*    */   extends NestedJarFile
/*    */ {
/*    */   private final UrlJarManifest manifest;
/*    */   private final Consumer<JarFile> closeAction;
/*    */   
/*    */   UrlNestedJarFile(File file, String nestedEntryName, Runtime.Version version, Consumer<JarFile> closeAction) throws IOException {
/* 42 */     super(file, nestedEntryName, version);
/* 43 */     this.manifest = new UrlJarManifest(() -> super.getManifest());
/* 44 */     this.closeAction = closeAction;
/*    */   }
/*    */ 
/*    */   
/*    */   public Manifest getManifest() throws IOException {
/* 49 */     return this.manifest.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public JarEntry getEntry(String name) {
/* 54 */     return UrlJarEntry.of(super.getEntry(name), this.manifest);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 59 */     if (this.closeAction != null) {
/* 60 */       this.closeAction.accept(this);
/*    */     }
/* 62 */     super.close();
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/UrlNestedJarFile.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */