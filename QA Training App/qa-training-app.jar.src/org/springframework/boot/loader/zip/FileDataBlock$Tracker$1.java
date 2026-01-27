package org.springframework.boot.loader.zip;

import java.nio.file.Path;

class null implements FileDataBlock.Tracker {
  public void openedFileChannel(Path path) {}
  
  public void closedFileChannel(Path path) {}
}


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/org/springframework/boot/loader/zip/FileDataBlock$Tracker$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */