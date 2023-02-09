package com.msdn.generator.service;

import cn.hutool.core.date.DateUtil;
import com.msdn.generator.entity.Column;
import com.msdn.generator.entity.Config;
import com.msdn.generator.entity.GenerateParameter;
import com.msdn.generator.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2021/5/13 21:26
 * @description 根据表生成xml文件中resultMap
 */
@Service
public class XmlGenerateService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(XmlGenerateService.class);

    @Autowired
    private FreemarkerService freemarkerService;

    /**
     * @param tableName 数据库表名
     * @param parameter 模块名
     * @param uuid
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
//        int index = tableName.indexOf("_", 2);
        Integer index = new Integer(parameter.getTableStartIndex());
        // 驼峰命名，首字母小写,比如：contractDetail
        String camelName = StringUtils.underscoreToCamel(tableName.substring(index));

        Map<String, Object> dataModel = new HashMap<>();
        //获取表中字段的具体信息，包括字段名，字段类型，备注等,排除指定字段
        List<Column> columns = getColumns(tableName, null);
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

        logger.info("准备生成模板代码的表名为：" + tableName + ",表描述为：" + dataModel.get("tableComment"));

        // 生成模板代码
        logger.info("**********开始生成Model模板文件**********");
        generateXML(dataModel, parameter);
    }

    /**
     * 生成 controller 模板代码
     *
     * @param dataModel
     * @param generateParameter
     * @throws Exception
     */
    private void generateXML(Map<String, Object> dataModel, GenerateParameter generateParameter)
            throws Exception {
        String path =
                "resources" + File.separator + "xml" + File.separator + dataModel.get("pascalName")
                        + "Mapper.xml";
        freemarkerService.write("mybatis-xml", dataModel, path, generateParameter);
    }
}
