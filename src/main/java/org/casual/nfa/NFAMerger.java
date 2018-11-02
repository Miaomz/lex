package org.casual.nfa;

import org.casual.entity.NFA;
import org.casual.entity.NFAState;
import org.casual.util.Pair;

import java.util.List;

/**
 * @author miaomuzhi
 * @since 2018/11/1
 */
public class NFAMerger {

    public NFA mergeNFA(List<NFA> nfaList){
        NFAState starter = new NFAState();
        starter.setId(0);
        NFA mergedNFA = new NFA();
        mergedNFA.setStart(starter);
        mergedNFA.getStates().add(starter);

        for (NFA nfa : nfaList) {
            int tempSize = mergedNFA.calcSize();
            nfa.getStates().forEach(state -> {
                state.setId(state.getId()+tempSize);
                state.getTransitions().forEach(pair -> pair.setVal(pair.getVal()+tempSize));
            });


            starter.getTransitions().add(new Pair<>("", nfa.getStart().getId()));
            mergedNFA.getStates().addAll(nfa.getStates());
        }
        return mergedNFA;
    }
}
