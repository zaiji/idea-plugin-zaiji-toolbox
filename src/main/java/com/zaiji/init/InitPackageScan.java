package com.zaiji.init;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import com.zaiji.annotation.DefaultComponent;
import com.zaiji.annotation.PluginComponentInfo;
import com.zaiji.entity.ComponentInfo;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 包扫描,注册所有工具类
 *
 * @author zaiji
 */
public class InitPackageScan {
    /**
     * 解析包下面的所有类
     *
     * @param packagePath 上一步获取包的全路径
     * @return
     */
    public static List<ComponentInfo> scanAllComponentInfo(String packagePath) {
        List<ComponentInfo> result = new LinkedList<>();

        //获取包的根路径
        final PluginId pluginId = PluginId.getId("com.zaiji.plugin");
        final IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(pluginId);
        final Path pluginPath = plugin.getPluginPath();

        File pluginRootFile = pluginPath.toFile();

        //获得插件jar包
        final List<File> jarFiles = new LinkedList<>();
        listAllJarFile(pluginRootFile, jarFiles);

        //解析所有组件
        jarFiles.forEach(jarFile -> {
            try {
                resolveFile(new JarFile(jarFile.getAbsolutePath()), packagePath, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return result;
    }

    /**
     * 列出所有满足条件的jar包
     *
     * @param file       根路径
     * @param allJarFile 所有的jar文件
     */
    private static void listAllJarFile(File file, List<File> allJarFile) {
        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            for (File tempFile : files) {
                if (tempFile.isDirectory()) {
                    listAllJarFile(tempFile, allJarFile);
                } else if (tempFile.getName().contains("idea-plugin-zaiji-toolbox") && tempFile.getName().endsWith(".jar")) {
                    allJarFile.add(tempFile);
                }
            }
        } else if (file.getName().contains("idea-plugin-zaiji-toolbox") && file.getName().endsWith(".jar")) {
            allJarFile.add(file);
        }
    }

    /**
     * 列出jar包内所有满足条件的类
     *
     * @param jarFile        jar包文件
     * @param packagePath    包名
     * @param componentInfos 组件信息
     */
    private static void resolveFile(JarFile jarFile, String packagePath, List<ComponentInfo> componentInfos) {
        Enumeration<JarEntry> enumFiles = jarFile.entries();

        while (enumFiles.hasMoreElements()) {
            final JarEntry jarEntry = enumFiles.nextElement();
            String jarEntryName = jarEntry.getName();
            //这里我们需要过滤不是class文件和不在basePack包名下的类
            if (jarEntryName.contains(".class") && jarEntryName.replaceAll("/", ".").startsWith(packagePath)) {
                String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                try {
                    Class<?> clazz = Class.forName(className);
                    //判断是否是组件
                    final PluginComponentInfo pluginComponentInfo = clazz.getAnnotation(PluginComponentInfo.class);
                    boolean isComponent = Objects.nonNull(pluginComponentInfo);
                    boolean isDefaultComponent = Objects.nonNull(clazz.getAnnotation(DefaultComponent.class));
                    if (isComponent) {
                        final String name = pluginComponentInfo.name();
                        //指定方法
                        final Method getContent = clazz.getMethod("getContent");
                        if (Objects.isNull(getContent)) {
                            throw new RuntimeException("组件【" + name + "】,类【" + className + "】，不存在public JPanel getContent()方法，请阅读ReadMe;");
                        } else {
                            final ComponentInfo componentInfo = new ComponentInfo();
                            componentInfo.setClassName(className)
                                    .setClazz(clazz)
                                    .setComponentName(name)
                                    .setDefaultComponent(isDefaultComponent);
                            componentInfos.add(componentInfo);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
