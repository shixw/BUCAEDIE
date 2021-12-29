package cc.bucaedie.ucede.event;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务用例事件实体，业务系统可继承此实体进行扩展
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UseCaseEvent implements Serializable {

    /**
     * 业务用例所属领域
     */
    private String domain;

    /**
     * 业务用例服务
     */
    private String useCaseServiceCode;

    /**
     * 业务用例code
     */
    private String useCaseCode;

    /**
     * 业务用例描述
     */
    private String useCaseDesc;

    /**
     * 业务唯一身份编码
     */
    private String identity;

    /**
     * 事件
     */
    private String event;

    /**
     * 事件描述
     */
    private String desc;

    /**
     * 事件发生时间
     */
    private Date eventTime;

    /**
     * 事件的唯一编码
     */
    private String uuid;

    /**
     * 业务单据的唯一编号
     */
    private String bizNo;

    /**
     * 产生事件的操作人
     */
    private String operator;

    /**
     * 产生事件的操作时间
     */
    private Date operationTime;

    /**
     * 消息
     */
    private String message;

    /**
     * 事件中包含的相关信息，
     */
    private String data;

    /**
     * 触发事件的入参， 主要为了记录日志
     */
    private String param;

    /**
     * 使用业务用例服务编号+事件ID作为唯一标识
     * @return
     */
    public String getEventUniqueKey(){
        return this.getEventUniqueKey(this.domain,this.event);
    }

    public static String getEventUniqueKey(String domain,String event){
        return domain+UNIQUE_KEY_SPLIT_FLAG+event;
    }

    // 业务唯一标识分割标识
    public static final String UNIQUE_KEY_SPLIT_FLAG = ".";

}
