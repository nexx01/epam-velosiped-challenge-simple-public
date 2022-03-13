package dev.abarmin.velosiped.task5;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DIContainerImpl implements DIContainer {

    private Map<String, Object> beans = new ConcurrentHashMap<>();

    @Override
    public void init() {

    }

    @Override
    public <T> T getBean(Class<T> beanClass) {
final Function<String,Object> objectFactory=new Function<String, Object>() {
    @Override
    public Object apply(String s) {
        try {
            Class<?> beanClass = Class.forName(s);
            if (beanClass.isInterface()) {
                s = s + "Impl";
                beanClass = Class.forName(s);
            }
            Constructor<?> declaredConstructor =
                    beanClass.getDeclaredConstructors()[0];

          return   declaredConstructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
};

        return
                (T) this.beans.computeIfAbsent(beanClass.getName(), objectFactory);
    }
}
