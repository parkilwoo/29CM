package kr.co._29cm.homework.util;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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

        String convert = matcher.replaceAll(matchResult -> String.format("%s_%s", matchResult.group(1), matchResult.group(2)));

        return convert.toUpperCase();
    }

    public static List<String> readFileConvertStringList(String filePath, boolean firstLineRemove) {
        List<String> stringList = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            if (firstLineRemove) br.readLine();
            while ((line = br.readLine()) != null) {
                stringList.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(filePath + "읽는중에 에러 발생");
        }

        return stringList;
    }


    public static String arrayMergeToString(String[] StringArray) {
        /*
            csv파일중 상품명에 ','가 포함된 상품이 있어서 처리하기 위한 UtilsMethod
         */
        List<String> strList = new LinkedList<>();
        strList.addAll(Arrays.asList(StringArray).subList(1, StringArray.length - 2));
        return String.join(",", strList);
    }

    public static boolean isOnlyDigitOrSpace(String checkValue) {
        return checkValue.matches("^\\s*$|\\d*$");
    }

    public static String convertWon(int digit) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###원");
        return decimalFormat.format(digit);  // 변환된 원화 문자열
    }
}
