package org.casual.nfa;

import org.casual.entity.DFA;
import org.casual.entity.NFA;
import org.casual.regex.RegexParser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author miaomuzhi
 * @since 2018/11/2
 */
public class NFATransformerTest {

    private NFAMerger merger = new NFAMerger();
    private RegexParser parser = new RegexParser();
    private NFATransformer nfaTransformer = new NFATransformer();

    @Test
    public void transform() {
        NFA nfa1 = parser.regexToNFA("a·(a|b)*", "first");
        NFA nfa2 = parser.regexToNFA("((|a)·b*)*", "second");
        NFA merged = merger.mergeNFA(new ArrayList<>(Arrays.asList(nfa1, nfa2)));
        DFA dfa = nfaTransformer.transform(merged);
        assertEquals(true, dfa.getStart().isAccepted());

        dfa = nfaTransformer.transform(parser.regexToNFA("(a|b)*·a·(a|b)·(a|b)", "casual"));
        assertEquals(9, dfa.calcSize());

        dfa = nfaTransformer.transform(parser.regexToNFA("(a|b)*·(a·a|b·b)·(a|b)*", "org"));
        assertEquals(7, dfa.calcSize());
    }
}