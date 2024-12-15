package services;

import org.example.exceptions.CompilationErrorException;
import org.example.exceptions.TimeLimitExceededException;
import org.example.exceptions.UnrecognizedLanguageException;
import org.example.services.CompilerService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class CompilerServiceTest {

    CompilerService compilerService = new CompilerService();

    @Test
    public void testPathConverter() throws IOException, InterruptedException {
        String path = "C:\\Users\\andre\\AppData\\Local\\Temp\\testrun";
        assertEquals("/mnt/c/Users/andre/AppData/Local/Temp/testrun", compilerService.convertToLinuxPath(path));
    }

    @Test
    public void testCompiler() {
        String code =
                "#include <iostream>\n" +
                "\n" +
                "int main() {\n" +
                "    std::cout<<\"Hello world!\";\n" +
                "    return 0;\n" +
                "}\n";
        String language = "cpp";
        String userName = "testUser";
        String problem = "Hello_world";
        try {
            String filePath = compilerService.createTempFile(code, language, userName, problem);
            String output = compilerService.compileCode(filePath, language, code, 10000);
            System.out.println(output);
        } catch (IOException | UnrecognizedLanguageException | CompilationErrorException | TimeLimitExceededException |
                 InterruptedException e) {
            System.out.println(e.getMessage());
            assertEquals(0, 1);
        }
    }
}
