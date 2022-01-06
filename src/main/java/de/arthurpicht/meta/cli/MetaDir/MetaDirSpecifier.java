package de.arthurpicht.meta.cli.MetaDir;

import de.arthurpicht.meta.Const;
import de.arthurpicht.meta.cli.Meta;

public enum MetaDirSpecifier {

    ENV_VAR("environment variable " + Const.META_DIR__ENV_VAR),
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
