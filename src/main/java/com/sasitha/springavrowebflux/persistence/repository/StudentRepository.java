package com.sasitha.springavrowebflux.persistence.repository;

import com.sasitha.springavrowebflux.persistence.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
}
