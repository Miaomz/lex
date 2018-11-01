package org.casual.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author miaomuzhi
 * @since 2018/10/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NFA {

    private NFAState start;

    private final List<NFAState> states = new ArrayList<>();

    public int calcSize(){
        return states.size();
    }

    /**
     * find the state of given id
     * @param id id of nfa state
     * @return state, null if it doesn't exist
     */
    public NFAState findById(int id){
        for (NFAState state : states) {
            if (state.getId() == id){
                return state;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < calcSize(); i++) {
            sb.append(i).append(findById(i).getTransitions().toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
