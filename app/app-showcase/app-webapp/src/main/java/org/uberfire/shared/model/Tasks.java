package org.uberfire.shared.model;

import java.util.ArrayList;
import java.util.List;

public class Tasks {

    private final String projectName;
    private List<Folder> folders = new ArrayList<Folder>(  );

    public Tasks( String projectName ) {

        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void newFolder( String folderName ) {
        folders.add(new Folder(folderName));
    }

    public List<Folder> getFolders() {
        return folders;
    }
}
