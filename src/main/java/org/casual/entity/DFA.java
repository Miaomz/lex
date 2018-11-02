package org.casual.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author miaomuzhi
 * @since 2018/11/1
 */
@Data
public class DFA {

    private DFAState start;

    private final List<DFAState> dfaStates = new ArrayList<>();


    public int calcSize(){
        return dfaStates.size();
    }

    /**
     * find the state of given id
     * @param id id of nfa state
     * @return state, null if it doesn't exist
     */
    public DFAState findById(int id){
        for (DFAState state : dfaStates) {
            if (state.getId() == id){
                return state;
            }
        }
        return null;
    }

    /**
     * find the state by given nfa states' set
     * @param set nfa states
     * @return corresponding dfa state, null if it doesn't exist
     */
    public DFAState findByNFAStates(Set<Integer> set){
        for (DFAState dfaState : dfaStates) {
            if (dfaState.getNfaStates().equals(set)){
                return dfaState;
            }
        }
        return null;
    }

    public void addState(DFAState state){
        this.dfaStates.add(state);
    }
}
