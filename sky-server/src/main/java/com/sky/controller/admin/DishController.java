package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.DishFlavor;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api("Dish Controller")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * Add New Dish
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Add New Dish")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("add new dish: {}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("Dish Search Pagination")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("dish search pagination: {}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);

        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("Delete Multiple Dish")
    // List<Long>: MVC will help us to convert String ids = (1,2,3) into List<Long> ids
    public Result delete(@RequestParam List<Long> ids) {
        log.info("delete multiple dish: {}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }
}
