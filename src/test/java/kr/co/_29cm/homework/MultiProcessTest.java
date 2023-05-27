package kr.co._29cm.homework;

import kr.co._29cm.homework.databse.MasterDatabase;
import kr.co._29cm.homework.databse.SlaveDatabase;
import kr.co._29cm.homework.domain.Order;
import kr.co._29cm.homework.domain.Orders;
import kr.co._29cm.homework.domain.Prompt;
import kr.co._29cm.homework.exception.SoldOutException;
import kr.co._29cm.homework.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiProcessTest {

    @BeforeEach
    void setUpDatabase() {
        MasterDatabase.getInstance();
        SlaveDatabase.getInstance();
    }

    @Test
    @DisplayName("동시성 테스트")
    public void multiProcessTest() throws InterruptedException {

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        OrderService orderService = new OrderService();
        Prompt prompt = new Prompt();

        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger soldOutExceptionCount = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                Order order = new Order();
                try {
                    order.OrderSetting(Order.OrderAttribute.PRODUCT_ID.getField(), 744775);
                    order.OrderSetting(Order.OrderAttribute.COUNT.getField(), 5);
                    Orders orders = new Orders();
                    orders.addOrder(order);
                    Map<String, Integer> result = orderService.doPayment(orders);
                    prompt.displayOrderHistory(result);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                catch (SoldOutException soldOutException) {
                    soldOutExceptionCount.getAndIncrement();
                }
                finally {
                    latch.countDown();
                }
            });
        }
        executorService.shutdown();
        latch.await();
        assert soldOutExceptionCount.get() == 3;
    }
}
