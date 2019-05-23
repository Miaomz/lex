package lexer;

import java.io.*;
import java.util.*;

import static lexer.MyLexer.Consts.*;

/**
 * @author miaomuzhi
 * @since 2018/11/3
 */
public class MyLexer {

    /**
     * index in the list is id, String key is the transition, Integer value is the index that transitions points to;
     * the first is the
     */
    private static List<Map<String, Integer>> transitions = new ArrayList<>();
    /**
     * index in the list is id, Strings is the tags of this DFA state(empty set means that this state is not accepted)
     * order by priority DESC
     */
    private static List<List<String>> tags = new ArrayList<>();

    /**
     * index is the priority, String is the tag of pattern
     */
    private static List<String> patterns = new ArrayList<>();

    public static void main(String[] args){
        if (args.length != 2){
            System.err.println("Illegal arguments");
            return;
        }

        addData();

        StringBuilder output = new StringBuilder();

        String content = readFile(args[0]);
        int outerPointer = 0;
        while (outerPointer < content.length()){
            int innerPointer = outerPointer;

            int prev = -1;
            Integer nextId = 0;//start state
            while (innerPointer < content.length()
                    && (nextId = transitions.get(nextId).get(String.valueOf(content.charAt(innerPointer)))) != null){
                prev = nextId;
                innerPointer ++;
            }

            if (prev == -1 || tags.get(prev).isEmpty()){//no pattern is matched
                System.err.println("ILLEGAL PATTERN: " + content.substring(outerPointer, innerPointer));
                return;
            }

            String tag = tags.get(prev).get(0);
            Consts consts = getReturn(patterns.indexOf(tag));//prev is NOT the index of tag in LEX PROGRAM
            if (consts.ordinal() != 0){
                output.append('<').append(consts).append(", ").append(content.substring(outerPointer, innerPointer)).append(">\n");
            }
            outerPointer = innerPointer;
        }

        writeFile(args[1], output.toString());
    }

    private static void addData(){
        /*The DFA data injection*/
    }

    private static String readFile(String path) {
        File file = new File(path);
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private static void writeFile(String path, String content) {
        File file = new File(path);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (!file.exists() && !file.createNewFile()){
                return;
            }
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*The DFA return methods*/

    /*The util methods*/

    /*The constants*/
}

