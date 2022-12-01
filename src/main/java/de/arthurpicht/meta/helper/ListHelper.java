package de.arthurpicht.meta.helper;

import java.util.ArrayList;
import java.util.List;

import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNull;

public class ListHelper {

    public static <T> void addIfNotYetContained(List<T> list, T element) {
        assertArgumentNotNull("list", list);
        assertArgumentNotNull("element", element);
        if (!list.contains(element)) list.add(element);
    }

    public static <T> List<T> addIfNotYetContained(List<T> listOne, List<T> listTwo) {
        assertArgumentNotNull("listOne", listOne);
        assertArgumentNotNull("listTwo", listTwo);
        List<T> sum = new ArrayList<>(listOne);
        for (T t : listTwo) addIfNotYetContained(sum, t);
        return sum;
    }

}
