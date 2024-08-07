package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api("Employee Controller")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Login
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("Employee Login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        // Generate Jwt Token after Login
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * Logout
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("Employee Logout")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    @ApiOperation("Add New Employee")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("add new employee: {}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * Employee Search Pagination
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("Employee Search Pagination")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("employee search pagination: {}", employeePageQueryDTO);

        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * Enable or Disable Employee Account
     * @param status
     * @param id
     * @return
     */
    // /status/1?id=2
    @PostMapping("/status/{status}")
    @ApiOperation("Enable or Disable Employee Account")
    public Result enableOrDisableAccount(@PathVariable Integer status, Long id) {
        log.info("enable or disable employee account: {}, {}", status, id);
        employeeService.enableOrDisableAccount(status, id);
        return Result.success();
    }

    /**
     * Get Employee Data By Id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("Get Employee By Id")
    public Result<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * Update Employee Data
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Update Employee Data")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("update employee data: {}", employeeDTO);
        employeeService.update(employeeDTO);

        return Result.success();
    }

    @PutMapping("/editPassword")
    @ApiOperation("Edit Password")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("edit employee password: {}", passwordEditDTO);
        employeeService.editPassword(passwordEditDTO);

        return Result.success();
    }
}
