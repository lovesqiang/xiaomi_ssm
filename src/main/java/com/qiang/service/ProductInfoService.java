package com.qiang.service;

import com.github.pagehelper.PageInfo;
import com.qiang.pojo.ProductInfo;
import com.qiang.pojo.vo.ProductInfoVo;

import java.util.List;

public interface ProductInfoService {

    //商品不分页
    List<ProductInfo> getAll();

    //商品分页
    PageInfo<ProductInfo> pageSplit(int pageNum,int pageSize);

    //新增商品
    int save(ProductInfo info);

    //更新商品,进行商品回显
    ProductInfo getById(int pid);

    //更新商品
    int updata(ProductInfo info);

    //删除单个商品
    int delete(int pid);

    //批量删除商品
    int deleteBatch(String[] pids);

    //多条件查询商品
    List<ProductInfo> selectCondition(ProductInfoVo vo);

    //多条件查询商品（分页）
    PageInfo splitPageVo(ProductInfoVo vo,int pageSize);

}
