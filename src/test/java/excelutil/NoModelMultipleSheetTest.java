package excelutil;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class NoModelMultipleSheetTest {

    @Test
    public void noModelMultipleSheet() throws Exception {

        try (InputStream inp = new FileInputStream("C:\\Users\\LiangJian\\Desktop\\快递精简模板.xls")) {
            //InputStream inp = new FileInputStream("workbook.xlsx");
            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheetAt(0);
            CellReference a1 = new CellReference("A");
            System.out.println(a1.getCol());
            System.out.println(sheet.getLastRowNum());
            Row row = sheet.getRow(2);

            Cell cell = row.getCell(3);
            if (cell == null)
                cell = row.createCell(3);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("a test");

            // Write the output to a file
            try (OutputStream fileOut = new FileOutputStream("C:\\Users\\LiangJian\\Desktop\\快递精简模板.xls")) {
                wb.write(fileOut);
            }
        }

    }

    @Test
    public void test() throws Exception {
        readZipCvsFile(new File("C:\\Users\\47099\\Desktop\\云南大理4G竣工资料-赵真亮\\模板位置\\大理古城区太邑乡己早村北-LZHN（模版）.docx"));
    }

    /**
     * 读取zip文件，不解压缩直接解析，支持文件名中文，解决内容乱码
     *
     * @param file
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void readZipCvsFile(File file) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipInputStream in = new ZipInputStream(new FileInputStream(file));
        //不解压直接读取,加上gbk解决乱码问题
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        ZipEntry zipFile;
        //循环读取zip中的cvs文件，无法使用jdk自带，因为文件名中有中文
        while ((zipFile = in.getNextEntry()) != null) {
            if (zipFile.isDirectory()) {
                //如果是目录，不处理
            }
            //获得cvs名字
            String fileName = zipFile.getName();
            System.out.println("-----" + fileName);
            if (fileName.equals("word/document.xml")) {
                String line;
                while ((line = br.readLine()) != null) {
                    byteArrayOutputStream.write(line.getBytes());
                    System.out.println(line);
                }
                System.out.println(byteArrayOutputStream.toString());
            }

            //检测文件是否存在
//            if (fileName != null && fileName.indexOf(".") != -1) {
//                String line;
//                while ((line = br.readLine()) != null) {
//                    System.out.println(line);
//                }
//            }
        }
        //关闭流
        br.close();
        in.close();
    }

}