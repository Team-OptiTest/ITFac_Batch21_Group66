/*    */ package org.springframework.boot.loader.ref;
/*    */ 
/*    */ import java.lang.ref.Cleaner;
/*    */ import java.util.function.BiConsumer;
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
/*    */ class DefaultCleaner
/*    */   implements Cleaner
/*    */ {
/* 29 */   static final DefaultCleaner instance = new DefaultCleaner();
/*    */   
/*    */   static BiConsumer<Object, Cleaner.Cleanable> tracker;
/*    */   
/* 33 */   private final Cleaner cleaner = Cleaner.create();
/*    */ 
/*    */   
/*    */   public Cleaner.Cleanable register(Object obj, Runnable action) {
/* 37 */     Cleaner.Cleanable cleanable = (action != null) ? this.cleaner.register(obj, action) : null;
/* 38 */     if (tracker != null) {
/* 39 */       tracker.accept(obj, cleanable);
/*    */     }
/* 41 */     return cleanable;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/ref/DefaultCleaner.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */