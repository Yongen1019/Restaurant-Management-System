package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@Slf4j
@Api("Order Controller")
@RequestMapping("/admin/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/conditionSearch")
    @ApiOperation("Order Search Pagination")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("order search pagination: {}", ordersPageQueryDTO);
        PageResult pageResult = orderService.pageQueryForAdmin(ordersPageQueryDTO);

        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    @ApiOperation("Get Quantity of Various Order Status")
    public Result<OrderStatisticsVO> statistics() {
        log.info("get quantity of various order status");
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();

        return Result.success(orderStatisticsVO);
    }

    @GetMapping("/details/{id}")
    @ApiOperation("Get Order Details")
    public Result<OrderVO> details(@PathVariable Long id) {
        log.info("get order details: {}", id);
        OrderVO orderVO = orderService.details(id);

        return Result.success(orderVO);
    }

    @PutMapping("/confirm")
    @ApiOperation("confirm order")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("confirm order: {}", ordersConfirmDTO);
        orderService.confirm(ordersConfirmDTO);

        return Result.success();
    }

    @PutMapping("/rejection")
    @ApiOperation("reject order")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("reject order: {}", ordersRejectionDTO);
        orderService.rejection(ordersRejectionDTO);

        return Result.success();
    }

    @PutMapping("/cancel")
    @ApiOperation("Cancel Order")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("cancel order: {}", ordersCancelDTO);
        orderService.cancel(ordersCancelDTO);

        return Result.success();
    }

    @PutMapping("/delivery/{id}")
    @ApiOperation("delivery order")
    public Result delivery(@PathVariable Long id) {
        log.info("delivery order: {}", id);
        orderService.delivery(id);

        return Result.success();
    }

    @PutMapping("/complete/{id}")
    @ApiOperation("complete order")
    public Result complete(@PathVariable Long id) {
        log.info("complete order: {}", id);
        orderService.complete(id);

        return Result.success();
    }
}
