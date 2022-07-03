package cc.bucaedie.ucede.usecase.annotation;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 业务用例服务路由注解， 由于标注了 @Primary , 所以在使用此注解标注的实现在spring依赖注入时会优先注入，
 * 避免一个业务用例有多个实现时，spring依赖注入报错
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Service
@Primary
public @interface UseCaseServiceRouterExtension {
}
