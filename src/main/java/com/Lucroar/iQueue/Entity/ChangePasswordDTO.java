package com.Lucroar.iQueue.Entity;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    String email;
    String oldPassword;
    String newPassword;
}
