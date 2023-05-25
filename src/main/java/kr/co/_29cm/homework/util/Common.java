package kr.co._29cm.homework.util;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {
    static final Pattern camelCasePattern = Pattern.compile("([a-z])([A-Z])");

    public static<T> T getObjectToYaml(String yamlFilePath, Class<T> objectClass) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        FileInputStream inputStream = new FileInputStream(yamlFilePath);

        return yaml.loadAs(inputStream, objectClass);
    }

    public static String camelCaseToSnakeCase(String camelCaseString) {

        Matcher matcher = camelCasePattern.matcher(camelCaseString);

        String convert = matcher.replaceAll(matchResult -> {
            return String.format("%s_%s", matchResult.group(1), matchResult.group(2));
        });

        return convert.toUpperCase();
    }
}
