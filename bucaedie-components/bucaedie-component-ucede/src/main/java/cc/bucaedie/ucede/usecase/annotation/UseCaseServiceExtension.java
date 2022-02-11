package cc.bucaedie.ucede.usecase.annotation;


import cc.bucaedie.ucede.commons.IdentityConstants;

import java.lang.annotation.*;

/**
 * 业务用例扩展注解
 * 使用到业务用例的具体实现上
 */

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UseCaseServiceExtension {

    /**
     * 此业务用例服务支持的业务身份
     */
    String[] identities() default {IdentityConstants.DEFAULT_IDENTITY};
}
