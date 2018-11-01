package org.casual.regex;

import org.casual.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author miaomuzhi
 * @since 2018/11/1
 */
public class PreProcessTest {

    private PreProcess preProcess;

    @Before
    public void setUp() throws Exception {
        preProcess = new PreProcess();
    }

    @Test
    public void solveRef() {
        List<Pair<String, String>> raw = new ArrayList<>(Arrays.asList(new Pair<>("digit", "1|2|3|4|5|6|7|8|9|0"),
                new Pair<>("number", "{digit}·{digit}*")));
        preProcess.solveRef(raw);
        assertEquals("(1|2|3|4|5|6|7|8|9|0)·(1|2|3|4|5|6|7|8|9|0)*", raw.get(1).getVal());

        raw.add(new Pair<>("decimal", "{number}·.·{number}"));
        preProcess.solveRef(raw);
        assertEquals("((1|2|3|4|5|6|7|8|9|0)·(1|2|3|4|5|6|7|8|9|0)*)·.·((1|2|3|4|5|6|7|8|9|0)·(1|2|3|4|5|6|7|8|9|0)*)", raw.get(2).getVal());
    }
}