package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Custom Scheduled Task Class
 */
@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * update time out pending payment order (after 15 minutes) status to cancelled every minute
     */
    @Scheduled(cron = "0 * * * * ? ") // every minute
    public void processTimeoutOrder() {
        log.info("update time out pending payment order status: {}", LocalDateTime.now());
        LocalDateTime orderTime = LocalDateTime.now().minusMinutes(15);

        List<Orders> orderList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, orderTime);

        if (orderList != null && !orderList.isEmpty()) {
            for (Orders order : orderList) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelTime(LocalDateTime.now());
                order.setCancelReason("Order payment timeout, Cancel Automatically");

                orderMapper.update(order);
            }
        }
    }

    /**
     * update out of delivery order status to completed every day when shop closed
     */
    @Scheduled(cron = "0 0 1 * * ? ") // every day 1 am
    public void processDeliveredOrder() {
        log.info("update out of delivery order status to completed: {}", LocalDateTime.now());
        // get all orders in status out of delivery where order time is at least one hour before
        LocalDateTime orderTime = LocalDateTime.now().minusHours(1);

        List<Orders> orderList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, orderTime);

        if (orderList != null && !orderList.isEmpty()) {
            for (Orders order : orderList) {
                order.setStatus(Orders.COMPLETED);
                order.setDeliveryTime(LocalDateTime.now());

                orderMapper.update(order);
            }
        }
    }
}
