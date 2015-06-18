package org.uberfire.client.screens;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.mvp.UberView;
import org.uberfire.client.screens.popups.NewProjectPresenter;
import org.uberfire.shared.events.ProjectSelectedEvent;
import org.uberfire.shared.model.Project;

@ApplicationScoped
@WorkbenchScreen(identifier = "ProjectsPresenter")
public class ProjectsPresenter {

    public interface View extends UberView<ProjectsPresenter> {

        void clearProjects();

        void addProject( String projectName,
                         boolean selected );

    }

    @Inject
    private View view;

    @Inject
    private PlaceManager placeManager;

    @Inject
    private NewProjectPresenter newProjectPresenter;

    @Inject
    private Event<ProjectSelectedEvent> projectSelectedEvent;

    private List<Project> projects = new ArrayList<Project>();

    @WorkbenchPartTitle
    public String getTitle() {
        return "Projects";
    }

    @WorkbenchPartView
    public UberView<ProjectsPresenter> getView() {
        return view;
    }

    public void newProject() {
        newProjectPresenter.show( this );
    }

    public void newProject( String projectName ) {
        projects.add( new Project( projectName ) );
        selectProject( projectName );
    }

    private void updateView() {
        view.clearProjects();
        for ( Project project : projects ) {
            view.addProject( project.getName(), project.isSelected() );
        }
    }

    public void selectProject( String projectName ) {
        setActiveProject( projectName );
        updateView();
        projectSelectedEvent.fire( new ProjectSelectedEvent( projectName ) );
    }

    private void setActiveProject( String projectName ) {
        for ( Project project : projects ) {
            if ( projectName.equalsIgnoreCase( project.getName() ) ) {
                project.setSelected( true );
            } else {
                project.setSelected( false );
            }
        }
    }
}
