package org.casual.entity;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * @author miaomuzhi
 * @since 2018/11/2
 */
public class DFAStateTest {

    @Test
    public void equals() {
        DFAState state1 = new DFAState();
        DFAState state2 = new DFAState();
        state1.setNfaStates(new HashSet<>(Arrays.asList(1, 3, 4)));
        state2.setNfaStates(new HashSet<>(Arrays.asList(1, 3, 4)));
        assertEquals(true, state1.equals(state2));

        state2.setNfaStates(new HashSet<>(Arrays.asList(3, 4)));
        assertEquals(false, state1.equals(state2));

        state2.setNfaStates(new HashSet<>(Arrays.asList(3, 4, 1)));
        assertEquals(true, state1.equals(state2));
    }
}