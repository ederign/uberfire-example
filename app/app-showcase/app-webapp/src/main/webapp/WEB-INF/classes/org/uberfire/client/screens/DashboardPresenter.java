package org.uberfire.client.screens;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberView;
import org.uberfire.lifecycle.OnOpen;
import org.uberfire.shared.events.TaskCreated;
import org.uberfire.shared.events.TaskDone;

@ApplicationScoped
@WorkbenchScreen(identifier = "DashboardPresenter")
public class DashboardPresenter {

    public interface View extends UberView<DashboardPresenter> {

        void addProject( String project,
                         int tasksCreated,
                         int tasksDone );
    }

    @Inject
    private View view;

    private Map<String, ProjectTasksCounter> projectTasksCounter = new HashMap<String, ProjectTasksCounter>(  );

    @WorkbenchPartTitle
    public String getTitle() {
        return "Dashboard";
    }

    @WorkbenchPartView
    public UberView<DashboardPresenter> getView() {
        return view;
    }

    @OnOpen
    public void onOpen() {
        updateView();
    }

    private void updateView() {
        for ( String project : projectTasksCounter.keySet() ) {
            ProjectTasksCounter projectTasksCounter = this.projectTasksCounter.get( project );
            view.addProject(project, projectTasksCounter.tasksCreated, projectTasksCounter.tasksDone);
        }
    }

    public void taskCreated( @Observes TaskCreated taskCreated ) {
        ProjectTasksCounter projectTasksCounter = getProjectTasksCounter( taskCreated.getProject() );
        projectTasksCounter.taskCreated();
    }

    public void taskDone( @Observes TaskDone taskDone ) {
        ProjectTasksCounter projectTasksCounter = getProjectTasksCounter( taskDone.getProject() );
        projectTasksCounter.taskDone();
    }


    public ProjectTasksCounter getProjectTasksCounter(String projectName){
        ProjectTasksCounter projectTasksCounter = this.projectTasksCounter.get( projectName );
        if(projectTasksCounter==null){
            projectTasksCounter = new ProjectTasksCounter();
            this.projectTasksCounter.put( projectName, projectTasksCounter );
        }
        return projectTasksCounter;
    }

    private class ProjectTasksCounter{

        int tasksDone;
        int tasksCreated;

        public void taskDone() {
            tasksDone++;
            tasksCreated--;
        }

        public void taskCreated() {
            tasksCreated++;
        }
    }

}
