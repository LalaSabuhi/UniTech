package az.unibank.unitech.controllers;

import az.unibank.unitech.dto.request.LoginRequest;
import az.unibank.unitech.dto.request.RegisterRequest;
import az.unibank.unitech.dto.response.CommonResponse;
import az.unibank.unitech.dto.response.LoginResponse;
import az.unibank.unitech.entities.User;
import az.unibank.unitech.repos.AccountRepository;
import az.unibank.unitech.repos.UserRepository;
import az.unibank.unitech.security.JwtTokenUtil;
import az.unibank.unitech.services.AccountService;
import az.unibank.unitech.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;

import static az.unibank.unitech.enums.ResponseEnum.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/api/users"})
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    AccountRepository accountRepository;

    @ApiOperation(value = "Register Endpoint", response = CommonResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 1, message = "Success operation"),
            @ApiResponse(code = -1, message = "Unknown error")
    })
    @RequestMapping(value = "/public/register", method = RequestMethod.POST)
    public CommonResponse register(@RequestBody @Valid RegisterRequest registerRequest) {
        try{
            return userService.register(registerRequest);
        }
        catch (Exception e){
            System.out.println(e);
            return new CommonResponse(UNKNOWN_ERROR.getCode(), UNKNOWN_ERROR.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }
    }

    @ApiOperation(value = "Login Endpoint", response = CommonResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 1, message = "Success operation"),
            @ApiResponse(code = -1, message = "Unknown error")
    })
    @RequestMapping(value = "/public/login", method = RequestMethod.POST)
    public CommonResponse login(@RequestBody @Valid LoginRequest request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getPin(), request.getPassword()));

            User user = (User) authenticate.getPrincipal();

            return CommonResponse.success(new LoginResponse(jwtTokenUtil.generateAccessToken(user)));
        }
        catch (BadCredentialsException ex) {
            return new CommonResponse(BAD_CREDENTIALS.getCode(), BAD_CREDENTIALS.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }
    }

    @ApiOperation(value = "Self Endpoint", response = CommonResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 1, message = "Success operation"),
            @ApiResponse(code = -1, message = "Unknown error")
    })
    @RequestMapping(value = "/self", method = RequestMethod.POST)
    public CommonResponse self() {
        try{
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user == null) return new CommonResponse(AUTH_ERROR.getCode(), AUTH_ERROR.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());

            return CommonResponse.success(userRepository.findById(user.getId()));
        }
        catch (Exception e){
            System.out.println(e);
            return new CommonResponse(UNKNOWN_ERROR.getCode(), UNKNOWN_ERROR.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }
    }

}
