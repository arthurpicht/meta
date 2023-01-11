package de.arthurpicht.meta.cli.feature.branchChangeSet;

import de.arthurpicht.meta.config.RepoConfigs;
import de.arthurpicht.utils.core.strings.Strings;

import java.util.List;

public class FeatureCheckoutBlockingMessage {

//    public static String getMessage(BranchChangeSet branchChangeSet) {
//        if (!branchChangeSet.isBlocked())
//            throw new IllegalStateException(BranchChangeSet.class.getSimpleName() + " not blocked.");
//
//        StringBuffer stringBuffer = new StringBuffer();
//
//        List<String> uncommittedChangedRepoNames
//                = RepoConfigs.getRepoNames(branchChangeSet.getUncommittedChangesBlockingBranches());
//
//        if (branchChangeSet.isForce()) {
//
//            stringBuffer
//                    .append("The following repos are affected and have uncommitted changes: ")
//                    .append(Strings.listing(uncommittedChangedRepoNames, ", "))
//                    .append(".\n")
//                    .append("Force checkout.");
//
//            List<String> modifiedFilesRepoNames
//                    = RepoConfigs.getRepoNames(branchChangeSet.getModifiedFilesBlockingBranches());
//
//        }
//
//
//    }

}
