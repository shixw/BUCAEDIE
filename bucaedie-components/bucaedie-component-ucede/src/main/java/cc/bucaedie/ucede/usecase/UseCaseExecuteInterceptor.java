package cc.bucaedie.ucede.usecase;

import cc.bucaedie.ucede.event.UseCaseEvent;

/**
 * 业务用例执行拦截器
 */
public interface UseCaseExecuteInterceptor<T extends UseCaseEvent> {

    /**
     * 用例执行前执行
     */
    void before(UseCaseInfo useCaseInfo,Object[] args);

    /**
     * 用例执行完成后执行
     */
    Object complete(UseCaseInfo useCaseInfo,Object[] args,Object result);

    /**
     * 用例执行完成后，根据相关信息，将结果转换为事件
     */
    T convertorResult2Event(UseCaseInfo useCaseInfo, Object[] args, Object result);

    /**
     * 用例执行抛出异常后执行
     */
    Object exception(UseCaseInfo useCaseInfo,Object[] args,Exception exception);

    /**
     * 根据业务用例入参等获取业务身份,不能返回路由业务身份
     * @param useCaseInfo
     * @param args
     * @return
     */
    String getIdentity(UseCaseInfo useCaseInfo,Object[] args);

    /**
     * 校验结果是否错误并且进行重试
     * @return
     */
    Boolean checkResultIsErrorAndRetry(UseCaseInfo useCaseInfo,Object[] args,Object result);

}
