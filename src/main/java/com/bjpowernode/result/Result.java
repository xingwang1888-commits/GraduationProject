package com.bjpowernode.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 企业项目开发中的常用做法
 *
 * 采用Result类封装后端返回的结果 （不管后端返回什么数据，都用Result封装，这样可以使得后端返回的数据结构是统一的）
 *
 */
@Builder //生成构建器模式的代码，这样让我们可以采用链式编程创建对象
@NoArgsConstructor //无参构造器
@AllArgsConstructor //所有参数的构造器
@Data
public class Result {

    private int code; //结果码

    private String msg; //结果信息（成功了？还是失败了？也就是提示信息）

    private Object info; //结果数据 （结果类型可能是string，也可能是user对象，也可能是List）

    public static Result OK() {
        //对象中的字段本身就是一个方法，通过该方法给对象设置数据
        //1、拿到构建器 + 2、给各个字段设置数据 + 3、调用build方法构建出该对象
        //return R.builder().code(200).msg("成功").info(null).build();

        Result re = new Result();
        re.setCode(200);
        re.setMsg("成功");
        re.setInfo(null);

        Result result = Result.builder().code(200).msg("成功").info(null).build();

        //return new R(200, "成功", null);

        return Result.builder().code(200).msg("成功").info(null).build();
    }

    public static Result OK(Object info) {
        return Result.builder().code(200).msg("成功").info(info).build();
    }

    public static Result FAIL() {
        return Result.builder().code(500).msg("失败").info(null).build();
    }
}
