package cc.bucaedie.ucede.event.subscribe;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务用例事件订阅者关系， 针对到业务身份维度， 不同的业务身份会对应不同的监听关系
 */
@Getter
@Setter
public class UseCaseEventIdentitySubscriber implements Serializable {

    /**
     * 业务身份
     */
    private String identity;

    /**
     * 此业务身份下事件监听关系
     * KEY为事件的唯一CODE
     * VALUE为会触发的业务用例的集合
     */
    private Map<String, List<UseCaseEventSubscriber>> useCaseEventSubscriberMap = new HashMap<>();

}
