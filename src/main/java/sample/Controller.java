package sample;

import excelutil.ExcelLoad;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;

public class Controller {
    @FXML
    private TextField mTextField;
    @FXML
    private TextField tempFolderPath;
    @FXML
    private TextField fileResutlPath;
    @FXML
    private TextField cloum;

    //选择EXCEL文件的位置
    @FXML
    public void onButtonClick(ActionEvent event) {
        System.out.println(event);
        ArrayList<String> filters = new ArrayList<>();
        filters.add("*.xls");
        filters.add("*.xlsx");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请选择Excel文件");
        fileChooser.setInitialDirectory(new File(FileSystemView.getFileSystemView().getHomeDirectory().getPath())
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("所有类型的Excel文件", filters),
                new FileChooser.ExtensionFilter(".xls", "*.xls"),
                new FileChooser.ExtensionFilter("xlsx", "*.xlsx")
        );

        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            mTextField.setText(file.getAbsolutePath());
        }
    }

    //获得生成模板的位置
    @FXML
    public void onGetTempFolderPath(ActionEvent event) {
        System.out.println(event);
        ArrayList<String> filters = new ArrayList<>();
//        filters.add("*.ftl");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请选择模文件");
        fileChooser.setInitialDirectory(new File(FileSystemView.getFileSystemView().getHomeDirectory().getPath()));
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("所有类型的FTL文件", filters),
//                new FileChooser.ExtensionFilter(".ftl", "*.ftl")
//        );
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            tempFolderPath.setText(file.getAbsolutePath());
        }
    }

    //获得生成目录的位置
    @FXML
    public void onButtonClickgGetFilePath(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(FileSystemView.getFileSystemView().getHomeDirectory().getPath()));
        File file = directoryChooser.showDialog(new Stage());
        if (file != null) {
            fileResutlPath.setText(file.getAbsolutePath());
        }
//        String path = file.getPath();//选择的文件夹路径
//        fileResutlPath.setText(path);
    }

    //启动生成
    @FXML
    public void onRunButtonClick() throws Exception {
        File file = new File(mTextField.getText());
        System.out.println(mTextField.getText());
        Alert alert = null;
        if (file.exists() && StringUtils.isNotBlank(mTextField.getText()) && StringUtils.isNotBlank(cloum.getText())) {
//等待添加方法
            int nameCloumNum = ExcelLoad.excelColStrToNum(cloum.getText());
            System.out.println(nameCloumNum);

            ExcelLoad.load(mTextField.getText(), tempFolderPath.getText(), fileResutlPath.getText(), nameCloumNum);
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("生成结束");
            alert.setContentText("任务生成结束，点击“确定”退出！");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        } else {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("生成失败");
            alert.setContentText("生成失败，请检查路径和命名列是否正确点，击“确定”退出！");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.CLOSE) {
                    System.out.println("Pressed OK.");
                }
            });
        }
    }

    @FXML
    public void onHelpbtn() throws Exception {
        try {
            //一定需要使用try-catch，不然编译器不会让你过的，Trust me!
            Parent anotherRoot = FXMLLoader.load(getClass().getResource("/image.fxml"));
            Stage anotherStage = new Stage();
            anotherStage.setTitle("文档工厂---帮助");
            anotherStage.setScene(new Scene(anotherRoot, 800, 800));
            anotherStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
