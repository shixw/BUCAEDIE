package cc.bucaedie.ucede.usecase;

import cc.bucaedie.ucede.commons.IdentityConstants;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
     * 业务用例服务code
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
     * 业务用例可能会产生的事件
     */
    private String[] events;

    /**
     * 路由扩展 spring bean 引用
     */
    private Object routerServiceBeanRef;

    /**
     * 路由扩展 用例对应的bean的方法
     */
    private Method routerUseCaseMethod;

    /**
     * 业务用例拦截器
     */
    private UseCaseExecuteInterceptor interceptor;

    /**
     * 被系统内部触发时发生错误重试次数
     */
    private Integer dispatchErrorRetryTimes;

    /**
     * 业务用例扩展实现map
     * key 为业务身份编码
     */
    private Map<String,UseCaseExtensionInfo> extensionInfoMap = new HashMap<>();

    /**
     * 获取所有扩展
     */
    public Map<String,UseCaseExtensionInfo> getAllExtensionInfo(){
        return this.extensionInfoMap;
    }

    /**
     * 根据业务身份获取扩展实现
     */
    public UseCaseExtensionInfo getExtensionInfo(String identity){
        if (this.extensionInfoMap.containsKey(identity)){// 如果有此业务身份的扩展实现则直接返回
            return this.extensionInfoMap.get(identity);
        }
        // 如果没有则返回默认的扩展实现
        return this.extensionInfoMap.get(IdentityConstants.DEFAULT_IDENTITY);
    }

    /**
     * 获取业务用例默认扩展信息
     * @return
     */
    public UseCaseExtensionInfo getDefaultExtensionInfo(){
        return this.getExtensionInfo(IdentityConstants.DEFAULT_IDENTITY);
    }

    /**
     * 添加业务用例扩展
     * @param extensionInfo
     */
    public void addExtensionInfo(UseCaseExtensionInfo extensionInfo){
        if (extensionInfoMap.containsKey(extensionInfo.getIdentity())){
            throw new IllegalArgumentException("业务用例:"+this.getUniqueKey()+",针对业务身份:"+extensionInfo.getIdentity()+" 存在多个扩展实现,请确认业务身份的唯一扩展实现");
        }
        this.extensionInfoMap.put(extensionInfo.getIdentity(),extensionInfo);
    }

    /**
     * 获取业务用例的唯一code
     * @param domain
     * @param serviceCode
     * @param code
     * @return
     */
    public static String getUniqueKey(String domain,String serviceCode,String code){
        return domain+UNIQUE_KEY_SPLIT_FLAG+serviceCode+UNIQUE_KEY_SPLIT_FLAG+code;
    }

    /**
     * 获取业务用例的唯一标识
     * @return
     */
    public String getUniqueKey(){
        return getUniqueKey(this.domain,this.serviceCode,this.code);
    }

    // 业务唯一标识分割标识
    public static final String UNIQUE_KEY_SPLIT_FLAG = ".";

}
