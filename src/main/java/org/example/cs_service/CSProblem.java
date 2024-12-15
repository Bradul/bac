package org.example.cs_service;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class CSProblem {
    private int id;
    private String name;
    private String statement;
    private int timeLimit;
    private List<Test> tests;

    public CSProblem(int id, String name, String statement, int timeLimit, List<Test> tests) {
        this.id = id;
        this.name = name;
        this.statement = statement;
        this.timeLimit = timeLimit;
        this.tests = tests;
    }

    @Override
    public String toString() {
        return "CSProblem[id=" + id + ",name=" + name + ",statement=" + statement + ",tests=" + tests.toString() + "]\n";
    }
}
