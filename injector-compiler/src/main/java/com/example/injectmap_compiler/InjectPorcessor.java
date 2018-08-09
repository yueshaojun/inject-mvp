package com.example.injectmap_compiler;

import com.example.lib.Presenter;
import com.example.lib.MVPComponent;
import com.example.lib.PresenterType;
import com.google.auto.service.AutoService;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;


/**
 * Created by yueshaojun on 2018/4/9.
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class InjectPorcessor extends AbstractProcessor {
    Filer filer;
    Elements elementUtil;
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("Presenter processing ...");

        Parser.parse(roundEnvironment);
        WrapperCreator.createFile(elementUtil,filer);


        BinderCreator.createFile(elementUtil,filer);
        SupportCreator.createFile(elementUtil,filer, PresenterType.ACTIVITY);
        SupportCreator.createFile(elementUtil,filer, PresenterType.FRAGMENT);

        return false;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        elementUtil = processingEnvironment.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Presenter.class.getCanonicalName());
        types.add(MVPComponent.class.getCanonicalName());
        return types;
    }
}
