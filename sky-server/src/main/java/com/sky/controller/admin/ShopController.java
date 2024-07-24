package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController") // modify bean name, if not will crash with user shop controller
@Slf4j
@Api("Shop Controller")
@RequestMapping("/admin/shop")
public class ShopController {
    public static final String KEY ="SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation("Set Shop Operation Status")
    public Result setStatus(@PathVariable Integer status) {
        log.info("set shop operation status: {}", status == 1 ? "Open" : "Closed");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("Get Shop Operation Status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("get shop operation status: {}", status == 1 ? "Open" : "Closed");
        return Result.success(status);
    }
}
