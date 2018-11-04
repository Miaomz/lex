package org.casual.dfa;

import org.casual.entity.DFA;
import org.casual.entity.DFAState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author miaomuzhi
 * @since 2018/11/4
 */
public class IdRegularizer {

    public void regularize(final DFA dfa){
        Integer[] ids = new Integer[dfa.calcSize()];
        for (int i = 0; i < dfa.calcSize(); i++) {
            ids[i] = dfa.getDfaStates().get(i).getId();
        }

        Arrays.sort(ids);
        List<Integer> sortedIds = new ArrayList<>(Arrays.asList(ids));
        for (DFAState state : dfa.getDfaStates()) {
            state.setId(sortedIds.indexOf(state.getId()));
            for (Map.Entry<String, Integer> entry : state.getTransitions().entrySet()) {
                state.getTransitions().put(entry.getKey(), sortedIds.indexOf(entry.getValue()));
            }
        }
    }
}
