package com.attendance.scheduler.Controller;

import com.attendance.scheduler.Dto.ClassDTO;
import com.attendance.scheduler.Dto.DeleteClassDTO;
import com.attendance.scheduler.Service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/*")
@RequiredArgsConstructor
public class AdminController {

    // 관리자 로직
    private final AdminService adminService;

    // 조회
    @GetMapping("manage")
    public String adminPage(Model model){

        List<ClassDTO> classTable = adminService.findClassTable();
        model.addAttribute("classList", new DeleteClassDTO());
        model.addAttribute("findClassTable", classTable);
        log.info("student = {}", adminService.findClassTable());
        return "admin/manage";
    }

    // 삭제
    @PostMapping("delete")
    public ResponseEntity<String> deleteSchedule(@ModelAttribute("classList") DeleteClassDTO deleteClassDTO){

        adminService.deleteClass(deleteClassDTO);
        log.info("delete_List={}", deleteClassDTO.getDeleteClassList());
        return ResponseEntity.ok("삭제되었습니다.");
    }
}
