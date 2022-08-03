package cc.bucaedie.ucede.usecase;

import cc.bucaedie.ucede.event.UseCaseEvent;

public class UseCaseCurrentExecutorContextHolder {


    private static ThreadLocal<String> holder = new ThreadLocal();

    public static void set(String event) {
        holder.set(event);
    }

    public static void remove() {
        holder.remove();
    }

    public static String get() {
        return holder.get();
    }
}
