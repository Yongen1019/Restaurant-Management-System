package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api("Client Side Shopping Cart Controller")
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    @ApiOperation("Add into Cart")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("add into cart: {}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);

        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("Get Shopping Cart List")
    public Result<List<ShoppingCart>> list() {
        List<ShoppingCart> shoppingCartList = shoppingCartService.showShoppingCart();

        return Result.success(shoppingCartList);
    }

    @DeleteMapping("/clean")
    @ApiOperation("Clear Shopping Cart")
    public Result clean() {
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    @PostMapping("/sub")
    @ApiOperation("Delete Product from Shopping Cart")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("delete product from shopping cart: {}", shoppingCartDTO);
        shoppingCartService.subShoppingCart(shoppingCartDTO);

        return Result.success();
    }
}
