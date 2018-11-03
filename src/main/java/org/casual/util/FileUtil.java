package org.casual.util;

import java.io.*;

/**
 * @author miaomuzhi
 * @since 2018/11/3
 */
public class FileUtil {

    private FileUtil() {}

    public static String readFile(String path) {
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

    public static ResultMessage writeFile(String path, String content) {
        File file = new File(path);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            return ResultMessage.FAILURE;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (!file.exists() && !file.createNewFile()){
                return ResultMessage.FAILURE;
            }

            writer.write(content);
            writer.flush();
            return ResultMessage.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ResultMessage.FAILURE;
        }
    }
}
