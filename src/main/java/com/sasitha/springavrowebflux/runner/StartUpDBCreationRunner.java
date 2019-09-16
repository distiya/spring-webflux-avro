package com.sasitha.springavrowebflux.runner;

import com.sasitha.springavrowebflux.persistence.entity.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class StartUpDBCreationRunner implements CommandLineRunner {

    private WebClient client = WebClient.create("http://localhost:8080");

    @Override
    public void run(String... args) throws Exception {
        Student studentVn = new Student();
        studentVn.setStudentName("Linh");
        studentVn.setStudentAddress("Vietnam");

        Student studentCn = new Student();
        studentCn.setStudentName("Adrian");
        studentCn.setStudentAddress("China");

        Mono<ClientResponse> resultVn = client.post()
                .uri("/students")
                .accept(MediaType.APPLICATION_JSON)
                .header("country","VN")
                .body(BodyInserters.fromObject(studentVn))
                .exchange();

        Mono<ClientResponse> resultCn = client.post()
                .uri("/students")
                .accept(MediaType.APPLICATION_JSON)
                .header("country","CN")
                .body(BodyInserters.fromObject(studentCn))
                .exchange();

        resultVn.subscribe();
        resultCn.subscribe();
    }
}
