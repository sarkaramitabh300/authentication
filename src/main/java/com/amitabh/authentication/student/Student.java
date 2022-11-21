package com.amitabh.authentication.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@Getter
@AllArgsConstructor
@ToString
public class Student {
    private int studentId;
    private String studentName;
}
