package cc.bucaedie.sample.context;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class OrderOperationResult implements Serializable {

    /**
     * 返回值code
     */
    private Integer code;

    /**
     * 是否成功 true: 成功 false:失败
     */
    private Boolean success;

    /**
     * 失败/成功等情况需要返回相关信息的情况
     */
    private String message;

    /**
     * 订单单号
     */
    private String orderNo;

    /**
     * 返回一些其他信息，供外部系统直接使用
     */
    private Object data;

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
     * 业务唯一身份编码
     */
    private String identity;


}
