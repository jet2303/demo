package com.daily.demo.service.ifs;

import com.daily.demo.dto.Header;
import com.daily.demo.entity.daily.enumData.Useyn;

public interface UserCrudIfs<req, res> {

    Header<res> create(Header<req> request);

    Header<res> read(Long id);

    Header<res> read(String email, Useyn useyn);

    Header<res> update(Header<req> request);

    Header delete(Long id);
}
