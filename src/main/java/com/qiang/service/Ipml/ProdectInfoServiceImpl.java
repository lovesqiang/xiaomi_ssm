package com.qiang.service.Ipml;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiang.mapper.ProductInfoMapper;
import com.qiang.pojo.ProductInfo;
import com.qiang.pojo.ProductInfoExample;
import com.qiang.pojo.vo.ProductInfoVo;
import com.qiang.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdectInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductInfo> getAll() {

        //调用数据访问层对象获取所有商品
        List<ProductInfo> list = productInfoMapper.selectByExample(new ProductInfoExample());

        return list;
    }

    //展示商品（分页）
    @Override
    public PageInfo<ProductInfo> pageSplit(int pageNum, int pageSize) {

        //分页插件使用PageHelper工具类完成分页设置
        PageHelper.startPage(pageNum,pageSize);

        /*
        * 进行PageInfo的数据封装
        * */
        //进行有条件的数据查询，必须要创建ProductInfoExample对象
        ProductInfoExample example = new ProductInfoExample();

        //设置排序，按主键降序排序
        //select * from product_info order by p_id desc
        example.setOrderByClause("p_id desc");

        //设置完排序后，取集合,切记切记：一定在取集合之前，设置PageHelper.startPage(pageNum,pageSize);
        List<ProductInfo> list = productInfoMapper.selectByExample(example);

        //将查询到的集合封装到PageInfo对象中，PageInfo中的所有成员变量统统被赋值
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    //新增商品
    @Override
    public int save(ProductInfo info) {
        return productInfoMapper.insert(info);
    }

    @Override
    public ProductInfo getById(int pid) {
        return productInfoMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int updata(ProductInfo info) {
        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int delete(int pid) {

        return productInfoMapper.deleteByPrimaryKey(pid);
    }

    @Override
    public int deleteBatch(String[] pids) {
        return productInfoMapper.deleteBatch(pids);
    }

    //多条件查询
    @Override
    public List<ProductInfo> selectCondition(ProductInfoVo vo) {

        return productInfoMapper.selectCondition(vo);
    }

    //多条件查询（分页）
    @Override
    public PageInfo splitPageVo(ProductInfoVo vo,int pageSize) {

        //分页的功能：所有的分页在取集合之前，都要先设置 PageHelper.startPage()的属性
        PageHelper.startPage(vo.getPage(),pageSize);

        List<ProductInfo> list = productInfoMapper.selectCondition(vo);

        PageInfo info = new PageInfo(list);

        return info;

    }
}
