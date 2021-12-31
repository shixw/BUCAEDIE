package cc.bucaedie.ucede.event.subscribe.spring;

import cc.bucaedie.ucede.event.UseCaseEvent;
import cc.bucaedie.ucede.event.subscribe.UseCaseEventIdentitySubscriber;
import cc.bucaedie.ucede.event.subscribe.UseCaseEventSubscriber;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.*;

public class IdentitySubscriberBeanDefinitionParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(UseCaseEventIdentitySubscriber.class);
        beanDefinition.setLazyInit(false);
        BeanDefinitionRegistry beanDefinitionRegistry = parserContext.getRegistry();

        // 获取订阅的事件
        List<Element> subscriberElements = DomUtils.getChildElementsByTagName(element, "subscriber");
        if (subscriberElements!=null && subscriberElements.size() > 0){
            // 对应关系
            Map<String, List<UseCaseEventSubscriber>> useCaseEventSubscriberMap = new HashMap<>();

            for (Element subscriberElement : subscriberElements) {
                String domain = subscriberElement.getAttribute("domain");
                String event = subscriberElement.getAttribute("event");

                // 获取事件触发的业务用例
                List<Element> triggerUseCaseElements = DomUtils.getChildElementsByTagName(subscriberElement, "triggerUseCase");
                if (triggerUseCaseElements!=null && triggerUseCaseElements.size() > 0){
                    List<UseCaseEventSubscriber> list = new ArrayList<>(triggerUseCaseElements.size());
                    for (Element triggerUseCaseElement : triggerUseCaseElements) {
                        String triggerDomain = triggerUseCaseElement.getAttribute("domain");
                        String triggerUseCase = triggerUseCaseElement.getAttribute("useCase");
                        String convertor = triggerUseCaseElement.getAttribute("convertor");
                        String orderStr = triggerUseCaseElement.getAttribute("order");
                        Integer order  = 0 ;
                        if (orderStr!=null && !"".equals(orderStr)){
                            order = Integer.valueOf(orderStr);
                        }
                        if (!beanDefinitionRegistry.containsBeanDefinition(convertor)){
                            throw new IllegalArgumentException("bean: "+convertor+" 不存在");
                        }
                        list.add(new UseCaseEventSubscriber(triggerDomain,triggerUseCase,convertor,order));
                    }
                    // 排序
                    list.sort(Comparator.comparing(UseCaseEventSubscriber::getOrder));
                    // 维护对应关系
                    useCaseEventSubscriberMap.put(UseCaseEvent.getEventUniqueKey(domain,event),list);
                }
            }
            // 维护业务身份和订阅关系
            beanDefinition.getPropertyValues().add("useCaseEventSubscriberMap", useCaseEventSubscriberMap);
        }
        beanDefinition.getPropertyValues().add("identity", element.getAttribute("identity"));
        beanDefinitionRegistry.registerBeanDefinition(element.getAttribute("identity")+"_SUBSCRIBER",beanDefinition);//注册bean到BeanDefinitionRegistry中
        return beanDefinition;
    }
}
