package com.kuafu.framework.excel;

import org.apache.commons.lang3.ClassUtils;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by leo on 6/22/15.
 */
public class ReflectBean<T> {

    private static final Map<Class,ReflectBean> REFLECT_BEAN_MAP = new HashMap<Class,ReflectBean>();

    private Class<T> objectClass;
    private FastClass fastClass;
    private Map<String, FastMethod> methodMap = new HashMap<String, FastMethod>();

    public static <T> ReflectBean<T> get(Class<T> objectClass){
        ReflectBean<T> reflectBean = ReflectBean.REFLECT_BEAN_MAP.get(objectClass);
        if(reflectBean == null){
            synchronized (ReflectBean.class){
                reflectBean = ReflectBean.REFLECT_BEAN_MAP.get(objectClass);
                if(reflectBean == null){
                    reflectBean = new ReflectBean<T>(objectClass);
                    ReflectBean.REFLECT_BEAN_MAP.put(objectClass,reflectBean);
                }
            }
        }
        return reflectBean;
    }

    private ReflectBean(Class<T> objectClass) {
        this.objectClass=objectClass;
        this.fastClass=FastClass.create(this.objectClass);

        List<Method> methods = new ArrayList<Method>();

        for(Method method:objectClass.getMethods()){
            methods.add(method);
        }
        for(Class<?> classs : ClassUtils.getAllSuperclasses(objectClass)){
            for(Method method:classs.getMethods()){
                methods.add(method);
            }
        }

        for (Method method : methods) {
            FastMethod fastMethod = this.fastClass.getMethod(method);
            methodMap.put(method.getName(), fastMethod);
            String methodName = method.getName();
            if(methodName.startsWith("is")){
                methodMap.put(methodName.replaceFirst("is", "get"),fastMethod);
            }
        }
        ReflectBean.REFLECT_BEAN_MAP.put(objectClass,this);
    }

    public T newInstance() throws IllegalAccessException, InstantiationException {
        return this.objectClass.newInstance();
    }

    public Object invokeGetter(Object target,String fieldName) throws InvocationTargetException {
        FastMethod getter = methodMap.get(getGetterMethodName(fieldName));
        if (getter != null) {
            return getter.invoke(target, new Object[0]);
        }
        return null;
    }

    public Class getFieldType(String fieldName){
        FastMethod setter = methodMap.get(getSetterMethodName(fieldName));
        if (setter == null) {
            throw new IllegalArgumentException("Setter Method Not Exist!!!");
        }
        return setter.getParameterTypes()[0];
    }

    public void invokeSetter(T t, String fieldName, Object value) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        FastMethod setter = methodMap.get(getSetterMethodName(fieldName));
        if (setter != null) {
            setter.invoke(t, new Object[]{value});
        } else {
            throw new IllegalArgumentException("Setter Method Not Exist!!!");
        }
    }

    private static String getGetterMethodName(String fieldName){
        StringBuffer sb = new StringBuffer();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        return sb.toString();
    }
    private static String getSetterMethodName(String fieldName){
        StringBuffer sb = new StringBuffer();
        sb.append("set");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        return sb.toString();
    }

}
