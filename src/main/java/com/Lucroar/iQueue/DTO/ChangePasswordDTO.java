package com.Lucroar.iQueue.DTO;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    String email;
    String oldPassword;
    String newPassword;
}
