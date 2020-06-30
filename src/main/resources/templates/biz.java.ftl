package ${package.Controller};

import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ${table.comment!} 业务处理类
 *
 * @author ${author}
 * @since ${date} 
 */
<#if kotlin>
interface ${table.controllerName} : ${superServiceClass}<${entity}>
<#else>
@Component
public class ${table.controllerName}{

     @Autowired
     private ${table.serviceName} ${table.serviceName?substring(1) ? uncap_first};

    <#list cfg?keys as k>
        <#if "${k}" == "${table.name}">
    /**
    *   保存
    * @author ${author}
    * @since ${date}
    */
    public Boolean save${cfg[k]}(${entity} ${entity?uncap_first}){
        return ${table.serviceName?substring(1)?uncap_first}.save${cfg[k]}(${entity?uncap_first});
    }

    /**
    *   通过ID更新
    *
    * @author ${author}
    * @since ${date}
    */
    public Boolean update${cfg[k]}ById(${entity} ${entity?uncap_first}){
        return ${table.serviceName?substring(1)?uncap_first}.update${cfg[k]}ById(${entity?uncap_first});
    }

    /**
    *   通过ID获取
    * @author ${author}
    * @since ${date}
    */
     public ${entity} get${cfg[k]}ById(Long ${cfg[k]?uncap_first}Id){
        return ${table.serviceName?substring(1)?uncap_first}.get${cfg[k]}ById(${cfg[k]?uncap_first}Id);
    }

    /**
    * 逻辑删除，不考虑逻辑字段，只要设置过逻辑字段数据，就会更改对应的字段数据
    * @author ${author}
    * @since ${date}
    */
    public Boolean remove${cfg[k]}ByLogicWithUpdate(${entity} ${entity?uncap_first}){
        return ${table.serviceName?substring(1)?uncap_first}.remove${cfg[k]}ByLogicWithUpdate(${entity?uncap_first});
    }
        </#if>
    </#list>


}
</#if>
