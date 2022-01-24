package com.zaiji.entity;

public class ComponentInfo {

    /**
     * 组件类名
     */
    private String className;

    /**
     * 组件类
     */
    private Class<?> clazz;

    /**
     * 组件名
     */
    private String componentName;

    /**
     * 是否是默认组件
     */
    private boolean defaultComponent;

    public String getClassName() {
        return className;
    }

    public ComponentInfo setClassName(String className) {
        this.className = className;
        return this;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public ComponentInfo setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    public String getComponentName() {
        return componentName;
    }

    public ComponentInfo setComponentName(String componentName) {
        this.componentName = componentName;
        return this;
    }

    public boolean isDefaultComponent() {
        return defaultComponent;
    }

    public ComponentInfo setDefaultComponent(boolean defaultComponent) {
        this.defaultComponent = defaultComponent;
        return this;
    }

    @Override
    public String toString() {
        return "ComponentInfo{" +
                "className='" + className + '\'' +
                ", componentName='" + componentName + '\'' +
                '}';
    }
}
