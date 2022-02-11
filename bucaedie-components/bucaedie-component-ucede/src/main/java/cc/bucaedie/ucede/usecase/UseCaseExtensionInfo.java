package cc.bucaedie.ucede.usecase;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 业务用例扩展信息
 */
@Getter
@Setter
public class UseCaseExtensionInfo implements Serializable {

    /**
     * 业务用例试用的业务身份
     */
    private String identity;

    /**
     * spring bean 引用
     */
    private Object serviceBeanRef;

    /**
     * 用例对应的bean的方法
     */
    private Method useCaseMethod;
}
