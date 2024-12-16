package org.example.services;

import lombok.NoArgsConstructor;
import org.example.exceptions.CompilationErrorException;
import org.example.exceptions.TimeLimitExceededException;
import org.example.exceptions.UnrecognizedLanguageException;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
public class CompilerService {

    /**
     * Creates a temp file containing the submission recorded.
     *
     * @param code Submission received from the student.
     * @param language Language in which the submitted code was written.
     * @param userName Name of the student who submitted.
     * @param problemName Name of the problem for which the code was submitted.
     * @return String representing the absolute path of the temp file created.
     * @throws IOException If there was a problem with creating the file.
     */
    public String createTempFile(String code, String language, String userName, String problemName) throws IOException {
        String extension = "." + language;
        String fileName = userName + problemName;
        File temp = File.createTempFile(fileName, extension);

        BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
        writer.write(code);
        writer.close();

        return temp.getAbsolutePath();
    }

    /**
     * Converts a file path in windows format to a path in Linux format.
     * It does so by calling the "wslpath" function on the passed parameter.
     *
     * @param windowsPath The file path, in windows format.
     * @return The converted file path, in Linux format.
     * @throws IOException If the output could not be read.
     * @throws InterruptedException If the process was interrupted.
     */
    public String convertToLinuxPath(String windowsPath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("wsl", "wslpath", "-a", windowsPath.replace("\\", "\\\\"));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String path = reader.readLine();
        reader.close();
        process.waitFor();
        return path.trim();
    }

    /**
     * Method for reading an output from a process.
     *
     * @param stream InputStream returned by a process.
     * @return The stream converted to String format.
     * @throws IOException If the stream could not be read.
     */
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

    /**
     * Waits for a process for a specified amount of time (in ms).
     *
     * @param process Process that was executed.
     * @param timeoutMs Time limit specified for the process.
     * @return True iff the process finished within the time limit.
     * @throws InterruptedException If the process was interrupted.
     */
    private boolean waitForProcess(Process process, int timeoutMs) throws InterruptedException {
        return process.waitFor(timeoutMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Compiles and executes a piece of code, in a specified language, from a file.
     *
     * @param filePath File where the code is located.
     * @param language Language in which the code was written.
     * @param timeLimit Time limit for the problem to execute.
     * @return String containing the output of the code.
     * @throws UnrecognizedLanguageException If the language in which the code was written is not supported.
     * @throws IOException If problems are encountered while accessing the file containing the code or the input.
     * @throws InterruptedException If the compilation process has been interrupted.
     * @throws TimeLimitExceededException If the execution of the program exceeds the allotted time.
     * @throws CompilationErrorException If the code does not compile.
     */
    public String compileCode(String filePath, String language, int globalTimeLimit)
            throws UnrecognizedLanguageException, IOException, InterruptedException, TimeLimitExceededException, CompilationErrorException {
        filePath = convertToLinuxPath(filePath);
        String compileCommand = "";
        String executeCommand = "";

        switch (language) {
            case "cpp" -> {
                String outFile = filePath.replace(".cpp", "");
                compileCommand = "wsl g++ -o " + outFile + " " + filePath;
                executeCommand = "wsl time " + outFile;
            }
            case "java" -> {
                compileCommand = "javac " + filePath;
                executeCommand = "java -cp " + new File(filePath).getParent() + " " + new File(filePath).getName().replace(".java", "");
            }
            default -> throw new UnrecognizedLanguageException();
        }


        Process compileProcess = Runtime.getRuntime().exec(compileCommand);
        if(!waitForProcess(compileProcess, globalTimeLimit)) {
            throw new TimeLimitExceededException("Compilation timed out.");
        }
        String compileErrors = readStream(compileProcess.getErrorStream());
        if(!compileErrors.isEmpty()) {
            throw new CompilationErrorException(compileErrors);
        }

        return executeCommand;
    }

    public String executeCode(String executeCommand, String input, int timeLimit, int globalTimeLimit)
            throws IOException, InterruptedException, TimeLimitExceededException {
        ProcessBuilder executeProcessBuilder = new ProcessBuilder(executeCommand.split(" "));
        //executeProcessBuilder.directory(new File(filePath).getParentFile());
        executeProcessBuilder.redirectErrorStream(true);

        if(input != null && !input.isEmpty()) {
            File inputFile = File.createTempFile("input", ".txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile));
            writer.write(input);
            writer.close();

            executeProcessBuilder.redirectInput(inputFile);
        }

        Process executeProcess = executeProcessBuilder.start();
        if(!waitForProcess(executeProcess, globalTimeLimit)) {
            throw new TimeLimitExceededException("Process time limit exceeded. Please contact an administrator.");
        }

        // Remove effects of UNIX time command.
        String rawOutput = readStream(executeProcess.getInputStream());
        int truncate = rawOutput.length();
        for(int i = 0;i < 3;i++) {
            truncate = rawOutput.lastIndexOf('\n', truncate - 1);
        }

        int finOutput = rawOutput.lastIndexOf('\n', truncate - 1);
        String output = rawOutput.substring(0, finOutput+1);
        String time = rawOutput.substring(finOutput+1, truncate);
        String[] secAndMs = time.split("m")[1].split("\\.");
        int sec = Integer.parseInt(secAndMs[0]);
        int ms = Integer.parseInt(secAndMs[1].substring(0, secAndMs[1].length()-1));
        int actualMs = sec*1000+ms;
        System.out.println("Process took " + actualMs + " ms to complete.");

        if(actualMs > timeLimit)
            throw new TimeLimitExceededException("Execution timed out.");

        return output;
    }
}
