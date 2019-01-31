package com.yueshaojun.injectmvp.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import com.yueshaojun.injectmvp.BinderWrapper;
import com.yueshaojun.injectmvp.PresenterType;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

/**
 * AndroidBinder creator
 *
 * @author yueshaojun
 * @date 2018/8/9
 */

public class BinderCreator {
    public static void createFile(Elements elementUtil, Filer filer) {
        System.out.println("BinderCreator createFile starting");
        createFile(elementUtil, filer, PresenterType.ACTIVITY);
        createFile(elementUtil, filer, PresenterType.FRAGMENT);
        System.out.println("BinderCreator createFile ending");
    }

    private static void createFile(Elements elementUtil, Filer filer, PresenterType type) {

        ClassName hashMap = ClassName.get("java.util", "HashMap");
        ClassName activity = ClassName.get("android.app", "Activity");
        ClassName fragment = ClassName.get("android.support.v4.app", "Fragment");
        ClassName string = ClassName.get("java.lang","String");

        WildcardTypeName activityWildType = WildcardTypeName.subtypeOf(activity);
        WildcardTypeName fragmentWildType = WildcardTypeName.subtypeOf(fragment);

        ClassName className = ClassName.get("java.lang", "Class");
        ClassName bindWrapperClassName = ClassName.get(Constants.API_PACKAGE_NAME, "BinderWrapper");

        ParameterizedTypeName key = null;
        ParameterizedTypeName value = null;
        if (type == PresenterType.ACTIVITY) {
            key = ParameterizedTypeName.get(className, activityWildType);
            value = ParameterizedTypeName.get(bindWrapperClassName, activityWildType);
        }
        if (type == PresenterType.FRAGMENT) {
            key = ParameterizedTypeName.get(className, fragmentWildType);
            value = ParameterizedTypeName.get(bindWrapperClassName, fragmentWildType);
        }

        ParameterizedTypeName bindWrapperMap = ParameterizedTypeName.get(hashMap, key, value);
        ClassName interfaceClassName = ClassName.get(Constants.API_PACKAGE_NAME, "AndroidBinder");

        ParameterizedTypeName defaultInterfaceTypeName = null;
        if (type == PresenterType.ACTIVITY) {
            defaultInterfaceTypeName = ParameterizedTypeName.get(interfaceClassName, activity);
        }
        if (type == PresenterType.FRAGMENT) {
            defaultInterfaceTypeName = ParameterizedTypeName.get(interfaceClassName, fragment);
        }

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(type == PresenterType.ACTIVITY ? "ActivityBinder" : "FragmentBinder")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(defaultInterfaceTypeName)
                .addField(bindWrapperMap, "classBinderWrapperHashMap", Modifier.PRIVATE);

        MethodSpec.Builder constructorBuilder =
                MethodSpec.constructorBuilder().
                        addModifiers(Modifier.PRIVATE)
                        .addStatement("classBinderWrapperHashMap = new HashMap<>()");

        //activity
        if (type == PresenterType.ACTIVITY) {
            for (String classNameHasPresenter : Parser.currentActivityClassTypeMap.keySet()) {
                System.out.println("create activity TypeElement :" + classNameHasPresenter );
                TypeName typeNameHasPresenter = TypeName.get(Parser.currentActivityClassTypeMap.get(classNameHasPresenter).asType());
                ParameterizedTypeName bindWrapper = ParameterizedTypeName.get(ClassName.get(BinderWrapper.class), typeNameHasPresenter);

                typeSpecBuilder.addField(bindWrapper, "bindWrapper_" + classNameHasPresenter, Modifier.PRIVATE);

                //add statement to constructor
                constructorBuilder
                        .addStatement("bindWrapper_" + classNameHasPresenter + " = " + "new " +
                                Parser.currentActivityClassTypeMap.get(classNameHasPresenter).getQualifiedName() +
                                "_BinderWrapper()")
                        .addStatement("classBinderWrapperHashMap.put($T.class,bindWrapper_" + classNameHasPresenter + ")",
                                Parser.currentActivityClassTypeMap.get(classNameHasPresenter).asType());
            }
        }

        //fragment
        if (type == PresenterType.FRAGMENT) {
            for (String classNameHasPresenter : Parser.currentFragmentClassTypeMap.keySet()) {
                System.out.println("create fragmentTypeElement :" + classNameHasPresenter);
                TypeName typeNameHasPresenter = TypeName.get(Parser.currentFragmentClassTypeMap.get(classNameHasPresenter).asType());
                ParameterizedTypeName bindWrapper = ParameterizedTypeName.get(ClassName.get(BinderWrapper.class), typeNameHasPresenter);

                typeSpecBuilder.addField(bindWrapper, "bindWrapper_" + classNameHasPresenter, Modifier.PRIVATE);

                //add statement to constructor
                constructorBuilder
                        .addStatement("bindWrapper_" + classNameHasPresenter + " = "
                                + "new " +
                                Parser.currentFragmentClassTypeMap.get(classNameHasPresenter).getQualifiedName() +
                                "_BinderWrapper()")
                        .addStatement("classBinderWrapperHashMap.put($T.class,bindWrapper_" + classNameHasPresenter + ")",
                                Parser.currentFragmentClassTypeMap.get(classNameHasPresenter).asType());

            }
        }

        MethodSpec.Builder createBuilder = MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.get(GlobleParam.PACKAGE_NAME, type == PresenterType.ACTIVITY ? "ActivityBinder" : "FragmentBinder"))
                .addStatement("return " + (type == PresenterType.ACTIVITY ? "new ActivityBinder()" : "new FragmentBinder()"));

