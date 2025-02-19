package edu.digipen.capstone.equizplatform.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserCredentials {

    private int userId;

    private String password;

    private boolean isLecturer;
}
