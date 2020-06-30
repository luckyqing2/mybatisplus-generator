package ${package.Repository};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};

/**
 * ${table.comment!} 服务类
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName}{


    <#list cfg?keys as k>
    <#if "${k}" == "${table.name}">
    /**
    *   保存
    * @author ${author}
    * @since ${date}
    */
    Boolean save${cfg[k]}(${entity} ${entity?uncap_first});

    /**
    *   通过ID更新
    *
    * @author ${author}
    * @since ${date}
    */
    Boolean update${cfg[k]}ById(${entity} ${entity?uncap_first});

    /**
    *   通过ID获取
    * @author ${author}
    * @since ${date}
    */
    ${entity} get${cfg[k]}ById(Long ${cfg[k]?uncap_first}Id);

    /**
    * 逻辑删除，不考虑逻辑字段，只要设置过逻辑字段数据，就会更改对应的字段数据
    * @author ${author}
    * @since ${date}
    */
    Boolean remove${cfg[k]}ByLogicWithUpdate(${entity} ${entity?uncap_first});
    </#if>
    </#list>



}
</#if>
