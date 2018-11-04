package org.casual.regex;

import lombok.Getter;
import org.casual.util.FileUtil;
import org.casual.util.Pair;

import java.util.*;

/**
 * @author miaomuzhi
 * @since 2018/11/3
 */
@Getter
public class LexParser {

    private List<String> constants;
    /**
     * regex including patterns and non-patterns
     */
    private List<Pair<String, String>> regexWithRef = new ArrayList<>();

    /**
     * key is the Tag, value is the pair of Priority and Method
     */
    private Map<String, Pair<Integer, String>> tags = new HashMap<>();

    private String utilMethods;


    public void parseLexFile(String path){
        String content = removeComments(FileUtil.readFile(path));
        content = stripConstants(content);

        List<String> lines = new ArrayList<>(Arrays.asList(content.split("\n")));
        lines.removeIf(line -> line.isEmpty()||isBlank(line));
        lines = stripRegex(lines);

        utilMethods = lines.stream().reduce((prev, temp) -> prev.concat(System.lineSeparator()).concat(temp)).orElse("");
    }

    /**
     *
     * @param content content without comments
     * @return content without constants part
     */
    private String stripConstants(String content){
        int constBegin = content.indexOf("%{");
        int constEnd = content.indexOf("%}");
        if (constBegin+2 > constEnd){
            System.err.println("Malformed Manifest Constants");
        }

        String constantsPart = content.substring(constBegin+2, constEnd);
        constantsPart = constantsPart.trim();
        this.constants = new ArrayList<>(Arrays.asList(constantsPart.split(",")));
        return content.substring(constEnd+2);//remove the parsed part
    }

    private boolean isBlank(String str){
        for (Character ch: str.toCharArray()) {
            if (ch != ' ' && ch != '\t'){
                return false;
            }
        }
        return true;
    }

    private List<String> stripRegex(List<String> lines){
        int linePointer;

        boolean isInTransform = false;
        int count = 0;
        for (linePointer=0; linePointer<lines.size(); linePointer++) {
            String line = lines.get(linePointer).trim();
            if (line.equals("%%") && !isInTransform){
                isInTransform = true;
                continue;
            } else if (line.equals("%%")){//end of transform rules part
                break;
            }

            int firstBlank = line.indexOf(' ');
            String firstPart = line.substring(0, firstBlank);
            String secondPart = line.substring(firstBlank+1);

            secondPart = secondPart.replace("\\t", "\t").replace("\\n", "\n");

            if (isInTransform){
                regexWithRef.add(new Pair<>(removeConnect(firstPart), firstPart));//use the regex as its tag for the patterns
                tags.put(removeConnect(firstPart), new Pair<>(count++, secondPart));
            } else {
                regexWithRef.add(new Pair<>(firstPart, secondPart));
            }
        }
        return lines.subList(linePointer+1, lines.size());
    }

    private String removeComments(String strWithComments){
        return strWithComments.replaceAll("/\\*.*\\*/", "");
    }

    /**
     * get the tag of a regex in the transition rules part
     * @param srcRegex regex of the transition rules
     * @return tag of regex
     */
    private String removeConnect(String srcRegex){
        return srcRegex.replace("·", "");
    }
}
