package kr.co._29cm.homework.service;

import kr.co._29cm.homework.domain.*;
import kr.co._29cm.homework.exception.NoOrderException;
import kr.co._29cm.homework.exception.NoSuchProductException;
import kr.co._29cm.homework.exception.SoldOutException;
import kr.co._29cm.homework.util.Common;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


public class PromptService {
    private final Prompt prompt;
    private final OrderService orderService;

    public PromptService() {
        this.orderService = new OrderService();
        this.prompt = new Prompt();
    }

    public void initPrompt() {
        String promptString;
        while (true) {
            prompt.displayInputPrompt();
            promptString = prompt.getValueFromPrompt();
            if (!validateInputPrompt(promptString)) {
                prompt.displayNotValidInputPrompt(promptString);
                continue;
            }
            prompt.displayProductsPrompt(getProductsPromptString());
            executeOrderPrompt();
        }
    }

    private boolean validateInputPrompt(String promptInput) {
        if (promptInput.equals(Input.QUIT.getValue())) {
            prompt.displayQuitePrompt();
            System.exit(0);
        }
        return promptInput.equals(Input.ORDER.getValue());
    }

    private String getProductsPromptString(){
        try {
            Products products = orderService.getAllProducts();
            return products.productToStringPromptFormat();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("상품 목록을 불러오는데 에러가 발생 하였습니다.");
        }
    }

    private void executeOrderPrompt() {
        Orders orders = new Orders();
        Order.OrderAttribute[] orderAttributes = Order.OrderAttribute.values();
        AtomicBoolean orderSuccess = new AtomicBoolean(false);
        while (!orderSuccess.get()) {
            try{
                generateOrderFromPrompt(orderAttributes).ifPresent(order -> {
                    String checkOrderResult = order.checkOrderAttribute();
                    if(checkOrderResult == null) {
                        orders.addOrder(order);
                        return;
                    }
                    if(checkOrderResult.isEmpty()) {
                        Map<String,Integer> paymentResult = orderService.doPayment(orders);
                        prompt.displayOrderHistory(paymentResult);
                        orderSuccess.set(true);
                        return;
                    }
                    prompt.displayAttributeRequiredPrompt(checkOrderResult);
                });
            }
            catch (SoldOutException | NoOrderException| NoSuchProductException soldOutException) {
                prompt.displayMessagePrompt(soldOutException.getMessage());
                break;
            }
        }
    }

    private Optional<Order> generateOrderFromPrompt(Order.OrderAttribute[] orderAttributes) {
        Order order = new Order();
        String promptValue;
        try {
            for (Order.OrderAttribute attr: orderAttributes) {
                prompt.displayOrderPrompt(attr.getName());
                promptValue = prompt.getValueFromPrompt();
                if (!validateOrderPromptResponse(attr.getName(), promptValue)) {
                    order = null;
                    break;
                }
                if (promptValue.isBlank()) continue;
                order.OrderSetting(attr.getField(), Integer.parseInt(promptValue));
            }
            return Optional.ofNullable(order);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Order 목록을 생성중 에러 발생");
        }

    }

    private boolean validateOrderPromptResponse(String target, String promptValue) {
        if (Common.isOnlyDigitOrSpace(promptValue)) return true;
        prompt.displayOnlyDigitOrSpacePrompt(target);
        return false;
    }
}
