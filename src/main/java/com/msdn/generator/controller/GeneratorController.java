package com.msdn.generator.controller;

import com.msdn.generator.entity.Config;
import com.msdn.generator.entity.GenerateParameter;
import com.msdn.generator.service.BaseService;
import com.msdn.generator.service.GenerateService;
import com.msdn.generator.service.XmlGenerateService;
import io.swagger.v3.oas.annotations.Operation;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/generator")
@Slf4j
@RequiredArgsConstructor
public class GeneratorController {

    private final GenerateService generateService;
    private final XmlGenerateService xmlGenerateService;

    /*
        // 请求参数
        {
            "database": "db_tl_sale",
            "flat": true,
            "type": "mybatis",
            "group": "base",
            "host": "127.0.0.1",
            "module": "sale",
            "password": "123456",
            "port": 3306,
            "table": [
                "t_xs_sale_contract"
            ],
            "username": "root"
        }
     */
    @PostMapping("/build")
    @Operation(description = "选择orm框架后生成基础模版代码")
    public void build(@RequestBody GenerateParameter parameter, HttpServletResponse response)
            throws Exception {
        log.info("**********欢迎使用基于FreeMarker的模板文件生成器**********");
        log.info("************************************************************");
        String uuid = UUID.randomUUID().toString();
        BaseService.setConnection(parameter);
        for (String table : parameter.getTable()) {
            generateService.generate(table, parameter, uuid);
        }
        log.info("**********模板文件生成完毕，准备下载**********");
        String path = Config.OUTPUT_PATH + File.separator + uuid;
        //设置响应头控制浏览器的行为，这里我们下载zip
        response.setHeader("Content-disposition", "attachment; filename=code.zip");
        response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
        // 将response中的输出流中的文件压缩成zip形式
        ZipDirectory(path, response.getOutputStream());
        // 递归删除目录
        FileSystemUtils.deleteRecursively(new File(path));
        BaseService.closeConnection();
        log.info("************************************************************");
        log.info("**********模板文件下载完毕，谢谢使用**********");
    }

    @PostMapping("/buildXml")
    @Operation(description = "选择orm框架后生成基础模版代码，针对Mybatis会补充生成xml文件中的resultMap")
    public void buildXml(@RequestBody GenerateParameter parameter, HttpServletResponse response)
            throws Exception {
        log.info("**********欢迎使用基于FreeMarker的模板文件生成器**********");
        log.info("************************************************************");
        String uuid = UUID.randomUUID().toString();
        BaseService.setConnection(parameter);
        for (String table : parameter.getTable()) {
            xmlGenerateService.generate(table, parameter, uuid);
        }
        log.info("**********模板文件生成完毕，准备下载**********");
        String path = Config.OUTPUT_PATH + File.separator + uuid;
        //设置响应头控制浏览器的行为，这里我们下载zip
        response.setHeader("Content-disposition", "attachment; filename=code.zip");
        response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
        // 将response中的输出流中的文件压缩成zip形式
        ZipDirectory(path, response.getOutputStream());
        // 递归删除目录
        FileSystemUtils.deleteRecursively(new File(path));
        BaseService.closeConnection();
        log.info("************************************************************");
        log.info("**********模板文件下载完毕，谢谢使用**********");
    }

    /**
     * 一次性压缩多个文件，文件存放至一个文件夹中
     */
    public static void ZipDirectory(String directoryPath, ServletOutputStream outputStream) {
        try {
            ZipOutputStream output = new ZipOutputStream(outputStream);
            List<File> files = getFiles(new File(directoryPath));
            for (File file : files) {
                try (InputStream input = new FileInputStream(file)) {
                    output.putNextEntry(
                            new ZipEntry(file.getPath().substring(directoryPath.length() + 1)));
                    int temp;
                    while ((temp = input.read()) != -1) {
                        output.write(temp);
                    }
                }
            }
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<File> getFiles(File file) {
        List<File> files = new ArrayList<>();
        for (File subFile : Objects.requireNonNull(file.listFiles())) {
            if (subFile.isDirectory()) {
                List<File> subFiles = getFiles(subFile);
                files.addAll(subFiles);
            } else {
                files.add(subFile);
            }
        }
        return files;
    }
}
