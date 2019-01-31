package com.yueshaojun.injectmvp.compiler;

import com.yueshaojun.injectmvp.MVPComponent;
import com.yueshaojun.injectmvp.Presenter;
import com.yueshaojun.injectmvp.PresenterType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;


/**
 * Created by yueshaojun on 2018/4/9.
 */

class Parser {
    static Map<String, List<FieldInfo>> currentClassInjectInfoMap = new HashMap<>(16);
    static Map<String, TypeElement> currentClassTypeMap = new HashMap<>(16);
    static Map<String,TypeElement> currentActivityClassTypeMap = new HashMap<>(16);
    static Map<String,TypeElement> currentFragmentClassTypeMap = new HashMap<>(16);
    static List<TypeElement> interfaceTypes = new ArrayList<>();

    public static void parse(RoundEnvironment roundEnvironment){
        parsePresenter(roundEnvironment);
        parseComponent(roundEnvironment);
    }
    private static void parsePresenter(RoundEnvironment roundEnvironment) {
        System.out.println("parsePresenter starting...");
        Set<? extends Element> presenterElements = roundEnvironment.getElementsAnnotatedWith(Presenter.class);
        for (Element element : presenterElements) {
            ElementKind kind = element.getKind();
            System.out.println(kind);
            if (kind == ElementKind.FIELD) {

                VariableElement variableElement = (VariableElement) element;
                Presenter presenter = variableElement.getAnnotation(Presenter.class);
                PresenterType presenterType = presenter.type();
                TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();

                String currentClassName = typeElement.getSimpleName().toString();
                List<FieldInfo> fieldInfoList = currentClassInjectInfoMap.get(currentClassName);
                if (fieldInfoList == null) {
                    fieldInfoList = new ArrayList<>(16);
                    currentClassInjectInfoMap.put(currentClassName, fieldInfoList);
                    currentClassTypeMap.put(currentClassName, typeElement);

                    if(presenterType == PresenterType.ACTIVITY){
                        currentActivityClassTypeMap.put(currentClassName, typeElement);
                    }
                    if(presenterType == PresenterType.FRAGMENT){
                        currentFragmentClassTypeMap.put(currentClassName, typeElement);
                    }
                }

                System.out.println("typeElement :"+currentActivityClassTypeMap.size()+"||"+currentFragmentClassTypeMap.size());

                //put element to current class field info list
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setClassName(currentClassName);
                fieldInfo.setVariableElement(variableElement);
                fieldInfoList.add(fieldInfo);
            }
        }
        System.out.println("parsePresenter ending...");
    }

    private static void parseComponent(RoundEnvironment roundEnvironment) {
        System.out.println("parseComponent starting...");
        Set<? extends Element> componentElements = roundEnvironment.getElementsAnnotatedWith(MVPComponent.class);
        for (Element element : componentElements) {
            ElementKind kind = element.getKind();
            System.out.println(kind);
            if (kind.isInterface()) {
                interfaceTypes.add((TypeElement) element);
            }
        }
        System.out.println("parseComponent ending...");
    }

    static void clear(){
        //clear static var
        currentClassTypeMap.clear();
        currentClassInjectInfoMap.clear();
        currentActivityClassTypeMap.clear();
        currentFragmentClassTypeMap.clear();
        interfaceTypes.clear();
    }

}
