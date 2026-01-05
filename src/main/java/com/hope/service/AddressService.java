package com.hope.service;

import com.hope.domain.pojo.Address;
import com.hope.domain.pojo.Result;

import java.util.List;

public interface AddressService {
    Result create(Address address);

    Result delete(Long id);

    Result update(Address address);

    Result getAddressById(Long id);

    Result<List<Address>> listByUserId();

    Result setTop(Long id);
}
