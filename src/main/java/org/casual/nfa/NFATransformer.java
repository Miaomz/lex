package org.casual.nfa;

import org.casual.entity.DFA;
import org.casual.entity.DFAState;
import org.casual.entity.NFA;
import org.casual.entity.NFAState;
import org.casual.util.Pair;

import java.util.*;

/**
 * @author miaomuzhi
 * @since 2018/11/1
 */
public class NFATransformer {

    /**
     * transform nfa to dfa
     * @param nfa nfa, whose value should not be mutated during this method
     * @return dfa transformed
     */
    public DFA transform(final NFA nfa){
        Set<String> allTransitions = findAllTransitions(nfa);

        DFA dfa = new DFA();
        //calculate the I0
        Set<Integer> startStates = calcMuClosure(nfa, 0);
        DFAState dfaState = new DFAState();
        dfaState.setId(0);
        dfaState.setNfaStates(startStates);
        dfaState.refreshAcceptability(nfa);
        dfa.setStart(dfaState);
        dfa.addState(dfaState);

        int count = 0;
        while (true){
            DFAState state = dfa.findById(count++);//the state whose transitions will be calculated
            if (state == null){
                break;
            }

            for (String transition : allTransitions) {
                Set<Integer> nextIds = calcNext(nfa, state.getNfaStates(), transition);
                if (nextIds.isEmpty()){//otherwise it may be considered as new DFA state
                    continue;
                }

                DFAState nextState = dfa.findByNFAStates(nextIds);
                if (nextState == null){//need to add new DFA state
                    nextState = new DFAState(dfa.calcSize(), new HashMap<>(), false, nextIds);
                    dfa.addState(nextState);
                }

                //set transitions
                state.getTransitions().put(transition, nextState.getId());
            }
            state.refreshAcceptability(nfa);
        }

//        for (DFAState state : dfa.getDfaStates()) {
//            for (Integer integer : state.getNfaStates()) {
//                System.err.print(nfa.findById(integer).getTag() + " ");
//            }
//            System.err.println();
//        }
        return dfa;
    }


    /**
     * find all transitions except mu-transition that appear in the nfa
     * @param nfa nfa
     * @return all transitions except mu-transition
     */
    private Set<String> findAllTransitions(final NFA nfa){
        Set<String> allTransitions = new HashSet<>();
        for (NFAState state : nfa.getStates()) {
            for (Pair<String, Integer> pair : state.getTransitions()) {
                if (!pair.getKey().isEmpty()){
                    allTransitions.add(pair.getKey());
                }
            }
        }
        return allTransitions;
    }

    /**
     * get the nfa states derived by this transition, including nmu-closure
     * @param nfa constant nfa
     * @param prev ids of nfa states of one dfa state
     * @param transition target transition
     * @return nfa states derived
     */
    private Set<Integer> calcNext(final NFA nfa, final Set<Integer> prev, final String transition){
        Set<Integer> temp = new HashSet<>();
        for (Integer integer : prev) {
            for (Pair<String, Integer> pair : nfa.findById(integer).getTransitions()) {
                if (pair.getKey().equals(transition)){
                    temp.add(pair.getVal());
                }
            }
        }

        Set<Integer> closures = new HashSet<>();
        for (Integer integer : temp) {
            closures.addAll(calcMuClosure(nfa, integer));
        }
        closures.addAll(temp);
        return closures;
    }

    private Set<Integer> calcMuClosure(final NFA nfa, final int id){
        Set<Integer> result = new HashSet<>();
        Deque<Integer> toBeDerived = new LinkedList<>();
        toBeDerived.push(id);
        while (!toBeDerived.isEmpty()){
            Integer tempId = toBeDerived.pop();
            result.add(tempId);
            NFAState nfaState = nfa.findById(tempId);
            if (nfaState == null){//although it should be impossible
                System.err.println("State "+id+" doesn't exist");
                continue;
            }

            for (Pair<String, Integer> pair : nfaState.getTransitions()) {
                if (pair.getKey().isEmpty() && !result.contains(pair.getVal())){//in case of infinite loop
                    toBeDerived.push(pair.getVal());
                }
            }
        }
        return result;
    }
}
