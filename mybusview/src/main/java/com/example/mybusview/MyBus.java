package com.example.mybusview;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 一个容器，key是类名，value是类里面有哪些方法
 */
public class MyBus {

    private static MyBus mMyBus = new MyBus();
    //容器，存放key-value
    private Map<Object, List<MethodManager>> mMap;

    private ExecutorService executorService;
    private Handler mhandler = new Handler(Looper.getMainLooper());

    public MyBus() {
        this.mMap = new HashMap<>();
        executorService = Executors.newCachedThreadPool();
    }

    public static MyBus getDefault() {
        return mMyBus;
    }


    /**
     * 注册，先判断是否已经添加了
     */
    public void regist(Object object) {
        Log.d("testtt", "regist");
        List<MethodManager> methods = mMap.get(object);
        if (methods == null) {
            List<MethodManager> methodlist = findMethod(object);
            mMap.put(object, methodlist);
        }
    }

    /**
     * 查找这个类有哪些标记了注解的方法，并加入list
     * 1.遍历有哪些方法
     * 2.方法里是否有注解
     * 3.遍历方法的成员变量 ，如果是1个就加入list
     *
     * @param object
     */
    private List<MethodManager> findMethod(Object object) {
        List<MethodManager> methods = new ArrayList<>();
        Class<?> myclass = object.getClass();
        Method[] declaredMethods = myclass.getDeclaredMethods();
        //遍历判断方法是否有注解
        for (Method method : declaredMethods) {
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            if (subscribe == null) {
                continue;
            }
            Class<?>[] parametertypes = method.getParameterTypes();
            if (parametertypes == null || parametertypes.length != 1) {
                continue;
            }
            MethodManager manager = new MethodManager(method,
                    parametertypes[0],
                    subscribe.threadmode());
            methods.add(manager);
        }
        return methods;
    }


    public void post(final Object setter) {
        Set<Object> objects = mMap.keySet();
        for (final Object object : objects) {
            List<MethodManager> methods = mMap.get(object);

            for (final MethodManager method : methods) {
                Class<?> type = method.getType();
                //判断类型是否一致
                if (type.isAssignableFrom(setter.getClass())) {
                    ThreadMode threadMode = method.getThreadMode();
                    switch (threadMode) {
                        case BACKEND:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        myinvoke(method.getmMethod(), setter, object);

                                    }
                                });
                            } else {
                                myinvoke(method.getmMethod(), setter, object);

                            }
                            break;
                        case MAIN:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                myinvoke(method.getmMethod(), setter, object);

                            } else {
                                mhandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        myinvoke(method.getmMethod(), setter, object);

                                    }
                                });
                            }
                            break;

                        case POSTING:
                            myinvoke(method.getmMethod(), setter, object);

                            break;
                    }
                }
            }
        }
    }

    private void myinvoke(Method method, Object setter, Object target) {
        try {
            method.invoke(target, setter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
