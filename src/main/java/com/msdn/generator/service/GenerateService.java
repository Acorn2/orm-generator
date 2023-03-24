package com.msdn.generator.service;

import cn.hutool.core.date.DateUtil;
import com.msdn.generator.entity.Column;
import com.msdn.generator.entity.Config;
import com.msdn.generator.entity.GenerateParameter;
import com.msdn.generator.utils.StringUtils;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/ 获取表字段信息，
 */
@Service
@Slf4j
public class GenerateService extends BaseService {

    @Autowired
    private FreemarkerService freemarkerService;

    /**
     * @param tableName 数据库表名
     * @param parameter 模块名
     * @param uuid      生成uuid
     * @throws Exception
     */
    public void generate(String tableName, GenerateParameter parameter, String uuid)
            throws Exception {

        // 各模块包名，比如 com.msdn.sale 或 com.msdn.finance
        String packagePrefix = "com.msdn." + parameter.getModule();
        // 分组
        if (!StringUtils.isEmpty(parameter.getGroup())) {
            packagePrefix = packagePrefix + "." + parameter.getGroup();
        }

        // 根据项目设计的表名获取到表名，比如表名叫做：t_sale_contract_detail
        // 现在表名截取起始索引该由参数配置
        Integer index = new Integer(parameter.getTableStartIndex());
        // 驼峰命名，首字母小写,比如：contractDetail
        String camelName = StringUtils.underscoreToCamel(tableName.substring(index));

        Map<String, Object> dataModel = new HashMap<>();
        //获取表中字段的具体信息，包括字段名，字段类型，备注等,排除指定字段
        String[] commonColumns = Config.MYBATIS_COMMON_COLUMNS;
        if ("jpa".equals(parameter.getType().toLowerCase())) {
            commonColumns = Config.JPA_COMMON_COLUMNS;
        } else if ("mybatis".equals(parameter.getType().toLowerCase())) {
            commonColumns = Config.MYBATIS_COMMON_COLUMNS;
        } else if ("mybatisplus".equals(parameter.getType().toLowerCase())) {
            commonColumns = Config.MYBATIS_PLUS_COMMON_COLUMNS;
        }
        List<Column> columns = getColumns(tableName, commonColumns);
        Column primaryColumn = columns.stream().filter(Column::getIsPrimaryKey).findFirst()
                .orElse(null);
        dataModel.put("package", packagePrefix);
        dataModel.put("camelName", camelName);
        // 首字母转大写，作为实体类名称等
        dataModel.put("pascalName", StringUtils.firstLetterUpperCase(camelName));
        dataModel.put("moduleName", parameter.getModule());
        dataModel.put("tableName", tableName);
        // 表描述
        dataModel.put("tableComment", getTableComment(tableName, parameter));
        dataModel.put("columns", columns);
        dataModel.put("primaryColumn", primaryColumn);
        dataModel.put("tempId", uuid);
        dataModel.put("author", Config.AUTHOR);
        dataModel.put("date", DateUtil.now());
        dataModel.put("type", parameter.getType());

        log.info("准备生成模板代码的表名为：" + tableName + ",表描述为：" + dataModel.get("tableComment"));

        // 生成模板代码
        log.info("**********开始生成Model模板文件**********");
        generateModel(dataModel, parameter);
        log.info("**********开始生成VO视图模板文件**********");
        generateVO(dataModel, parameter);
        log.info("**********开始生成DTO模板文件**********");
        generateDTO(dataModel, parameter);
        log.info("**********开始生成Struct模板文件**********");
        generateStruct(dataModel, parameter);
        log.info("**********开始生成Mapper模板文件**********");
        generateMapper(dataModel, parameter);
        log.info("**********开始生成Service模板文件**********");
        generateService(dataModel, parameter);
        log.info("**********开始生成Controller模板文件**********");
        generateController(dataModel, parameter);
    }

    /**
     * 生成 controller 模板代码
     *
     * @param dataModel
     * @param generateParameter
     * @throws Exception
     */
    private void generateController(Map<String, Object> dataModel,
            GenerateParameter generateParameter) throws Exception {
        String path =
                "java" + File.separator + "controller" + File.separator + dataModel
                        .get("pascalName")
                        + "Controller.java";
        freemarkerService.write("controller", dataModel, path, generateParameter);
    }

    private void generateDTO(Map<String, Object> dataModel, GenerateParameter generateParameter)
            throws Exception {
        String path =
                "java" + File.separator + "dto" + File.separator + dataModel.get("pascalName");
        freemarkerService.write("dto", dataModel, path + "DTO.java", generateParameter);
        freemarkerService
                .write("dto-page", dataModel, path + "QueryPageDTO.java", generateParameter);
    }

    //
    private void generateModel(Map<String, Object> dataModel, GenerateParameter generateParameter)
            throws Exception {
        String path =
                "java" + File.separator + "model" + File.separator + dataModel.get("pascalName")
                        + ".java";
        freemarkerService.write("model", dataModel, path, generateParameter);
    }

    private void generateStruct(Map<String, Object> dataModel, GenerateParameter generateParameter)
            throws Exception {
        String path =
                "java" + File.separator + "struct" + File.separator + dataModel.get("pascalName")
                        + "Struct.java";
        freemarkerService.write("struct", dataModel, path, generateParameter);
    }

    private void generateMapper(Map<String, Object> dataModel, GenerateParameter generateParameter)
            throws Exception {
        if (!"jpa".equals(generateParameter.getType().toLowerCase())) {
            String path = "java" + File.separator + "mapper" + File.separator + dataModel
                    .get("pascalName")
                    + "Mapper.java";
            freemarkerService.write("mapper", dataModel, path, generateParameter);

            path = "resources" + File.separator + dataModel.get("pascalName") + "Mapper.xml";
            freemarkerService.write("mapper-xml", dataModel, path, generateParameter);
        } else {
            String path = "java" + File.separator + "repository" + File.separator + dataModel
                    .get("pascalName")
                    + "Repository.java";
            freemarkerService.write("repository", dataModel, path, generateParameter);
        }
    }

    private void generateService(Map<String, Object> dataModel, GenerateParameter generateParameter)
            throws Exception {
        String path =
                "java" + File.separator + "service" + File.separator + dataModel.get("pascalName")
                        + "Service.java";
        freemarkerService.write("service", dataModel, path, generateParameter);

    }

    private void generateVO(Map<String, Object> dataModel, GenerateParameter generateParameter)
            throws Exception {
        String path =
                "java" + File.separator + "vo" + File.separator + dataModel.get("pascalName")
                        + "VO.java";
        freemarkerService.write("vo", dataModel, path, generateParameter);
    }
}
