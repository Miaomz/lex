package org.casual.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.casual.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author miaomuzhi
 * @since 2018/10/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NFAState {

    /**
     * the size of NFA states before it is added
     */
    private int id;

    /**
     * the key is the edge, the value is the id of next state
     */
    private List<Pair<String, Integer>> transitions = new ArrayList<>();

    private boolean accepted;

    /**
     * only valid when it is accepted
     */
    private String tag;

    public NFAState(int id, List<Pair<String, Integer>> transitions, boolean accepted) {
        this.id = id;
        this.transitions = transitions;
        this.accepted = accepted;
    }

}
