package cc.bucaedie.sample.context;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class OrderOperationContext implements Serializable {

    /**
     * 本次请求的唯一编码
     */
    private String uuid;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作时间
     */
    private Date operationTime;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 业务身份
     */
    private String identity;
}
