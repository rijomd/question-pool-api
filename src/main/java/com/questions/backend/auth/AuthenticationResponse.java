package com.questions.backend.auth;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.questions.backend.user.Users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("refresh_token")
  private String refreshToken;
  @JsonProperty("menu_list")
  private ArrayList<String> menu_list;
  @JsonProperty("user_details")
  private Users users;

}
