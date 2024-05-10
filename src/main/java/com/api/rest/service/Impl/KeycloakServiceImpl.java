package com.api.rest.service.Impl;

import com.api.rest.controller.dto.UserDto;
import com.api.rest.service.IKeycloakService;
import com.api.rest.util.KeycloakProvider;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KeycloakServiceImpl implements IKeycloakService {

  public List<UserRepresentation> findAll() {
    return KeycloakProvider.getRealmResource()
        .users()
        .list();
  }

  public List<UserRepresentation> getByUsername(String username) {
    return KeycloakProvider.getRealmResource()
        .users()
        .searchByUsername(username, true);
  }

  public String create(@NonNull UserDto UserDto) {

    int status = 0;
    UsersResource usersResource = KeycloakProvider.getUserResource();

    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setFirstName(UserDto.getFirstName());
    userRepresentation.setLastName(UserDto.getLastName());
    userRepresentation.setEmail(UserDto.getEmail());
    userRepresentation.setUsername(UserDto.getUsername());
    userRepresentation.setEnabled(true);
    userRepresentation.setEmailVerified(true);

    Response response = usersResource.create(userRepresentation);

    status = response.getStatus();

    if (status == 201) {
      String path = response.getLocation().getPath();
      String userId = path.substring(path.lastIndexOf("/") + 1);

      CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
      credentialRepresentation.setTemporary(false);
      credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
      credentialRepresentation.setValue(UserDto.getPassword());

      usersResource.get(userId).resetPassword(credentialRepresentation);

      RealmResource realmResource = KeycloakProvider.getRealmResource();

      List<RoleRepresentation> rolesRepresentation = null;

      if (UserDto.getRoles() == null || UserDto.getRoles().isEmpty()) {
        rolesRepresentation = List.of(realmResource.roles().get("user").toRepresentation());
      } else {
        rolesRepresentation = realmResource.roles()
            .list()
            .stream()
            .filter(role -> UserDto.getRoles()
                .stream()
                .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
            .toList();
      }

      realmResource.users().get(userId).roles().realmLevel().add(rolesRepresentation);

      return "User created successfully!!";

    } else if (status == 409) {
      log.error("User exist already!");
      return "User exist already!";
    } else {
      log.error("Error creating user, please contact with the administrator.");
      return "Error creating user, please contact with the administrator.";
    }
  }

  public void deleteById(String userId) {
    KeycloakProvider.getUserResource()
        .get(userId)
        .remove();
  }

  public void update(String userId, @NonNull UserDto UserDto) {

    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
    credentialRepresentation.setTemporary(false);
    credentialRepresentation.setType(OAuth2Constants.PASSWORD);
    credentialRepresentation.setValue(UserDto.getPassword());

    UserRepresentation user = new UserRepresentation();
    user.setUsername(UserDto.getUsername());
    user.setFirstName(UserDto.getFirstName());
    user.setLastName(UserDto.getLastName());
    user.setEmail(UserDto.getEmail());
    user.setEnabled(true);
    user.setEmailVerified(true);
    user.setCredentials(Collections.singletonList(credentialRepresentation));

    UserResource usersResource = KeycloakProvider.getUserResource().get(userId);
    usersResource.update(user);
  }
}