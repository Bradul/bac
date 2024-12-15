package org.example.services;

import lombok.NoArgsConstructor;
import org.example.exceptions.CompilationErrorException;
import org.example.exceptions.TimeLimitExceededException;
import org.example.exceptions.UnrecognizedLanguageException;

import java.io.*;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
public class CompilerService {

    public String createTempFile(String code, String language, String userName, String problemName) throws IOException {
        String extension = "." + language;
        String fileName = userName + problemName;
        File temp = File.createTempFile(fileName, extension);

        BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
        writer.write(code);
        writer.close();

        return temp.getAbsolutePath();
    }

    public String convertToLinuxPath(String windowsPath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("wsl", "wslpath", "-a", windowsPath.replace("\\", "\\\\"));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String path = reader.readLine();
        process.waitFor();
        return path.trim();
    }

    private String readStream(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = reader.readLine();
        while(line != null) {
            builder.append(line).append("\n");
            line = reader.readLine();
        }
        return builder.toString();
    }

    private boolean waitForProcess(Process process, int timeoutMs) throws InterruptedException {
        return process.waitFor(timeoutMs, TimeUnit.MILLISECONDS);
    }

    public String compileCode(String filePath, String language, String input, int timeLimit)
            throws UnrecognizedLanguageException, IOException, InterruptedException, TimeLimitExceededException, CompilationErrorException {
        filePath = convertToLinuxPath(filePath);
        String compileCommand = "";
        String executeCommand = "";

        switch (language) {
            case "cpp" -> {
                String outFile = filePath.replace(".cpp", "");
                compileCommand = "wsl g++ -o " + outFile + " " + filePath;
                executeCommand = "wsl " + outFile;
            }
            case "java" -> {
                compileCommand = "javac " + filePath;
                executeCommand = "java -cp " + new File(filePath).getParent() + " " + new File(filePath).getName().replace(".java", "");
            }
            default -> throw new UnrecognizedLanguageException();
        }

        int compileTimeLimit = timeLimit * 10;

        Process compileProcess = Runtime.getRuntime().exec(compileCommand);
        if(!waitForProcess(compileProcess, compileTimeLimit)) {
            throw new TimeLimitExceededException("Compilation timed out.");
        }
        String compileErrors = readStream(compileProcess.getErrorStream());
        if(!compileErrors.isEmpty()) {
            throw new CompilationErrorException(compileErrors);
        }

        /*ProcessBuilder executeProcessBuilder = new ProcessBuilder(executeCommand.split(" "));
        if(input != null && !input.isEmpty()) {
            executeProcessBuilder.redirectInput(new File(input));
        }
        executeProcessBuilder.directory(new File(filePath).getParentFile());
        executeProcessBuilder.redirectErrorStream(true);
        Process executeProcess = executeProcessBuilder.start();

        if(!waitForProcess(executeProcess, timeLimit)) {
            throw new TimeLimitExceededException("Execution timed out.");
        }*/
        ProcessBuilder executeProcessBuilder = new ProcessBuilder(executeCommand.split(" "));
        //executeProcessBuilder.directory(new File(filePath).getParentFile()); - not necessary?
        executeProcessBuilder.redirectErrorStream(true);

        Process executeProcess = executeProcessBuilder.start();
        if(!waitForProcess(executeProcess, timeLimit)) {
            throw new TimeLimitExceededException("Execution timed out.");
        }

        return readStream(executeProcess.getInputStream());
    }
}
