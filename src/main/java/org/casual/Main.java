package org.casual;

import org.casual.dfa.CodeGenerator;
import org.casual.dfa.DFASimplifier;
import org.casual.dfa.IdRegularizer;
import org.casual.entity.DFA;
import org.casual.entity.NFA;
import org.casual.nfa.NFAMerger;
import org.casual.nfa.NFATransformer;
import org.casual.regex.LexParser;
import org.casual.regex.PreProcess;
import org.casual.regex.RegexParser;
import org.casual.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author miaomuzhi
 * @since 2018/10/31
 */
public class Main {

    public static void main(String[] args){
        if (args.length != 2){
            System.err.println("Illegal arguments");
            return;
        }

        String path = args[0];
        LexParser lexParser = new LexParser();
        lexParser.parseLexFile(path);

        new PreProcess().solveRef(lexParser.getRegexWithRef());

        List<NFA> nfaList = new ArrayList<>(lexParser.getTags().size());
        RegexParser regexParser = new RegexParser();
        int firstPatternIndex = lexParser.getRegexWithRef().size() - lexParser.getTags().size();
        for (; firstPatternIndex < lexParser.getRegexWithRef().size(); firstPatternIndex++) {
            Pair<String, String> pair = lexParser.getRegexWithRef().get(firstPatternIndex);
            nfaList.add(regexParser.regexToNFA(pair.getVal(), pair.getKey()));
        }

        NFA merged = new NFAMerger().mergeNFA(nfaList);

        DFA dfa = new NFATransformer().transform(merged);

        dfa = new DFASimplifier().simplifyDFA(dfa);
        new IdRegularizer().regularize(dfa);
        System.out.println(dfa);
        new CodeGenerator().generateCode(dfa, merged, lexParser, args[1]);
    }
}
