package de.arthurpicht.meta.helper;

import de.arthurpicht.utils.core.assertion.AssertMethodPrecondition;

import java.util.List;

public class ListHelper {

    public static <T> void addIfNotYetContained(List<T> list, T element) {
        AssertMethodPrecondition.parameterNotNull("list", list);
        AssertMethodPrecondition.parameterNotNull("element", element);
        if (!list.contains(element)) list.add(element);
    }

}
