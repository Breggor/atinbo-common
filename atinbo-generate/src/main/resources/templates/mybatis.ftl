<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${classInfo.packageName}.mapper.${classInfo.className}Mapper">

    <resultMap id="${classInfo.className}" type="${classInfo.packageName}.model.${classInfo.className}" >
    <#if classInfo.fieldList?exists && classInfo.fieldList?size gt 0>
    <#list classInfo.fieldList as fieldItem >
        <#if classInfo.primaryField.columnName == fieldItem.columnName>
        <id column="${fieldItem.columnName}" property="${fieldItem.fieldName}" />
        <#else>
        <result column="${fieldItem.columnName}" property="${fieldItem.fieldName}" />
        </#if>
    </#list>
    </#if>
    </resultMap>

    <sql id="Base_Column_List">
    <#if classInfo.fieldList?exists && classInfo.fieldList?size gt 0>
    <#list classInfo.fieldList as fieldItem >
        `${fieldItem.columnName}`<#if fieldItem_has_next>,</#if>
    </#list>
    </#if>
    </sql>

    <insert id="insert" parameterType="java.util.Map" >
        INSERT INTO ${classInfo.tableName} (
        <#if classInfo.fieldList?exists && classInfo.fieldList?size gt 0>
        <#list classInfo.fieldList as fieldItem >
            <#if classInfo.primaryField.columnName != fieldItem.columnName>
            `${fieldItem.columnName}`<#if fieldItem_has_next>,</#if>
            </#if>
        </#list>
        </#if>
        )
        VALUES(
        <#if classInfo.fieldList?exists && classInfo.fieldList?size gt 0>
        <#list classInfo.fieldList as fieldItem >
        <#if classInfo.primaryField.columnName != fieldItem.columnName>
            ${r"#{"}${classInfo.className?uncap_first}.${fieldItem.fieldName}${r"}"}<#if fieldItem_has_next>,</#if>
        </#if>
        </#list>
        </#if>
        )
    </insert>

    <delete id="delete" parameterType="java.util.Map" >
        DELETE FROM ${classInfo.tableName}
        WHERE `${classInfo.primaryField.columnName}` = ${r"#{"}${classInfo.primaryField.fieldName}${r"}"}
    </delete>

    <update id="update" parameterType="java.util.Map" >
        UPDATE ${classInfo.tableName}
        SET
        <#list classInfo.fieldList as fieldItem >
        <#if classInfo.primaryField.columnName != fieldItem.columnName>
            `${fieldItem.columnName}` = ${r"#{"}${classInfo.className?uncap_first}.${fieldItem.fieldName}${r"}"}<#if fieldItem_has_next>,</#if>
        </#if>
        </#list>
        WHERE `${classInfo.primaryField.columnName}` = ${r"#{"}${classInfo.className?uncap_first}.${classInfo.primaryField.fieldName}${r"}"}
    </update>

    <select id="selectById" resultMap="${classInfo.className}">
        SELECT <include refid="Base_Column_List" />
        FROM ${classInfo.tableName}
        WHERE `${classInfo.primaryField.columnName}` = ${r"#{"}${classInfo.primaryField.fieldName}${r"}"}
    </select>

    <select id="pageList" parameterType="java.util.Map" resultMap="${classInfo.className}">
        SELECT <include refid="Base_Column_List" />
        FROM ${classInfo.tableName}
        LIMIT ${r"#{offset}"}, ${r"#{pagesize}"}
    </select>

    <select id="pageListCount" parameterType="java.util.Map" resultType="int">
        SELECT count(1)
        FROM ${classInfo.tableName}
    </select>

</mapper>
