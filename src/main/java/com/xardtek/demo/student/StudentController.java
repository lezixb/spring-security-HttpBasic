package com.xardtek.demo.student;

import com.xardtek.demo.models.Student;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/students")

public class StudentController {

    private static final List<Student> STUDENTS= Arrays.asList(
            new Student(1,"Alexander "),
            new Student(2,"Sandra "),
            new Student(3,"Joshua "),
            new Student(4,"Rex ")
    );

    @GetMapping
    public List<Student> getAllStudents(){
        return STUDENTS;
    }

}
