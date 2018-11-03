package org.casual.dfa;

import org.casual.entity.DFA;
import org.casual.nfa.NFATransformer;
import org.casual.regex.RegexParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author miaomuzhi
 * @since 2018/11/2
 */
public class DFASimplifierTest {

    private RegexParser parser = new RegexParser();
    private NFATransformer nfaTransformer = new NFATransformer();
    private DFASimplifier simplifier = new DFASimplifier();

    @Test
    public void simplifyDFA() {
        DFA dfa = simplifier.simplifyDFA(nfaTransformer.transform(parser.regexToNFA("(a|b)*·a·(a|b)·(a|b)", "org")));
        assertEquals(8, dfa.calcSize());
        dfa = simplifier.simplifyDFA(nfaTransformer.transform(parser.regexToNFA("(a|b)*·a·(a|b)·(a|b)·(a|b)", "org")));
        assertEquals(16, dfa.calcSize());
        dfa = simplifier.simplifyDFA(nfaTransformer.transform(parser.regexToNFA("(a|b)*·a·(a|b)", "org")));
        assertEquals(4, dfa.calcSize());
    }
}