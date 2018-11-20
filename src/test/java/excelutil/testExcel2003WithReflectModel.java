package excelutil;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
public class testExcel2003WithReflectModel {
    @Test
    public void testExcel2003WithReflectModel() throws Exception {
        InputStream inputStream = new FileInputStream("C:\\Users\\47099\\Desktop\\云南大理4G竣工资料-赵真亮\\站点信息表.xlsx");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//        InputStream inputStream = getInputStream("loan1.xls");
        try {
            // 解析每行结果在listener中处理
            AnalysisEventListener listener = new ExcelListener();

            ExcelReader excelReader = new ExcelReader(bufferedInputStream, null, listener);

            excelReader.read(new Sheet(1, 0));
        } catch (Exception e) {

        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
