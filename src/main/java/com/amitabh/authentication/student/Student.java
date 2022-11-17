package com.amitabh.authentication.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class Student {
    private int studentId;
    private String studentName;
}
