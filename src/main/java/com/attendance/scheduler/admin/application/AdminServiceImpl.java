package com.attendance.scheduler.admin.application;

import com.attendance.scheduler.admin.domain.AdminEntity;
import com.attendance.scheduler.admin.dto.ChangeTeacherDTO;
import com.attendance.scheduler.admin.dto.EditEmailDTO;
import com.attendance.scheduler.admin.dto.EmailDTO;
import com.attendance.scheduler.admin.repository.AdminRepository;
import com.attendance.scheduler.course.domain.ClassEntity;
import com.attendance.scheduler.course.dto.StudentClassDTO;
import com.attendance.scheduler.course.repository.ClassJpaRepository;
import com.attendance.scheduler.course.repository.ClassRepository;
import com.attendance.scheduler.infra.email.FindPasswordDTO;
import com.attendance.scheduler.student.domain.StudentEntity;
import com.attendance.scheduler.student.repository.StudentJpaRepository;
import com.attendance.scheduler.teacher.domain.TeacherEntity;
import com.attendance.scheduler.teacher.dto.PwdEditDTO;
import com.attendance.scheduler.teacher.dto.TeacherDTO;
import com.attendance.scheduler.teacher.repository.TeacherJpaRepository;
import com.attendance.scheduler.teacher.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ClassJpaRepository classJpaRepository;
    private final AdminRepository adminRepository;
    private final TeacherJpaRepository teacherJpaRepository;
    private final TeacherRepository teacherRepository;
    private final StudentJpaRepository studentJpaRepository;
    private final ClassRepository classRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<TeacherDTO> getTeacherList() {
        return teacherRepository.getTeacherList();
    }

    @Override
    public List<TeacherDTO> findTeacherInformation(String username) {
        return teacherRepository.getTeacherInfoByUsername(username);
    }


    @Override
    public Optional<EmailDTO> findAdminEmailByID(EmailDTO emailDTO) {
        Optional<AdminEntity> adminAccount = Optional.ofNullable(adminRepository
                .findByUsernameIs(emailDTO.getUsername()));
        return adminAccount.map(adminEntity -> EmailDTO.builder()
                .username(adminEntity.getUsername())
                .email(adminEntity.getEmail())
                .build());
    }

    @Override
    @Transactional
    public void grantAuth(String teacherId) {
        TeacherEntity teacherEntity = teacherJpaRepository.findByUsernameIs(teacherId);
        teacherEntity.updateApprove(true);
        teacherJpaRepository.save(teacherEntity);
    }

    @Override
    @Transactional
    public void revokeAuth(String teacherId) {
        TeacherEntity teacherEntity = teacherJpaRepository.findByUsernameIs(teacherId);
        teacherEntity.updateApprove(false);
        teacherJpaRepository.save(teacherEntity);
    }

    @Override
    @Transactional
    public void changeExistTeacher(ChangeTeacherDTO changeTeacherDTO) throws IllegalStateException{
        Long teacherId = changeTeacherDTO.getTeacherId();
        Long studentId = changeTeacherDTO.getStudentId();

        Optional<TeacherEntity> teacherEntity = teacherJpaRepository.findTeacherEntityById(teacherId);
        Optional<StudentEntity> studentEntity = studentJpaRepository.findStudentEntityById(studentId);

        if(studentEntity.isPresent() && teacherEntity.isPresent()){
            studentEntity.get().setTeacherEntity(teacherEntity.get());
            //학생의 수업엔티티와 교사의 수업을 비교
            StudentClassDTO studentClassByStudentName = classRepository.getStudentClassByStudentName(studentEntity.get().getStudentName());
            List<StudentClassDTO> studentClassByTeacherName = classRepository.getStudentClassByTeacherName(teacherEntity.get().getTeacherName());

            classValidator(studentClassByStudentName, studentClassByTeacherName);

            List<ClassEntity> classEntity= classRepository.getStudentClassEntityByStudentName(studentEntity.get().getStudentName());
            if(!classEntity.isEmpty()) {
                ClassEntity entity = classEntity.get(0);
                entity.updateTeacherName(teacherEntity.get().getTeacherName());
                classJpaRepository.save(entity);
            }
            studentJpaRepository.save(studentEntity.get());
        }
    }

    private void classValidator(StudentClassDTO studentClassByStudentName, List<StudentClassDTO> studentClassByTeacherName){

        for (StudentClassDTO classDTOList: studentClassByTeacherName) {
            Integer mondayValue = classDTOList.getMonday();
            Integer tuesdayValue = classDTOList.getTuesday();
            Integer wednesdayValue = classDTOList.getWednesday();
            Integer thursdayValue = classDTOList.getThursday();
            Integer fridayValue = classDTOList.getFriday();

            if (mondayValue.equals(studentClassByStudentName.getMonday())) throw new IllegalStateException("학생의 월요일 수업 중에 겹치는 날이 있습니다.");
            if (tuesdayValue.equals(studentClassByStudentName.getTuesday())) throw new IllegalStateException("학생의 화요일 수업 중에 겹치는 날이 있습니다.");
            if (wednesdayValue.equals(studentClassByStudentName.getWednesday())) throw new IllegalStateException("학생의 수요일 수업 중에 겹치는 날이 있습니다.");
            if (thursdayValue.equals(studentClassByStudentName.getThursday())) throw new IllegalStateException("학생의 목요일 수업 중에 겹치는 날이 있습니다.");
            if (fridayValue.equals(studentClassByStudentName.getFriday())) throw new IllegalStateException("학생의 일요일 수업 중에 겹치는 날이 있습니다.");
        }
    }


    @Override
    @Transactional
    public void deleteTeacherAccount(String teacherId) {
        Optional<TeacherEntity> teacherEntity = Optional.ofNullable(
                teacherJpaRepository.findByUsernameIs(teacherId));

        //교사 엔티티가 존재
        if(teacherEntity.isPresent()) {
            //수업이 있는 교사 엔티티를 찾는다.
            List<StudentClassDTO> studentClassByTeacherName
                    = classRepository.getStudentClassByTeacherName(teacherEntity.get().getTeacherName());

            if(!studentClassByTeacherName.isEmpty())
                throw new IllegalStateException("학생 수업 시간이 남아 있습니다.");

            //교사 엔티티를 가져온다.
            classJpaRepository.deleteByTeacherEntity(teacherEntity.get());
            teacherJpaRepository.deleteByUsernameIs(teacherId);
        }
    }





    @Override
    public boolean emailConfirmation(FindPasswordDTO findPasswordDTO) {
        return adminRepository.existsByEmail(findPasswordDTO.getEmail());
    }



    @Override
    @Transactional
    public void initializePassword(PwdEditDTO pwdEditDTO) {
        final String encodePassword = passwordEncoder.encode(pwdEditDTO.getPassword());
        pwdEditDTO.setPassword(encodePassword);
        AdminEntity adminEntity = adminRepository.findByUsernameIs(pwdEditDTO.getUsername());
        adminEntity.updatePassword(encodePassword);
        adminRepository.save(adminEntity);
    }

    @Override
    @Transactional
    public void updateEmail(EditEmailDTO editEmailDTO) {
        AdminEntity adminEntity = adminRepository.findByUsernameIs(editEmailDTO.getUsername());
        adminEntity.updateEmail(editEmailDTO);
        adminRepository.save(adminEntity);
    }
}