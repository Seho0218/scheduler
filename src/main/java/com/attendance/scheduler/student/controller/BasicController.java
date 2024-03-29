package com.attendance.scheduler.student.controller;

import com.attendance.scheduler.course.dto.StudentClassDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BasicController {

    @GetMapping("/")
    public String basic(Model model){
        model.addAttribute("studentClassDTO", new StudentClassDTO());
        return "index";
    }
}