package kr.co._29cm.homework;

import kr.co._29cm.homework.databse.MasterDatabase;
import kr.co._29cm.homework.databse.SlaveDatabase;
import kr.co._29cm.homework.service.PromptService;
public class Main {
    public static void main(String[] args){
        initDatabase();
        PromptService promptService = new PromptService();
        promptService.initPrompt();

    }

    private static void initDatabase(){
        MasterDatabase.getInstance();
        SlaveDatabase.getInstance();
    }
}
