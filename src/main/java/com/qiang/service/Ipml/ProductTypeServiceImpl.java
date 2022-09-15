package com.qiang.service.Ipml;

import com.qiang.mapper.ProductTypeMapper;
import com.qiang.pojo.ProductType;
import com.qiang.pojo.ProductTypeExample;
import com.qiang.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ProductTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Override
    public List<ProductType> getAll() {

        return productTypeMapper.selectByExample(new ProductTypeExample());
    }
}
