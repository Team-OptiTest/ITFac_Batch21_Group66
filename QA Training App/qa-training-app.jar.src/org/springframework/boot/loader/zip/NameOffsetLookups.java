/*    */ package org.springframework.boot.loader.zip;
/*    */ 
/*    */ import java.util.BitSet;
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
/*    */ class NameOffsetLookups
/*    */ {
/* 30 */   public static final NameOffsetLookups NONE = new NameOffsetLookups(0, 0);
/*    */   
/*    */   private final int offset;
/*    */   
/*    */   private final BitSet enabled;
/*    */   
/*    */   NameOffsetLookups(int offset, int size) {
/* 37 */     this.offset = offset;
/* 38 */     this.enabled = (size != 0) ? new BitSet(size) : null;
/*    */   }
/*    */   
/*    */   void swap(int i, int j) {
/* 42 */     if (this.enabled != null) {
/* 43 */       boolean temp = this.enabled.get(i);
/* 44 */       this.enabled.set(i, this.enabled.get(j));
/* 45 */       this.enabled.set(j, temp);
/*    */     } 
/*    */   }
/*    */   
/*    */   int get(int index) {
/* 50 */     return isEnabled(index) ? this.offset : 0;
/*    */   }
/*    */   
/*    */   int enable(int index, boolean enable) {
/* 54 */     if (this.enabled != null) {
/* 55 */       this.enabled.set(index, enable);
/*    */     }
/* 57 */     return !enable ? 0 : this.offset;
/*    */   }
/*    */   
/*    */   boolean isEnabled(int index) {
/* 61 */     return (this.enabled != null && this.enabled.get(index));
/*    */   }
/*    */   
/*    */   boolean hasAnyEnabled() {
/* 65 */     return (this.enabled != null && this.enabled.cardinality() > 0);
/*    */   }
/*    */   
/*    */   NameOffsetLookups emptyCopy() {
/* 69 */     return new NameOffsetLookups(this.offset, this.enabled.size());
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/NameOffsetLookups.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */