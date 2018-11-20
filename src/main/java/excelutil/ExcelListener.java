package excelutil;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* 解析监听器，
 * 每解析一行会回调invoke()方法。
 * 整个excel解析结束会执行doAfterAllAnalysed()方法
 *
 * 下面只是我写的一个样例而已，可以根据自己的逻辑修改该类。
 * @author jipengfei
 * @date 2017/03/14
 */
public class ExcelListener extends AnalysisEventListener {

    //自定义用于暂时存储data。
    //可以通过实例获取该值
    private List<String> keyName;

    private List<Map<String, String>> maps = new ArrayList<>(10);

    private int nameCloumNum;

    public ExcelListener() {
    }

    public ExcelListener(int nameCloumNum) {
        this.nameCloumNum = nameCloumNum;
    }

    public void invoke(Object object, AnalysisContext context) {
        List<String> tempList = (List<String>) object;
        if (context.getCurrentRowNum() == 0) {
            keyName = (List<String>) object;
            System.out.println(keyName);
        } else {
            System.out.println(tempList);
            Map<String, String> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("fileName", tempList.get(nameCloumNum - 1));
            for (int i = 0; i < keyName.size(); i++) {
                if (ObjectUtils.allNotNull(keyName.get(i))) {
                    stringStringHashMap.put(keyName.get(i), tempList.get(i));
                }
            }
            doSomething(stringStringHashMap);//根据自己业务做处理
        }
    }

    private void doSomething(Map<String, String> stringStringHashMap) {
        maps.add(stringStringHashMap);
        System.out.println(stringStringHashMap);
        //1、入库调用接口

    }

    public void doAfterAllAnalysed(AnalysisContext context) {
        // datas.clear();//解析结束销毁不用的资源
    }

    public List<Map<String, String>> getMaps() {
        return maps;
    }

    public void setMaps(List<Map<String, String>> maps) {
        this.maps = maps;
    }

    public int getNameCloumNum() {
        return nameCloumNum;
    }

    public void setNameCloumNum(int nameCloumNum) {
        this.nameCloumNum = nameCloumNum;
    }
}