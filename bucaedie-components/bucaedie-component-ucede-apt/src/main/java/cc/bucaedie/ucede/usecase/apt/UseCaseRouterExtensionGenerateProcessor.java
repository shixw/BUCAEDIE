package cc.bucaedie.ucede.usecase.apt;

import cc.bucaedie.ucede.usecase.annotation.UseCaseService;
import cc.bucaedie.ucede.usecase.annotation.UseCaseServiceRouterExtension;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class UseCaseRouterExtensionGenerateProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(UseCaseService.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.size() == 0) {
            return false;
        }
        // 获取所有使用注解标注的类型
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(UseCaseService.class);
        for (Element element : elements) {
            // 校验类型是否为接口
            if (element.getKind() != ElementKind.INTERFACE) {
                error("注解 @UseCaseService 只能标注到接口上", element);
            }else{
                // 为接口生成默认的实现
                Elements elementUtils = processingEnv.getElementUtils();
                // 获取接口的包名
                String pkgName = elementUtils.getPackageOf(element).getQualifiedName().toString();
                // 获取接口名称
                String useCaseServiceInterfaceName = element.getSimpleName().toString();
                // 获取接口所有的方法
                List<ExecutableElement> executableElements = ElementFilter.methodsIn(element.getEnclosedElements());
                List<MethodSpec> methodSpecList = new ArrayList<>();
                for (ExecutableElement executableElement : executableElements) {
                    // 方法名
                    String methodName = executableElement.getSimpleName().toString();
                    // 构建方法体
                    MethodSpec.Builder  methodBuilder = MethodSpec.methodBuilder(methodName) // 方法名
                            .addModifiers(Modifier.PUBLIC);


                    // 方法的入参
                    List<? extends VariableElement> parameters = executableElement.getParameters();
                    for (VariableElement parameter : parameters) {
                        TypeName parameterTypeName = ParameterizedTypeName.get(parameter.asType());
                        methodBuilder.addParameter(parameterTypeName,parameter.getSimpleName().toString());
                    }
                    // 返回值
                    TypeMirror returnType = executableElement.getReturnType();
                    if (!"void".equals(returnType.toString())) {
                        TypeName returnTypeName = TypeVariableName.get(returnType);
                        //
                        methodBuilder.returns(returnTypeName) // 返回值Class<?>
                                .addStatement("return null");
                    }

                    // 构建添加
                    methodSpecList.add(methodBuilder.build());
                }
                // 构建类
                TypeSpec type = TypeSpec.classBuilder(useCaseServiceInterfaceName+"RouterImpl")
                        .addModifiers(Modifier.PUBLIC) // final
                        .addAnnotation(UseCaseServiceRouterExtension.class)
                        .addSuperinterface(element.asType())
                        .addMethods(methodSpecList) // 添加方法体
                        .build(); // 构建

                // 在指定的包名下，生成Java类文件
                JavaFile javaFile = JavaFile.builder(pkgName, type)
                        .build();
                try {
                    javaFile.writeTo(processingEnv.getFiler());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }


    /**
     * 打印note日志
     */
    private void note(String msg, Element e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg, e);
    }

    /**
     * 打印错误日志
     */
    private void error(String msg, Element e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, e);
    }
}
