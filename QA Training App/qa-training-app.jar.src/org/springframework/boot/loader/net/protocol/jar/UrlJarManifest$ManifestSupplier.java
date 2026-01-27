package org.springframework.boot.loader.net.protocol.jar;

import java.io.IOException;
import java.util.jar.Manifest;

@FunctionalInterface
interface ManifestSupplier {
  Manifest getManifest() throws IOException;
}


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/net/protocol/jar/UrlJarManifest$ManifestSupplier.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */