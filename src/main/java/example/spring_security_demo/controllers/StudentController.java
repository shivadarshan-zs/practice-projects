package com.example.spring_security_demo.controllers;

import com.example.spring_security_demo.models.Student;
import com.example.spring_security_demo.sevices.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    StudentService studentService;

    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @GetMapping("/hello")
    public String hello(){
        return "App is working";
    }

    @PostMapping("/student")
    @ResponseStatus(HttpStatus.CREATED)
    public String addStudent(@RequestBody Student s){
        studentService.addStudent(s);
        return "added";
    }

    @GetMapping("/student")
    public List<Student> getStudent(){
        List<Student> l = studentService.getAllStudent();
        return l;
    }

    @GetMapping("/students/marks")
    public List<Student> getStudentByMarksAsc(){
        List<Student> l = studentService.findByAscMarks();
        return l;
    }
}
