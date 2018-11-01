package org.casual.regex;

import org.casual.util.Pair;

import java.util.List;

import static org.casual.util.ConstantString.WARNING;

/**
 * @author miaomuzhi
 * @since 2018/11/1
 */
public class PreProcess {

    /**
     * replace the reference with basic elements in regex;
     * key is the tag, and the value is the regular expression;
     * the order of regex is very similar to the declaration of function in C/C++
     *
     * @param regexWithRef a list of regex which contains reference closed by curly brackets
     */
    public void solveRef(List<Pair<String, String>> regexWithRef){
        for (int i = 0; i < regexWithRef.size(); i++) {
            String regex = regexWithRef.get(i).getVal();
            int openIndex;
            while ((openIndex = findRef(regex)) >= 0){
                int closeIndex;//if index > 0, the close bracket/ref must exist
                for (closeIndex = openIndex+1; closeIndex < regex.length(); closeIndex++) {
                    if (regex.charAt(closeIndex) == '}'){
                        break;
                    }
                }

                regex = replaceSpecificRef(regex, openIndex, closeIndex, regexWithRef, i);
            }

            regexWithRef.get(i).setVal(regex);
        }
    }

    /**
     * find the index of ref
     * @param regex regex
     * @return index of the ref's open bracket, -1 if it's absent
     */
    private int findRef(String regex){
        int i = regex.indexOf('{');
        if (i == -1 || regex.indexOf('}') > i){
            return i;
        } else {//irregular
            System.err.println(WARNING);
            return -1;
        }
    }

    /**
     * replace one certain reference in the regex
     * @param regex the regex to be changed
     * @param openIndex index of open bracket
     * @param closeIndex index of close bracket
     * @param regexWithRef all the regex
     * @param regexIndex the temp point to the regex
     * @return replaced regex
     */
    private String replaceSpecificRef(String regex, final int openIndex, final int closeIndex,
                                      final List<Pair<String, String>> regexWithRef, final int regexIndex){
        String ref = regex.substring(openIndex+1, closeIndex);

        boolean isReplaced = false;
        for (int j = 0; j < regexIndex; j++) {
            if (regexWithRef.get(j).getKey().equals(ref)){
                regex = regex.substring(0, openIndex)
                        .concat("(")
                        .concat(regexWithRef.get(j).getVal())
                        .concat(")")
                        .concat(regex.substring(closeIndex+1));
                isReplaced = true;
                break;
            }
        }

        if (!isReplaced){
            System.err.println("Regular expression " + (regexIndex+1) + " contains undefined reference");
            System.exit(1);
        }
        return regex;
    }
}
