/*    */ package BOOT-INF.classes.com.qatraining.dto;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class PageResponse<T>
/*    */ {
/*    */   private List<T> content;
/*    */   private int number;
/*    */   private int size;
/*    */   private long totalElements;
/*    */   private int totalPages;
/*    */   private boolean first;
/*    */   private boolean last;
/*    */   
/*    */   public List<T> getContent() {
/* 16 */     return this.content;
/*    */   }
/*    */   
/*    */   public void setContent(List<T> content) {
/* 20 */     this.content = content;
/*    */   }
/*    */   
/*    */   public int getNumber() {
/* 24 */     return this.number;
/*    */   }
/*    */   
/*    */   public void setNumber(int number) {
/* 28 */     this.number = number;
/*    */   }
/*    */   
/*    */   public int getSize() {
/* 32 */     return this.size;
/*    */   }
/*    */   
/*    */   public void setSize(int size) {
/* 36 */     this.size = size;
/*    */   }
/*    */   
/*    */   public long getTotalElements() {
/* 40 */     return this.totalElements;
/*    */   }
/*    */   
/*    */   public void setTotalElements(long totalElements) {
/* 44 */     this.totalElements = totalElements;
/*    */   }
/*    */   
/*    */   public int getTotalPages() {
/* 48 */     return this.totalPages;
/*    */   }
/*    */   
/*    */   public void setTotalPages(int totalPages) {
/* 52 */     this.totalPages = totalPages;
/*    */   }
/*    */   
/*    */   public boolean isFirst() {
/* 56 */     return this.first;
/*    */   }
/*    */   
/*    */   public void setFirst(boolean first) {
/* 60 */     this.first = first;
/*    */   }
/*    */   
/*    */   public boolean isLast() {
/* 64 */     return this.last;
/*    */   }
/*    */   
/*    */   public void setLast(boolean last) {
/* 68 */     this.last = last;
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/dto/PageResponse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */