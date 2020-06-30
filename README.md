"# code-gen" 
 ## 代码生成器定制
  #####原始代码生成器，是MP官方的：https://mp.baomidou.com/guide/generator.html#%E4%BD%BF%E7%94%A8%E6%95%99%E7%A8%8B
  改造原因：因有些工作实属多余，为了减少不必要的一些琐碎的工作，故进而对原始的加以改造
  改造结果：达到目前开发所需的一些基础类和方法，减少一部分工作
  
  具体的步骤如下：
  *   ：CodeGenerator：代码生成启动类
  需要注意的地方：
      GlobalConfig
              .setMapperName("%sDao");
              .setXmlName("%sMapper");
              .setServiceName("I%sRepository");
              .setServiceImplName("%sRepository");
              .setEntityName("%sEntity");
             
      设置对应生成的文件后缀名称：如果设置了就取自己自定义的，处理后缀名称(在这个方法中)，否则取默认的：com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder.processTable
      TableInfo
            .entityName;
            .mapperName;
            .xmlName;
            .serviceName;
            .serviceImplName;
            .controllerName;
       这个tableInfo当做参数传入到模板，然后通过table取数据，例：${table.serviceName}= IxxxService ,设置过后：IxxxRepository
      PackageConfig
              .setMapper("repository.dao");
              .setEntity("repository.entity");
              .setXml("repository.mapper");
              .setServiceImpl("repository.impl");
              .setService("repository");
       设置生成包名的后缀名称，加上传入的parentPackage，处理包名位置：com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder.handlerPackage
       方法处理：packageInfo和pathInfo  举例：以service举个栗子
       packageInfo.put()
          key:ConstVal.SERVICE = Service
          value:joinPackage(PackageConfig.Parent, PackageConfig.Service)=PackageConfig.Parent +'.''+ PackageConfig.Service
       pathInfo.put()
          key:ConstVal.SERIVCE_PATH
          value:joinPath(outputDir, packageInfo.get(ConstVal.SERIVCE))
          
       这些都被封装了，所以不好处理，如果想要新增一个template，就需要修改模板引擎数据，默认的是AbstractTemplateEngine
       这个类里面包含了一些已经初始化完成的数据，所以可以对初始化完成的数据进行修改
       重写init方法，可以获取到configBuilder。里面含了所有你想要的数据
       
       ```$xslt
  
      @Override
      public TemplateExtEngine init(ConfigBuilder configBuilder) {
          //获取自定义注入配置，模板获取方式：${cfg.key}},具体的看repositoryImpl.java.ftl
          InjectionConfig injectionConfig = configBuilder.getInjectionConfig();
          //处理需要带入的数据
          Map<String, Object> map = injectionConfig.getMap();
           //这里获取到所有表列表数据，返回数据为：key:tableName value: tableName 驼峰去掉表前缀， 例：t_goods_merchant_category 结果：MerchantCategory
           //因为在模板里面要获取到这个数据，不然在里面不好处理根据自定义的数据
          List<TableInfo> tableInfoList = configBuilder.getTableInfoList();
          Map<String, String> tableName = tableInfoList.stream().collect(Collectors.toMap(TableInfo::getName, info -> {
              String entityName = info.getEntityName();
              String entity = entityName.substring(0,entityName.lastIndexOf("Entity"));
              return entity;
          }));
          logger.info("tableNameMap:{}",tableName);
          map.putAll(tableName);
          logger.info("configBuilder :" + Objects.toString(configBuilder));
          super.init(configBuilder);
          Map<String, String> packageInfo = configBuilder.getPackageInfo();
          Map<String, String> pathInfo = configBuilder.getPathInfo();
  
          packageInfo.put("Repository", CodeGenerator.parentPackage + ".repository");
          packageInfo.put("RepositoryImpl", CodeGenerator.parentPackage + ".repository.impl");
          String parentPackage = CodeGenerator.parentPackage;
          String s = parentPackage.replaceAll("\\.", "/");
          pathInfo.put("repository_path", CodeGenerator.projectPath + "/src/main/java/"+ s + "/repository");
          pathInfo.put("repository_impl_path", CodeGenerator.projectPath + "/src/main/java/" + s + "/repository/impl");
          configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
          configuration.setDefaultEncoding(ConstVal.UTF8);
          configuration.setClassForTemplateLoading(TemplateExtEngine.class, StringPool.SLASH);
          return this;
      }
      ```
      
      
   
  初始化完成之后：接下来就是对文件的生成和目录创建相关了：com.mybatis.generator.TemplateExtEngine.batchOutput 这是拓展后的批量输出
  遍历tableInfoList，对每个文件进行输出
  
      ```
     
       public AbstractTemplateEngine batchOutput() {
       //不适用父类的输出，而是使用自己的输出
      //super.batchOutput();
      
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
                              writer(objectMap, templateFilePath(template.getEntity(getConfigBuilder().getGlobalConfig().isKotlin())), entityFile);
                          }
                      }
                         //生成repository
                      logger.info("tableInfo :{}",tableInfo.toString());
                      if (pathInfo.get(PackagePathConfig.REPOSITORY_PATH) != null) {
                          String entityFile = String.format((pathInfo.get(PackagePathConfig.REPOSITORY_PATH) + File.separator + tableInfo.getServiceName() + suffixJavaOrKt()), entityName);
                          logger.info("FACADE_PATH : entityFile-----------" + entityFile);
                          if (isCreate(null, entityFile)) {
                          //模板的位置
                              writer(objectMap, templateFilePath(PackagePathConfig.TEMPLATES_DIR_REPOSITORY), entityFile);
                          }
                      }
                      //生成repository实现
                      logger.info("tableInfo :{}",tableInfo.toString());
                      if (pathInfo.get(PackagePathConfig.REPOSITORY_IMPL_PATH) != null) {
                          String entityFile = String.format((pathInfo.get(PackagePathConfig.REPOSITORY_IMPL_PATH) + File.separator + tableInfo.getServiceImplName() + suffixJavaOrKt()), entityName);
                          logger.info("FACADE_PATH : entityFile-----------" + entityFile);
                          if (isCreate(null, entityFile)) {
                          //模板实现的位置
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
                     /* // IMpService.java 这里注释掉了是不想通过原先的进行生成,而是用自己的模板生成对应的类
                      if (null != tableInfo.getServiceName() && null != pathInfo.get(ConstVal.SERVICE_PATH)) {
                          String serviceFile = String.format((pathInfo.get(ConstVal.SERVICE_PATH) + File.separator + tableInfo.getServiceName() + suffixJavaOrKt()), entityName);
                          if (isCreate(FileType.SERVICE, serviceFile)) {
                              writer(objectMap, templateFilePath(template.getService()), serviceFile);
                          }
                      }
                      // MpServiceImpl.java  这里注释掉了是不想通过原先的进行生成,而是用自己的模板生成对应的类
                      if (null != tableInfo.getServiceImplName() && null != pathInfo.get(ConstVal.SERVICE_IMPL_PATH)) {
                          String implFile = String.format((pathInfo.get(ConstVal.SERVICE_IMPL_PATH) + File.separator + tableInfo.getServiceImplName() + suffixJavaOrKt()), entityName);
                          if (isCreate(FileType.SERVICE_IMPL, implFile)) {
                              writer(objectMap, templateFilePath(template.getServiceImpl()), implFile);
                          }
                      }*/
                      // MpController.java
                      if (null != tableInfo.getControllerName() && null != pathInfo.get(ConstVal.CONTROLLER_PATH)) {
                          String controllerFile = String.format((pathInfo.get(ConstVal.CONTROLLER_PATH) + File.separator + tableInfo.getControllerName() + suffixJavaOrKt()), entityName);
                          if (isCreate(FileType.CONTROLLER, controllerFile)) {
                              writer(objectMap, templateFilePath(template.getController()), controllerFile);
                          }
                      }
                  }
              } catch (Exception e) {
                  logger.error("无法创建文件，请检查配置信息！", e);
              }
              return this;
          }
      
      
      ```
  如果后续需要拓展，可以再init方法中加入对应的packageInfo和pathInfo，然后在batchOutput对应的生成数据即可