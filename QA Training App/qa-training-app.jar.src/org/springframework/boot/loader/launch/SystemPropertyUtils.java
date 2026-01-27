/*     */ package org.springframework.boot.loader.launch;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SystemPropertyUtils
/*     */ {
/*     */   private static final String PLACEHOLDER_PREFIX = "${";
/*     */   private static final String PLACEHOLDER_SUFFIX = "}";
/*     */   private static final String VALUE_SEPARATOR = ":";
/*  41 */   private static final String SIMPLE_PREFIX = "${".substring(1);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String resolvePlaceholders(Properties properties, String text) {
/*  47 */     return (text != null) ? parseStringValue(properties, text, text, new HashSet<>()) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String parseStringValue(Properties properties, String value, String current, Set<String> visitedPlaceholders) {
/*  52 */     StringBuilder result = new StringBuilder(current);
/*  53 */     int startIndex = current.indexOf("${");
/*  54 */     while (startIndex != -1) {
/*  55 */       int endIndex = findPlaceholderEndIndex(result, startIndex);
/*  56 */       if (endIndex == -1) {
/*  57 */         startIndex = -1;
/*     */         continue;
/*     */       } 
/*  60 */       String placeholder = result.substring(startIndex + "${".length(), endIndex);
/*  61 */       String originalPlaceholder = placeholder;
/*  62 */       if (!visitedPlaceholders.add(originalPlaceholder)) {
/*  63 */         throw new IllegalArgumentException("Circular placeholder reference '" + originalPlaceholder + "' in property definitions");
/*     */       }
/*     */       
/*  66 */       placeholder = parseStringValue(properties, value, placeholder, visitedPlaceholders);
/*  67 */       String propertyValue = resolvePlaceholder(properties, value, placeholder);
/*  68 */       if (propertyValue == null) {
/*  69 */         int separatorIndex = placeholder.indexOf(":");
/*  70 */         if (separatorIndex != -1) {
/*  71 */           String actualPlaceholder = placeholder.substring(0, separatorIndex);
/*  72 */           String defaultValue = placeholder.substring(separatorIndex + ":".length());
/*  73 */           propertyValue = resolvePlaceholder(properties, value, actualPlaceholder);
/*  74 */           propertyValue = (propertyValue != null) ? propertyValue : defaultValue;
/*     */         } 
/*     */       } 
/*  77 */       if (propertyValue != null) {
/*  78 */         propertyValue = parseStringValue(properties, value, propertyValue, visitedPlaceholders);
/*  79 */         result.replace(startIndex, endIndex + "}".length(), propertyValue);
/*  80 */         startIndex = result.indexOf("${", startIndex + propertyValue.length());
/*     */       } else {
/*     */         
/*  83 */         startIndex = result.indexOf("${", endIndex + "}".length());
/*     */       } 
/*  85 */       visitedPlaceholders.remove(originalPlaceholder);
/*     */     } 
/*  87 */     return result.toString();
/*     */   }
/*     */   
/*     */   private static String resolvePlaceholder(Properties properties, String text, String placeholderName) {
/*  91 */     String propertyValue = getProperty(placeholderName, null, text);
/*  92 */     if (propertyValue != null) {
/*  93 */       return propertyValue;
/*     */     }
/*  95 */     return (properties != null) ? properties.getProperty(placeholderName) : null;
/*     */   }
/*     */   
/*     */   static String getProperty(String key) {
/*  99 */     return getProperty(key, null, "");
/*     */   }
/*     */   
/*     */   private static String getProperty(String key, String defaultValue, String text) {
/*     */     try {
/* 104 */       String value = System.getProperty(key);
/* 105 */       value = (value != null) ? value : System.getenv(key);
/* 106 */       value = (value != null) ? value : System.getenv(key.replace('.', '_'));
/* 107 */       value = (value != null) ? value : System.getenv(key.toUpperCase(Locale.ENGLISH).replace('.', '_'));
/* 108 */       return (value != null) ? value : defaultValue;
/*     */     }
/* 110 */     catch (Throwable ex) {
/* 111 */       System.err.println("Could not resolve key '" + key + "' in '" + text + "' as system property or in environment: " + ex);
/*     */       
/* 113 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
/* 118 */     int index = startIndex + "${".length();
/* 119 */     int withinNestedPlaceholder = 0;
/* 120 */     while (index < buf.length()) {
/* 121 */       if (substringMatch(buf, index, "}")) {
/* 122 */         if (withinNestedPlaceholder > 0) {
/* 123 */           withinNestedPlaceholder--;
/* 124 */           index += "}".length();
/*     */           continue;
/*     */         } 
/* 127 */         return index;
/*     */       } 
/*     */       
/* 130 */       if (substringMatch(buf, index, SIMPLE_PREFIX)) {
/* 131 */         withinNestedPlaceholder++;
/* 132 */         index += SIMPLE_PREFIX.length();
/*     */         continue;
/*     */       } 
/* 135 */       index++;
/*     */     } 
/*     */     
/* 138 */     return -1;
/*     */   }
/*     */   
/*     */   private static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
/* 142 */     for (int j = 0; j < substring.length(); j++) {
/* 143 */       int i = index + j;
/* 144 */       if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
/* 145 */         return false;
/*     */       }
/*     */     } 
/* 148 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/launch/SystemPropertyUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */