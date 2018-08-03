package com.example.injectmap_compiler;

import com.example.lib.BinderWrapper;
import com.example.lib.Presenter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;


/**
 * Created by yueshaojun on 2018/4/9.
 */

class PresenterParser {
    static Map<String, List<FieldInfo>> currentClassInjectInfo = new HashMap<>(16);
    static Map<String, TypeElement> currentClassType = new HashMap<>(16);

    static void parse(RoundEnvironment roundEnvironment) {
        //清除静态的集合
        currentClassType.clear();
        currentClassInjectInfo.clear();

        //解析Presenter元素
        parsePresenter(roundEnvironment);
    }
    private static void parsePresenter(RoundEnvironment roundEnvironment) {
        Set<? extends Element> presenterElements = roundEnvironment.getElementsAnnotatedWith(Presenter.class);
        for (Element element : presenterElements) {
            ElementKind kind = element.getKind();
            System.out.println(kind);
            if (kind == ElementKind.FIELD) {

                VariableElement variableElement = (VariableElement) element;
                Presenter presenter = variableElement.getAnnotation(Presenter.class);
                String presenterName = presenter.name();
                TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
                String currentClassName = typeElement.getSimpleName().toString();
                List<FieldInfo> fieldInfoList = currentClassInjectInfo.get(currentClassName);
                if (fieldInfoList == null) {
                    fieldInfoList = new ArrayList<>(16);
                    currentClassInjectInfo.put(currentClassName, fieldInfoList);
                    currentClassType.put(currentClassName, typeElement);
                }

                //将元素信息放在一个list里，这个list是当前类下的
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setClassName(currentClassName);
                fieldInfo.setFieldName(presenterName);
                fieldInfo.setVariableElement(variableElement);
                fieldInfoList.add(fieldInfo);
            }
        }

    }

    static void createFile(Elements elementUtil, Filer filer) {
        for (String classNameHasPresenter : currentClassInjectInfo.keySet()) {
            List<FieldInfo> fieldInfos = currentClassInjectInfo.get(classNameHasPresenter);
            TypeElement typeElementForClass = currentClassType.get(classNameHasPresenter);

            MethodSpec bindMethodSpec = MethodSpec.methodBuilder("bindMember")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addAnnotation(Override.class)
                    .addParameter(
                            ParameterSpec.
                                    builder(TypeName.get(typeElementForClass.asType()), "activity", Modifier.FINAL)
                                    .build()
                    ).build();
            MethodSpec.Builder bindMethodSpecBuilder = bindMethodSpec.toBuilder();
            for (FieldInfo injectInfo : fieldInfos) {

                bindMethodSpecBuilder.addStatement(
                        "activity.$N.attachView(activity)",
                        injectInfo.getVariableElement().getSimpleName().toString()
                );
            }

            MethodSpec unBindMethodSpec = MethodSpec.methodBuilder("unbind")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addAnnotation(Override.class)
                    .addParameter(
                            ParameterSpec.
                                    builder(TypeName.get(typeElementForClass.asType()), "activity", Modifier.FINAL)
                                    .build()
                    ).build();
            MethodSpec.Builder unBindMethodSpecBuilder = unBindMethodSpec.toBuilder();
            for (FieldInfo injectInfo : fieldInfos) {

                unBindMethodSpecBuilder.addStatement(
                        "activity.$N.detachView()",
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


    }
}
