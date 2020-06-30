package com.mybatis.generator.underline;

import com.mybatis.generator.CodeGenerator;

/**
 *  项目代码生成器（实体对象驼峰，字段为下划线）
 *
 * @author 清歌
 * @version v1.1
 * @date 2020/5/27 上午10:38
 */
public class UnderlineCodeGenerator {



   static {
       /*表前缀*/
       CodeGenerator.tablePrefix = "t_";
       /*repository生成的代码根包 -- 一定要指定*/
       CodeGenerator.parentPackage = "*";
       /*生成的代码注释作者信息*/
       CodeGenerator.author = "清歌";
       /*代码生成存放的路径-一定要指定*/
       CodeGenerator.projectPath = "*";
       /*是否覆盖生成代码*/
       CodeGenerator.isOverwrite = true;

       /* 数据库连接url属性*/
       CodeGenerator.jdbcUrl="jdbc:mysql://**:3306/**?useUnicode=true&characterEncoding=utf8"
               + "&characterSetResults=utf8";
       /* 数据库连接账号属性*/
       CodeGenerator.username="root";
       /* 数据库连接密码属性*/
//       CodeGenerator.password="**";


   }


    public static void main(String args[]){

       CodeGenerator.createCode(true,false);

    }

}
