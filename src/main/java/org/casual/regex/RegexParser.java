package org.casual.regex;

import org.casual.entity.NFA;
import org.casual.entity.NFAState;
import org.casual.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.casual.util.ConstantString.WARNING;

/**
 * @author miaomuzhi
 * @since 2018/10/31
 */
public class RegexParser {

    /**
     * main function of this component
     * @param regex presuming that all ·(connects) have been added, {ref} has been replaced
     * @return NFA of this regex
     */
    public NFA regexToNFA(final String regex, final String tag){
        NFA nfa = new NFA();
        nfa.setStart(new NFAState(0, new ArrayList<>(Collections.singleton(new Pair<>(regex, 1))), false));
        nfa.getStates().addAll(Arrays.asList(nfa.getStart(), new NFAState(1, new ArrayList<>(), true, tag)));

        int count = 0;
        while (count < nfa.calcSize()){
            NFAState temp = nfa.findById(count++);
            while (isDividable(temp)){
                refine(nfa, temp);
            }
        }
        return nfa;
    }

    /**
     * refine the transitions
     * @param nfa nfa
     * @param state the state whose transitions will be refined
     */
    private void refine(final NFA nfa, final NFAState state){
        List<Pair<String, Integer>> updatedTransitions = new ArrayList<>();//we will reset the transitions later

        for (Pair<String, Integer> pair : state.getTransitions()) {
            List<String> splits = splitRegex(pair.getKey());
            switch (splits.get(0)){
                case "|":
                    updatedTransitions.addAll(Arrays.asList(new Pair<>(splits.get(1), pair.getVal()), 
                        new Pair<>(splits.get(2), pair.getVal())));
                    break;
                case "·":
                    NFAState mediaState = new NFAState(nfa.calcSize(),
                        new ArrayList<>(Collections.singleton(new Pair<>(splits.get(2), pair.getVal()))), false);
                    updatedTransitions.add(new Pair<>(splits.get(1), nfa.calcSize()));
                    nfa.getStates().add(mediaState);
                    break;
                case "*":
                    NFAState loopState = new NFAState(nfa.calcSize(),
                            new ArrayList<>(Arrays.asList(new Pair<>("", pair.getVal()), new Pair<>(splits.get(1), nfa.calcSize()))),
                            false);
                    updatedTransitions.add(new Pair<>("", nfa.calcSize()));
                    nfa.getStates().add(loopState);
                    break;
                default://the atom or reference
                    updatedTransitions.add(new Pair<>(splits.get(0), pair.getVal()));
                    break;
            }
        }
        state.setTransitions(updatedTransitions);
    }

    /**
     * try to split regex into small parts
     * @param regex all ·s have been added
     * @return the first element will be operator if it exists, then the operands;
     * if there is no operator, it will be only one element
     */
    private List<String> splitRegex(String regex){
        while (regex.startsWith("(")
                && (getCorrespondingClose(regex, 0) == regex.length()-1)){//strip the OUTER parentheses if possible
            regex = regex.substring(1, regex.length()-1);
        }

        for (Character character : Arrays.asList('|', '·', '*')) {//ordered by priority
            List<String> split = splitRegexWithOp(regex, character);
            if (!split.isEmpty()){
                return split;
            }
        }

        //if the program reaches here, then the regex should be inalienable
        if (isDividable(regex)){
            System.err.println(WARNING);
        }
        return new ArrayList<>(Collections.singleton(regex));
    }

    /**
     * split regex with certain expression and operator
     * @param regex regex, it is at least not surrounded by parentheses
     * @param character operator
     * @return the strings, but empty list if it fails to split
     */
    private List<String> splitRegexWithOp(final String regex, final Character character){
        for (int i = 0; i < regex.length(); i++) {
            char ch = regex.charAt(i);
            if (ch == '('){
                int jumpLen = getCorrespondingClose(regex, i);
                if (jumpLen >= 0){
                    i = jumpLen;//let the pointer escape the parentheses
                }
            } else if (ch == character && (ch == '|' || ch == '·')){
                return new ArrayList<>(Arrays.asList(String.valueOf(ch), regex.substring(0, i), regex.substring(i+1)));
            } else if (ch == character && ch == '*'){//* is a relatively special operator
                return new ArrayList<>(Arrays.asList("*", regex.substring(0, regex.length()-1)));
            }
        }
        return new ArrayList<>();
    }

    /**
     * judge if there are any transitions from this state could be divided
     * @param state state
     * @return if a state can be divided
     */
    private boolean isDividable(final NFAState state){
        if (state == null){
            return false;
        }

        for (Pair<String, Integer> stringIntegerPair : state.getTransitions()) {
            if (isDividable(stringIntegerPair.getKey())){
                return true;
            }
        }
        return false;
    }

    /**
     * judge if a regex can be divided
     * @param regex regex to be tested
     * @return if a regex can be divided
     */
    private boolean isDividable(final String regex){
        return regex != null && regex.length() > 1;
    }

    /**
     * find a bracket's pair, including round, rectangular and curly brackets
     * @param s string
     * @param openIndex the index of open symbol
     * @return the index of close symbol, -1 if they fail to find matching one
     */
    private int getCorrespondingClose(final String s, final int openIndex){
        List<Character> openList = new ArrayList<>(Arrays.asList('(', '{', '['));
        List<Character> closeList = new ArrayList<>(Arrays.asList(')', '}', ']'));

        char open = s.charAt(openIndex);
        char close = closeList.get(openList.indexOf(open));
        int count = 1;
        for (int i = openIndex+1; i < s.length(); i++) {
            if (s.charAt(i) == open){
                count ++;
            } else if (s.charAt(i) == close){
                count --;
            }

            if (count == 0){
                return i;
            }
        }
        return -1;
    }
}
