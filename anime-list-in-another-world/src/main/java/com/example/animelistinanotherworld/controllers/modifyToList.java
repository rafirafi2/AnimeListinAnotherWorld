package com.example.animelistinanotherworld.controllers;

import com.example.animelistinanotherworld.animeIdForm;
import com.example.animelistinanotherworld.jwt.JwtService;
import com.example.animelistinanotherworld.list.AnimeListRepository;
import com.example.animelistinanotherworld.list.animelist;
import com.example.animelistinanotherworld.user.User;
import com.example.animelistinanotherworld.user.UserDataRepository;
import com.example.animelistinanotherworld.user.UserRepository;
import com.example.animelistinanotherworld.user.userData;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

import static com.example.animelistinanotherworld.list.Status.*;

@Controller
@RequiredArgsConstructor
public class modifyToList {

    private final AnimeListRepository animeListRepository;
    private final UserRepository userRepository;
    private final UserDataRepository userDataRepository;
    private final JwtService jwtService;

    @RequestMapping(value = "/addToWatched**", method = RequestMethod.POST)
    public String addToWatched(@ModelAttribute animeIdForm animeIdForm, Model model, HttpServletRequest request){

        int animeId = animeIdForm.getMalId();
        System.out.println(animeId);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    String token = cookie.getValue();
                    // Here, you have the token from the cookie
                    // You can use it to authenticate the user and perform actions accordingly
                    User user = getUserFromToken(token);
                    if (user != null) {

                        //animelist animeList = animeListRepository.findByUser_Id(user.getId());

                        animelist animeList = new animelist();

                        userData userData = userDataRepository.findByUser_Id(user.getId());
                        // If AnimeList entry already exists, update its properties
                        animeList.setAnimeId(animeId);
                        animeList.setStatus(WATCHED);
                        animeList.setUser(user);



                        userData.setTotalWatched(userData.getTotalWatched() + 1);
                        userData.setTotalCount(userData.getTotalCount() + 1);
                        userData.setUser(user);

                        animeListRepository.save(animeList);
                        userDataRepository.save(userData);
                    } else {
                        return "/login";
                    }
                }
            }
        }
        return "redirect:/login";

    }



    @RequestMapping(value = "/addToWatching**", method = RequestMethod.POST)
    public String addToWatching(@ModelAttribute animeIdForm animeIdForm, Model model, HttpServletRequest request){

        int animeId = animeIdForm.getMalId();

        System.out.println(animeId);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    String token = cookie.getValue();
                    // Here, you have the token from the cookie
                    // You can use it to authenticate the user and perform actions accordingly
                    User user = getUserFromToken(token);
                    if (user != null) {

                        //animelist animeList = animeListRepository.findByUser_Id(user.getId());
                        animelist animeList = new animelist();
                        userData userData = userDataRepository.findByUser_Id(user.getId());
                        // If AnimeList entry already exists, update its properties
                        animeList.setAnimeId(animeId);
                        animeList.setStatus(WATCHING);
                        animeList.setUser(user);

                        userData.setTotalWatching(userData.getTotalWatching()+1);
                        userData.setTotalCount(userData.getTotalCount()+1);
                        userData.setUser(user);
                        animeListRepository.save(animeList);
                        userDataRepository.save(userData);

                        }
                    }
                }
            }
        return "redirect:/login";

    }

    @RequestMapping(value = "/addToPlanning**", method = RequestMethod.POST)
    public String addToPlanning(@ModelAttribute animeIdForm animeIdForm, Model model, HttpServletRequest request){

        int animeId = animeIdForm.getMalId();

        System.out.println(animeId);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    String token = cookie.getValue();
                    // Here, you have the token from the cookie
                    // You can use it to authenticate the user and perform actions accordingly
                    User user = getUserFromToken(token);
                    if (user != null) {

                        animelist animeList = new animelist();

                        userData userData = userDataRepository.findByUser_Id(user.getId());

                        // If AnimeList entry already exists, update its properties
                        animeList.setAnimeId(animeId);
                        animeList.setStatus(PLANNING);
                        animeList.setUser(user);

                        userData.setTotalPlanning(userData.getTotalPlanning()+1);
                        userData.setTotalCount(userData.getTotalCount()+1);
                        userData.setUser(user);
                        animeListRepository.save(animeList);
                        userDataRepository.save(userData);
                    }
                }
            }
        }
        return "redirect:/login";

    }



    private User getUserFromToken(String token) {
        // Extract the username from the token
        String username = jwtService.extractUsername(token);

        // Retrieve the user details from the database based on the username
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElse(null);
    }


}
