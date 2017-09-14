package com.algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * 含有2次某字符串的最短字符串
 * 输入 abcab
 *          abcab
 * 输出  abcabcab
 * Created by xuhandong on 2017/9/8/008.
 */
public class Demo1 {
    public static void main(String[] args) throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        String temp = s;
        int i;
        loop1 : for(i = 1 ; i < s.length() ; i ++ ){
            int j;
            loop2: for(j = i ; j < s.length() ; j ++ ){
                if(s.charAt(j) != temp.charAt(j-i)){
                    break loop2;
                }
            }
            if(j == s.length()){
                break loop1;
            }
        }
        String result = s.substring(0, i).concat(temp);
        System.out.println(result);

    }
}
