package com.qiang.listener;

import com.qiang.pojo.ProductType;
import com.qiang.service.ProductTypeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

@WebListener
public class ProductTypeListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        //获取spring容器
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext_*.xml");

        //从spring容器中获取ProductTypeServiceImpl对象
        ProductTypeService productTypeService = (ProductTypeService) context.getBean("ProductTypeServiceImpl");

        //ProductTypeServiceImpl对象调用业务逻辑层的getAll()获取所有商品类型
        List<ProductType> typeList = productTypeService.getAll();

        //将商品类型保存在application全局作用域中,供新增页面、修改页面、前台查询功能提供全部商品的集合
        servletContextEvent.getServletContext().setAttribute("typeList",typeList);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
