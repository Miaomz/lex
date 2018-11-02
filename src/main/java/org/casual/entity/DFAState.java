package org.casual.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author miaomuzhi
 * @since 2018/11/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DFAState {

    private int id;

    private Map<String, Integer> transitions = new HashMap<>();

    private boolean accepted;

    private String tag;

    /**
     * ids of the nfa states, binding to a specific NFA
     */
    private Set<Integer> nfaStates;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DFAState dfaState = (DFAState) o;

        return Objects.equals(nfaStates, dfaState.nfaStates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nfaStates);
    }

    /**
     * reset the accepted and tag attributes by well-defined nfaStates
     * @param nfa the nfa transformed
     * @return isAccepted
     */
    public boolean refreshAcceptability(final NFA nfa){
        this.accepted = false;
        for (Integer nfaStateId : this.nfaStates) {
            NFAState nfaState = nfa.findById(nfaStateId);
            if (nfaState != null && nfaState.isAccepted()){
                this.accepted = true;
                this.tag = nfaState.getTag();
                break;
            }
        }
        return this.accepted;
    }

    @Override
    public String toString() {
        return "DFAState{" + System.lineSeparator() +
                "id=" + id + System.lineSeparator() +
                ", transitions=" + transitions + System.lineSeparator() +
                ", accepted=" + accepted + System.lineSeparator() +
                ", tag='" + tag + '\'' + System.lineSeparator() +
                ", nfaStates=" + nfaStates + System.lineSeparator() +
                '}';
    }
}
