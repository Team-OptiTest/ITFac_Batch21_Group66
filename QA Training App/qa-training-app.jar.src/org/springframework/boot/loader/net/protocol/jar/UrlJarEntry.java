/*    */ package org.springframework.boot.loader.net.protocol.jar;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.jar.Attributes;
/*    */ import java.util.jar.JarEntry;
/*    */ import java.util.zip.ZipEntry;
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
/*    */ final class UrlJarEntry
/*    */   extends JarEntry
/*    */ {
/*    */   private final UrlJarManifest manifest;
/*    */   
/*    */   private UrlJarEntry(JarEntry entry, UrlJarManifest manifest) {
/* 34 */     super(entry);
/* 35 */     this.manifest = manifest;
/*    */   }
/*    */ 
/*    */   
/*    */   public Attributes getAttributes() throws IOException {
/* 40 */     return this.manifest.getEntryAttributes(this);
/*    */   }
/*    */   
/*    */   static UrlJarEntry of(ZipEntry entry, UrlJarManifest manifest) {
/* 44 */     return (entry != null) ? new UrlJarEntry((JarEntry)entry, manifest) : null;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/UrlJarEntry.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */