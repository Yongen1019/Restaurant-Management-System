package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkspaceService workspaceService;

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

    /**
     * Get Order Statistics
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        // store all date between begin and end into list
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> totalOrderList = new ArrayList<>();
        List<Integer> validOrderList = new ArrayList<>();
        while(!begin.isAfter(end)) {
            dateList.add(begin);

            // get total order and completed order amount by date
            LocalDateTime beginDateTime = LocalDateTime.of(begin, LocalTime.MIN);
            LocalDateTime endDateTime = LocalDateTime.of(begin, LocalTime.MAX);

            // get total order (all orders included cancelled)
            Integer totalOrder = getOrderCount(beginDateTime, endDateTime, null);
            totalOrderList.add(totalOrder);

            // get completed (all orders in status completed)
            Integer validOrder = getOrderCount(beginDateTime, endDateTime, Orders.COMPLETED);
            validOrderList.add(validOrder);

            // add begin by 1 day
            begin = begin.plusDays(1);
        }

        // calculate sum of totalOrderList using stream
        Integer totalOrderCount = totalOrderList.stream().reduce(Integer::sum).get();

        // calculate sum of completedOrderList using stream
        Integer validOrderCount = validOrderList.stream().reduce(Integer::sum).get();

        // calculate orders completion rate
        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(totalOrderList, ","))
                .validOrderCountList(StringUtils.join(validOrderList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * Get Sales Top 10 Products
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginDateTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginDateTime, endDateTime);

        // collect name one by one and gather in a list using stream map
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(names, ","))
                .numberList(StringUtils.join(numbers, ","))
                .build();
    }

    /**
     * Export Operational Data Analytics Report
     * @param response
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) {
        // get business data from database -- get data for the last 30 days
        LocalDate beginDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(1);

        LocalDateTime beginDateTime = LocalDateTime.of(beginDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);

        // get overview data
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(beginDateTime, endDateTime);

        // write business data into Excel using Apache POI
        // get an input stream using class loader to load template resource
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/Operational Data Analytics Report.xlsx");
        // create a new Excel file based on the template Excel
        try {
            XSSFWorkbook excel = new XSSFWorkbook(in);

            // get sheet and fill in overview data
            XSSFSheet sheet = excel.getSheet("Sheet1");
            sheet.getRow(1).getCell(1).setCellValue("Date: " + beginDate + " to " + endDate);

            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());

            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

            // fill in detailed data
            for (int i = 0; i < 30; i ++) {
                LocalDate date = beginDate.plusDays(i);
                // get detailed data of the date
                BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                // start from Excel file row 8
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }

            // download Excel file in client side browser via httpServletResponse output stream
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            // close resource
            out.close();
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Integer getOrderCount(LocalDateTime begin, LocalDateTime end, Integer status) {
        Map map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", status);

        return orderMapper.countByMap(map);
    }
}
