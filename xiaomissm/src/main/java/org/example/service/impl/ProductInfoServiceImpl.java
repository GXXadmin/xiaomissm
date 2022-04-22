package org.example.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.mapper.AdminMapper;
import org.example.mapper.ProductInfoMapper;
import org.example.pojo.ProductInfo;
import org.example.pojo.ProductInfoExample;
import org.example.pojo.vo.ProductInfoVo;
import org.example.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    //在业务逻辑层，一定会有数据访问层的对象
    //@Autowired，代替其实现类
    @Autowired
    ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductInfo> getAll() {
        return productInfoMapper.selectByExample(new ProductInfoExample());
    }

    /**
     * SELECT * FROM product_info LIMIT 起始记录数，每页条数
     * <p>
     * 启示记录数 = （当前页 - 1）* 每页条数
     * SELECT * FROM product_info LIMIT 15,5
     *
     * @param pageNum  起始记录数
     * @param pageSize 每页条数
     * @return
     */
    @Override
    public PageInfo splitPage(int pageNum, int pageSize) {
        //分页插件使用PageHelper工具类完成分页设置
        PageHelper.startPage(pageNum, pageSize);

        //进行PageInfo的数据封装
        //进行条件查询，必须创建ProductInfoExample
        ProductInfoExample example = new ProductInfoExample();

        //设置主键并降序排列
        example.setOrderByClause("p_id desc");

        //设置完排序后一定，取集合
        //取集合前，一定要设置PageHelper.startPage(pageNum,pageSize);
        List<ProductInfo> list = productInfoMapper.selectByExample(example);

        //查询集合封装到PageInfo对象中
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    @Override
    public int save(ProductInfo info) {
        return productInfoMapper.insert(info);
    }

    @Override
    public ProductInfo getById(int pid) {
        return productInfoMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int update(ProductInfo info) {
        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int delete(int pid) {
        return productInfoMapper.deleteByPrimaryKey(pid);
    }

    @Override
    public int deleteBatch(String[] ids) {
        return productInfoMapper.deleteBatch(ids);
    }

    @Override
    public List<ProductInfo> selectCondition(ProductInfoVo vo) {
        return productInfoMapper.selectCondition(vo);
    }

    @Override
    public PageInfo<ProductInfo> splitPageVo(ProductInfoVo vo, int pageSize) {
        //分页插件使用PageHelper工具类完成分页设置
        PageHelper.startPage(vo.getPage(), pageSize);

        //设置完排序后一定，取集合
        //取集合前，一定要设置PageHelper.startPage(pageNum,pageSize);
        List<ProductInfo> list = productInfoMapper.selectCondition(vo);


        return new PageInfo<>(list);
    }


}







