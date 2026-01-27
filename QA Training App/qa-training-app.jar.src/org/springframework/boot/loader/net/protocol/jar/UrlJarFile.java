/*    */ package org.springframework.boot.loader.net.protocol.jar;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.jar.JarFile;
/*    */ import java.util.jar.Manifest;
/*    */ import java.util.zip.ZipEntry;
/*    */ import org.springframework.boot.loader.ref.Cleaner;
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
/*    */ class UrlJarFile
/*    */   extends JarFile
/*    */ {
/*    */   private final UrlJarManifest manifest;
/*    */   private final Consumer<JarFile> closeAction;
/*    */   
/*    */   UrlJarFile(File file, Runtime.Version version, Consumer<JarFile> closeAction) throws IOException {
/* 41 */     super(file, true, 1, version);
/*    */     
/* 43 */     Cleaner.instance.register(this, null);
/* 44 */     this.manifest = new UrlJarManifest(() -> super.getManifest());
/* 45 */     this.closeAction = closeAction;
/*    */   }
/*    */ 
/*    */   
/*    */   public ZipEntry getEntry(String name) {
/* 50 */     return UrlJarEntry.of(super.getEntry(name), this.manifest);
/*    */   }
/*    */ 
/*    */   
/*    */   public Manifest getManifest() throws IOException {
/* 55 */     return this.manifest.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 60 */     if (this.closeAction != null) {
/* 61 */       this.closeAction.accept(this);
/*    */     }
/* 63 */     super.close();
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/UrlJarFile.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */