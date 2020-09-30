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
            new Student(1,"Alexander Nana"),
            new Student(2,"Sandra Owusu"),
            new Student(3,"Joshua Pringle"),
            new Student(4,"T-Rex Danquah")
    );

    @GetMapping
    public List<Student> getAllStudents(){
        return STUDENTS;
    }

}
