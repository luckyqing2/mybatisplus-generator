package com.mybatis.generator;

import static com.mybatis.generator.PackagePathConfig.TEMPLATES_DIR_BIZ;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: mybatis-generator
 * @description:
 * @author: "清歌"
 * @create: 2020-04-21 15:47
 **/
public class TemplateExtEngine extends AbstractTemplateEngine {


    private Configuration configuration;

    @Override
    public TemplateExtEngine init(ConfigBuilder configBuilder) {
        InjectionConfig injectionConfig = configBuilder.getInjectionConfig();
        //处理需要带入的数据
        Map<String, Object> map = injectionConfig.getMap();

        List<TableInfo> tableInfoList = configBuilder.getTableInfoList();
        Map<String, String> tableName = tableInfoList.stream().collect(Collectors.toMap(TableInfo::getName, info -> {
            String entityName = info.getEntityName();
            String entity = entityName.substring(0,entityName.lastIndexOf("Entity"));
            return entity;
        }));
        logger.info("tableNameMap:{}",tableName);
        if (Objects.isNull(map)) {
            map = new HashMap<>(tableInfoList.size());
        }
        map.putAll(tableName);

        logger.info("configBuilder :" + Objects.toString(configBuilder));
        super.init(configBuilder);
        Map<String, String> packageInfo = configBuilder.getPackageInfo();
        Map<String, String> pathInfo = configBuilder.getPathInfo();


//        configBuilder.get
        packageInfo.put("Repository",packageInfo.get("Service"));
        packageInfo.put("RepositoryImpl",packageInfo.get("ServiceImpl"));

        String parentPackage = CodeGenerator.parentPackage;
        String s = parentPackage.replaceAll("\\.", "/");
        pathInfo.put(PackagePathConfig.REPOSITORY_PATH, pathInfo.get("service_path"));
        pathInfo.put(PackagePathConfig.REPOSITORY_IMPL_PATH, pathInfo.get("service_impl_path"));

        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(ConstVal.UTF8);
        configuration.setClassForTemplateLoading(TemplateExtEngine.class, StringPool.SLASH);
        return this;
    }


    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        Template template = configuration.getTemplate(templatePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            template.process(objectMap, new OutputStreamWriter(fileOutputStream, ConstVal.UTF8));
        }
        logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }


    @Override
    public String templateFilePath(String filePath) {
        return filePath + ".ftl";
    }

    @Override
    public AbstractTemplateEngine batchOutput() {
//        super.batchOutput();

        try {
            List<TableInfo> tableInfoList = getConfigBuilder().getTableInfoList();
            for (TableInfo tableInfo : tableInfoList) {
                Map<String, Object> objectMap = getObjectMap(tableInfo);
                Map<String, String> pathInfo = getConfigBuilder().getPathInfo();
                logger.debug("路径:" + pathInfo.toString());
                TemplateConfig template = getConfigBuilder().getTemplate();
                // 自定义内容
                InjectionConfig injectionConfig = getConfigBuilder().getInjectionConfig();
                if (null != injectionConfig) {
                    injectionConfig.initTableMap(tableInfo);
                    objectMap.put("cfg", injectionConfig.getMap());
                    List<FileOutConfig> focList = injectionConfig.getFileOutConfigList();
                    if (CollectionUtils.isNotEmpty(focList)) {
                        for (FileOutConfig foc : focList) {
                            if (isCreate(FileType.OTHER, foc.outputFile(tableInfo))) {
                                writer(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo));
                            }
                        }
                    }
                }
                // Mp.java
                String entityName = tableInfo.getEntityName();
                if (null != entityName && null != pathInfo.get(ConstVal.ENTITY_PATH)) {
                    String entityFile = String.format((pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.ENTITY, entityFile)) {
                        writer(objectMap, templateFilePath(PackagePathConfig.TEMPLATES_DIR_ENTITY), entityFile);
                    }
                }
                //IXXXRepository
                logger.info("tableInfo :{}",tableInfo.toString());
                if (pathInfo.get(PackagePathConfig.REPOSITORY_PATH) != null) {
                    String entityFile = String.format((pathInfo.get(PackagePathConfig.REPOSITORY_PATH) + File.separator + tableInfo.getServiceName() + suffixJavaOrKt()), entityName);
                    logger.info("FACADE_PATH : entityFile-----------" + entityFile);
                    if (isCreate(null, entityFile)) {
                        writer(objectMap, templateFilePath(PackagePathConfig.TEMPLATES_DIR_REPOSITORY), entityFile);
                    }
                }
                //RepositoryImpl
                logger.info("tableInfo :{}",tableInfo.toString());
                if (pathInfo.get(PackagePathConfig.REPOSITORY_IMPL_PATH) != null) {
                    String entityFile = String.format((pathInfo.get(PackagePathConfig.REPOSITORY_IMPL_PATH) + File.separator + tableInfo.getServiceImplName() + suffixJavaOrKt()), entityName);
                    logger.info("FACADE_PATH : entityFile-----------" + entityFile);
                    if (isCreate(null, entityFile)) {
                        writer(objectMap, templateFilePath(PackagePathConfig.TEMPLATES_DIR_REPOSITORY_IMPL), entityFile);
                    }
                }

                // MpMapper.java
                if (null != tableInfo.getMapperName() && null != pathInfo.get(ConstVal.MAPPER_PATH)) {
                    String mapperFile = String.format((pathInfo.get(ConstVal.MAPPER_PATH) + File.separator + tableInfo.getMapperName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.MAPPER, mapperFile)) {
                        writer(objectMap, templateFilePath(template.getMapper()), mapperFile);
                    }
                }
                // MpMapper.xml
                if (null != tableInfo.getXmlName() && null != pathInfo.get(ConstVal.XML_PATH)) {
                    String xmlFile = String.format((pathInfo.get(ConstVal.XML_PATH) + File.separator + tableInfo.getXmlName() + ConstVal.XML_SUFFIX), entityName);
                    if (isCreate(FileType.XML, xmlFile)) {
                        writer(objectMap, templateFilePath(template.getXml()), xmlFile);
                    }
                }
               /* // IMpService.java
                if (null != tableInfo.getServiceName() && null != pathInfo.get(ConstVal.SERVICE_PATH)) {
                    String serviceFile = String.format((pathInfo.get(ConstVal.SERVICE_PATH) + File.separator + tableInfo.getServiceName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.SERVICE, serviceFile)) {
                        writer(objectMap, templateFilePath(template.getService()), serviceFile);
                    }
                }
                // MpServiceImpl.java
                if (null != tableInfo.getServiceImplName() && null != pathInfo.get(ConstVal.SERVICE_IMPL_PATH)) {
                    String implFile = String.format((pathInfo.get(ConstVal.SERVICE_IMPL_PATH) + File.separator + tableInfo.getServiceImplName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.SERVICE_IMPL, implFile)) {
                        writer(objectMap, templateFilePath(template.getServiceImpl()), implFile);
                    }
                }*/
                // Biz
                if (null != tableInfo.getControllerName() && null != pathInfo.get(ConstVal.CONTROLLER_PATH)) {
                    String controllerFile = String.format((pathInfo.get(ConstVal.CONTROLLER_PATH) + File.separator + tableInfo.getControllerName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.CONTROLLER, controllerFile)) {
                        writer(objectMap, templateFilePath(TEMPLATES_DIR_BIZ), controllerFile);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("无法创建文件，请检查配置信息！", e);
        }
        return this;
    }
}




























































