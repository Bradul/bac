package org.example.math_service;

import lombok.*;
import org.example.exceptions.IllegalAnswerFormatException;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Document(collection = "problems")
public class MathProblem {
    private String statement;
    private String answer;
    private List<SubProblem> subProblemList;

    public MathProblem(String statement, String answer) {
        this.statement = statement;
        this.answer = answer;
        this.subProblemList = new ArrayList<>();
    }

    public boolean compareIntegerOutput(String userAnswer) throws IllegalAnswerFormatException {
        int value, expected;
        try {
            value = Integer.parseInt(userAnswer);
        } catch(NumberFormatException e) {
            throw new IllegalAnswerFormatException("Expected an integer output");
        }

        try {
            expected = Integer.parseInt(answer);
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("Something went wrong");
        }

        return value == expected;
    }

    public boolean compareDoubleOutputWithPrecision(String userAnswer, double precision) throws IllegalAnswerFormatException {
        double value, expected;
        try {
            value = Double.parseDouble(userAnswer);
        } catch(NumberFormatException e) {
            throw new IllegalAnswerFormatException("Expected a decimal point number output");
        }

        try {
            expected = Double.parseDouble(answer);
        } catch(NumberFormatException e) {
            throw new IllegalAnswerFormatException("Something went wrong");
        }
        return Math.abs(value - expected) < precision;
    }
}
