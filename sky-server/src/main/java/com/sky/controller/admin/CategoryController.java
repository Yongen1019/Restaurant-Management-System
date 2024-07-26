package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 分类管理
 */
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
@Api(tags = "Category Controller")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @ApiOperation("Add Category")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO){
        log.info("add category：{}", categoryDTO);
        categoryService.save(categoryDTO);

        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("Category Search Pagination")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("category search pagination: {}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);

        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("Delete Category By Id")
    public Result<String> deleteById(Long id){
        log.info("delete category by id：{}", id);
        categoryService.deleteById(id);

        return Result.success();
    }

    @PutMapping
    @ApiOperation("Update Category")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);

        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("Start or Stop Category")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        categoryService.startOrStop(status,id);

        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("Get Category By Type")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.list(type);

        return Result.success(list);
    }
}
