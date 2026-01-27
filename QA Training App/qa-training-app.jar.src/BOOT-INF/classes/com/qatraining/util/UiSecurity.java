/*    */ package BOOT-INF.classes.com.qatraining.util;
/*    */ 
/*    */ import jakarta.servlet.http.HttpSession;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UiSecurity
/*    */ {
/*    */   public static boolean isLoggedIn(HttpSession session) {
/* 12 */     return (session != null && session.getAttribute("JWT_TOKEN") != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean hasRole(HttpSession session, String role) {
/* 17 */     if (!isLoggedIn(session)) {
/* 18 */       return false;
/*    */     }
/*    */     
/* 21 */     Set<String> roles = (Set<String>)session.getAttribute("USER_ROLES");
/* 22 */     return (roles != null && roles.contains(role));
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/util/UiSecurity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */