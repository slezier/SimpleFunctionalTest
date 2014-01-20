package sft.javalang.parser;


import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import sft.Fixture;
import sft.FixturesHolder;
import sft.report.FileSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JavaFileParser {
    protected static final FileSystem fileSystem = new FileSystem();
    protected final Class<?> javaClass;

    public JavaFileParser(Class<?> aClass) {
        this.javaClass = aClass;
    }

    protected TypeDeclaration getMaintType() throws ParseException, IOException {
        File javaFile = fileSystem.sourceFolder.getFileFromClass(javaClass, ".java");
        CompilationUnit cu = JavaParser.parse(javaFile, "UTF-8");
        return cu.getTypes().get(0);
    }

    protected void feedTestFixture(MethodDeclaration methodDeclaration, FixturesHolder helper) {
        for (Fixture fixture : helper.fixtures) {
            if (fixture.method.getName().equals(methodDeclaration.getName())) {
                fixture.parametersName = extractParametersName(methodDeclaration);
                return;
            }
        }
    }

    protected ArrayList<String> extractParametersName(MethodDeclaration methodDeclaration) {
        ArrayList<String> parametersName = new ArrayList<String>();
        if (methodDeclaration.getParameters() != null) {
            for (Parameter parameter : methodDeclaration.getParameters()) {
                parametersName.add(parameter.getId().getName());
            }
        }
        return parametersName;
    }
}
