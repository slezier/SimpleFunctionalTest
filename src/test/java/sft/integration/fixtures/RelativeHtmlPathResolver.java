package sft.integration.fixtures;

import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

public class RelativeHtmlPathResolver {
    public String getRelativePathToFile(String callerPath, String targetPath) {

        LinkedList<String> callerPathElements = getLinkedListOfPathElements(callerPath);
        LinkedList<String> targetPathElements = getLinkedListOfPathElements(targetPath);

        List<String> relativePathToFile = getRelativePathToFile(callerPathElements, targetPathElements);

        return generateRelativePathString(relativePathToFile);
    }

    private LinkedList<String> getLinkedListOfPathElements(String path) {
        return new LinkedList<String>(asList(path.split("/")));
    }

    private List<String> getRelativePathToFile(LinkedList<String> pathOfSourceFile, LinkedList<String> pathOfDestinationFile) {
        if (pathOfSourceFile.size() == 1) {
            return pathOfDestinationFile;
        } else if (pathOfSourceFile.get(0).equals(pathOfDestinationFile.get(0))) {
            pathOfSourceFile.remove(0);
            pathOfDestinationFile.remove(0);
            return getRelativePathToFile(pathOfSourceFile, pathOfDestinationFile);
        } else {
            LinkedList<String> toCommonPath = new LinkedList<String>();
            for (int i = 0; i < pathOfSourceFile.size() - 1; i++) {
                toCommonPath.add("..");
            }
            toCommonPath.addAll(pathOfDestinationFile);
            return toCommonPath;
        }
    }

    private String generateRelativePathString(List<String> relativePathToFile) {
        String result = "";
        for (String element : relativePathToFile) {
            if (!result.isEmpty()) {
                result += "/";
            }
            result += element;
        }
        return result;
    }


}
