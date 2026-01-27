/*    */ package org.springframework.boot.loader.jar;
/*    */ 
/*    */ import java.util.jar.Attributes;
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
/*    */ class ManifestInfo
/*    */ {
/* 32 */   private static final Attributes.Name MULTI_RELEASE = new Attributes.Name("Multi-Release");
/*    */   
/* 34 */   static final ManifestInfo NONE = new ManifestInfo(null, Boolean.valueOf(false));
/*    */ 
/*    */   
/*    */   private final Manifest manifest;
/*    */ 
/*    */   
/*    */   private volatile Boolean multiRelease;
/*    */ 
/*    */ 
/*    */   
/*    */   ManifestInfo(Manifest manifest) {
/* 45 */     this(manifest, null);
/*    */   }
/*    */   
/*    */   private ManifestInfo(Manifest manifest, Boolean multiRelease) {
/* 49 */     this.manifest = manifest;
/* 50 */     this.multiRelease = multiRelease;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Manifest getManifest() {
/* 58 */     return this.manifest;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean isMultiRelease() {
/* 66 */     if (this.manifest == null) {
/* 67 */       return false;
/*    */     }
/* 69 */     Boolean multiRelease = this.multiRelease;
/* 70 */     if (multiRelease != null) {
/* 71 */       return multiRelease.booleanValue();
/*    */     }
/* 73 */     Attributes attributes = this.manifest.getMainAttributes();
/* 74 */     multiRelease = Boolean.valueOf(attributes.containsKey(MULTI_RELEASE));
/* 75 */     this.multiRelease = multiRelease;
/* 76 */     return multiRelease.booleanValue();
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/jar/ManifestInfo.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */