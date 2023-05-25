package kr.co._29cm.homework;

import kr.co._29cm.homework.databse.MasterDatabase;
import kr.co._29cm.homework.databse.SlaveDatabase;

import java.io.FileNotFoundException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {
        initDatabase();
    }

    private static void initDatabase() throws FileNotFoundException, ClassNotFoundException {
        URL masterYamlUrl = Main.class.getClassLoader().getResource("config/master_db.yaml");
        String masterYamlPath = masterYamlUrl.getFile();
        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        masterDatabase.setConnectInfo(masterYamlPath);

        URL slaveYamlUrl = Main.class.getClassLoader().getResource("slave_db.yaml");
        String slaveYamlPath = slaveYamlUrl.getFile();
        SlaveDatabase slaveDatabase = SlaveDatabase.getInstance();
        slaveDatabase.setConnectInfo(slaveYamlPath);
    }
}
