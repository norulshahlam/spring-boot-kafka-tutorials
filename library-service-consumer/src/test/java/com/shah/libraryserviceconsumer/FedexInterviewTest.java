package com.shah.libraryserviceconsumer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.stream.Collectors;

class FedexInterviewTest {

    @Test
    void question_1() {

        /*
        assign first char to temp
        initialize count
        initialize result
        loop string using length of string
        check if temp == char(i)
            count++
        else
            append result+count
            temp = char(i)
            count = 1
         final loop, append last result
         */

        String a = "ffgghh";
        char temp = a.charAt(0);
        int count = 0;
        String result = "";

        for (int i = 0; i < a.length(); i++) {
            if (temp == a.charAt(i)) {
                count++;
            } else {
                result = result.concat(count + "" + a.charAt(i - 1) + ", ");
                temp = a.charAt(i);
                count = 1;
            }
        }
        result = result.concat(count + "" + temp);
        System.out.println(result);
    }

    @Test
    void hackerRacnk() {

        String s = "He is a very very good boy, isn't he?";

        StringTokenizer str = new StringTokenizer(s, " .,'?!_@");
        System.out.println(str.countTokens());
        while (str.hasMoreTokens()) {
            System.out.println(str.nextToken());
        }

    }

    @Test
    void litInterview() {
        /*
        count number of occurences of all words i a string
         */
        String s = "Welcome Welcome to the program";
        String[] split = s.split(" ");
        Map<String, Integer> count = new HashMap<>();

        for (String b : split) {
            if (count.containsKey(b))
                count.put(b, count.get(b) + 1);
            else
                count.put(b, 1);
        }
        System.out.println(count);

    }
    @Test
    void litInterview2() {
        /*
        count number of occurences of all words i a string - approach 2
         */
        String s = "Welcome Welcome to the program";
        String[] split = s.split(" ");
        Map<String, Long> collect = Arrays.stream(split).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println(collect);

    }
}