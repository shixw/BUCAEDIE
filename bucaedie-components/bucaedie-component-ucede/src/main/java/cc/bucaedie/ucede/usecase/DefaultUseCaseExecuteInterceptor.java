package cc.bucaedie.ucede.usecase;

import cc.bucaedie.ucede.event.UseCaseEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultUseCaseExecuteInterceptor implements UseCaseExecuteInterceptor<UseCaseEvent>{
    @Override
    public void before(UseCaseInfo useCaseInfo, Object[] args) {

    }

    @Override
    public Object complete(UseCaseInfo useCaseInfo, Object[] args, Object result) {
        return result;
    }

    @Override
    public UseCaseEvent convertorResult2Event(UseCaseInfo useCaseInfo, Object[] args, Object result) {
        return new UseCaseEvent();
    }

    @Override
    public Object exception(UseCaseInfo useCaseInfo, Object[] args, Exception exception) {
        log.error("执行业务用例服务:"+useCaseInfo.getServiceCode()+",用例:"+useCaseInfo.getCode()+" 发生异常,",exception);
        return null;
    }
}
