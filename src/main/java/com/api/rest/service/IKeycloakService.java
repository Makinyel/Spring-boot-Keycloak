package com.api.rest.service;

import com.api.rest.controller.dto.UserDto;
import java.util.List;
import org.keycloak.representations.idm.UserRepresentation;

public interface IKeycloakService {

  List<UserRepresentation> findAllUsers();

  List<UserRepresentation> searchUserByUsername(String username);

  String createUser(UserDto userDTO);

  void deleteUser(String userId);

  void updateUser(String userId, UserDto userDTO);
}