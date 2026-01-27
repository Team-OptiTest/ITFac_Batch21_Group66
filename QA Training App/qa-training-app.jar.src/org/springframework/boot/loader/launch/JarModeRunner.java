/*    */ package org.springframework.boot.loader.launch;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.boot.loader.jarmode.JarMode;
/*    */ import org.springframework.core.io.support.SpringFactoriesLoader;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ final class JarModeRunner
/*    */ {
/* 32 */   static final String DISABLE_SYSTEM_EXIT = JarModeRunner.class.getName() + ".DISABLE_SYSTEM_EXIT";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void main(String[] args) {
/* 38 */     String mode = System.getProperty("jarmode");
/* 39 */     List<JarMode> candidates = SpringFactoriesLoader.loadFactories(JarMode.class, 
/* 40 */         ClassUtils.getDefaultClassLoader());
/* 41 */     for (JarMode candidate : candidates) {
/* 42 */       if (candidate.accepts(mode)) {
/* 43 */         candidate.run(mode, args);
/*    */         return;
/*    */       } 
/*    */     } 
/* 47 */     System.err.println("Unsupported jarmode '" + mode + "'");
/* 48 */     if (!Boolean.getBoolean(DISABLE_SYSTEM_EXIT))
/* 49 */       System.exit(1); 
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/launch/JarModeRunner.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */