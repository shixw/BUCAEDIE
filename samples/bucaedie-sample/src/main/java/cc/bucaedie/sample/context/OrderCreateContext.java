package cc.bucaedie.sample.context;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class OrderCreateContext extends OrderOperationContext implements Serializable {

    /**
     * 顾客
     */
    private String consumer;

    /**
     * 价格
     */
    private BigDecimal price;

}
