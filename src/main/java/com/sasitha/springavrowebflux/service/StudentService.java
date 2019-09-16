package com.sasitha.springavrowebflux.service;

import com.sasitha.springavrowebflux.persistence.entity.Student;
import com.sasitha.springavrowebflux.persistence.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {
    private StudentRepository repository;

    public StudentService(StudentRepository repository){
        this.repository = repository;
    }

    public Student saveStudent(Student student){
        return repository.save(student);
    }

    public Optional<Student> getStudent(Long id){
        return repository.findById(id);
    }

    public void deleteStudent(Long id){
        repository.deleteById(id);
    }

}
