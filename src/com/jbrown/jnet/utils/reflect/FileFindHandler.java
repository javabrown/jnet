package com.jbrown.jnet.utils.reflect;

import java.io.File;

public interface FileFindHandler {
  abstract public void handleFile(File file);

  abstract public void onComplete();
}
