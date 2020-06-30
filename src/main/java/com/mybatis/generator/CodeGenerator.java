package com.mybatis.generator;


import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.beingmate.cloud.paas.db.entity.BaseBizEntity;
import com.beingmate.cloud.paas.db.entity.BaseCloudBizEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @program: mybatis-generator
 * @description:
 * @author: "清歌"
 * @create: 2020-04-20 18:04
 **/
public class CodeGenerator {


    /**
     * 表前缀
     */
    public static String tablePrefix = "t_goods_";

    /**
     * 父包
     */
    public static String parentPackage = "";

    /**
     * 作者
     */
    public static String author = "清歌";

    /**
     * 代码生成的根目录
     */
    public static String projectPath = "";

    /**
     * 是否覆盖
     */
    public static boolean isOverwrite = true;

    /* 数据库连接url属性*/
    public static String jdbcUrl="jdbc:mysql://*:3306/*"
            + "&characterSetResults=utf8";

    /* 数据库连接账号属性*/
    public static String username="";

    /* 数据库连接密码属性*/
    public static String password="";


    /**
     *
     * @param isCreateMysqlPachage  是否生成mysql包
     * @param isCamel 表字段是否是以驼峰命名
     */
    public static void createCode(boolean isCreateMysqlPachage,boolean isCamel) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig globalConfig = CodeGenerator.globalConfig("/src/main/java/", isOverwrite);
        //设置全局设置
        mpg.setGlobalConfig(globalConfig);

        //设置数据源信息
        mpg.setDataSource(initDataSourceConfig());

        //设置配置策略
        mpg.setStrategy(CodeGenerator.initStrategyConfig(isCamel));

        // 包配置
        /* 修改本地父包*/
        final PackageConfig pc = new PackageConfig();
        pc.setParent(parentPackage);

        if(isCreateMysqlPachage){
            pc.setMapper("repository.mysql.dao");
            pc.setEntity("repository.mysql.entity");
            pc.setXml("repository.mysql.mapper");
            pc.setServiceImpl("repository.mysql.impl");
        }else{
            pc.setMapper("repository.dao");
            pc.setEntity("repository.entity");
            pc.setXml("repository.mapper");
            pc.setServiceImpl("repository.impl");
        }

        pc.setService("repository");
        pc.setController("biz");
        mpg.setPackageInfo(pc);

        //自定义属性注入:abc
        //在.ftl(或者是.vm)模板中，通过${cfg.abc}获取属性
        InjectionConfig cfg = new InjectionConfig() {
            //自定义属性注入:abc
            //在.ftl(或者是.vm)模板中，通过${cfg.abc}获取属性
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put(PackagePathConfig.REPOSITORY_TEMPLATE_NAME, PackagePathConfig.REPOSITORY_FILE_NAME);
                this.setMap(map);
            }
        };

        // 如果模板引擎是 freemarker
       // String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        /*focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！

                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName() + "/" + tableInfo
                        .getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });*/

        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);


        // 配置模板
        TemplateExtConfig templateConfig = new TemplateExtConfig();
//        templateConfig.setController(null);

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();
        mpg.setTemplate(templateConfig);
        mpg.setTemplateEngine(new TemplateExtEngine());

        mpg.execute();
    }

    private static StrategyConfig initStrategyConfig(boolean isCamel) {
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();

        //这里是自定义的一个实体父类
//        TODO:自定义的一个实体父类

        if(isCamel){//驼峰
            strategy.setNaming(NamingStrategy.underline_to_camel);
            strategy.setColumnNaming(NamingStrategy.underline_to_camel);
            strategy.setSuperEntityClass(BaseBizEntity.class);
            // 写于父类中的公共字段
            strategy.setSuperEntityColumns("id", "memo", "creatorId", "createdTime", "lastModifierId", "lastModifiedTime",
                    "deletedFlag", "version");
        }else{//下划线
            strategy.setNaming(NamingStrategy.underline_to_camel);
            strategy.setColumnNaming(NamingStrategy.no_change);
            strategy.setSuperEntityClass(BaseCloudBizEntity.class);
            // 写于父类中的公共字段
            strategy.setSuperEntityColumns("id", "memo", "creator_id", "created_time", "last_modifier_id", "last_modified_time",
                    "deleted_flag", "version");
        }

//        strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
//        strategy.setSuperServiceClass(null);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);

        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        //TODO 生成表
        //去掉表前缀
        strategy.setTablePrefix(tablePrefix);
        return strategy;
    }

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
    /**
     * 初始化数据源配置文件
     */
    private static DataSourceConfig initDataSourceConfig() {
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(jdbcUrl);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(username);
        dsc.setPassword(password);
        return dsc;
    }

    /**
     * 全局配置
     */
    private static GlobalConfig globalConfig(String outputDir, Boolean isOverwrite) {
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + outputDir);
        gc.setAuthor(author);
        // 不需要ActiveRecord特性的请改为false
        gc.setActiveRecord(false);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(true);
        //是否生成swagger
        gc.setSwagger2(false);
        gc.setDateType(DateType.ONLY_DATE);

        gc.setOpen(false);
        gc.setMapperName("%sDao");
        gc.setXmlName("%sMapper");
        gc.setServiceName("I%sRepository");
        gc.setServiceImplName("%sRepository");
        gc.setEntityName("%sEntity");
        gc.setControllerName("%sBiz");
        gc.setFileOverride(isOverwrite);
        return gc;
    }

}


