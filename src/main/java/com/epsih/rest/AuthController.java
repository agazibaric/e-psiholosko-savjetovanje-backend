package com.epsih.rest;

import static org.springframework.http.HttpStatus.OK;

import javax.validation.Valid;

import com.epsih.constants.Endpoints;
import com.epsih.dto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epsih.security.jwt.JWTFilter;
import com.epsih.security.jwt.JWTToken;
import com.epsih.service.AuthService;

import lombok.AllArgsConstructor;

/**
 * Controller to authenticate users.
 */
@AllArgsConstructor
@RestController
@RequestMapping(Endpoints.AUTH_ROOT)
public class AuthController {

   private final AuthService authService;

   @PostMapping(Endpoints.AUTH_LOGIN)
   public ResponseEntity<JWTToken> login(@Valid @RequestBody LoginDto loginDto) {
      String jwt = authService.authenticate(loginDto);
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
      return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
   }

   @PostMapping(Endpoints.AUTH_REGISTER)
   public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto registerDto) {
      authService.register(registerDto);
      return ResponseEntity.ok().build();
   }

   @PostMapping(Endpoints.AUTH_REGISTER_DOCTOR)
   public ResponseEntity<Void> registerDoctor(@Valid @RequestBody DoctorDto doctorDto) {
      authService.registerDoctor(doctorDto);
      return ResponseEntity.ok().build();
   }

   @GetMapping(Endpoints.AUTH_ACTIVATE)
   public ResponseEntity<String> activateAccount(@PathVariable String token) {
      authService.activateAccount(token);
      return new ResponseEntity<>("Account activated successfully", OK);
   }

   @PostMapping(Endpoints.AUTH_CHANGE_PASSWORD)
   public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
      authService.changePassword(changePasswordDto);
      return new ResponseEntity<>("Password is successfully changed", OK);
   }

   @PostMapping(Endpoints.AUTH_REQUEST_RESET_PASSWORD)
   public ResponseEntity<String> requestResetPassword() {
      authService.requestResetPassword();
      return new ResponseEntity<>("You will receive reset password link on your email", OK);
   }

   @PostMapping(Endpoints.AUTH_RESET_PASSWORD)
   public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
      authService.resetPassword(resetPasswordDto);
      return new ResponseEntity<>("Password is updated successfully", OK);
   }

}
