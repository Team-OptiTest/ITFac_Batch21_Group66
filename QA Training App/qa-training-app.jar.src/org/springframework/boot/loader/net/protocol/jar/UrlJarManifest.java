/*    */ package org.springframework.boot.loader.net.protocol.jar;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import java.util.jar.Attributes;
/*    */ import java.util.jar.JarEntry;
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
/*    */ class UrlJarManifest
/*    */ {
/* 33 */   private static final Object NONE = new Object();
/*    */   
/*    */   private final ManifestSupplier supplier;
/*    */   
/*    */   private volatile Object supplied;
/*    */   
/*    */   UrlJarManifest(ManifestSupplier supplier) {
/* 40 */     this.supplier = supplier;
/*    */   }
/*    */   
/*    */   Manifest get() throws IOException {
/* 44 */     Manifest manifest = supply();
/* 45 */     if (manifest == null) {
/* 46 */       return null;
/*    */     }
/* 48 */     Manifest copy = new Manifest();
/* 49 */     copy.getMainAttributes().putAll((Map<?, ?>)manifest.getMainAttributes().clone());
/* 50 */     manifest.getEntries().forEach((key, value) -> copy.getEntries().put(key, cloneAttributes(value)));
/* 51 */     return copy;
/*    */   }
/*    */   
/*    */   Attributes getEntryAttributes(JarEntry entry) throws IOException {
/* 55 */     Manifest manifest = supply();
/* 56 */     if (manifest == null) {
/* 57 */       return null;
/*    */     }
/* 59 */     Attributes attributes = manifest.getEntries().get(entry.getName());
/* 60 */     return cloneAttributes(attributes);
/*    */   }
/*    */   
/*    */   private Attributes cloneAttributes(Attributes attributes) {
/* 64 */     return (attributes != null) ? (Attributes)attributes.clone() : null;
/*    */   }
/*    */   
/*    */   private Manifest supply() throws IOException {
/* 68 */     Object supplied = this.supplied;
/* 69 */     if (supplied == null) {
/* 70 */       supplied = this.supplier.getManifest();
/* 71 */       this.supplied = (supplied != null) ? supplied : NONE;
/*    */     } 
/* 73 */     return (supplied != NONE) ? (Manifest)supplied : null;
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   static interface ManifestSupplier {
/*    */     Manifest getManifest() throws IOException;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/UrlJarManifest.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */