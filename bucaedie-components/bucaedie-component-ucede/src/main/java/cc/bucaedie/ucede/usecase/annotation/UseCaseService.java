package cc.bucaedie.ucede.usecase.annotation;

import cc.bucaedie.ucede.usecase.DefaultUseCaseExecuteInterceptor;
import cc.bucaedie.ucede.usecase.UseCaseExecuteInterceptor;

import java.lang.annotation.*;


/**
 * 业务用例服务注解
 * 注解使用到接口上
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UseCaseService {

    /**
     * 业务用例服务所属领域
     * @return
     */
    String domain();

    /**
     * 业务用例服务code
     * @return
     */
    String serviceCode();

    /**
     * 执行的拦截器
     */
    Class<? extends UseCaseExecuteInterceptor> interceptor() default DefaultUseCaseExecuteInterceptor.class;
}
