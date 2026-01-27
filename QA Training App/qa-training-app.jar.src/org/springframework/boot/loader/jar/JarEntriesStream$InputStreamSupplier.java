package org.springframework.boot.loader.jar;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
interface InputStreamSupplier {
  InputStream get() throws IOException;
}


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/jar/JarEntriesStream$InputStreamSupplier.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */