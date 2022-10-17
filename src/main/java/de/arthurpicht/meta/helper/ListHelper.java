package de.arthurpicht.meta.helper;

import java.util.List;

import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNull;

public class ListHelper {

    public static <T> void addIfNotYetContained(List<T> list, T element) {
        assertArgumentNotNull("list", list);
        assertArgumentNotNull("element", element);
        if (!list.contains(element)) list.add(element);
    }

}
