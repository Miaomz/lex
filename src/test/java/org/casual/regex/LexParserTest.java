package org.casual.regex;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

/**
 * @author miaomuzhi
 * @since 2018/11/3
 */
public class LexParserTest {

    private LexParser lexParser;

    @Before
    public void setUp() throws Exception {
        lexParser = new LexParser();
    }

    @Test
    public void parseLexFile() {
        lexParser.parseLexFile(getClass().getResource("/test.l").getPath());
        assertEquals(9, lexParser.getTags().size());
    }

    @Test
    public void removeComments() throws Exception{
        Method method = LexParser.class.getDeclaredMethod("removeComments", String.class);
        method.setAccessible(true);
        String removed = (String) method.invoke(lexParser, "fsadf/*fasd, fasdf, fasg, gsg*/fsdf" + System.lineSeparator() +
                "kljl /* ,sfsf *f fsdf */");
        assertEquals("fsadffsdf\nkljl ", removed);
    }
}