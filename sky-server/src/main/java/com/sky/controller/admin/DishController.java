package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api("Dish Controller")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

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
        String key = "dish_" + dishDTO.getCategoryId();
        cleanRedisCache(key);

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
        cleanRedisCache("dish_*");

        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get Dish By Id")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("get dish by id: {}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("Update Dish")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("update dish: {}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        cleanRedisCache("dish_*");

        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("Get Dish By Category Id")
    public Result<List<Dish>> getByCategoryId(Long categoryId) {
        log.info("get dish by category id: {}", categoryId);
        List<Dish> dishList = dishService.getByCategoryId(categoryId);

        return Result.success(dishList);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("Enable or Disable Dish")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true) // clear all value in setmealCache
    public Result startOrStop(@PathVariable Integer status, Long id) {
        dishService.startOrStop(status, id);
        cleanRedisCache("dish_*");

        return Result.success();
    }

    private void cleanRedisCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
