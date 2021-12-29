package cc.bucaedie.ucede.spring;

import cc.bucaedie.ucede.event.subscribe.spring.IdentitySubscriberBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class UcedeNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("identitySubscriber", new IdentitySubscriberBeanDefinitionParser());
    }
}
