package de.arthurpicht.meta.cli.metaDir;

import de.arthurpicht.meta.cli.Meta;

public enum MetaDirSpecifier {

    META_DIR_FILE("user specific meta_dir file"),
    WORKING_DIR("current working directory"),
    CLI("global cli parameter '" + Meta.OPTION_META_DIR + "'");

    private final String text;

    MetaDirSpecifier(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
