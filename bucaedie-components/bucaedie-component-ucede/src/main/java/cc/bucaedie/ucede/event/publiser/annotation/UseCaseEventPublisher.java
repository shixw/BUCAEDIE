package cc.bucaedie.ucede.event.publiser.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface UseCaseEventPublisher {

    /**
     * 业务事件publisher对应的领域
     */
    String domain();
}
