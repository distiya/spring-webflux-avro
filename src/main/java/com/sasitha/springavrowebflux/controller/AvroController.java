package com.sasitha.springavrowebflux.controller;

import com.sasitha.springavrowebflux.domain.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AvroController {
    @GetMapping(value = "/avro",produces = "application/avro+json")
    public Mono<Student> getStudent(){
        Student student = Student.newBuilder().setAddress("Singapore").setName("Sasitha").setId(1l).build();
        return Mono.justOrEmpty(student);
    }

    @PostMapping(value = "/avro",produces = "application/avro+json",consumes = "application/avro+json")
    public Mono<Student> saveStudent(@RequestBody Student studentBody){
        return Mono.justOrEmpty(studentBody);
    }
}
