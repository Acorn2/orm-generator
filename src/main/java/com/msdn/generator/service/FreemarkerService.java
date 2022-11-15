package com.msdn.generator.service;

import com.msdn.generator.entity.Config;
import com.msdn.generator.entity.GenerateParameter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com
 * 使用FreeMarker 根据设定好的文件模板来生成相关文件
 */
@Service
public class FreemarkerService {

  @Autowired
  private Configuration configuration;

  /**
   * 输出文件模板
   *
   * @param templateName      resources 文件夹下的模板名，比如说model.ftl，是生成实体类的模块
   * @param dataModel         表名，字段名等内容集合
   * @param filePath          输出文件名，包括路径
   * @param generateParameter
   * @throws Exception
   */
  public void write(String templateName, Map<String, Object> dataModel, String filePath,
      GenerateParameter generateParameter) throws Exception {
    // FTL(freemarker templete language)模板的文件名称
    Template template = configuration
        .getTemplate(dataModel.get("type") + File.separator + templateName + ".ftl");
    File file;
    // 判断是不是多表，如果是，则按照表名生成各自的文件夹目录
    if (generateParameter.isFlat()) {
      file = new File(
          Config.OUTPUT_PATH + File.separator + dataModel.get("tempId") + File.separator + filePath);
    } else {
      file = new File(
          Config.OUTPUT_PATH + File.separator + dataModel.get("tempId") + File.separator + dataModel
              .get("tableName") + File.separator + filePath);
    }
    if (!file.exists()) {
      file.getParentFile().mkdirs();
      file.createNewFile();
    }
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,
        StandardCharsets.UTF_8);
    template.process(dataModel, outputStreamWriter);
    fileOutputStream.flush();
    fileOutputStream.close();
  }
}
