package cc.bucaedie.ucede.usecase;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.function.BiFunction;

/**
 * 业务事件执行者,会根据
 */

@Slf4j
public class UseCaseExecutor {

    @Autowired
    private UseCaseRepository useCaseRepository;

    /**
     * 执行业务能力,需要直接传入业务身份
     * @return
     */
    public Object execute(String domain,String useCaseServiceCode, String useCaseCode,String identity,Object... args) {
        return this.innerExecute(domain,useCaseServiceCode,useCaseCode,args,((useCaseInfo, params) -> identity));
    }
    /**
     * 执行业务能力,主要是内部使用
     * @return
     */
    public Object execute(String domain,String useCaseServiceCode, String useCaseCode,Object... args){
        return this.innerExecute(domain,useCaseServiceCode,useCaseCode,args,((useCaseInfo, params) -> {
            UseCaseExecuteInterceptor interceptor = useCaseInfo.getInterceptor();
            // 获取业务身份
            String identity = interceptor.getIdentity(useCaseInfo, params);
            if (identity==null || "".equals(identity)){
                throw new IllegalArgumentException("执行业务用例:"+useCaseInfo.getUniqueKey()+",根据上下文未获取到业务身份");
            }
            return identity;
        }));
    }

    /**
     * 内部抽象的公共方法
     * @return
     */
    private Object innerExecute(String domain, String useCaseServiceCode, String useCaseCode, Object[] args, BiFunction<UseCaseInfo,Object[],String> getIdentity){
        // 获取业务用例
        UseCaseInfo useCase = useCaseRepository.getUseCase(domain, useCaseServiceCode, useCaseCode);
        if (useCase==null){
            throw new IllegalArgumentException("未查询到业务领域:"+domain+",业务用例:"+useCaseCode+" 对应的信息");
        }
        String identity = getIdentity.apply(useCase, args);
        // 获取当前业务身份下的扩展
        UseCaseExtensionInfo extensionInfo = useCase.getExtensionInfo(identity);
        if (extensionInfo==null){// 如果没有获取到业务身份扩展，则使用默认业务身份扩展
            throw new IllegalArgumentException("执行业务用例:"+useCase.getUniqueKey()+",未获取到业务身份 "+identity+" 对应的扩展实现,同时默认扩展实现也为获取到");
        }
        // 业务用例执行方法
        Method useCaseMethod = extensionInfo.getUseCaseMethod();
        try {
            if (log.isDebugEnabled()){
                log.debug("执行业务领域:"+useCase.getDomain()+",业务服务:"+useCase.getServiceCode()+",用例:"+useCase.getCode()+",入参: "+ JSON.toJSONString(args));
            }
            // 执行具体业务用例 此处没有判断业务用例没有参数的情况，暂时认为业务用例都是有参数的
            Object result = useCaseMethod.invoke(extensionInfo.getServiceBeanRef(), args);
            if (log.isDebugEnabled()){
                log.debug("执行业务领域:"+useCase.getDomain()+",:"+useCase.getServiceCode()+",用例:"+useCase.getCode()+",结果: "+ JSON.toJSONString(result));
            }
            return result;
        } catch (Exception e) {
            log.error("执行业务领域:"+useCase.getDomain()+",:"+useCase.getServiceCode()+",用例:"+useCase.getCode()+" 失败，失败原因：",e);
            throw new RuntimeException("执行业务领域:"+useCase.getDomain()+",:"+useCase.getServiceCode()+",用例:"+useCase.getCode()+" 失败", e);
        }
    }

}
