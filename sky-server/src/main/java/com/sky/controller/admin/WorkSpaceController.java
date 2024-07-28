package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "WorkSpace Controller")
public class WorkSpaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping("/businessData")
    @ApiOperation("WorkSpace Get Today Business Data Overview")
    public Result<BusinessDataVO> businessData(){
        log.info("workSpace get today business data overview");
        // Get Today Begin and End DateTime
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);

        BusinessDataVO businessDataVO = workspaceService.getBusinessData(begin, end);

        return Result.success(businessDataVO);
    }

    @GetMapping("/overviewOrders")
    @ApiOperation("Get Orders Data Overview")
    public Result<OrderOverViewVO> orderOverView(){
        log.info("get orders data overview");
        OrderOverViewVO orderOverViewVO = workspaceService.getOrderOverView();

        return Result.success(orderOverViewVO);
    }

    @GetMapping("/overviewDishes")
    @ApiOperation("Get Dishes Data Overview")
    public Result<DishOverViewVO> dishOverView(){
        log.info("get dishes data overview");
        DishOverViewVO dishOverViewVO = workspaceService.getDishOverView();

        return Result.success(dishOverViewVO);
    }

    @GetMapping("/overviewSetmeals")
    @ApiOperation("Get Setmeals Data Overview")
    public Result<SetmealOverViewVO> setmealOverView(){
        log.info("get setmeals data overview");
        SetmealOverViewVO setmealOverViewVO = workspaceService.getSetmealOverView();

        return Result.success(setmealOverViewVO);
    }
}
