package com.mybatis.generator;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * @program: mybatis-generator
 * @description:
 * @author: "清歌"
 * @create: 2020-04-21 15:42
 **/
@Data
public class PackagePathConfig{



    public static final String REPOSITORY_TEMPLATE_NAME  = "repository";

    public static final String REPOSITORY_FILE_NAME = "Repository";

    public static final String TEMPLATES_DIR_REPOSITORY = "/templates/repository.java";

    public static final String TEMPLATES_DIR_REPOSITORY_IMPL = "/templates/repositoryImpl.java";

    public static final String TEMPLATES_DIR_BIZ = "/templates/biz.java";

    public static final String TEMPLATES_DIR_ENTITY = "/templates/entity.java";

    public static final String REPOSITORY_PATH = "repository_path";

    public static final String REPOSITORY_IMPL_PATH = "repository_impl_path";


}
