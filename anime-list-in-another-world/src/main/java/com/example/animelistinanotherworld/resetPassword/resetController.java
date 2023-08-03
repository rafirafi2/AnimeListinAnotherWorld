package com.example.animelistinanotherworld.resetPassword;

import com.example.animelistinanotherworld.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class resetController {

    private final com.example.animelistinanotherworld.user.UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    //display forgot form
    @RequestMapping(value = "/forgot", method = RequestMethod.GET)
    public ModelAndView displayForgotPasswordPage() {
        return new ModelAndView("forgotPassword");
    }

    // form processing
    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public ModelAndView processForgotPasswordForm(ModelAndView modelAndView, @RequestParam("email") String userEmail, HttpServletRequest request) {

        Optional<User> optional = userRepository.findByEmail(userEmail);
        if (!optional.isPresent()) {
            modelAndView.addObject("errorMessage", "We didn't find an account for that e-mail address.");
        }else{

            // Generate random 36-character string token for reset password
            User user = optional.get();
            user.setReset(UUID.randomUUID().toString());
            userRepository.save(user);
            String appUrl = request.getScheme() + "://" + request.getServerName();

            //email
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom("alinanotherworld@gmail.com");
            passwordResetEmail.setTo(user.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl
                    + "/reset?token=" + user.getReset());
            emailService.sendEmail(passwordResetEmail);

            //password reset sent to email! screen -->>TODO
            modelAndView.addObject("successMessage", "A password reset link has been sent to " + userEmail);
        }

        modelAndView.setViewName("forgotPassword");
        return modelAndView;

    }


    // Display form to reset password
    @RequestMapping(value = "/reset**", method = RequestMethod.GET)
    public ModelAndView displayResetPasswordPage(ModelAndView modelAndView, @RequestParam("token") String token,Model model) {

        model.addAttribute("resetForm", new resetForm());
        model.addAttribute("resetToken", token);

        Optional<User> optional = userRepository.findByReset(token);
        System.out.println("works");

        if (optional.isPresent()) { // Token found in DB
            modelAndView.addObject("resetToken", token);
        } else { // Token not found in DB
            modelAndView.addObject("errorMessage", true);
        }

        modelAndView.setViewName("reset");
        return modelAndView;
    }


    // Process reset password form
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public ModelAndView setNewPassword(ModelAndView modelAndView, @RequestParam("token") String token, @ModelAttribute("resetForm") resetForm request, Model model) {
        System.out.println("Inside setNewPassword method");
        // Find the user associated with the reset token
        Optional<User> optional = userRepository.findByReset(token);
        System.out.println("Inside setNewPassword method");

        // This should always be non-null but we check just in case
        if (optional.isPresent()) {
            System.out.println("SUCCESS");
            User resetUser = optional.get();
            //...
            // Set new password
            resetUser.setPassword(passwordEncoder.encode(request.getPassword()));

            // Set the reset token to null so it cannot be used again
            resetUser.setReset(null);

            // Save user
            userRepository.save(resetUser);


            modelAndView.setViewName("redirect:/login");
            return modelAndView;

        } else {
            System.out.println("FAIL");
            modelAndView.addObject("errorMessage", true);
            modelAndView.setViewName("/reset");
        }
        System.out.println("Nothing");

        return modelAndView;
    }


    //going to reset with no token redirects to login page
   // @ExceptionHandler(MissingServletRequestParameterException.class)
    //public ModelAndView handleMissingParams(MissingServletRequestParameterException ex) {
    //    return new ModelAndView("redirect:/login");
    //}

}
