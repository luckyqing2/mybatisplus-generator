package com.mybatis.generator;

import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @program: mybatis-generator
 * @description:
 * @author: "清歌"
 * @create: 2020-04-21 15:38
 **/
@Data
@Accessors(chain = true)
public class TemplateExtConfig extends TemplateConfig {


    @Getter(AccessLevel.NONE)
    private String facade = "/templates/repository.java";

}
