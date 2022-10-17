package de.arthurpicht.meta.cli.target;

import java.util.Locale;
import java.util.Objects;

import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNull;

public final class Target {

    public static final String PROD = "prod";
    public static final String DEV = "dev";

    private final String name;

    public Target(String name) {
        assertArgumentNotNull("name", name);
        this.name = name.toLowerCase(Locale.ROOT);
        if (!isDev() && !isProd()) throw new IllegalArgumentException("Illegal task name: [" + name + "].");
    }

    public String getName() {
        return name;
    }

    public boolean isProd() {
        return this.name.equals(PROD) || this.name.startsWith(prodPrefix());
    }

    public boolean isDev() {
        return this.name.equals(DEV) || this.name.startsWith(devPrefix());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Target target = (Target) o;
        return name.equals(target.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    private static String prodPrefix() {
        return PROD + "-";
    }

    private static String devPrefix() {
        return DEV + "-";
    }
}
