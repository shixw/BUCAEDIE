package cc.bucaedie.ucede.usecase.annotation;

import cc.bucaedie.ucede.usecase.DefaultUseCaseExecuteInterceptor;
import cc.bucaedie.ucede.usecase.UseCaseExecuteInterceptor;

import java.lang.annotation.*;

/**
 * 业务用例注解
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UseCase {

    /**
     * 业务用例编号
     */
    String code();

    /**
     * 业务用例描述
     */
    String desc();

    /**
     * 此用例会产生的事件
     */
    String[] events();

    /**
     * 执行的拦截器
     */
    Class<? extends UseCaseExecuteInterceptor> interceptor() default DefaultUseCaseExecuteInterceptor.class;

    /**
     * 被系统内部触发时发生错误重试次数
     * 默认为 0 , 表示不重试
     */
    int dispatchErrorRetryTimes() default 0;

}
