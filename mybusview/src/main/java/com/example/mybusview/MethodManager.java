package com.example.mybusview;

import java.lang.reflect.Method;

/**
 * 订阅方法封装类
 */
public class MethodManager {

    private Method mMethod;

    private Class<?> type;

    private ThreadMode threadMode;

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public MethodManager(Method mMethod, Class<?> type,ThreadMode threadMode) {
        this.mMethod = mMethod;
        this.type = type;
        this.threadMode = threadMode;
    }

    public Method getmMethod() {
        return mMethod;
    }

    public void setmMethod(Method mMethod) {
        this.mMethod = mMethod;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
