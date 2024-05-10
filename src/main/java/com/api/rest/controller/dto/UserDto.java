package com.api.rest.controller.dto;

import java.io.Serializable;
import java.util.Set;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
@Builder
public class UserDto implements Serializable {

  private String username; // aespinosag - email
  private String email;
  private String firstName;
  private String lastName;
  private String password; // A_ES_GU_2024(año actual)_*
  private Set<String> roles;
}