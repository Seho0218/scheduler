package com.attendance.scheduler.Controller;

import com.attendance.scheduler.Dto.Teacher.JoinTeacherDTO;
import com.attendance.scheduler.Service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/join/")
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    //회원가입 폼
    @GetMapping("teacher")
    public String joinForm(Model model) {
        model.addAttribute("join", new JoinTeacherDTO());
        return "join";
    }

    //회원가입 완료
    @PostMapping("approved")
    public String approved(@Validated @ModelAttribute("join") JoinTeacherDTO joinTeacherDTO, BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "join";
        }

        boolean duplicateTeacherId = joinService.findDuplicateTeacherId(joinTeacherDTO);
        boolean duplicateTeacherEmail = joinService.findDuplicateTeacherEmail(joinTeacherDTO);

        if (duplicateTeacherId) {
            model.addAttribute("idErrorMessage", "이미 가입된 아이디 입니다.");
            return "join";
        }

        if (duplicateTeacherEmail) {
            model.addAttribute("emailErrorMessage", "이미 가입된 이메일 입니다.");
            return "join";
        }

        try {
            joinService.joinTeacher(joinTeacherDTO);
            model.addAttribute("login", new JoinTeacherDTO());
            return "redirect:/login";
        } catch (Exception e) {
            e.getStackTrace();
            return "join";
        }
    }
}
