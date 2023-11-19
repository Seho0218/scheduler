package com.attendance.scheduler.course.repository;

import com.attendance.scheduler.course.domain.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClassJpaRepository extends JpaRepository<ClassEntity, Long> {

    /*
    * find StudentClass by StudentName
    * */
    boolean existsByStudentNameIs(String studentName);

    /*
    * delete StudentName
    * */
    @Transactional
    void deleteByStudentName(String studentName);

    @Transactional
    void deleteByStudentNameIn(List<String> studentName);

}