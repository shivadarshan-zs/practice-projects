package com.example.spring_security_demo.sevices;

import com.example.spring_security_demo.models.Student;
import com.example.spring_security_demo.repositories.StudentRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    StudentRepo studentRepo;

    public StudentService(StudentRepo studentRepo){
        this.studentRepo = studentRepo;
    }

    public Student addStudent(Student s) {
        return studentRepo.save(s);
    }

    public List<Student> getAllStudent() {
        List<Student> l = studentRepo.findAll();
        return l;
    }

    public List<Student> findByAscMarks() {
        List<Student> l = studentRepo.findByAscMarks();
        return l;
    }
}
