package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api("Client Side Dish Controller")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * Get Category By Category Id
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Get Category By Category Id")
    public Result<List<DishVO>> list(Long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE); // get dish which status is enable

        List<DishVO> list = dishService.listWithFlavor(dish);

        return Result.success(list);
    }

}
