package com.api.rest.service;

import com.api.rest.controller.dto.UserDto;
import java.util.List;
import org.keycloak.representations.idm.UserRepresentation;

public interface IKeycloakService {

  List<UserRepresentation> findAll();

  List<UserRepresentation> getByUsername(String username);

  String create(UserDto userDTO);

  void deleteById(String userId);

  void update(String userId, UserDto userDTO);
}