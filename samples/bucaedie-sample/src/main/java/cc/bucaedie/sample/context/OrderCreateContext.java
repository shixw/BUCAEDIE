package cc.bucaedie.sample.context;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class OrderCreateContext implements Serializable {

    /**
     * 顾客
     */
    private String consumer;

    /**
     * 价格
     */
    private BigDecimal price;

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
}
