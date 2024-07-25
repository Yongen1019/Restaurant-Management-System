package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api("Setmeal Controller")
@RequestMapping("/admin/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @ApiOperation("Add New Setmeal")
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.categoryId") // remove key in redis : setmealCache::{categoryId}
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("add new setmeal: {}", setmealDTO);
        setmealService.saveWithDishes(setmealDTO);

        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("Setmeal Search Pagination")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("setmeal search pagination: {}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);

        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("Delete Multiple Setmeal")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true) // clear all value in setmealCache
    public Result delete(@RequestParam List<Long> ids) {
        log.info("delete multiple setmeal: {}", ids);
        setmealService.deleteBatch(ids);

        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get Setmeal By Id")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("get setmeal by id: {}", id);
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);

        return Result.success(setmealVO);
    }

    @PutMapping
    @ApiOperation("Update Setmeal")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true) // clear all value in setmealCache
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        setmealService.update(setmealDTO);

        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("Enable or Disable Setmeal")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true) // clear all value in setmealCache
    public Result startOrStop(@PathVariable Integer status, Long id) {
        setmealService.startOrStop(status, id);

        return Result.success();
    }
}
