/*    */ package org.springframework.boot.loader.ref;
/*    */ 
/*    */ import java.lang.ref.Cleaner;
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
/*    */ public interface Cleaner
/*    */ {
/* 33 */   public static final Cleaner instance = DefaultCleaner.instance;
/*    */   
/*    */   Cleaner.Cleanable register(Object paramObject, Runnable paramRunnable);
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/ref/Cleaner.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */