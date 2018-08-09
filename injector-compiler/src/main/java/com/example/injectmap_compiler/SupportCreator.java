package com.example.injectmap_compiler;

import com.example.lib.PresenterType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

/**
 * support 文件生成
 * @author yueshaojun
 * @date 2018/8/9
 */

public class SupportCreator {
    public static void createFile(Elements elementUtil, Filer filer, PresenterType type) {

        ClassName activity = ClassName.get("android.app", "Activity");
        ClassName fragment = ClassName.get("android.support.v4.app", "Fragment");
        ClassName interfaceClassName = ClassName.get(Constants.API_PACKAGE_NAME, "AndroidBinder");
        ParameterizedTypeName returnTypeName = ParameterizedTypeName.get(interfaceClassName,type == PresenterType.ACTIVITY?activity:fragment);

        TypeSpec.Builder typeSpecBuilder =
                TypeSpec.interfaceBuilder((type == PresenterType.ACTIVITY?"Activity":"Fragment")+"Supporter");
        typeSpecBuilder.addModifiers(Modifier.PUBLIC);
        MethodSpec.Builder getSupportBuilder = MethodSpec.methodBuilder("get"+(type == PresenterType.ACTIVITY?"Activity":"Fragment")+"Support");
        getSupportBuilder
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .returns(returnTypeName);
        typeSpecBuilder.addMethod(getSupportBuilder.build());
        String packageName = Constants.DEFAULT_PACKAGE_NAME ;
        try {
            JavaFile.builder(packageName, typeSpecBuilder.build()).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
