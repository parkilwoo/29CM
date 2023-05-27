package kr.co._29cm.homework.domain;

import kr.co._29cm.homework.util.Common;

import java.util.Map;
import java.util.Scanner;

public class Prompt {

    private final String inputPrompt = "입력(o[order]:주문, q[quit]: 종료) : ";
    private final String notValidInputPrompt = "%s은(는) 잘못된 입력입니다.";
    private final String productsPrompt = "상품번호   상품명   판매가격   재고수\n";

    private final String quitePrompt = "사용자의 요청에 의해 종료합니다.";

    private final String orderPrompt = "%s : ";
    private final String onlyDigitOrSpacePrompt = "%s은(는) 숫자, 공백만 입력이 가능합니다.";
    private final String attributeRequiredPrompt = "%s은(는) 필수값입니다.";

    private final String orderHistoryPrompt = "주문내역:\n";
    private final String orderHistoryTemplatePrompt = "-----------------------------------------------------------------\n";
    private final Scanner scanner = new Scanner(System.in);

    public void displayMessagePrompt(String message) {
        System.out.println(message);
    }


    public String getValueFromPrompt() {
        return scanner.nextLine();
    }

    public void displayInputPrompt() {
        System.out.print(inputPrompt);
    }
    public void displayNotValidInputPrompt(String notValidInput) {
        System.out.printf((notValidInputPrompt) + "%n", notValidInput);
    }

    public void displayProductsPrompt(String productToStringPromptFormat) {
        System.out.println(productsPrompt + productToStringPromptFormat);
    }

    public void displayQuitePrompt() {
        System.out.println(quitePrompt);
    }

    public void displayOrderPrompt(String target) {
        System.out.printf(orderPrompt, target);
    }

    public void displayOnlyDigitOrSpacePrompt(String target) {
        System.out.printf((onlyDigitOrSpacePrompt) + "%n", target);
    }

    public void displayAttributeRequiredPrompt(String target) {
        System.out.printf((attributeRequiredPrompt) + "%n", target);
    }

    public void displayOrderHistory(Map<String, Integer> paymentResult) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(orderHistoryPrompt);
        stringBuilder.append(orderHistoryTemplatePrompt);
        int orderCost = 0;
        for (String key : paymentResult.keySet()) {
            stringBuilder.append(key).append("\n");
            orderCost += paymentResult.get(key);
        }
        stringBuilder.append(orderHistoryTemplatePrompt);
        stringBuilder.append("주문금액: ").append(Common.convertWon(orderCost)).append("\n");
        if (orderCost < 50000) {
            stringBuilder.append("배송비: ").append(Common.convertWon(2500)).append("\n");
            orderCost += 2500;
        }
        stringBuilder.append(orderHistoryTemplatePrompt);
        stringBuilder.append("지불금액: ").append(Common.convertWon(orderCost)).append("\n");
        stringBuilder.append(orderHistoryTemplatePrompt);

        System.out.println(stringBuilder);
    }
}
