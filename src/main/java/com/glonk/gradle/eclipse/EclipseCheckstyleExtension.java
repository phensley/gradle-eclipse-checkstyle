package com.glonk.gradle.eclipse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class EclipseCheckstyleExtension {

  Set<String> exclude = new HashSet<String>();
  
  boolean syncFormatter;
  
  public void setExclude(String ... options) {
    this.exclude.addAll(Arrays.asList(options));
  }

  public void setSyncFormatter(boolean flag) {
    this.syncFormatter = flag;
  }
  
}
