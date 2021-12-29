package cc.bucaedie.ucede.usecase;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

/**
 * 业务用例实体对象
 */
@Getter
@Setter
public class UseCaseInfo {

    /**
     * 业务用例所属领域
     */
    private String domain;

    /**
     * 业务用例所属领域
     */
    private String serviceCode;

    /**
     * 业务用例CODE
     */
    private String code;

    /**
     * 业务用例描述
     */
    private String desc;

    /**
     * spring bean 引用
     */
    private Object serviceBeanRef;

    /**
     * 用例对应的bean的方法
     */
    private Method useCaseMethod;

    /**
     * 业务用例可能会产生的事件
     */
    private String[] events;

    /**
     * 业务用例拦截器
     */
    private UseCaseExecuteInterceptor interceptor;

    /**
     * 获取业务用例的唯一标识
     * @return
     */
    public String getUniqueKey(){
        return this.domain+UNIQUE_KEY_SPLIT_FLAG+this.code;
    }

    // 业务唯一标识分割标识
    public static final String UNIQUE_KEY_SPLIT_FLAG = ".";

}
