package org.casual.dfa;

import org.casual.entity.DFA;
import org.casual.entity.DFAState;
import org.casual.entity.NFA;
import org.casual.regex.LexParser;
import org.casual.util.FileUtil;

import java.util.*;


/**
 * @author miaomuzhi
 * @since 2018/11/3
 */
public class CodeGenerator {

    public void generateCode(final DFA dfa, final NFA nfa, LexParser parser, String path){
        String content = FileUtil.readFile(getClass().getResource("/template/MyLexer.java").getPath());
        StringBuilder sb = new StringBuilder("enum Consts { ");
        for (int i = 0; i < parser.getConstants().size(); i++) {
            sb.append(parser.getConstants().get(i)).append((i==parser.getConstants().size()-1) ? '}' : ',');
        }
        content = content.replace("/*The util methods*/", parser.getUtilMethods()).replace("/*The constants*/", sb.toString());

        StringBuilder dataAdder = new StringBuilder(10000);
        for (int i = 0; i < dfa.calcSize(); i++) {
            addTransition(dfa, i, dataAdder);
            addTag(dfa, i, dataAdder, nfa, parser);
        }
        addPattern(parser, dataAdder);
        content = content.replace("/*The DFA data injection*/", dataAdder.toString());

        content = content.replace("/*The DFA return methods*/", addReturnMethods(parser));
        FileUtil.writeFile(path, content);
    }


    private void addTransition(final DFA dfa, final int i, final StringBuilder dataAdder){
        DFAState dfaState = dfa.findById(i);
        dataAdder.append("transitions.add(new HashMap<>());\n");
        for (Map.Entry<String, Integer> entry : dfaState.getTransitions().entrySet()) {
            if (!entry.getKey().equals(System.lineSeparator())){
                dataAdder.append("transitions.get(").append(i).append(").put(\"")
                        .append(entry.getKey()).append("\", ").append(entry.getValue()).append(");\n");
            } else {
                dataAdder.append("transitions.get(").append(i).append(").put(System.lineSeparator(), ").append(entry.getValue()).append(");\n");
            }
        }
    }

    private void addTag(final DFA dfa, final int i, final StringBuilder dataAdder, final NFA nfa, final LexParser parser){
        DFAState dfaState = dfa.findById(i);
        dataAdder.append("tags.add(new ArrayList<>());\n");
        Map<String, Integer> tags = new HashMap<>();
        for (Integer nfaId : dfaState.getNfaStates()) {
            String tag = nfa.findById(nfaId).getTag();
            if (tag != null && !tag.isEmpty()){
                tags.put(tag, parser.getTags().get(tag).getKey());
            }
        }
        List<String> sortedTags = map2List(tags);
        for (String sortedTag : sortedTags) {
            dataAdder.append("tags.get(").append(i).append(").add(\"").append(sortedTag).append("\");\n");
        }
    }

    private void addPattern(LexParser parser, StringBuilder sb){
        for (int i = parser.getRegexWithRef().size()-parser.getTags().size(); i < parser.getRegexWithRef().size(); i++) {
            sb.append("patterns.add(\"").append(parser.getRegexWithRef().get(i).getKey()).append("\");\n");
        }
    }

    private String addReturnMethods(final LexParser parser){
        StringBuilder methodAdder = new StringBuilder(10000);
        methodAdder.append("private static Consts getReturn(int index) {\n\tswitch(index){\n");
        for (int i = 0; i < parser.getTags().size(); i++) {
            int index = i + parser.getRegexWithRef().size() - parser.getTags().size();
            String method = parser.getTags().get(parser.getRegexWithRef().get(index).getKey()).getVal();
            methodAdder.append("case ").append(i).append(":").append(method).append(System.lineSeparator());
        }
        methodAdder.append("}return null;}").append(System.lineSeparator());
        return methodAdder.toString();
    }

    /**
     *
     * @param tags key is tag, value is priority
     * @return list which is ordered by priority(ASC)
     */
    private List<String> map2List(Map<String, Integer> tags){
        Integer[] priorities = new Integer[tags.size()];
        int count = 0;
        for (Map.Entry<String, Integer> entry : tags.entrySet()) {
            priorities[count++] = entry.getValue();
        }
        Arrays.sort(priorities);
        List<Integer> sortedPriorities = new ArrayList<>(Arrays.asList(priorities));

        String[] tagsList = new String[tags.size()];
        for (Map.Entry<String, Integer> entry : tags.entrySet()) {
            tagsList[sortedPriorities.indexOf(entry.getValue())] = entry.getKey();
        }
        return new ArrayList<>(Arrays.asList(tagsList));
    }

}
