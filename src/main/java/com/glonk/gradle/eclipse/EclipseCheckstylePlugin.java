package com.glonk.gradle.eclipse;

import java.util.HashMap;
import java.util.Map;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.plugins.ide.eclipse.model.EclipseModel;
import org.gradle.plugins.ide.eclipse.model.EclipseProject;


public class EclipseCheckstylePlugin implements Plugin<Project> {

  private static final String ECLIPSE_TASK = "eclipse";

  private static final String CLEAN_ECLIPSE_TASK = "cleanEclipse";
  
  private static final String CHECKSTYLE_TASK = "checkstyle";

  private static final String ECLIPSE_CHECKSTYLE = "eclipseCheckstyle";
  
  private static final String GRADLE_NATURE = "org.springsource.ide.eclipse.gradle.core.nature";
  
  private static final String CHECKSTYLE_NATURE = "net.sf.eclipsecs.core.CheckstyleNature";

  private static final String CHECKSTYLE_BUILDER = "net.sf.eclipsecs.core.CheckstyleBuilder";

  private static final String JAVA_BUILDER = "org.eclipse.jdt.core.javabuilder";
  
  @Override
  public void apply(Project project) {
    project.apply(dependencies(ECLIPSE_TASK));
    project.apply(dependencies(CHECKSTYLE_TASK));

    project.getExtensions().create(ECLIPSE_CHECKSTYLE, EclipseCheckstyleExtension.class);
    
    // Ensure that the project has the checkstyle nature / builder applied
    EclipseModel eclipseModel = project.getExtensions().getByType(EclipseModel.class);
    EclipseProject eclipseProject = eclipseModel.getProject();
    eclipseProject.natures(CHECKSTYLE_NATURE, GRADLE_NATURE);
  
    // Explicitly set the Java builder before Checkstyle so they don't get added
    // in an arbitrary order.
    eclipseProject.buildCommand(JAVA_BUILDER);
    eclipseProject.buildCommand(CHECKSTYLE_BUILDER);
    
    // Wire up our actions to the relevant eclipse tasks..
    Task task = project.getTasks().findByName(ECLIPSE_TASK);
    if (task != null) {
      task.doLast(EclipseCheckstyle.generateAction());
    }
    
    task = project.getTasks().findByName(CLEAN_ECLIPSE_TASK);
    if (task != null) {
      task.doLast(EclipseCheckstyle.cleanAction());
    }

  }
  
  private static Map<String, ?> dependencies(String plugin) {
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("plugin", plugin);
    return result;
  }
  
}
