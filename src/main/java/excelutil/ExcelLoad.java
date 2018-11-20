package excelutil;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import freemarker.FreeMarkerUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Title: testExcel2003WithReflectModel
 * Description:
 * Date: 2018年11月17日 21:16
 *
 * @author 47099
 * @version 1.0
 * Significant Modify：
 * Date                  Author                 Content
 * =================================================================
 * 2018年11月17日         47099         创建文件,实现基本功能
 * =================================================================
 */
public class ExcelLoad {

    public static void load(String excelFileName, String tempFolderPath, String resultPath, int nameCloumNum) throws Exception {

        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(excelFileName));
//        InputStream inputStream = getInputStream("loan1.xls");
        try {
            // 解析每行结果在listener中处理
            AnalysisEventListener listener = new ExcelListener(nameCloumNum);

            ExcelReader excelReader = new ExcelReader(inputStream, null, listener);

            excelReader.read(new Sheet(1, 0));

            List<Map<String, String>> maps = ((ExcelListener) listener).getMaps();
            for (Map<String, String> map : maps) {
                FreeMarkerUtil.produceResult(new File(tempFolderPath).getParent(), map, new File(tempFolderPath).getName(), resultPath);
            }

        } catch (
                Exception e) {

        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Excel column index begin 1
     *
     * @param colStr
     * @param length
     * @return
     */
    private static int excelColStrToNum(String colStr, int length) {
        int num = 0;
        int result = 0;
        for (int i = 0; i < length; i++) {
            char ch = colStr.charAt(length - i - 1);
            num = (int) (ch - 'A' + 1);
            num *= Math.pow(26, i);
            result += num;
        }
        return result;
    }

    public static int excelColStrToNum(String colStr) {
        colStr = StringUtils.upperCase(colStr);
        return excelColStrToNum(colStr, colStr.length());
    }
}
