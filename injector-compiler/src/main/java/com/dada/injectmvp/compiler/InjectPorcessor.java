package com.dada.injectmvp.compiler;

import com.dada.injectmvp.MVPComponent;
import com.dada.injectmvp.Presenter;
import com.google.auto.service.AutoService;

import java.util.LinkedHashSet;
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
        }catch (Exception e){
            messager.printMessage(Diagnostic.Kind.ERROR,e.getMessage());
        }
        return true;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        System.out.println("[INJECT init]");
        filer = processingEnvironment.getFiler();
        types = processingEnvironment.getTypeUtils();
        messager = processingEnvironment.getMessager();
        elementUtil = processingEnvironment.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Presenter.class.getCanonicalName());
        types.add(MVPComponent.class.getCanonicalName());
        return types;
    }

    private boolean processImpl(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment){
        if(roundEnvironment.processingOver()){
            System.out.println("[INJECT OVER]");
            return true;
        }else {
            System.out.println("[INJECT START]");
            Parser.parse(roundEnvironment);
            WrapperCreator.createFile(elementUtil,filer);
            BinderCreator.createFile(elementUtil,filer);
            SupportCreator.createFile(elementUtil,filer);
            Parser.clear();
        }
        return false;
    }
}
