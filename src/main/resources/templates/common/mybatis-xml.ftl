<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package}.mapper.${pascalName}Mapper">

    <resultMap id="baseResultMap" type="${package}.model.${pascalName}">
    <#list columns as column>
    <#if column.isPrimaryKey>
        <id column="${column.fieldName}" property="${column.camelName}" />
    <#else>
        <result column="${column.fieldName}" property="${column.camelName}" />
    </#if>
    </#list>
    </resultMap>

</mapper>
