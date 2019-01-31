package com.yueshaojun.injectmvp.compiler;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * the {@link Presenter} annotation mark
 * @author yueshaojun988
 * @date 2017/12/28
 */

public class FieldInfo {
    /**
     * class name the field belongs to
     */
    private String className;
    /**
     * field
     */
    private VariableElement variableElement;
    private String fieldName;
    private TypeElement typeElement;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public VariableElement getVariableElement() {
        return variableElement;
    }

    public void setVariableElement(VariableElement variableElement) {
        this.variableElement = variableElement;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setTypeElement(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }
}
