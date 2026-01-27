package org.springframework.boot.loader.jarmode;

public interface JarMode {
  boolean accepts(String paramString);
  
  void run(String paramString, String[] paramArrayOfString);
}


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/jarmode/JarMode.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */