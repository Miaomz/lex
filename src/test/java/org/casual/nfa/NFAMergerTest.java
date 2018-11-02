package org.casual.nfa;

import org.casual.entity.NFA;
import org.casual.regex.RegexParser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author miaomuzhi
 * @since 2018/11/1
 */
public class NFAMergerTest {

    private NFAMerger merger;
    private RegexParser parser;

    @Before
    public void setUp() throws Exception {
        merger = new NFAMerger();
        parser = new RegexParser();
    }

    @Test
    public void mergeNFA() {
        NFA nfa1 = parser.regexToNFA("a·(a|b)*", "first");
        NFA nfa2 = parser.regexToNFA("((|a)·b*)*", "second");
        NFA merged = merger.mergeNFA(new ArrayList<>(Arrays.asList(nfa1, nfa2)));
        assertEquals(10, merged.calcSize());
    }
}