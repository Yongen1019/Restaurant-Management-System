package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@Slf4j
@Api("Client Side Order Controller")
@RequestMapping("/user/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @ApiOperation("User Place Order")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("user place order: {}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);

        return Result.success(orderSubmitVO);
    }

    @PutMapping("/payment")
    @ApiOperation("Order Payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("order payment: {}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);

        return Result.success(orderPaymentVO);
    }

    @GetMapping("/historyOrders")
    @ApiOperation("Get Order History")
    public Result<PageResult> page(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("get order history: {}", ordersPageQueryDTO);
        PageResult pageResult = orderService.pageQueryForUser(ordersPageQueryDTO);

        return Result.success(pageResult);
    }

    @GetMapping("/orderDetail/{id}")
    @ApiOperation("Get Specific Order and Order Details")
    public Result<OrderVO> details(@PathVariable Long id) {
        log.info("get specific order and order details: {}", id);
        OrderVO orderVO = orderService.details(id);

        return Result.success(orderVO);
    }

    @PutMapping("/cancel/{id}")
    @ApiOperation("Cancel Order")
    public Result cancel(@PathVariable Long id) {
        log.info("cancel order: {}", id);
        orderService.userCancelById(id);

        return Result.success();
    }

    @PostMapping("/repetition/{id}")
    @ApiOperation("Repeat Order")
    public Result repetition(@PathVariable Long id) {
        log.info("repeat order: {}", id);
        orderService.repetition(id);

        return Result.success();
    }
}
