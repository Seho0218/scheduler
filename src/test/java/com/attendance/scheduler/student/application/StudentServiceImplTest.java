package com.attendance.scheduler.student.application;

import com.attendance.scheduler.student.repository.StudentJpaRepository;
import com.attendance.scheduler.teacher.application.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.attendance.scheduler.config.TestDataSet.testTeacherDataSet;

@SpringBootTest
class StudentServiceImplTest {

    @Autowired private StudentJpaRepository studentJpaRepository;
    @Autowired private TeacherService teacherService;

    @BeforeEach
    void beforeEach(){
        boolean duplicateTeacherID = teacherService
                .findDuplicateTeacherID(testTeacherDataSet());

        if (!duplicateTeacherID) {
            teacherService.joinTeacher(testTeacherDataSet());
        }
    }

    @Test
    @DisplayName("학생 인적 사항 정보 저장")
    void findStudentEntityByStudentName() {
//        Optional<StudentEntity> studentEntityByStudentName
//                = studentJpaRepository.findStudentEntityByStudentName(testStudentInformationDTO().getStudentName());
//        studentEntityByStudentName.ifPresent( studentEntity ->
//                assertThat(testStudentInformationDTO().getStudentName()).isEqualTo(studentEntity.getStudentName()));
    }

}
