package services;

import org.example.cs_service.CSProblem;
import org.example.cs_service.Test;
import org.example.exceptions.CompilationErrorException;
import org.example.exceptions.TimeLimitExceededException;
import org.example.exceptions.UnrecognizedLanguageException;
import org.example.services.CompilerService;
import org.example.services.EvaluationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EvaluationServiceTest {

    CompilerService compilerService = new CompilerService();
    EvaluationService evaluationService = new EvaluationService(compilerService);

    @org.junit.jupiter.api.Test
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

        Test test1 = new Test(1, "5\n1 2 3 4 5", "15\n", 1, null);
        Test test2 = new Test(2, "3\n7 5 -1", "11\n", 1, null);
        Test test3 = new Test(3, "10\n1 3 5 7\n9 11 13 15\n17 19", "100\n", 2, null);
        Test test4 = new Test(4, "2\n-1 -3", "-4\n", 2, null);
        Test test5 = new Test(5, "0", "0\n", 1, null);
        Test incorrect = new Test(6, "0", "2\n", 3, null);

        List<Test> tests = new ArrayList<>(List.of(test1, test2, test3, test4, test5));

        CSProblem problem = new CSProblem(1, "Vector_sum", "Sum all elements", 100, tests);
        try {
            assertEquals(100.0, evaluationService.evaluateSubmission(code, language, userName, problem));
            tests.add(incorrect);
            assertEquals(70.0, evaluationService.evaluateSubmission(code, language, userName, problem));
        } catch (IOException | UnrecognizedLanguageException | CompilationErrorException | TimeLimitExceededException |
                 InterruptedException e) {
            System.out.println(e.getMessage());
            assertEquals(0, 1);
        }
    }
}
