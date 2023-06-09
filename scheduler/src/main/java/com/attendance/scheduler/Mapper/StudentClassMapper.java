package com.attendance.scheduler.Mapper;

import com.attendance.scheduler.Dto.StudentClassDTO;
import com.attendance.scheduler.Entity.ClassEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface StudentClassMapper {
    StudentClassDTO toClassDTO(ClassEntity classEntity);
}
