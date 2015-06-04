package com.glonk.gradle.eclipse;

import java.util.HashMap;
import java.util.Map;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.plugins.ide.eclipse.model.EclipseModel;
import org.gradle.plugins.ide.eclipse.model.EclipseProject;


public class EclipseCheckstylePlugin implements Plugin<Project> {

  private static final String ECLIPSE_TASK = "eclipse";

  private static final String CHECKSTYLE_TASK = "checkstyle";

  private static final String ECLIPSE_CHECKSTYLE = "eclipseCheckstyle";
  
  private static final String CHECKSTYLE_NATURE = "net.sf.eclipsecs.core.CheckstyleNature";

  private static final String CHECKSTYLE_BUILDER = "net.sf.eclipsecs.core.CheckstyleBuilder";
  
  @Override
  public void apply(Project project) {
    project.apply(dependencies(ECLIPSE_TASK));
    project.apply(dependencies(CHECKSTYLE_TASK));
    
    project.getExtensions().create(ECLIPSE_CHECKSTYLE, EclipseCheckstyleExtension.class);
    
    // Ensure that the project has the checkstyle nature / builder applied
    EclipseModel eclipseModel = project.getExtensions().getByType(EclipseModel.class);
    EclipseProject eclipseProject = eclipseModel.getProject();
    eclipseProject.natures(CHECKSTYLE_NATURE);
    eclipseProject.buildCommand(CHECKSTYLE_BUILDER);
    
    Task eclipseTask = project.getTasks().findByName(ECLIPSE_TASK);
    if (eclipseTask != null) {
      eclipseTask.doLast(new Action<Task>() {
        @Override
        public void execute(Task task) {
          new EclipseCheckstyleGenerator(task).execute();
        }
      });
    }
  }

  private static Map<String, ?> dependencies(String plugin) {
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("plugin", plugin);
    return result;
  }
  
}
