package de.arthurpicht.meta.tasks.status;

import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.utils.core.strings.Strings;

public class Status {

    public static void execute(ProjectConfig projectConfig) {

        System.out.println("Found projects: "
                + Strings.listing(projectConfig.getProjectNames(), " ", "", "", "[", "]"));

    }

}
