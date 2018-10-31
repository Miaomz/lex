package org.casual.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private List<NFAState> states;

}
