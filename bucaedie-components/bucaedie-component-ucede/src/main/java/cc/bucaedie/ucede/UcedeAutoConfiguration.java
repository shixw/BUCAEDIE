package cc.bucaedie.ucede;

import cc.bucaedie.ucede.event.subscribe.UseCaseEventSubscriberDispatcher;
import cc.bucaedie.ucede.event.subscribe.UseCaseEventListener;
import cc.bucaedie.ucede.event.subscribe.UseCaseEventSubscriberRepository;
import cc.bucaedie.ucede.event.publiser.UseCaseEventPublisherManger;
import cc.bucaedie.ucede.usecase.DefaultUseCaseExecuteInterceptor;
import cc.bucaedie.ucede.usecase.UseCaseExecutor;
import cc.bucaedie.ucede.usecase.UseCaseRepository;
import cc.bucaedie.ucede.usecase.aspect.UseCaseAroundAdvice;
import cc.bucaedie.ucede.usecase.aspect.UseCaseRouterAroundAdvice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class UcedeAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(UseCaseRepository.class)
    public UseCaseRepository useCaseRepository(){
        return new UseCaseRepository();
    }

    @Bean
    @ConditionalOnMissingBean(UseCaseAroundAdvice.class)
    public UseCaseAroundAdvice useCaseAroundAdvice(){
        return new UseCaseAroundAdvice();
    }

    @Bean
    @ConditionalOnMissingBean(UseCaseRouterAroundAdvice.class)
    public UseCaseRouterAroundAdvice useCaseRouterAroundAdvice(){
        return new UseCaseRouterAroundAdvice();
    }


    @Bean
    @ConditionalOnMissingBean(DefaultUseCaseExecuteInterceptor.class)
    public DefaultUseCaseExecuteInterceptor useCaseExecuteInterceptor(){
        return new DefaultUseCaseExecuteInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean(UseCaseEventSubscriberDispatcher.class)
    public UseCaseEventSubscriberDispatcher useCaseEventDispatcher(){
        return new UseCaseEventSubscriberDispatcher();
    }

    @Bean
    @ConditionalOnMissingBean(UseCaseEventListener.class)
    public UseCaseEventListener useCaseEventSubscriber(){
        return new UseCaseEventListener();
    }

    @Bean
    @ConditionalOnMissingBean(UseCaseExecutor.class)
    public UseCaseExecutor useCaseExecutor(){
        return new UseCaseExecutor();
    }

    @Bean
    @ConditionalOnMissingBean(UseCaseEventSubscriberRepository.class)
    public UseCaseEventSubscriberRepository useCaseEventSubscriberRepository(){
        return new UseCaseEventSubscriberRepository();
    }
    @Bean
    @ConditionalOnMissingBean(UseCaseEventPublisherManger.class)
    public UseCaseEventPublisherManger useCaseEventPublisherManger(){
        return new UseCaseEventPublisherManger();
    }



}