        MethodSpec.Builder bindMethodBuilder = MethodSpec.methodBuilder("bind");
        ParameterSpec.Builder paramBuilder = ParameterSpec
                .builder(type == PresenterType.ACTIVITY ? activity : fragment, "instance");

        bindMethodBuilder
                .addParameter(paramBuilder.build())
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addCode(
                        "Class key = instance.getClass();\n"+
                        "while(key != $N){\n" +
                        "   BinderWrapper<$T> wrapper = (BinderWrapper<$T>) classBinderWrapperHashMap.get(key);\n" +
                        "   if(wrapper != null){\n" +
                        "       wrapper.bindMember(instance);\n" +
                        "   }\n" +
                        "   key = key.getSuperclass();\n"+
                        "}",
                        type == PresenterType.ACTIVITY ? "Activity.class":"Fragment.class",
                        type == PresenterType.ACTIVITY ? activity.box() : fragment.box(),
                        type == PresenterType.ACTIVITY ? activity.box() : fragment.box());

        MethodSpec.Builder unbindMethodBuilder = MethodSpec.methodBuilder("unbind");

        unbindMethodBuilder
                .addParameter(paramBuilder.build())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addCode(
                        "Class key = instance.getClass();\n"+
                        "while(key != $N){\n" +
                        "BinderWrapper<$T> wrapper = (BinderWrapper<$T>) classBinderWrapperHashMap.get(key);\n" +
                        "   if(wrapper != null){\n" +
                        "        wrapper.unbind(instance);\n" +
                        "    }\n" +
                        "   key = key.getSuperclass();\n"+
                        "}",
                        type == PresenterType.ACTIVITY ? "Activity.class":"Fragment.class",
                        type == PresenterType.ACTIVITY ? activity.box() : fragment.box(),
                        type == PresenterType.ACTIVITY ? activity.box() : fragment.box());
        typeSpecBuilder
                .addMethod(constructorBuilder.build())
                .addMethod(createBuilder.build())
                .addMethod(bindMethodBuilder.build())
                .addMethod(unbindMethodBuilder.build());

        String packageName = GlobleParam.PACKAGE_NAME;
        try {
            JavaFile.builder(packageName, typeSpecBuilder.build()).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
