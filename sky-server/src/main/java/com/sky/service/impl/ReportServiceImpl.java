package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * Get Turnover Statistics
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // store all date between begin and end into list
        List<LocalDate> dateList = new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();
        while (!begin.isAfter(end)) {
            dateList.add(begin);

            // get turnover by date
            LocalDateTime beginDateTime = LocalDateTime.of(begin, LocalTime.MIN);
            LocalDateTime endDateTime = LocalDateTime.of(begin, LocalTime.MAX);

            Map map = new HashMap<>();
            map.put("begin", beginDateTime);
            map.put("end", endDateTime);
            map.put("status", Orders.COMPLETED);

            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);

            // add begin by 1 day
            begin = begin.plusDays(1);
        }

        // join list item with , and return as string
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * Get User Statistics
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // store all date between begin and end into list
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();
        while(!begin.isAfter(end)) {
            dateList.add(begin);

            // get total user and new user amount by date
            LocalDateTime beginDateTime = LocalDateTime.of(begin, LocalTime.MIN);
            LocalDateTime endDateTime = LocalDateTime.of(begin, LocalTime.MAX);

            Map map = new HashMap<>();

            // get total user (all user create before the end date)
            map.put("end", endDateTime);
            Integer totalUser = userMapper.countByMap(map);
            totalUserList.add(totalUser);

            // get new user (all user create between the begin and end date)
            map.put("begin", beginDateTime);
            Integer newUser = userMapper.countByMap(map);
            newUserList.add(newUser);

            // add begin by 1 day
            begin = begin.plusDays(1);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }
}
