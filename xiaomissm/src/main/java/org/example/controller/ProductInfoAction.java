package org.example.controller;


import com.github.pagehelper.PageInfo;
import org.example.pojo.ProductInfo;
import org.example.pojo.vo.ProductInfoVo;
import org.example.service.ProductInfoService;
import org.example.utils.FileNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {

    //每页显示的记录数
    public static final int PAGE_SIZE = 5;

    //异步上传图片名称
    String saveFileName = "";

    //切记：所有的界面层，一定有业务逻辑层的对象
    @Autowired
    ProductInfoService productInfoService;

    //显示商品，不分页
    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request) {
        List<ProductInfo> list = productInfoService.getAll();
        request.setAttribute("list", list);
        return "product";
    }

    //显示第一页
    @RequestMapping("/split")
    public String split(HttpServletRequest request) {
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("prodVo");
        if (vo != null) {
            info = productInfoService.splitPageVo((ProductInfoVo) vo, PAGE_SIZE);
            request.getSession().removeAttribute("prodVo");
        } else {
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.setAttribute("info", info);
        return "product";
    }

    /**
     * ajax分页，翻页处理
     * <p>
     * springmvc处理器方法返回对象，
     * 转为json响应ajax步骤
     * 在处理器方法上面加@ResponseBody
     */
    @ResponseBody
    @RequestMapping("/ajaxSplit")
    public void ajaxSplit(ProductInfoVo vo, HttpSession session) {
        PageInfo info = productInfoService.splitPageVo(vo, PAGE_SIZE);
        session.setAttribute("info", info);
    }

    //多条件查询
    @ResponseBody
    @RequestMapping("/condition")
    public void condition(ProductInfoVo vo, HttpSession session) {
        List<ProductInfo> list = productInfoService.selectCondition(vo);
        session.setAttribute("list", list);
    }

    //异步ajax上传文件处理
    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(MultipartFile pimage, HttpServletRequest request) {

        //提取生成文件名UUID+上传图片的后缀 .jpg .png ...
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());

        //获取项目中存储图片的路径  D:\spring代码\实战\xiaomissm\target\xiaomissm-1.0-SNAPSHOT\image_big
        String path = request.getServletContext().getRealPath("/image_big");

        //转存
        //比如说要在temp目录下建立一个test.txt文件，在Windows下应该这么写：
        //File file1 = new File ("C:\tmp\test.txt");
        //在Linux下则是这样的：
        //File file2 = new File ("/tmp/test.txt");
        //
        //如果要考虑跨平台，则最好是这么写：
        //File myFile = new File("C:" + File.separator + "tmp" + File.separator, "test.txt");
        try {
            pimage.transferTo(new File(path + File.separator + saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //返回客户端JSON对象，封装图片的路径，为了在页面实现立即回显
        JSONObject object = new JSONObject();
        object.put("imgurl", saveFileName);

        return object.toString();
    }

    @RequestMapping("/save")
    public String save(ProductInfo info, HttpServletRequest request) {
        //数据补全
        info.setpImage(saveFileName);
        info.setpDate(new Date());

        //info数据提交
        int num = -1;
        try {
            num = productInfoService.save(info);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (num > 0) {
            request.setAttribute("msg", "商品增加成功！！！");
        } else {
            request.setAttribute("msg", "商品增加失败！！！");
        }

        //增加新功能后应重新访问数据看
        return "forward:/prod/split.action";
    }

    @RequestMapping("/one")
    public String one(int pid, ProductInfoVo vo, Model model, HttpSession session) {
        ProductInfo info = productInfoService.getById(pid);
        model.addAttribute("prod", info);
        //将多条件及页码放入session中，更新处理结束后，分页时读取条件和页码进行处理
        session.setAttribute("prodVo", vo);
        return "update";
    }

    @RequestMapping("/update")
    public String update(ProductInfo info, HttpServletRequest request) {
        //不为空，说明使用了ajax上传图片
        if (!saveFileName.equals("")) {
            info.setpImage(saveFileName);
        }

        //完成更新处理
        int num = -1;

        try {
            num = productInfoService.update(info);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "更新成功！！！");
        } else {
            request.setAttribute("msg", "更新失败！！！");
        }
        saveFileName = "";
        return "forward:/prod/split.action";
    }


    @RequestMapping("/delete")
    public String delete(int pid, ProductInfoVo vo, HttpServletRequest request) {
        int num = -1;
        try {
            num = productInfoService.delete(pid);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "删除成功！！！");
            request.getSession().setAttribute("deleteProdVo", vo);
        } else {
            request.setAttribute("msg", "删除失败！！！");
        }
        //删除结束后

        return "forward:/prod/deleteAjaxSplit.action";
    }

    @ResponseBody
    @RequestMapping(value = "/deleteAjaxSplit", produces = "text/html;charset=UTF-8")
    public Object deleteAjaxSplit(HttpServletRequest request) {
        //第一页数据
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("deleteProdVo");
        if (vo != null) {
            info = productInfoService.splitPageVo((ProductInfoVo) vo, PAGE_SIZE);
            request.getSession().removeAttribute("deleteProdVo");
        } else {
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.getSession().setAttribute("info", info);
        return request.getAttribute("msg");
    }


    @RequestMapping("/deletebatch")
    public String deleteBatch(String pids, HttpServletRequest request) {
        //将ids转换为String[]
        String[] ps = pids.split(",");

        try {
            int num = productInfoService.deleteBatch(ps);
            if (num > 0) {
                request.setAttribute("msg", "批量删除成功！！！");
            } else {
                request.setAttribute("msg", "批量删除失败！！！");
            }
        } catch (Exception exception) {
            request.setAttribute("msg", "商品不可删除！！！");
        }

        return "forward:/prod/deleteAjaxSplit.action";
    }


}





