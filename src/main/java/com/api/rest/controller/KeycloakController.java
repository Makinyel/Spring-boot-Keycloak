package com.api.rest.controller;

import com.api.rest.controller.dto.UserDto;
import com.api.rest.service.IKeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/keycloak/user")
@PreAuthorize("hasRole('admin-client')")
public class KeycloakController {

  @Autowired
  private IKeycloakService keycloakService;


  @GetMapping("/getAll")
  public ResponseEntity<?> findAllUsers(){
    return ResponseEntity.ok(keycloakService.findAll());
  }


  @GetMapping("/getByName/{username}")
  public ResponseEntity<?> searchUserByUsername(@PathVariable String username){
    return ResponseEntity.ok(keycloakService.getByUsername(username));
  }


  @PostMapping("/create")
  public ResponseEntity<?> createUser(@RequestBody UserDto userDto) throws URISyntaxException {
    String response = keycloakService.create(userDto);
    return ResponseEntity.created(new URI("/keycloak/user/create")).body(response);
  }


  @PutMapping("/update/{userId}")
  public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDto userDto){
    keycloakService.update(userId, userDto);
    return ResponseEntity.ok("User updated successfully");
  }


  @DeleteMapping("/delete/{userId}")
  public ResponseEntity<?> deleteUser(@PathVariable String userId){
    keycloakService.deleteById(userId);
    return ResponseEntity.noContent().build();
  }
}