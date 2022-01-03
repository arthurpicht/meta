package de.arthurpicht.meta.cli.target;

import java.util.Collections;
import java.util.Set;

public class Targets {

    private final Set<String> targetNames;

    public Targets(Set<String> targetNames) {
        this.targetNames = Collections.unmodifiableSet(targetNames);
    }

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
