package com.yueshaojun.injectmvp.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.yueshaojun.injectmvp.BinderWrapper;

import java.io.IOException;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * wrapper creator
 * @author yueshaojun
 * @date 2018/8/9
 */

public class WrapperCreator {
    static void createFile(Elements elementUtil, Filer filer) {
        System.out.println("WrapperCreator createFile starting");
        for (String classNameHasPresenter : Parser.currentClassInjectInfoMap.keySet()) {
            List<FieldInfo> fieldInfos = Parser.currentClassInjectInfoMap.get(classNameHasPresenter);
            TypeElement typeElementForClass = Parser.currentClassTypeMap.get(classNameHasPresenter);

            MethodSpec bindMethodSpec = MethodSpec.methodBuilder("bindMember")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addAnnotation(Override.class)
                    .addParameter(
                            ParameterSpec.
                                    builder(TypeName.get(typeElementForClass.asType()), "target", Modifier.FINAL)
                                    .build()
                    ).build();
            MethodSpec.Builder bindMethodSpecBuilder = bindMethodSpec.toBuilder();
            for (FieldInfo injectInfo : fieldInfos) {

                bindMethodSpecBuilder.addStatement(
                        "target.$N.attachView(target)",
                        injectInfo.getVariableElement().getSimpleName().toString()
                );
            }

            MethodSpec unBindMethodSpec = MethodSpec.methodBuilder("unbind")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addAnnotation(Override.class)
                    .addParameter(
                            ParameterSpec.
                                    builder(TypeName.get(typeElementForClass.asType()), "target", Modifier.FINAL)
                                    .build()
                    ).build();
            MethodSpec.Builder unBindMethodSpecBuilder = unBindMethodSpec.toBuilder();
            for (FieldInfo injectInfo : fieldInfos) {

                unBindMethodSpecBuilder.addStatement(
                        "target.$N.detachView()",
                        injectInfo.getVariableElement().getSimpleName().toString()
                );
            }

            TypeSpec.Builder typeSpecBuilder =
                    TypeSpec.classBuilder(typeElementForClass.getSimpleName()+"_BinderWrapper")
                            .addModifiers(Modifier.PUBLIC)
                            .addSuperinterface(ParameterizedTypeName.get(ClassName.get(BinderWrapper.class),TypeName.get(typeElementForClass.asType())))
                            .addMethod(bindMethodSpecBuilder.build())
                            .addMethod(unBindMethodSpecBuilder.build());

            String packageName = elementUtil.getPackageOf(typeElementForClass).toString();
            try {
                JavaFile.builder(packageName,typeSpecBuilder.build()).build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("WrapperCreator createFile ending");

    }
}
