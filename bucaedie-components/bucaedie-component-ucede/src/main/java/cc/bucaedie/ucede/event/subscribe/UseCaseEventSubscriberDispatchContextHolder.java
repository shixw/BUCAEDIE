package cc.bucaedie.ucede.event.subscribe;

import cc.bucaedie.ucede.event.UseCaseEvent;

/**
 * 业务用例事件驱动上下文， 用于存储驱动用例执行的事件
 */
public class UseCaseEventSubscriberDispatchContextHolder {


    private static ThreadLocal<UseCaseEvent> holder = new ThreadLocal();

    public static void add(UseCaseEvent event) {
        holder.set(event);
    }

    public static void remove() {
        holder.remove();
    }

    public static UseCaseEvent get() {
        return holder.get();
    }
}
