package org.casual.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.casual.util.Pair;

import java.util.List;

/**
 * @author miaomuzhi
 * @since 2018/10/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NFAState {

    private long id;

    /**
     * the key is the edge, the value is the id of next state
     */
    private List<Pair<String, Long>> transitions;

    boolean accepted;
}
