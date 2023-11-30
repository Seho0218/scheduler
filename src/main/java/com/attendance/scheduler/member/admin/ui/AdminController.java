package com.attendance.scheduler.member.admin.ui;

import com.attendance.scheduler.member.admin.application.AdminService;
import com.attendance.scheduler.member.admin.dto.ApproveTeacherDTO;
import com.attendance.scheduler.member.admin.dto.ChangeTeacherDTO;
import com.attendance.scheduler.member.teacher.dto.TeacherDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/")
public class AdminController {

    public final AdminService adminService;

    /*
     * TeacherList View
     * */
    @GetMapping("teacherList")
    public String teacherList(Model model) {
        List<TeacherDTO> teacherList = adminService.getTeacherList();
        model.addAttribute("teacherList", teacherList);
        return "admin/teacherList";
    }

    /*
     * grant Teacher Auth
     * */
    @PostMapping("grant")
    public ResponseEntity<String> grantAuth(ApproveTeacherDTO approveTeacherDTO) {
        adminService.grantAuth(approveTeacherDTO);
        return ResponseEntity.ok("승인되었습니다.");
    }

    /*
     * revoke Teacher Auth
     * */
    @PostMapping("revoke")
    public ResponseEntity<String> revokeAuth(ApproveTeacherDTO approveTeacherDTO) {
        adminService.revokeAuth(approveTeacherDTO);
        return ResponseEntity.ok("승인 취소되었습니다.");
    }

    @PostMapping("changeTeacher")
    public ResponseEntity<String> changeTeacher(ChangeTeacherDTO changeTeacherDTO) {
        try {
            adminService.changeExistTeacher(changeTeacherDTO);
            return ResponseEntity.ok("변경되었습니다.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("delete")
    public ResponseEntity<String> delete(ApproveTeacherDTO approveTeacherDTO) {
        adminService.deleteTeacherAccount(approveTeacherDTO);
        return ResponseEntity.ok("삭제 되었습니다.");
    }
}