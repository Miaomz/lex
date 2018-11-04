package org.casual.dfa;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author miaomuzhi
 * @since 2018/11/4
 */
public class CodeGeneratorTest {

    private CodeGenerator generator = new CodeGenerator();

    @Test
    @SuppressWarnings("unchecked")
    public void map2List() throws Exception{
        Method method = CodeGenerator.class.getDeclaredMethod("map2List", Map.class);
        method.setAccessible(true);
        Map<String, Integer> map = new HashMap<>();
        map.put("first", 1);
        map.put("third", 5);
        map.put("second", 2);
        List<String> tags = (List<String>) method.invoke(generator, map);
        assertEquals("[first, second, third]", tags.toString());
    }
}