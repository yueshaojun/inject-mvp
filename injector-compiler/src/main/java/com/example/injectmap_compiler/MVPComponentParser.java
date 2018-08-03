package com.example.injectmap_compiler;

import com.example.lib.BinderWrapper;
import com.example.lib.MVPComponent;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;


/**
 * Created by yueshaojun on 2018/5/29.
 */

public class MVPComponentParser {
    private static TypeElement interfaceTypes;

    static void parse(RoundEnvironment roundEnvironment) {

        //解析Component元素
        parseComponent(roundEnvironment);
    }

    private static void parseComponent(RoundEnvironment roundEnvironment) {
        Set<? extends Element> componentElements = roundEnvironment.getElementsAnnotatedWith(MVPComponent.class);
        for (Element element : componentElements) {
            ElementKind kind = element.getKind();
            System.out.println(kind);
            if (kind.isInterface()) {
                interfaceTypes = ((TypeElement) element);
            }
        }
    }

    public static void createFile(Elements elementUtil, Filer filer) {


        ClassName hashMap = ClassName.get("java.util", "HashMap");
        ClassName activity = ClassName.get("android.app", "Activity");
        WildcardTypeName activityWildType = WildcardTypeName.subtypeOf(activity);
        ClassName className = ClassName.get("java.lang", "Class");
        ClassName bindWrapperClassName = ClassName.get("com.example.lib", "BinderWrapper");
        ParameterizedTypeName key = ParameterizedTypeName.get(className, activityWildType);
        ParameterizedTypeName value = ParameterizedTypeName.get(bindWrapperClassName, activityWildType);
        ParameterizedTypeName bindWrapperMap = ParameterizedTypeName.get(hashMap, key, value);
        ClassName interfaceClassName = ClassName.get(interfaceTypes);

        ParameterizedTypeName interfaceType = ParameterizedTypeName.get(interfaceClassName, activity);
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder("PresentersBinder")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(interfaceType)
                .addField(bindWrapperMap, "classBinderWrapperHashMap", Modifier.PRIVATE);

        MethodSpec.Builder constructorBuilder =
                MethodSpec.constructorBuilder().
                        addModifiers(Modifier.PRIVATE)
                        .addStatement("classBinderWrapperHashMap = new HashMap<>()");

        Map<TypeName, String> typeNames = new HashMap<>(16);

        for (String classNameHasPresenter : PresenterParser.currentClassType.keySet()) {
            ParameterizedTypeName bindWrapper = ParameterizedTypeName.get(ClassName.get(BinderWrapper.class),
                    TypeName.get(PresenterParser.currentClassType.get(classNameHasPresenter).asType()));

            typeSpecBuilder.addField(bindWrapper, "bindWrapper_" + classNameHasPresenter, Modifier.PRIVATE);

            constructorBuilder
                    .addStatement("bindWrapper_" + classNameHasPresenter + " = "
                            + "new " +
                            PresenterParser.currentClassType.get(
                                    classNameHasPresenter).getQualifiedName() +
                            "_BinderWrapper()")
                    .addStatement("classBinderWrapperHashMap.put($T.class,bindWrapper_" + classNameHasPresenter + ")",
                            PresenterParser.currentClassType.get(classNameHasPresenter).asType());

            typeNames.put(TypeName.get(PresenterParser.currentClassType.get(classNameHasPresenter).asType()),
                    "bindWrapper_" + classNameHasPresenter);
        }

        MethodSpec.Builder createBuilder = MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.get("com.dada.presenter", "PresentersBinder"))
                .addStatement("return new PresentersBinder()");

        List<? extends Element> enclosedElements =
                interfaceTypes.getEnclosedElements();

        for (Element element : enclosedElements) {
            if (element.getKind() != ElementKind.METHOD) {
                continue;
            }
            ExecutableElement executableElement = (ExecutableElement) element;

            MethodSpec.Builder injectBuilder =
                    MethodSpec
                            .methodBuilder(executableElement.getSimpleName().toString())
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.get(executableElement.getReturnType()));

            List<ParameterSpec> parameterSpecs = new ArrayList<>();
            for (VariableElement param : executableElement.getParameters()) {
                System.out.println("method name = " + executableElement.getSimpleName());
                if ("inject".equals(executableElement.getSimpleName().toString())) {

                    System.out.println("method name = " + executableElement.getSimpleName());
                    ParameterSpec.Builder paramBuilder = ParameterSpec
                            .builder(activity, param.getSimpleName().toString());

                    parameterSpecs.add(paramBuilder.build());

                    injectBuilder.addStatement(
                                    "BinderWrapper<Activity> wrapper = (BinderWrapper<Activity>) classBinderWrapperHashMap.get(instance.getClass());\n" +
                                    "wrapper.bindMember($N);",
                            param.getSimpleName().toString());
                }
                if ("unbind".equals(executableElement.getSimpleName().toString())) {
                    ParameterSpec.Builder paramBuilder = ParameterSpec
                            .builder(activity, param.getSimpleName().toString());

                    parameterSpecs.add(paramBuilder.build());

                    injectBuilder.addStatement(
                            "BinderWrapper<Activity> wrapper = (BinderWrapper<Activity>) classBinderWrapperHashMap.get(instance.getClass());\n" +
                            "wrapper.unbind($N);",
                            param.getSimpleName().toString());
                }


            }
            injectBuilder
                    .addParameters(parameterSpecs);

            typeSpecBuilder.addMethod(injectBuilder.build());
        }

        typeSpecBuilder
                .addMethod(constructorBuilder.build())
                .addMethod(createBuilder.build());

        String packageName = "com.dada.presenter";
        try {
            JavaFile.builder(packageName, typeSpecBuilder.build()).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
