package org.example.cs_service;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Document(collection = "tests")
public class Test {

    @Id
    private int id;
    @Field("input")
    private String input;
    @Field("output")
    private String output;
    @Field("points")
    private int pointsWorth;
    @Field("problem")
    private CSProblem problem;

    @Override
    public String toString() {
        return "Test[id=" + id + ",worth=" + pointsWorth + "]\n";
    }
}
