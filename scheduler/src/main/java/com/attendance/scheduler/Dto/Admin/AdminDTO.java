package com.attendance.scheduler.Dto.Admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@NoArgsConstructor
public class AdminDTO {

    private String username;

    private String password;

    private String adminEmail;
}
