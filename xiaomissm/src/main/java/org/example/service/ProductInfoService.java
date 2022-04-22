package org.example.service;

import com.github.pagehelper.PageInfo;
import org.example.pojo.ProductInfo;
import org.example.pojo.vo.ProductInfoVo;

import java.util.List;

public interface ProductInfoService {

    //显示全部商品，不分页
    List<ProductInfo> getAll();

    //分页功能
    PageInfo splitPage(int pageNum,int pageSize);

    //增加商品
    int save(ProductInfo info);

    //按主键查询商品
    ProductInfo getById(int pid);

    //更新商品
    int update(ProductInfo info);

    //单个删除商品
    int delete(int pid);

    //批量删除商品
    int deleteBatch(String[] ids);

    //多条件商品查询
    List<ProductInfo> selectCondition(ProductInfoVo vo);

    //多条件商品分页查询
    public PageInfo splitPageVo(ProductInfoVo vo,int pageSize);

}
