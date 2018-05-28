package com.scent.feedservices.controller;

import java.util.*;

import java.util.stream.IntStream;

public class MissingNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String n = scanner.nextLine();
        int nSize = Integer.parseInt(n);
        int[] A = new int[nSize];
        int i =0;
        for(i =0; i< nSize; i++){
             A[i] = scanner.nextInt();
        }
        int mSize = scanner.nextInt();
        int[] B = new int[mSize];

        for(i =0; i< mSize; i++){
            B[i] = scanner.nextInt();

        }
        String finalResult = "";
        List<Integer> result = getMissing2(A, B);
//        for (i=0; i< result.length; i++) {
//            finalResult += result[i] + " ";
//        }
        for (Integer j: result){
            finalResult += j + " ";
        }
        System.out.println(finalResult.trim());

    }
    static final int SPAN = 100;

    public static int[] getMissing(int[] A, int[] B) {
        final boolean isMissing[] = new boolean[SPAN*2+1];
        final int offset = B[0] - SPAN;

        for (int a = 0, b = 0 ; b < B.length ; b++)
            if (A.length <= a || A[a] != B[b])
                isMissing[B[b]-offset] = true;
            else
                a++;

        int nMissing = 0,
                missing[] = new int[SPAN+1];
        for (int i = 0; i < isMissing.length; i++)
            if (isMissing[i])
                missing[nMissing++] = i + offset;

        return java.util.Arrays.copyOf(missing, nMissing);
    }

    public static List<Integer> getMissing2(int[] array1, int[] array2) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        List<Integer> missingNumberList = new ArrayList<>();

        IntStream.of(array2)
                .forEach((int i) ->
                { frequencyMap.put(i, frequencyMap.getOrDefault(i, 0) + 1); });

        IntStream.of(array1)
                .forEach((int i) ->
                { frequencyMap.put(i, frequencyMap.get(i) - 1);});

        frequencyMap.entrySet()
                .stream()
                .forEach(
                        (Map.Entry<Integer, Integer> entry) ->
                        { if (entry.getValue() > 0) {
                            missingNumberList.add(entry.getKey());
                        }});

        Collections.sort(missingNumberList);
        return missingNumberList;
    }
}
