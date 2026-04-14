package com.example.spring_security_demo.repositories;

import com.example.spring_security_demo.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<Student,Integer> {

    @Query("select s from Student s order by s.marks desc")
    List<Student> findByAscMarks();
}
