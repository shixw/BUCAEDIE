package cc.bucaedie.ucede.event.subscribe;

import cc.bucaedie.ucede.event.UseCaseEvent;
import com.alibaba.fastjson.JSON;

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


    /**
     * 转换时间中data工具方法
     * @param event
     * @param clazz
     * @param <T>
     * @return
     */
    default <T> T parseData(UseCaseEvent event,Class<T> clazz) {
        String dataStr = event.getData();
        if (dataStr!=null && !"".equals(dataStr)){
            return JSON.parseObject(dataStr, clazz);
        }else{
            throw new IllegalArgumentException("服务单相关事件转换失败，未返回操作情况数据");
        }
    }
}
