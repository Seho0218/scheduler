package com.attendance.scheduler.admin.dto;

import com.attendance.scheduler.admin.domain.AdminEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@NoArgsConstructor
public class AdminAccountDTO {
    @NotEmpty(message = "아이디를 입력해 주세요")
    private String username;

    @NotEmpty(message = "비밀번호를 입력해 주세요")
    private String password;

    private String name;

    private String email;

    public AdminEntity toEntity() {
        return AdminEntity.builder()
                .username(username)
                .password(password)
                .name(name)
                .email(email)
                .build();
    }
}
