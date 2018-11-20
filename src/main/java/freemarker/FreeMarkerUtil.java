package freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Title: FreeMarkerUtil
 * Description:
 * Date: 2018年11月17日 23:18
 *
 * @author 47099
 * @version 1.0
 * Significant Modify：
 * Date                  Author                 Content
 * =================================================================
 * 2018年11月17日         47099         创建文件,实现基本功能
 * =================================================================
 */
public class FreeMarkerUtil {
    private static Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);

    private static final List<String> ZIP_FILE = Arrays.asList("docx", "xlsx");

    static {
        cfg.setDefaultEncoding("UTF-8");
    }

    public static void produceResult(String tempFolderPath, Map dataModelMap, String ftlName, String resultPath) throws Exception {
        //如果以"docx", "xlsx"结尾则
        if (ZIP_FILE.contains(StringUtils.substringAfterLast(ftlName, ".").toLowerCase())) {
            produceResult(dataModelMap, tempFolderPath + "/" + ftlName, resultPath);
        } else {
            //不包含则直接按文本处理
            cfg.setDirectoryForTemplateLoading(new File(tempFolderPath));
            Template temp = cfg.getTemplate(ftlName);

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultPath + "/"
                    + dataModelMap.get("fileName") + "." + StringUtils.substringAfterLast(ftlName, ".")));
            temp.process(dataModelMap, bufferedWriter);
            bufferedWriter.close();
        }
    }

    private static void produceResult(Map dataModelMap, String ftlName, String resultPath) {
        ZipInputStream zipInputStream = null;
        ZipOutputStream zipOutputStream = null;
        try {
            //输入流取到模板
            zipInputStream = new ZipInputStream(new FileInputStream(ftlName), StandardCharsets.UTF_8);
            zipOutputStream = new ZipOutputStream(new FileOutputStream(resultPath + "/"
                    + dataModelMap.get("fileName") + "." + StringUtils.substringAfterLast(ftlName, ".")), StandardCharsets.UTF_8);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(zipInputStream);
            ZipEntry zipFile;
            //循环读取zip中的cvs文件，无法使用jdk自带，因为文件名中有中文
            while ((zipFile = zipInputStream.getNextEntry()) != null) {
                System.out.println(zipFile.getName());
                //放入一个新的实体压缩内容
                zipOutputStream.putNextEntry(new ZipEntry(zipFile.getName()));
                if ("xml".equals(StringUtils.substringAfterLast(zipFile.getName(), "."))) {
                    //内存缓冲流从bufferedInputStream取出数据到byteArrayOutputStream缓冲一下数据
                    //如果不存入内存流里面Freemarker内部会把传进去的流关闭
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    IOUtils.copy(bufferedInputStream, byteArrayOutputStream);
                    System.out.println(byteArrayOutputStream.toString("UTF-8"));
//            ----------------------------------------------------------------
                    Template template = new Template(zipFile.getName(), byteArrayOutputStream.toString("UTF-8"), cfg);
                    ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                    template.process(dataModelMap, new OutputStreamWriter(byteArrayOutputStream2, StandardCharsets.UTF_8));
                    System.out.println(byteArrayOutputStream2.toString());
                    //从内存流写入压缩输出流
                    byteArrayOutputStream2.writeTo(zipOutputStream);
//
                    byteArrayOutputStream2.flush();
                    byteArrayOutputStream2.close();
                } else {
                    IOUtils.copy(bufferedInputStream, zipOutputStream);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zipInputStream != null) {
                try {
                    zipInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}