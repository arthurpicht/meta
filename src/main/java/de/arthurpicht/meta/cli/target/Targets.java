package de.arthurpicht.meta.cli.target;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Targets {

    private final Set<String> targetNames;

    public Targets() {
        this.targetNames = new HashSet<>();
        this.targetNames.add(Target.DEV);
        this.targetNames.add(Target.PROD);
    }

    public Targets(Set<String> targetNames) {
        this.targetNames = Collections.unmodifiableSet(targetNames);
    }

//    public Targets getSelection(Set<String> targetNames) throws RedundantTargetException, UnknownTargetException {
//        for (String targetName : targetNames) {
//            if (!hasTarget(targetName))
//                throw new UnknownTargetException(targetName);
//        }
//        return new Targets(targetNames);
//    }

    public boolean hasTarget(String targetName) {
        return this.targetNames.contains(targetName);
    }

    public boolean hasTarget(Target target) {
        return this.targetNames.contains(target.getName());
    }

    public Set<String> getAllTargetNames() {
        return targetNames;
    }

}
