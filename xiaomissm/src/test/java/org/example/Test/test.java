package org.example.Test;

import org.example.mapper.ProductInfoMapper;
import org.example.pojo.ProductInfo;
import org.example.pojo.vo.ProductInfoVo;
import org.example.utils.MD5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_service.xml","classpath:applicationContext_dao.xml"})
public class test {

    @Autowired
    ProductInfoMapper mapper;

    @Test
    public void testMD5() {
        String mi = MD5Util.getMD5("000000");
        System.out.println(mi);
    }

    @Test
    public void select(){
        ProductInfoVo vo = new ProductInfoVo();
//        vo.setPname("4");
//        vo.setTypeid(3);
        vo.setLprice(3000);
        vo.setHprice(4000);
        List<ProductInfo> list = mapper.selectCondition(vo);
        list.forEach(productInfo -> System.out.println(productInfo));
    }

    @Test
    public void test() throws InstantiationException, IllegalAccessException {
        Class test = ProductInfoVo.class;
        test.newInstance();
    }

    @Test
    public void breakText(){
        a: for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (j==3){
                    break a;
                }
                System.out.println(i+"====="+j);
            }
            System.out.println("------------");
            System.out.println("------------");
            System.out.println("------------");
        }
    }



}






