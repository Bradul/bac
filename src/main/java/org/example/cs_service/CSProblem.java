package org.example.cs_service;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Document(collection = "CSProblems")
public class CSProblem {
    @Id
    private int id;
    @Field("name")
    private String name;
    @Field("statement")
    private String statement;
    @Field("time_limit")
    private int timeLimit;
    @Field("tests")
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
