package com.urise.webapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainStream {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 3, 2, 3};
        System.out.println(minValue(arr));

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        System.out.println(oddOrEven(list));
    }

    private static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce((left, right) -> left * 10 + right)
                .orElseThrow();
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        boolean even = integers.stream().mapToInt(value -> value).sum() % 2 == 0;
        return integers.stream().filter(integer -> (integer % 2 != 0) == even).collect(Collectors.toList());
    }
}


