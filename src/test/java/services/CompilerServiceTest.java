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
    public void testCompilerHelloWorld() {
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
        String input = "";
        try {
            String filePath = compilerService.createTempFile(code, language, userName, problem);
            String executeCommand = compilerService.compileCode(filePath, language, 10000);
            String output = compilerService.executeCode(executeCommand, input, 100, 10000);
            assertEquals("Hello world!\n", output);
        } catch (IOException | UnrecognizedLanguageException | CompilationErrorException | TimeLimitExceededException |
                 InterruptedException e) {
            System.out.println(e.getMessage());
            assertEquals(0, 1);
        }
    }

    @Test
    public void testCompilerInput() {
        String code =
                "#include <iostream>\n" +
                "\n" +
                "int main() {\n" +
                "    std::string s;\n" +
                "    std::cin>>s;\n" +
                "    std::cout<<\"Hello, \"<<s;\n" +
                "    return 0;\n" +
                "}\n";
        String language = "cpp";
        String userName = "testUser";
        String problem = "Hello_world";
        String input = "Radu";
        try {
            String filePath = compilerService.createTempFile(code, language, userName, problem);
            String executeCommand = compilerService.compileCode(filePath, language, 10000);
            String output = compilerService.executeCode(executeCommand, input, 100, 10000);
            assertEquals("Hello, " + input + "\n", output);
        } catch (IOException | UnrecognizedLanguageException | CompilationErrorException | TimeLimitExceededException |
                 InterruptedException e) {
            System.out.println(e.getMessage());
            assertEquals(0, 1);
        }
    }

    @Test
    public void testSimpleAddition() {
        String code =
                "#include <iostream>\n" +
                "\n" +
                "int main() {\n" +
                "    int a,b;\n" +
                "    std::cin>>a>>b;\n" +
                "    std::cout<<a+b;\n" +
                "    return 0;\n" +
                "}\n";
        String language = "cpp";
        String userName = "testUser";
        String problem = "Hello_world";
        String input = "102 203";
        try {
            String filePath = compilerService.createTempFile(code, language, userName, problem);
            String executeCommand = compilerService.compileCode(filePath, language, 10000);
            String output = compilerService.executeCode(executeCommand, input, 100, 10000);
            assertEquals("305\n", output);
        } catch (IOException | UnrecognizedLanguageException | CompilationErrorException | TimeLimitExceededException |
                 InterruptedException e) {
            System.out.println(e.getMessage());
            assertEquals(0, 1);
        }
    }

    @Test
    public void testComplexProblem() {
        String code = "#include <iostream>\n" +
                "#include <vector>\n" +
                "\n" +
                "std::vector<int>v;\n" +
                "\n" +
                "int main() {\n" +
                "    int n,x;\n" +
                "    std::cin>>n;\n" +
                "    for(int i = 1;i <= n;i++) {\n" +
                "        std::cin>>x;\n" +
                "        v.push_back(x);\n" +
                "    }\n" +
                "    int sum = 0;\n" +
                "    for(auto it = v.begin();it != v.end();it++)\n" +
                "        sum += *it;\n" +
                "    std::cout<<sum;\n" +
                "    return 0;\n" +
                "}\n";
        String language = "cpp";
        String userName = "testUser";
        String problem = "Hello_world";
        String input = "5\n1 2 3 4 5";
        try {
            String filePath = compilerService.createTempFile(code, language, userName, problem);
            String executeCommand = compilerService.compileCode(filePath, language, 10000);
            String output = compilerService.executeCode(executeCommand, input, 100, 10000);
            assertEquals("15\n", output);
        } catch (IOException | UnrecognizedLanguageException | CompilationErrorException | TimeLimitExceededException |
                 InterruptedException e) {
            System.out.println(e.getMessage());
            assertEquals(0, 1);
        }
    }
}
