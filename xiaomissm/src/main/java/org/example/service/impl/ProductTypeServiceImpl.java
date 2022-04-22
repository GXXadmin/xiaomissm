package org.example.service.impl;

import org.example.mapper.ProductTypeMapper;
import org.example.pojo.ProductType;
import org.example.pojo.ProductTypeExample;
import org.example.service.ProductInfoService;
import org.example.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ProductTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {

    //切记：所有的界面层，一定有业务逻辑层的对象
    @Autowired
    ProductTypeMapper productTypeMapper;

    @Override
    public List<ProductType> getAll() {
        return productTypeMapper.selectByExample(new ProductTypeExample());
    }
}
