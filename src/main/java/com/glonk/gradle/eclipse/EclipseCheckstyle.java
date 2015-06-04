package com.glonk.gradle.eclipse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.quality.CheckstyleExtension;


public class EclipseCheckstyle {
  
  private static final String CHECKSTYLE_CONFIG = ".checkstyle";
  
  private static final String FLAG_DERIVED = "derived";
  
  private final Project project;
  
  private final Logger log;
  
  public EclipseCheckstyle(Task task) {
    this.project = task.getProject();
    this.log = task.getLogger();
  }

  public static Action<Task> generateAction() {
    return new Action<Task>() {
      @Override
      public void execute(Task task) {
        new EclipseCheckstyle(task).generate();
      }
    };
  }
  
  public static Action<Task> cleanAction() {
    return new Action<Task>() {
      @Override
      public void execute(Task task) {
        new EclipseCheckstyle(task).clean();
      }
    };
  }
  
  public void generate() {
    ExtensionContainer extensions = project.getExtensions();
    EclipseCheckstyleExtension prefs = extensions.getByType(EclipseCheckstyleExtension.class);
    
    CheckstyleExtension ext = project.getExtensions().getByType(CheckstyleExtension.class);
    if (ext == null) {
      return;
    }
    
    // Without a checkstyle config file defined, we currently don't do anything.
    File configFile = ext.getConfigFile();
    if (configFile == null || !configFile.exists()) {
      return;
    }

    log.info("eclipse checkstyle: configuring eclipse checkstyle using config {}", configFile);
    String configPath = configFile.toString();
    String projectDir = project.getProjectDir().getAbsolutePath() + File.separator;
    String name = project.getName() + "-checkstyle";
    write(projectDir, CHECKSTYLE_CONFIG, build(name, configPath, prefs));
  }

  public void clean() {
    String projectDir = project.getProjectDir().getAbsolutePath();
    File configFile = new File(projectDir, CHECKSTYLE_CONFIG);
    if (configFile.exists()) {
      configFile.delete();
    }
  }
  
  /**
   * Quick-n-very-dirty XML generation.  If further configuration is required for this
   * plugin we can generalize this, do proper escaping, etc.
   */
  private String build(String name, String path, EclipseCheckstyleExtension prefs) {
    Set<String> exclude = new HashSet<String>(prefs.exclude);
    
    StringBuilder buf = new StringBuilder();
    buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    buf.append("<fileset-config file-format-version=\"1.2.0\" simple-config=\"true\" ")
      .append("sync-formatter=\"")
      .append(prefs.syncFormatter)
      .append("\">\n");
    
    buf.append("<local-check-config name=\"")
      .append(name)
      .append("\" location=\"")
      .append(path)
      .append("\" type=\"external\" description=\"\">\n");

    buf.append("<additional-data name=\"protect-config-file\" value=\"false\"/>\n");
    buf.append("</local-check-config>\n");
    
    buf.append("<fileset name=\"all\" enabled=\"true\" check-config-name=\"")
      .append(name)
      .append("\" local=\"true\">\n");
    
    buf.append("<file-match-pattern match-pattern=\".\" include-pattern=\"true\"/>\n");
    buf.append("</fileset>\n");
    
    buf.append("<filter name=\"DerivedFiles\" enabled=\"")
      .append(exclude.contains(FLAG_DERIVED))
      .append("\"/>\n");
    
    buf.append("</fileset-config>");
    return buf.toString();
  }
  
  /**
   * File writing, Java 6-style.
   */
  private void write(String parent, String name, String data) {
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(new File(parent, name));
      writer.write(data);
      
    } catch (IOException e) {
      log.error("Failed to create eclipse checkstyle config", e);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }
  
}
