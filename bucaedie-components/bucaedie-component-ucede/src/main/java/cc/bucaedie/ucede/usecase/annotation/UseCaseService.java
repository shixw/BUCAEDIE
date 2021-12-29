package cc.bucaedie.ucede.usecase.annotation;

import cc.bucaedie.ucede.usecase.DefaultUseCaseExecuteInterceptor;
import cc.bucaedie.ucede.usecase.UseCaseExecuteInterceptor;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UseCaseService {

    /**
     * 业务用例服务编码
     * @return
     */
    String domain();

    /**
     * 执行的拦截器
     */
    Class<? extends UseCaseExecuteInterceptor> interceptor() default DefaultUseCaseExecuteInterceptor.class;
}
