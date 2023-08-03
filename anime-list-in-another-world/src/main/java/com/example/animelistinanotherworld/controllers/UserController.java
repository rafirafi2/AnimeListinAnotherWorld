package com.example.animelistinanotherworld.controllers;

import com.example.animelistinanotherworld.AuthenticationRequest;
import com.example.animelistinanotherworld.AuthenticationResponse;
import com.example.animelistinanotherworld.RegisterRequest;
import com.example.animelistinanotherworld.jwt.AuthService;
import com.example.animelistinanotherworld.jwt.JwtService;
import com.example.animelistinanotherworld.user.User;
import com.example.animelistinanotherworld.user.UserDataRepository;
import com.example.animelistinanotherworld.user.UserRepository;
import com.example.animelistinanotherworld.user.userService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final com.example.animelistinanotherworld.user.userService userService;

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @RequestMapping(value = "/signUp", method = RequestMethod.GET)
    public String getSignUpPage(Model model){
        model.addAttribute("RegisterRequest", new RegisterRequest());
        return "signUp";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginForm(Model model, HttpServletRequest request){
        model.addAttribute("AuthenticationRequest", new AuthenticationRequest());

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    // Redirect to the profile page if the user cookie exists
                    String token = cookie.getValue();
                    try {
                        User user = getUserFromToken(token);
                        if (user != null) {
                            return "redirect:/profile";
                        }
                    }catch (ExpiredJwtException exception){

                    }

                }
            }
        }



        return "login";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("RegisterRequest") RegisterRequest request, Model model) {

        AuthenticationResponse response = authService.register(request);
        if (response != null && response.getToken() != null) {

            // Redirect to the profile page after successful registration
            return "redirect:/login";
        } else {
            // Registration failed, show error message on the signup page
            model.addAttribute("AuthenticationResponse", response);
            return "signup"; // Return to the signup page
        }
    }

   // @PostMapping("/authenticate")
   // public ResponseEntity<AuthenticationResponse> authenticate(
   //         @RequestBody AuthenticationRequest request
    //) {

    //    return ResponseEntity.ok(authService.authenticate(request));

    //}

    @PostMapping("/authenticate")
    @ResponseBody
    public ModelAndView authenticate(@ModelAttribute("AuthenticationRequest") AuthenticationRequest request, HttpSession session, HttpServletResponse Hresponse) {
        AuthenticationResponse response = authService.authenticate(request);
        if (response != null && response.getToken() != null) {
            // Authentication successful, store the JWT token in an HTTP-only cookie
            Cookie jwtCookie = new Cookie("jwtToken", response.getToken());
            jwtCookie.setPath("/");
            Hresponse.addCookie(jwtCookie);

            // Redirect to the profile page
            ModelAndView modelAndView = new ModelAndView("redirect:/profile");
            return modelAndView;
        } else {
            // Authentication failed, show an error message on the login page
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("errorMessage", true);
            return modelAndView;
        }
    }


    private User getUserFromToken(String token) {
        // Extract the username from the token
        String username = jwtService.extractUsername(token);

        // Retrieve the user details from the database based on the username
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElse(null);
    }

}
