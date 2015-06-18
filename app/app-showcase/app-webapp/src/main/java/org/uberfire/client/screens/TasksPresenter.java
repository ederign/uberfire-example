package org.uberfire.client.screens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberView;
import org.uberfire.client.screens.popups.NewFolderPresenter;
import org.uberfire.shared.events.ProjectSelectedEvent;
import org.uberfire.shared.model.Folder;
import org.uberfire.shared.model.Tasks;

@ApplicationScoped
@WorkbenchScreen(identifier = "TasksPresenter")
public class TasksPresenter {

    public interface View extends UberView<TasksPresenter> {
        void activateNewFolder();
        void clearTasks();
        void newFolder( String name,
                        Integer size,
                        List<String> strings );
    }

    @Inject
    private View view;

    @Inject
    private NewFolderPresenter newFolderPresenter;

    private String currentSelectedProject;

    private Map<String, Tasks> tasksPerProject = new HashMap<String, Tasks>();

    @WorkbenchPartTitle
    public String getTitle() {
        return "Tasks";
    }

    @WorkbenchPartView
    public UberView<TasksPresenter> getView() {
        return view;
    }

    public void projectSelected( @Observes ProjectSelectedEvent projectSelectedEvent ) {
        this.currentSelectedProject = projectSelectedEvent.getName();
        selectFolder();
    }

    public void createTask( String folderName,
                            String task ) {

        Folder folder = getFolder( folderName );
        if ( folder != null ) {
            folder.addTask( task );
        }
        updateView();
    }

    private Folder getFolder( String folderName ) {
        Tasks tasks = getTasks();
        for ( final Folder folder : tasks.getFolders() ) {
            if ( folder.getName().equalsIgnoreCase( folderName ) ) {
                return folder;
            }
        }
        return null;
    }

    public void doneTask( String folderName,
                          String taskText ) {
        Folder folder = getFolder( folderName );
        if ( folder != null ) {
            folder.removeTask( taskText );
        }
        updateView();
    }

    private Tasks getTasks() {
        Tasks tasks = tasksPerProject.get( currentSelectedProject );
        if ( tasks == null ) {
            tasks = new Tasks( currentSelectedProject );
        }
        return tasks;
    }

    private void selectFolder() {
        view.activateNewFolder();
        updateView();
    }

    private void updateView() {
        view.clearTasks();
        Tasks tasks = getTasks();
        for ( final Folder folder : tasks.getFolders() ) {
            view.newFolder( folder.getName(), folder.getTasks().size(), folder.getTasks() );
        }
    }

    public void newFolder( String folderName ) {
        Tasks tasks = getTasks();
        tasks.newFolder( folderName );
        tasksPerProject.put( currentSelectedProject, tasks );
        updateView();
    }

    public void showNewFolder() {
        newFolderPresenter.show( this );
    }
}