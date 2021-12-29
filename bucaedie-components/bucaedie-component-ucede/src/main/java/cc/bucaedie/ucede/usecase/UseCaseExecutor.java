package cc.bucaedie.ucede.usecase;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

/**
 * 业务事件执行者
 */

@Slf4j
public class UseCaseExecutor {

    @Autowired
    private UseCaseRepository useCaseRepository;

    /**
     * 执行业务能力,主要是内部使用
     * @return
     */
    public Object execute(String domain,String useCaseCode,Object... args){
        // 获取业务用例
        UseCaseInfo useCase = useCaseRepository.getUseCase(domain, useCaseCode);
        if (useCase==null){
            throw new IllegalArgumentException("未查询到业务领域:"+domain+",业务用例:"+useCaseCode+" 对应的信息");
        }
        // 业务用例执行方法
        Method useCaseMethod = useCase.getUseCaseMethod();
        try {
            if (log.isDebugEnabled()){
                log.debug("执行业务领域:"+useCase.getDomain()+",业务服务:"+useCase.getServiceCode()+",用例:"+useCase.getCode()+",入参: "+ JSON.toJSONString(args));
            }
            // 执行具体业务用例 此处没有判断业务用例没有参数的情况，暂时任务业务用例都是有参数的
            Object result = useCaseMethod.invoke(useCase.getServiceBeanRef(), args);
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
