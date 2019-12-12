package com.yueshaojun.injectmvp.compiler.core;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.yueshaojun.injectmvp.PresenterType;
import com.yueshaojun.injectmvp.compiler.constant.Constants;
import com.yueshaojun.injectmvp.compiler.GlobleParam;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

/**
 * support creator
 *
 * @author yueshaojun
 * @date 2018/8/9
 */

public class SupportCreator {
    public static void createFile(Elements elementUtil, Filer filer) {
        System.out.println("SupportCreator createFile starting...");
        createFile(elementUtil, filer, PresenterType.ACTIVITY);
        createFile(elementUtil, filer, PresenterType.FRAGMENT);
        System.out.println("SupportCreator createFile ending...");
    }

    private static void createFile(Elements elementUtil, Filer filer, PresenterType type) {

        ClassName activity = ClassName.get("android.app", "Activity");
        ClassName fragment = ClassName.get("android.support.v4.app", "Fragment");
        ClassName interfaceClassName = ClassName.get(Constants.INTERFACE_PACKAGE_NAME, "AndroidBinder");
        ParameterizedTypeName returnTypeName =
                ParameterizedTypeName.get(interfaceClassName, type == PresenterType.ACTIVITY ? activity : fragment);

        TypeSpec.Builder typeSpecBuilder =
                TypeSpec.interfaceBuilder((type == PresenterType.ACTIVITY ? "Activity" : "Fragment") + "Supporter")
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder getSupportBuilder =
                MethodSpec.methodBuilder("get" + (type == PresenterType.ACTIVITY ? "Activity" : "Fragment") + "Support");
        getSupportBuilder
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(returnTypeName);

        typeSpecBuilder.addMethod(getSupportBuilder.build());
        String packageName = GlobleParam.PACKAGE_NAME;
        try {
            JavaFile.builder(packageName, typeSpecBuilder.build()).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
