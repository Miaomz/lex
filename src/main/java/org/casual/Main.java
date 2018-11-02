package org.casual;

import org.casual.regex.RegexParser;

/**
 * @author miaomuzhi
 * @since 2018/10/31
 */
public class Main {

    public static void main(String[] args){
        System.out.println(new RegexParser().regexToNFA("(a)·(a|b)*", "third"));
    }
}
