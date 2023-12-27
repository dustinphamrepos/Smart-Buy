package com.project.smartbuy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDTO {

  @JsonProperty("phone_number")
  @NotBlank(message = "Phone number is required!")
  private String phoneNumber;

  @NotBlank(message = "Password is required!")
  private String password;
}
