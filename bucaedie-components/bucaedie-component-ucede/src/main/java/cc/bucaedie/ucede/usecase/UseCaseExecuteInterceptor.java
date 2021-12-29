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


}
