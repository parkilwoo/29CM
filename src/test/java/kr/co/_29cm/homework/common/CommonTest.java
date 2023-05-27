package kr.co._29cm.homework.common;


import kr.co._29cm.homework.Main;
import kr.co._29cm.homework.databse.ConnectInfo;
import kr.co._29cm.homework.util.Common;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class CommonTest {

    @Test
    @DisplayName("getObjectToYaml Test")
    public void getObjectToYamlTest() throws FileNotFoundException {
        final URL masterYamlUrl = Main.class.getClassLoader().getResource("config/master_db.yaml");
        final String masterYamlPath = Objects.requireNonNull(masterYamlUrl).getPath();

        ConnectInfo connectInfo = Common.getObjectToYaml(masterYamlPath, ConnectInfo.class);
        assert connectInfo.getDriverClassName().equals("org.h2.Driver");
        assert connectInfo.getUrl().equals("jdbc:h2:mem:29cm_master_db;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;");
        assert connectInfo.getUsername().equals("sa");
        assert connectInfo.getPassword().equals("password29");
    }

    @Test
    @DisplayName("camelCaseToSnakeCase Test")
    public void camelCaseToSnakeCaseTest() {
        assert Common.camelCaseToSnakeCase("productId").equals("PRODUCT_ID");
    }

    @Test
    @DisplayName("readFileConvertStringList Test")
    public void readFileConvertStringListTest() {
        final URL testUrl =getClass().getClassLoader().getResource("test.csv");
        final String testUrlPath = Objects.requireNonNull(testUrl).getPath();

        List<String> test = Common.readFileConvertStringList(testUrlPath, false);
        assert test.size() == 2;
        assert test.get(0).equals("TEST제목");
        assert test.get(1).equals("TEST자료");

        List<String> test2 = Common.readFileConvertStringList(testUrlPath, true);
        assert test2.size() == 1;
        assert test2.get(0).equals("TEST자료");
    }

    @Test
    @DisplayName("arrayMergeToString Test")
    public void arrayMergeToStringTest() {
        String testString = "779049,\"[리퍼브/키친마켓] Fabrik Pottery Cup, Saucer (단품)\",10000,64";
        String[] testStringArray = testString.split(",");
        assert Common.arrayMergeToString(testStringArray).equals("\"[리퍼브/키친마켓] Fabrik Pottery Cup, Saucer (단품)\"");
    }

    @Test
    @DisplayName("isOnlyDigitOrSpace Test")
    public void isOnlyDigitOrSpaceTest() {
        assert Common.isOnlyDigitOrSpace(" ");
        assert Common.isOnlyDigitOrSpace("1");
        assert !Common.isOnlyDigitOrSpace("ab");
    }

    @Test
    @DisplayName("convertWon Test")
    public void convertWon() {
        assert Common.convertWon(99999).equals("99,999원");
    }
}
