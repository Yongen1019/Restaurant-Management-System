package com.sky.controller.user;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api("Client Side Address Book Controller")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/list")
    @ApiOperation("Get Current User All Address Details")
    public Result<List<AddressBook>> list() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);

        return Result.success(list);
    }

    @PostMapping
    @ApiOperation("Add Address Details")
    public Result save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);

        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get Address By Id")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);

        return Result.success(addressBook);
    }

    @PutMapping
    @ApiOperation("Update Address By Id")
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }

    @PutMapping("/default")
    @ApiOperation("Set Default Address")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("Delete Address By Id")
    public Result deleteById(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/default")
    @ApiOperation("Get Default Address")
    public Result<AddressBook> getDefault() {
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }

        return Result.error("No Default Address Found");
    }

}
