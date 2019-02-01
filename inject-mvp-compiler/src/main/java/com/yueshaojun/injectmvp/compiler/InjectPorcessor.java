package com.yueshaojun.injectmvp.compiler;

import com.google.auto.service.AutoService;
import com.yueshaojun.injectmvp.annotation.MVPComponent;
import com.yueshaojun.injectmvp.annotation.Presenter;
import com.yueshaojun.injectmvp.compiler.constant.Constants;
import com.yueshaojun.injectmvp.compiler.creator.BinderCreator;
import com.yueshaojun.injectmvp.compiler.creator.SupportCreator;
import com.yueshaojun.injectmvp.compiler.creator.WrapperCreator;
import com.yueshaojun.injectmvp.utils.CollectionUtil;
import com.yueshaojun.injectmvp.utils.StringUtil;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;


/**
 * Created by yueshaojun on 2018/4/9.
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class InjectPorcessor extends AbstractProcessor {
    Filer filer;
    Elements elementUtil;
    Messager messager;
    Types types;

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        try {
            return processImpl(set, roundEnvironment);
        } catch (Exception e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return true;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        types = processingEnvironment.getTypeUtils();
        messager = processingEnvironment.getMessager();
        elementUtil = processingEnvironment.getElementUtils();
        initParam(processingEnvironment);
    }

    private void initParam(ProcessingEnvironment processingEnvironment) {
        Map<String, String> param = processingEnvironment.getOptions();
        if (param == null || param.isEmpty() || ((GlobleParam.MODULE_NAME = param.get(Constants.MODULE_NAME)) == null)) {
            messager.printMessage(Diagnostic.Kind.WARNING, "please add arguments in your build.gradle like this:" +
                    "defaultConfig {\n" +
                    "        javaCompileOptions {\n" +
                    "            annotationProcessorOptions {\n" +
                    "                arguments = [ moduleName : project.getName() ]\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }");
        }
        if(StringUtil.isEmpty(GlobleParam.PACKAGE_NAME)){
            GlobleParam.PACKAGE_NAME =Constants.DEFAULT_PACKAGE_NAME;
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Presenter.class.getCanonicalName());
        types.add(MVPComponent.class.getCanonicalName());
        return types;
    }

    private boolean processImpl(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // must deal with this action ,or wile create duplicated
        if(CollectionUtil.isEmpty(set)){
            return false;
        }
        System.out.println("->>>"+GlobleParam.MODULE_NAME+"_[INJECT START]<<<-");
        Parser.parse(roundEnvironment);
        WrapperCreator.createFile(elementUtil, filer);
        BinderCreator.createFile(elementUtil, filer);
        SupportCreator.createFile(elementUtil, filer);
        Parser.clear();
        System.out.println("->>>"+GlobleParam.MODULE_NAME+"_[INJECT OVER]<<<-");
        return true;
    }
}
