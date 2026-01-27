/*    */ package BOOT-INF.classes.com.qatraining.util;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import java.util.Base64;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JwtUtils
/*    */ {
/*    */   public static Map<String, Object> decodePayload(String token) {
/*    */     try {
/* 13 */       String payload = token.split("\\.")[1];
/* 14 */       byte[] decoded = Base64.getUrlDecoder().decode(payload);
/* 15 */       return (Map<String, Object>)(new ObjectMapper()).readValue(decoded, Map.class);
/* 16 */     } catch (Exception e) {
/* 17 */       throw new RuntimeException("Invalid JWT", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/util/JwtUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */