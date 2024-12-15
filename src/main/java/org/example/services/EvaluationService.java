package org.example.services;

import org.example.cs_service.CSProblem;
import org.example.cs_service.Test;
import org.example.exceptions.CompilationErrorException;
import org.example.exceptions.TimeLimitExceededException;
import org.example.exceptions.UnrecognizedLanguageException;

import java.io.IOException;
import java.util.List;

public class EvaluationService {
    private final CompilerService compilerService;

    public EvaluationService(CompilerService compilerService) {
        this.compilerService = compilerService;
    }

    /**
     * Evaluates a submission for a specified problem against the tests saved for that problem.
     *
     * @param code The student's submitted code.
     * @param language The language in which the code was written.
     * @param userName Name of the student who made the submission.
     * @param problem Name of the problem for which the submission was made.
     * @return int containing a percentage of obtained points (between 0% and 100%)
     * @throws IOException If there was a problem with creating a temp file.
     * @throws UnrecognizedLanguageException If the language is not supported.
     * @throws CompilationErrorException If the code does not compile.
     * @throws TimeLimitExceededException If the execution of the program exceeds the allotted time.
     * @throws InterruptedException If the compilation or execution processes have been interrupted.
     */
    public double evaluateSubmission(String code, String language, String userName, CSProblem problem)
            throws IOException, UnrecognizedLanguageException, CompilationErrorException, TimeLimitExceededException, InterruptedException {
        int points = 0, possible = 0;
        List<Test> tests = problem.getTests();
        String codeFilePath = compilerService.createTempFile(code, language, userName, problem.getName());
        String executeCommand = compilerService.compileCode(codeFilePath, language, 5000);
        for(Test test : tests) {
            String output = compilerService.executeCode(executeCommand, test.getInput(), problem.getTimeLimit());
            String expected = test.getOutput();
            possible += test.getPointsWorth();
            if(expected.equals(output))
                points += test.getPointsWorth();
        }
        return 100.0 * points / possible;
    }
}
