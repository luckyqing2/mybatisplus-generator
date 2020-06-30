package ${package.RepositoryImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Repository}.${table.serviceName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
/**
 * ${table.comment!} 服务类
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
@Component
public class ${table.serviceImplName} implements ${table.serviceName}{

     @Autowired
     private ${table.mapperName} ${table.mapperName?uncap_first};

    <#list cfg?keys as k>
        <#if "${k}" == "${table.name}">
    /**
    *   保存
    * @author ${author}
    * @since ${date}
    */
    @Override
    public Boolean save${cfg[k]}(${entity} ${entity?uncap_first}){
        return SqlHelper.retBool(${table.mapperName?uncap_first}.insert( ${entity?uncap_first}));
    }

    /**
    *   通过ID更新
    *
    * @author ${author}
    * @since ${date}
    */
    @Override
    public Boolean update${cfg[k]}ById(${entity} ${entity?uncap_first}){
        return SqlHelper.retBool(${table.mapperName?uncap_first}.updateById(${entity?uncap_first}));
    }

    /**
    *   通过ID获取
    * @author ${author}
    * @since ${date}
    */
    @Override
     public ${entity} get${cfg[k]}ById(Long ${cfg[k]?uncap_first}Id){
        return ${table.mapperName?uncap_first}.selectById(${cfg[k]?uncap_first}Id);
    }

    /**
    * 逻辑删除，不考虑逻辑字段，只要设置过逻辑字段数据，就会更改对应的字段数据
    * @author ${author}
    * @since ${date}
    */
    @Override
    public Boolean remove${cfg[k]}ByLogicWithUpdate(${entity} ${entity?uncap_first}){
        return ${table.mapperName?uncap_first}.deleteByLogicWithUpdate(${entity?uncap_first});
    }
        </#if>
    </#list>


}
</#if>
