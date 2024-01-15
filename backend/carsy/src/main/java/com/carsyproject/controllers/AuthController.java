package com.carsyproject.controllers;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.carsyproject.security.services.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.carsyproject.models.ERole;
import com.carsyproject.models.Role;
import com.carsyproject.models.User;
import com.carsyproject.payload.request.LoginRequest;
import com.carsyproject.payload.request.SignupRequest;
import com.carsyproject.payload.response.UserInfoResponse;
import com.carsyproject.payload.response.MessageResponse;
import com.carsyproject.repository.RoleRepository;
import com.carsyproject.repository.UserRepository;
import com.carsyproject.security.jwt.JwtUtils;
import com.carsyproject.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "auth", description = "the auth API")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/check")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(userDetails.getUsername());
    }

    @PostMapping("/signin")
    @Operation(summary = "Sign in", description = "Sign in to the application")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // Authenticate the user using the AuthenticationManager
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // Set the authenticated user in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get the UserDetails from the authenticated user
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));
        if (!user.isVerified()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User is not verified!"));
        }

        // Generate a JWT token for the authenticated user
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        // Get the roles of the authenticated user
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Return the JWT token and user info in the response
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));
    }

    @PostMapping("/signup")
    @Operation(summary = "Sign up", description = "Register a new user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        // Generate the verification code
        String verificationCode = emailService.generateVerificationCode();

        // Save the verification code in the database
        user.setVerificationCode(verificationCode);
        userRepository.save(user);

        // Send the verification email
        String emailContent = "Thank you for registering. To complete your registration, please enter the following verification code: " + verificationCode
                + "\n\nIf you did not register at our site, ignore this email."
                + "\n\nThanks,\nCarsy Team";
        emailService.sendEmail(user.getEmail(), "Complete your registration", emailContent);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify email", description = "Verify the user's email address")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        if (!user.getVerificationCode().equals(code)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Verification code is incorrect!"));
        }

        user.setVerified(true);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Email verified successfully!"));
    }

    @PostMapping("/signout")
    @Operation(summary = "Sign out", description = "Sign out from the current session")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgotten password", description = "Sends new password to provided e-mail address. Requires USER, MODERATOR, or ADMIN role.")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email not found!"));
        }

        // Generate the temporary password
        String tempPassword = emailService.generateRandomPassword(10);

        // Update the user's password
        user.setPassword(encoder.encode(tempPassword));
        userRepository.save(user);

        // Send the password reset email
        emailService.sendEmail(user.getEmail(), "Your password has been reset",
                "Your new carsy password is: " + tempPassword
            + "\n\nPlease change your password after logging in."
        + "\n\nIf you did not request this password reset, ignore this email."
        + "\n\nThanks,\nCarsy Team");

        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Reset password", description = "Reset the current user's password. Requires USER, MODERATOR, or ADMIN role.")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("Error: User is not found."));

        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        if (!encoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Old password is incorrect!"));
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Password updated successfully!"));
    }


}