package com.java2word;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xhd
 * @date 2017-10-30
 */
public class Test {

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<String, Object>();

        List<Dept> deptList = new ArrayList<Dept>();
        Dept dept1 = new Dept();
        dept1.setRn(1);
        dept1.setRnText("一");
        dept1.setDeptName("部门1");
        List<Catalog> list1 = new ArrayList<Catalog>();
        Catalog catalog1 = new Catalog();
        catalog1.setRn(1);
        catalog1.setRestitle("名称1");
        catalog1.setResProviderDeptName("提供方1");
        catalog1.setResProviderCode("提供方代码1");
        catalog1.setResFormartClassify("格式分类1");
        catalog1.setResFormartType("格式类型1");
        catalog1.setOtherTypeResFormatDesc("格式描述1");
        catalog1.setShareStateText("共享1");
        catalog1.setIsOpenText("开放1");
        catalog1.setResUpdateFrequencyText("每天1");

        Catalog catalog2 = new Catalog();
        catalog2.setRn(2);
        catalog2.setRestitle("名称2");
        catalog2.setResProviderDeptName("提供方2");
        catalog2.setResProviderCode("提供方代码2");
        catalog2.setResFormartClassify("格式分类2");
        catalog2.setResFormartType("格式类型2");
        catalog2.setOtherTypeResFormatDesc("格式描述2");
        catalog2.setShareStateText("共享2");
        catalog2.setIsOpenText("开放2");
        catalog2.setResUpdateFrequencyText("每天2");
        list1.add(catalog1);
        list1.add(catalog2);
        dept1.setResList(list1);


        Dept dept2 = new Dept();
        dept2.setRn(2);
        dept2.setRnText("二");
        dept2.setDeptName("部门2");
        List<Catalog> list2 = new ArrayList<Catalog>();
        Catalog catalog3 = new Catalog();
        catalog3.setRn(3);
        catalog3.setRestitle("名称3");
        catalog3.setResProviderDeptName("提供方3");
        catalog3.setResProviderCode("提供方代码3");
        catalog3.setResFormartClassify("格式分类3");
        catalog3.setResFormartType("格式类型3");
        catalog3.setOtherTypeResFormatDesc("格式描述3");
        catalog3.setShareStateText("共享3");
        catalog3.setIsOpenText("开放3");
        catalog3.setResUpdateFrequencyText("每天3");

        Catalog catalog4 = new Catalog();
        catalog4.setRn(4);
        catalog4.setRestitle("名称4");
        catalog4.setResProviderDeptName("提供方4");
        catalog4.setResProviderCode("提供方代码4");
        catalog4.setResFormartClassify("格式分类4");
        catalog4.setResFormartType("格式类型4");
        catalog4.setOtherTypeResFormatDesc("格式描述4");
        catalog4.setShareStateText("共享4");
        catalog4.setIsOpenText("开放4");
        catalog4.setResUpdateFrequencyText("每天4");
        list2.add(catalog3);
        list2.add(catalog4);
        dept2.setResList(list2);

        deptList.add(dept1);
        deptList.add(dept2);
        map.put("deptList",deptList);

        createDoc(map,"resume");
    }



    private static Configuration configuration = null ;
    private static Map<String, Template> allTemplates = null ;

    static {
        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassForTemplateLoading(Test.class, "/");
        allTemplates = new HashMap<String, Template>();   //
        try {
            allTemplates.put("resume", configuration.getTemplate("编目word模板.ftl"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Test() {
        throw new AssertionError();
    }

    public static File createDoc(Map<?, ?> dataMap, String type) {
        String name = "temp" + (int) (Math.random() * 100000) + ".doc";
        File f = new File(name);
        Template t = allTemplates.get(type);
        try {
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            t.process(dataMap, w);
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return f;
    }


//    private Configuration configuration = ;
//
//    public WordTest(){
//        configuration = new Configuration();
//        configuration.setDefaultEncoding("UTF-8");
//    }
//
//    public static void main(String[] args) {
//        WordTest test = new WordTest();
//        test.createWord();
//    }
//
//    public void createWord(){
//        Map<String,Object> dataMap=new HashMap<String,Object>();
//        getData(dataMap);
//        configuration.setClassForTemplateLoading(this.getClass(), "/com");  //FTL文件所存在的位置
//        Template t=;
//        try {
//            t = configuration.getTemplate("wordModel.ftl"); //文件名
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        File outFile = new File("O:/outFilessa"+Math.random()*10000+".doc");
//        Writer out = ;
//        try {
//            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
//        } catch (FileNotFoundException e1) {
//            e1.printStackTrace();
//        }
//
//        try {
//            t.process(dataMap, out);
//        } catch (TemplateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getData(Map<String, Object> dataMap) {
//        dataMap.put("title", "标题");
//        dataMap.put("year", "2012");
//        dataMap.put("month", "2");
//        dataMap.put("day", "13");
//        dataMap.put("auditor", "唐鑫");
//        dataMap.put("phone", "13020265912");
//        dataMap.put("weave", "占文涛");
////      dataMap.put("number", 1);
////      dataMap.put("content", "内容"+2);
//
//        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//        for (int i = 0; i < 10; i++) {
//            Map<String,Object> map = new HashMap<String,Object>();
//            map.put("number", i);
//            map.put("content", "内容"+i);
//            list.add(map);
//        }
//
//
//        dataMap.put("list", list);
//    }
}
