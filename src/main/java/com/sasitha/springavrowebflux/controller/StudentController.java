package com.sasitha.springavrowebflux.controller;

import com.sasitha.springavrowebflux.persistence.entity.Student;
import com.sasitha.springavrowebflux.service.StudentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @GetMapping(value = "/students/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Student> getStudent(@PathVariable("id") Long id){
        return Mono.justOrEmpty(studentService.getStudent(id));
    }

    @PostMapping(value = "/students",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Student> saveStudent(@RequestBody Student student){
        return Mono.just(studentService.saveStudent(student));
    }

    @PutMapping(value = "/students",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Student> updateStudent(@RequestBody Student student){
        return Mono.just(student)
                .map(s->{
                    Object country = Mono.subscriberContext().map(ctx -> ctx.get("country")).block();
                    return studentService.saveStudent(s);
                });
        //return Mono.justOrEmpty(studentService.saveStudent(student));
    }

    @DeleteMapping(value = "/students/{id}")
    public Mono<Void> deleteStudent(@PathVariable("id") Long id){
        return Mono.just(id)
                .doOnNext(n->studentService.deleteStudent(id))
                .flatMap(s->Mono.empty());
    }
}
