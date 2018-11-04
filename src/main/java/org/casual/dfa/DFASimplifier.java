package org.casual.dfa;

import org.casual.entity.DFA;
import org.casual.entity.DFAState;

import java.util.*;

import static org.casual.util.ConstantString.EXCEPTION;

/**
 * @author miaomuzhi
 * @since 2018/11/2
 */
public class DFASimplifier {

    public DFA simplifyDFA(final DFA dfa){
        List<Set<DFAState>> stateSets = getStateSets(dfa);
        return buildDFABySets(stateSets);
    }

    /**
     *
     * @param dfa dfa
     * @return split subsets' list
     */
    private List<Set<DFAState>> getStateSets(final DFA dfa){
        List<Set<DFAState>> stateSets = buildInitialSets(dfa);
        final Set<String> allTransitions = dfa.findAllTransition();

        boolean isAnySetDividable = true;
        while (isAnySetDividable){
            isAnySetDividable = false;//loop until no division is made in the iteration

            int count = 0;
            while (count < stateSets.size()){
                boolean isSetPossible = false;
                for (String transition : allTransitions) {
                    if (splitSets(stateSets, count, transition)){
                        isSetPossible = true;
                        isAnySetDividable = true;
                    }
                }

                if (!isSetPossible){
                    count ++;
                }
            }
        }
        return stateSets;
    }

    private DFA buildDFABySets(List<Set<DFAState>> stateSets){
        DFA simplifiedDfa = new DFA();//in the following steps, we will always choose the smallest one as the representative
        for (Set<DFAState> stateSet : stateSets) {
            DFAState representative = new DFAState((DFAState) stateSet.toArray()[0]);//ensure that the old data is effectively final
            representative.setId(findMinIdInEquSet(stateSet, representative.getId()));//choose id

            for (DFAState dfaState : stateSet) {//choose tags
                representative.getNfaStates().addAll(dfaState.getNfaStates());
            }

            for (Map.Entry<String, Integer> entry : representative.getTransitions().entrySet()) {//choose transitions
                for (Set<DFAState> set : stateSets) {
                    int minId;
                    if ((minId = findMinIdInEquSet(set, entry.getValue())) >= 0){
                        representative.getTransitions().put(entry.getKey(), minId);
                    }
                }
            }

            if (representative.getId() == 0){
                simplifiedDfa.setStart(representative);
            }
            simplifiedDfa.addState(representative);
        }
        return simplifiedDfa;
    }

    private List<Set<DFAState>> buildInitialSets(final DFA dfa){
        List<Set<DFAState>> stateSets = new ArrayList<>(Collections.singleton(new HashSet<>()));
        for (DFAState state : dfa.getDfaStates()) {
            if (!state.isAccepted()){
                stateSets.get(0).add(state);
            } else {
                boolean isAdded = false;
                for (int i = 1; i < stateSets.size(); i++) {
                    if ((stateSets.get(i).toArray()[0]).equals(state)){//no stateSet is empty except the first one
                        stateSets.get(i).add(state);
                        isAdded = true;
                        break;
                    }
                }

                if (!isAdded){
                    stateSets.add(new HashSet<>(Collections.singleton(state)));
                }
            }
        }
        return stateSets;
    }

    /**
     * split one specific set with one specific transition
     * @param stateSets the sets of DFAStates in the prev level
     * @param index index of the set to be split
     * @param transition the transition will be used
     * @return if the set has been split
     */
    private boolean splitSets(final List<Set<DFAState>> stateSets, final int index, final String transition){
        Set<DFAState> toBeSplit = stateSets.get(index);

        List<Set<DFAState>> splits = new ArrayList<>(stateSets.size()+1);//this lists will replace toBeSplit
        for (int i = 0; i < stateSets.size()+1; i++) {
            splits.add(new HashSet<>());
        }

        for (DFAState state : toBeSplit) {
            Integer nextId = state.getTransitions().get(transition);
            if (nextId == null){//corresponds to empty set
                splits.get(0).add(state);
            } else {
                int belongedSetId = findBelongedSet(stateSets, nextId);
                splits.get(belongedSetId+1).add(state);
            }
        }

        splits.removeIf(Set::isEmpty);
        if (splits.size() > 1){
            stateSets.remove(toBeSplit);
            stateSets.addAll(index, splits);
        }
        return splits.size() > 1;
    }

    private int findBelongedSet(final List<Set<DFAState>> stateSets, int id){
        for (int i = 0; i < stateSets.size(); i++) {
            for (DFAState state : stateSets.get(i)) {
                if (state.getId() == id){
                    return i;
                }
            }
        }
        System.err.println(EXCEPTION);
        return -1;
    }

    private int findMinIdInEquSet(Set<DFAState> states, final int id){
        boolean isIdIn = false;
        int tempMin = id;
        for (DFAState state : states) {
            if (state.getId() == id){
                isIdIn = true;
            } else if (state.getId() < tempMin){
                tempMin = state.getId();
            }
        }
        return isIdIn ? tempMin : -1;
    }
}
