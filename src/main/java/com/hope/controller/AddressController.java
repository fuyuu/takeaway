package com.hope.controller;

import com.hope.domain.pojo.Address;
import com.hope.domain.pojo.Result;
import com.hope.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 马mq
 */
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/create")
    public Result create(@RequestBody Address address) {
        return addressService.create(address);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        return addressService.delete(id);
    }

    @GetMapping("/{id}")
    public Result getAddressById(@PathVariable Long id){
        return addressService.getAddressById(id);
    }

    @GetMapping()
    public Result<List<Address>> getAddressList(){
        return addressService.listByUserId();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Address address){
        return addressService.update(address);
    }

    @PostMapping("/top/{id}")
    public Result setTop(@PathVariable Long id){
        return addressService.setTop(id);
    }
}
