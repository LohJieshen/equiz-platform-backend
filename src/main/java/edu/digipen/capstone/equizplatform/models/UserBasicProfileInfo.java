package edu.digipen.capstone.equizplatform.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class UserBasicProfileInfo {
    private String firstName;

    private int courseId;

    private boolean isLecturer;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate lastLoginDate;
}
