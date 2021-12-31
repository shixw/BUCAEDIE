package cc.bucaedie.ucede.event.subscribe;

import cc.bucaedie.ucede.event.UseCaseEvent;

import java.util.Map;

/**
 * 业务事件转换器
 */
public interface UseCaseEvent2ArgsConvertor {

    /**
     * 将业务事件转换为关注的业务用例执行的入参
     * @param event
     * @return
     */
    Object[] convert(UseCaseEvent event, Map<String,String> parameter);
}
