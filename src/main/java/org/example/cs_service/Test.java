package org.example.cs_service;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Test {
    private int id;
    private String input;
    private String output;
    private int pointsWorth;
    private CSProblem problem;

    @Override
    public String toString() {
        int problemId = problem != null ? problem.getId() : 0;
        return "Test[id=" + id + ",worth=" + pointsWorth + ",problem=" + problemId + "]\n";
    }
}
