package com.qiang.controller;

import com.github.pagehelper.PageInfo;
import com.qiang.pojo.ProductInfo;
import com.qiang.pojo.vo.ProductInfoVo;
import com.qiang.service.ProductInfoService;
import com.qiang.utils.FileNameUtil;
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
public class ProductInfoController {

    public static final int PAGE_SIZE = 5;

    String fileName = "";   //防止空指针异常

    @Autowired
    private ProductInfoService productInfoService;

    //显示全部商品不分页
    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request){
        List<ProductInfo> list = productInfoService.getAll();
        request.setAttribute("list",list);
        return "product";
    }

    //显示第1页的5条记录
    @RequestMapping("/split")
    public String split(HttpServletRequest request){

        PageInfo info = null;

        Object vo = request.getSession().getAttribute("prodVo");

        if(vo!=null){
            info = productInfoService.splitPageVo((ProductInfoVo)vo,PAGE_SIZE);
            request.getSession().removeAttribute("prodVo");
        }else {
            //获取第1页的数据
            info = productInfoService.pageSplit(1, PAGE_SIZE);
        }

        request.setAttribute("info",info);  //将info传下去，传递到jsp页面

        return "product";
    }

    //Ajax分页翻页处理(多条件查询)
    @RequestMapping("/ajaxsplit")
    @ResponseBody   //ajax请求来的，一定要使用@ResponseBody注解
    public void ajaxSplit(ProductInfoVo vo,HttpSession session){

        //取得当前page的页面数据
//        PageInfo<ProductInfo> info = productInfoService.pageSplit(page, PAGE_SIZE);
        PageInfo info = productInfoService.splitPageVo(vo, PAGE_SIZE);

        //把数据传递到jsp页面上
        session.setAttribute("info",info);
    }


    //多条件查询
    @ResponseBody
    @RequestMapping("/condition")
    public void condition(ProductInfoVo vo,HttpSession session){

        List<ProductInfo> list = productInfoService.selectCondition(vo);

        session.setAttribute("list",list);

    }


    //ajax文件的异步上传
    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(MultipartFile pimage, HttpServletRequest request){
        //提取生成文件名UUID+上传图片的后缀
        fileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());

        //获取存储路径
        String path = request.getServletContext().getRealPath("/image_big");

        System.out.println("path==="+path);

        //转存
        try {
            pimage.transferTo(new File(path+File.separator+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //为了在客户端显示图片，要将存储的文件名回传下去，由于是自定义的上传插件，所以此处要手工处理JSON
        JSONObject object = new JSONObject();
        object.put("imgurl",fileName);
        //切记切记：JSON对象一定要toString()回到客户端
        return object.toString();
    }

    //添加商品
    @RequestMapping("/save")
    public String save(ProductInfo info,HttpServletRequest request){

        info.setpImage(fileName);
        info.setpDate(new Date());

        int i = productInfoService.save(info);

        if(i>0){
            request.setAttribute("msg","添加成功");
        }else {
            request.setAttribute("msg","添加失败");
        }

        //清空fileName变量中的内容，为了下次增加或修改的异步Ajax的上传处理
        fileName = "";

        //增加成功后，应该重新访问数据库，所以跳转到分页的split.action上
        return "forward:/prod/split.action";

    }

    //更新商品（商品回显）
    @RequestMapping("/one")
    public String one(int pid, Model model,ProductInfoVo vo,HttpSession session){

        ProductInfo info = productInfoService.getById(pid);


        model.addAttribute("prod",info);

        session.setAttribute("prodVo",vo);  //将多条件及页码放在session作用域中

        return "update";

    }

    //更新商品
    @RequestMapping("/update")
    public String update(ProductInfo info,HttpServletRequest request){

        if(!fileName.equals("")){
            info.setpImage(fileName);
        }

        request.setAttribute("prod",info);

        int num = -1;
        try {
            num = productInfoService.updata(info);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(num>0){
            request.setAttribute("msg","更新成功");
        }else {
            request.setAttribute("msg","更新失败");
        }

        //清空fileName中的内容
        fileName = "";

        return "forward:/prod/split.action";
    }

    //单个删除
    @RequestMapping("/delete")
    public String delete(int pid,ProductInfoVo vo,HttpServletRequest request){


        int num = -1;

        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(num>0){
            request.setAttribute("msg","删除成功");
            request.getSession().setAttribute("deleteProdVo", vo);  //删除成功后，携带当前页码和条件
        }else {
            request.setAttribute("msg","删除失败");
        }

        return "forward:/prod/deleteAjaxSplit.action";
    }


    @ResponseBody
    @RequestMapping(value = "/deleteAjaxSplit", produces = "text/html;charset=UTF-8")
    public Object deleteAjaxSplit(HttpServletRequest request) {

        PageInfo info = null;
        Object vo = request.getSession().getAttribute("deleteProdVo");
        if(vo != null){
            info = productInfoService.splitPageVo((ProductInfoVo)vo,PAGE_SIZE);
        }else {
            //取得第1页的数据
            info = productInfoService.pageSplit(1, PAGE_SIZE);
        }
        request.getSession().setAttribute("info",info);
        return request.getAttribute("msg");
    }

    //批量删除商品
    @RequestMapping("/deleteBatch")
    public String deleteBatch(String pids,ProductInfoVo vo,HttpServletRequest request){

        //将上传上来的字符串截开,形成商品id的字符数组
        String[] ps = pids.split(",");
        int num = -1;
        try {
            num = productInfoService.deleteBatch(ps);

            if(num > 0 ){
                request.setAttribute("msg","批量删除成功!");
                request.getSession().setAttribute("deleteProdVo", vo);
            }else{
                request.setAttribute("msg","批量删除失败!");
            }

        } catch (Exception e) {
            request.setAttribute("msg","商品不可删除!");
        }

//        return "forward:/prod/split.action";
        return "forward:/prod/deleteAjaxSplit.action";
    }



}
