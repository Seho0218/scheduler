package com.attendance.scheduler.Service;

import com.attendance.scheduler.Dto.Teacher.DeleteClassDTO;

public interface ManageService {

    // 수업 삭제
    void deleteClass(DeleteClassDTO deleteClassDTO);

}
