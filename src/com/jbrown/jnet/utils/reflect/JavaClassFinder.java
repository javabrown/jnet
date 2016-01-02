package com.jbrown.jnet.utils.reflect;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JavaClassFinder {
  public static final String JAVA_CLASS_PATH_PROPERTY = "java.class.path";
  public static final String CUSTOM_CLASS_PATH_PROPERTY = "custom.class.path";

  private ArrayList<Class<?>> foundClasses;
  private Class<?> toFind;
  private JavaClassFileWalker fileWalker;
  private ClassLoadingFileHandler fileHandler;

  @SuppressWarnings("unchecked")
  public <T> List<Class<? extends T>> findAllMatchingTypes(Class<T> toFind) {
    foundClasses = new ArrayList<Class<?>>();
    List<Class<? extends T>> returnedClasses = new ArrayList<Class<? extends T>>();
    this.toFind = toFind;
    walkClassPath();
    for (Class<?> clazz : foundClasses) {
      returnedClasses.add((Class<? extends T>) clazz);
    }
    return returnedClasses;
  }

  private void walkClassPath() {
    fileHandler = new ClassLoadingFileHandler();
    fileWalker = new JavaClassFileWalker(fileHandler);

    String[] classPathRoots = getClassPathRoots();
    for (int i = 0; i < classPathRoots.length; i++) {
      String path = classPathRoots[i];
      if (path.endsWith(".jar")) {
        continue;
      }
      fileHandler.updateClassPathBase(path);
      fileWalker.setBaseDir(path);
      fileWalker.walk();
    }
  }

  public String[] getClassPathRoots() {
    String classPath;
    if (System.getProperties().containsKey(CUSTOM_CLASS_PATH_PROPERTY)) {
      classPath = System.getProperty(CUSTOM_CLASS_PATH_PROPERTY);
    } else {
      classPath = System.getProperty(JAVA_CLASS_PATH_PROPERTY);
    }
    String[] pathElements = classPath.split(File.pathSeparator);
    return pathElements;
  }

  private void handleClass(Class<?> clazz) {
    boolean isMatch = false;
    isMatch = toFind == null || toFind.isAssignableFrom(clazz);
    if (isMatch) {
      foundClasses.add(clazz);
    }
  }

  class ClassLoadingFileHandler extends FileFindHandlerAdapter {
    private FileToClassConverter converter;

    public void updateClassPathBase(String classPathRoot) {
      if (converter == null) {
        converter = new FileToClassConverter(classPathRoot);
      }
      converter.setClassPathRoot(classPathRoot);
    }

    @Override
    public void handleFile(File file) {
      Class<?> clazz = converter.convertToClass(file);
      if (clazz == null) {
        return;
      }
      handleClass(clazz);
    }
  }

  public List<Class<?>> findAllMatchingTypes() {
    return findAllMatchingTypes(null);
  }

  public int getScannedClassesCount() {
    if (fileWalker == null) {
      return 0;
    }
    return fileWalker.getAllFilesCount();
  }
}
