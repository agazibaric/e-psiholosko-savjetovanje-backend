package com.epsih.service;

import com.epsih.constants.AuthorityConstants;
import com.epsih.dto.*;
import com.epsih.exceptions.BadRequestException;
import com.epsih.exceptions.UserException;
import com.epsih.model.mail.NotificationEmail;
import com.epsih.model.user.*;
import com.epsih.repository.*;
import com.epsih.security.jwt.TokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.Period;
import java.util.*;

@Service
@AllArgsConstructor
public class AuthService {

   private final TokenProvider tokenProvider;
   private final AuthenticationManagerBuilder authenticationManagerBuilder;
   private final ActivationTokenRepository activationTokenRepository;
   private final ResetPasswordTokenRepository resetPasswordTokenRepository;
   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
   private final AuthorityRepository authorityRepository;
   private final UserService userService;
   private final PatientRepository patientRepository;
   private final DoctorRepository doctorRepository;
   private final MailService mailService;

   public String authenticate(LoginDto loginDto) {
      UsernamePasswordAuthenticationToken authenticationToken =
         new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

      Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      boolean rememberMe = loginDto.getRememberMe() != null && loginDto.getRememberMe();
      return tokenProvider.createToken(authentication, rememberMe);
   }

   public void activateAccount(String token) {
      Optional<ActivationToken> activationToken = activationTokenRepository.findByToken(token);
      fetchUserAndEnable(activationToken.orElseThrow(() -> new BadRequestException("Invalid Activation Token")));
   }

   @Transactional
   public void register(RegisterDto registerDto) {
      if (userRepository.findByUsername(registerDto.getUsername()).isPresent())
         throw new UserException("User with given username already exists");

      User user = User.builder()
         .email(registerDto.getEmail())
         .firstname(registerDto.getFirstname())
         .lastname(registerDto.getLastname())
         .username(registerDto.getUsername())
         .phoneNumber(registerDto.getPhoneNumber())
         .password(passwordEncoder.encode(registerDto.getPassword()))
         .authorities(new HashSet<>(Collections.singletonList(authorityRepository.getOne(AuthorityConstants.ROLE_USER))))
         .activated(false)
         .build();

      Patient patient = Patient.builder()
         .user(user)
         .diagnosis("")
         .build();

      patientRepository.save(patient);

      String token = generateActivationToken(user);
      String activationLink = "http://localhost:8080/api/activate/" + token;
      mailService.sendMail(NotificationEmail.builder()
      .recipient(user.getEmail())
      .subject("Activate E-psychological Counseling Account")
      .body("To activate your E-psychological Counseling account click on this link: " + activationLink)
      .build());
   }

   public void changePassword(ChangePasswordDto changePasswordDto) {
      User user = userService.getUserWithAuthorities().get();
      if (!user.getPassword().equals(passwordEncoder.encode(changePasswordDto.getOldPassword())))
         throw new BadRequestException("You entered wrong old password");
      if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword()))
         throw new BadRequestException("New password and repeated password do not match");

      user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
      userRepository.save(user);
   }

   public void requestResetPassword() {
      User user = userService.getUserWithAuthorities().get();
      String token = generateResetPasswordToken(user);

      System.out.println(token);
      // TODO: Send token to user's email
   }

   public void resetPassword(ResetPasswordDto resetPasswordDto) {
      if (!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword()))
         throw new BadRequestException("New password and confirm password do not match");

      ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByToken(resetPasswordDto.getToken())
         .orElseThrow(() -> new BadRequestException("Reset password token is invalid"));

      if (resetPasswordToken.getExpiryDate().isBefore(Instant.now()))
         throw new BadRequestException("Reset password token expired");

      resetPasswordTokenRepository.delete(resetPasswordToken);

      User user = userService.getUserWithAuthorities().get();
      user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
      userRepository.save(user);
   }

   public void registerDoctor(DoctorDto doctorDto) {
      RegisterDto registerDto = doctorDto.getRegisterDto();
      if (userRepository.findByUsername(registerDto.getUsername()).isPresent())
         throw new UserException("User with given username already exists");

      User user = User.builder()
         .email(registerDto.getEmail())
         .firstname(registerDto.getFirstname())
         .lastname(registerDto.getLastname())
         .username(registerDto.getUsername())
         .phoneNumber(registerDto.getPhoneNumber())
         .password(passwordEncoder.encode(registerDto.getPassword()))
         .authorities(new HashSet<>(Collections.singletonList(authorityRepository.getOne(AuthorityConstants.ROLE_DOCTOR))))
         .activated(false)
         .build();

      Doctor doctor = Doctor.builder()
         .user(user)
         .profession(doctorDto.getProfession())
         .position(doctorDto.getPosition())
         .build();

      doctorRepository.save(doctor);

      String token = generateActivationToken(user);
      String activationLink = "http://localhost:8080/api/activate/" + token;
      mailService.sendMail(NotificationEmail.builder()
         .recipient(user.getEmail())
         .subject("Activate E-psychological Counseling Doctor Account")
         .body("To activate your E-psychological Counseling account click on this link: " + activationLink)
         .build());
   }

   //////////////////////////////////
   // Private methods:
   //////////////////////////////////

   private void fetchUserAndEnable(ActivationToken activationToken) {
      String username = activationToken.getUser().getUsername();
      User user = userRepository.findByUsername(username).orElseThrow(() -> new UserException("User not found with name - " + username));
      user.setActivated(true);
      userRepository.save(user);
   }

   private String generateActivationToken(User user) {
      String token = UUID.randomUUID().toString();
      ActivationToken activationToken = new ActivationToken();
      activationToken.setToken(token);
      activationToken.setUser(user);
      activationTokenRepository.save(activationToken);
      return token;
   }

   private String generateResetPasswordToken(User user) {
      String token = UUID.randomUUID().toString();
      ResetPasswordToken resetPasswordToken = ResetPasswordToken.builder()
         .token(token)
         .user(user)
         .expiryDate(Instant.now().plus(Period.ofDays(ResetPasswordToken.DAYS_TO_EXPIRE)))
         .build();
      resetPasswordTokenRepository.save(resetPasswordToken);
      return token;
   }

}
