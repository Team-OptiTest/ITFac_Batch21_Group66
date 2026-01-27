/*    */ package org.springframework.boot.loader.launch;
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
/*    */ public class WarLauncher
/*    */   extends ExecutableArchiveLauncher
/*    */ {
/*    */   public WarLauncher() throws Exception {}
/*    */   
/*    */   protected WarLauncher(Archive archive) throws Exception {
/* 35 */     super(archive);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getEntryPathPrefix() {
/* 40 */     return "WEB-INF/";
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isLibraryFileOrClassesDirectory(Archive.Entry entry) {
/* 45 */     String name = entry.name();
/* 46 */     if (entry.isDirectory()) {
/* 47 */       return name.equals("WEB-INF/classes/");
/*    */     }
/* 49 */     return (name.startsWith("WEB-INF/lib/") || name.startsWith("WEB-INF/lib-provided/"));
/*    */   }
/*    */   
/*    */   public static void main(String[] args) throws Exception {
/* 53 */     (new WarLauncher()).launch(args);
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/launch/WarLauncher.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */