package cc.bucaedie.ucede.event.subscribe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class UseCaseEventSubscriber implements Serializable {

    /**
     * 触发的业务用例领域
     */
    private String triggerUseCaseDomain;

    /**
     * 触发的业务用例
     */
    private String triggerUseCase;

    /**
     * 业务事件转换器Bean name
     */
    private String convertorBeanName;

    /**
     * 业务事件监听者执行顺序,此值越小执行越靠前
     */
    private Integer order = 0;

    /**
     * 业务事件转换器
     * 主要将事件信息转换为用例执行需要的入参
     */
    private UseCaseEvent2ArgsConvertor convertor;

    public UseCaseEventSubscriber(String triggerUseCaseDomain, String triggerUseCase, String convertorBeanName) {
        this.triggerUseCaseDomain = triggerUseCaseDomain;
        this.triggerUseCase = triggerUseCase;
        this.convertorBeanName = convertorBeanName;
    }


    public UseCaseEventSubscriber(String triggerUseCaseDomain, String triggerUseCase, String convertorBeanName,Integer order) {
        this.triggerUseCaseDomain = triggerUseCaseDomain;
        this.triggerUseCase = triggerUseCase;
        this.convertorBeanName = convertorBeanName;
        if (order != null){
            this.order = order;
        }
    }
}
