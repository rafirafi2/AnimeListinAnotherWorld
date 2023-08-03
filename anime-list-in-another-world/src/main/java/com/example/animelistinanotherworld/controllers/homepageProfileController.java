package com.example.animelistinanotherworld.controllers;

import com.example.animelistinanotherworld.jwt.JwtService;
import com.example.animelistinanotherworld.list.AnimeListRepository;
import com.example.animelistinanotherworld.list.AnimeListService;
import com.example.animelistinanotherworld.list.animelist;
import com.example.animelistinanotherworld.user.User;
import com.example.animelistinanotherworld.user.UserDataRepository;
import com.example.animelistinanotherworld.user.UserRepository;
import com.example.animelistinanotherworld.user.userData;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class homepageProfileController {

    private final UserRepository userRepository;
    private final UserDataRepository userDataRepository;
    private final AnimeListRepository animeListRepository;
    @Autowired
    private JwtService jwtService;

    @RequestMapping(value = "/profile**", method = RequestMethod.GET)
    public String getHomeProfilePage(HttpServletRequest request, Model model, @RequestParam(name = "page", defaultValue = "0") int page)throws IOException, SQLException{

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    String token = cookie.getValue();
                    // Here, you have the token from the cookie
                    // You can use it to authenticate the user and perform actions accordingly
                    User user = getUserFromToken(token);
                    if (user != null) {
                        // Add the user details to the model to display on the profile page
                        userData userData = userDataRepository.findByUser_Id(user.getId());
                        byte [] imageBytes = userData.getProfileImage().getBytes(1,(int) userData.getProfileImage().length());
                        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                        model.addAttribute("name", user.getUsername());
                        model.addAttribute("profileImage", base64Image);
                        model.addAttribute("totalCount", userData.getTotalCount());
                        model.addAttribute("totalWatching", userData.getTotalWatching());
                        model.addAttribute("totalPlanning", userData.getTotalPlanning());
                        model.addAttribute("totalWatched", userData.getTotalWatched());

                        if(userData.getTotalCount()==0){
                            model.addAttribute("rank", "F Class");
                        }else if(userData.getTotalCount()>0 && userData.getTotalCount()<20){
                            model.addAttribute("rank", "D Class");
                        }else if(userData.getTotalCount()>19 && userData.getTotalCount()<50){
                            model.addAttribute("rank", "C Class");
                        }else if(userData.getTotalCount()>49 && userData.getTotalCount()<80){
                            model.addAttribute("rank", "B Class");
                        }else if(userData.getTotalCount()>79 && userData.getTotalCount()<100){
                            model.addAttribute("rank", "A Class");
                        }else if(userData.getTotalCount()>99 && userData.getTotalCount()<150){
                            model.addAttribute("rank", "A+ Class");
                        }else if(userData.getTotalCount()>149 && userData.getTotalCount()<200){
                            model.addAttribute("rank", "S Class");
                        }else if(userData.getTotalCount()>199 && userData.getTotalCount()<260){
                            model.addAttribute("rank", "SS Class");
                        }else if(userData.getTotalCount()>259 && userData.getTotalCount()<350){
                            model.addAttribute("rank", "SSS Class");
                        }else if(userData.getTotalCount()>349){
                            model.addAttribute("rank", "Legendary Class");
                        }

                        return "profile";
                    } else {
                        // If the token is invalid or user details cannot be retrieved, redirect to the login page
                        return "redirect:/login";
                    }
                }
            }
        }
        return "redirect:/login";
    }


    @PostMapping("/add")
    public String addImagePost(HttpServletRequest request,@RequestParam("image") MultipartFile file) throws IOException, SerialException, SQLException
    {
        byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);


        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    String token = cookie.getValue();
                    // Here, you have the token from the cookie
                    // You can use it to authenticate the user and perform actions accordingly
                    User user = getUserFromToken(token);
                    if (user != null) {
                        // Add the user details to the model to display on the profile page
                        userData userData = userDataRepository.findByUser_Id(user.getId());
                        userData.setProfileImage(blob);

                        userData.setUser(user);
                        userDataRepository.save(userData);


                        return "redirect:/profile";
                    } else {
                        // If the token is invalid or user details cannot be retrieved, redirect to the login page
                        return "redirect:/";
                    }
                }
            }
        }
        return "redirect:/";
    }



    private User getUserFromToken(String token) {
        // Extract the username from the token
        String username = jwtService.extractUsername(token);

        // Retrieve the user details from the database based on the username
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElse(null);
    }

    @ExceptionHandler(IOException.class)
    public ModelAndView handleIOException(IOException ex) {
        return new ModelAndView("imageError");
    }

    @ExceptionHandler(SQLException.class)
    public ModelAndView handleSQLException(SQLException ex) {
        return new ModelAndView("imageError");
    }


}
