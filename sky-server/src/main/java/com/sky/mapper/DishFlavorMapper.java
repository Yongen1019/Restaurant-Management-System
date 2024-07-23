package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * Insert Multiple Flavors Records
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    void deleteByDishIds(List<Long> dishIds);
}
