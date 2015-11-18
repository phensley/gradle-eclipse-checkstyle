package com.glonk.gradle.eclipse;

import java.util.ArrayList;
import java.util.List;

import groovy.lang.Closure;


public class EclipseCheckstyleExtension {

  List<FilePattern> patterns = new ArrayList<FilePattern>();
  
  boolean excludeDerived;

  boolean syncFormatter;
  
  public void setExcludeDerived(boolean flag) {
    this.excludeDerived = flag;
  }
  
  public void setSyncFormatter(boolean flag) {
    this.syncFormatter = flag;
  }
  
  public void patterns(Closure<?> closure) {
    closure.setDelegate(new FilePatterns());
    closure.setResolveStrategy(Closure.DELEGATE_ONLY);
    closure.call();
  }

  public class FilePatterns {
    
    public void include(String pattern) {
      patterns.add(new FilePattern(true, pattern));
    }
    
    public void exclude(String pattern) {
      patterns.add(new FilePattern(false, pattern));
    }
    
  }

  public class FilePattern {

    boolean include;
    
    String pattern;
    
    public FilePattern(boolean include, String pattern) {
      this.include = include;
      this.pattern = pattern;
    }
    
  }
}
