package com.example.animelistinanotherworld.controllers;

import com.example.animelistinanotherworld.animeIdForm;
import com.example.animelistinanotherworld.jwt.JwtService;
import com.example.animelistinanotherworld.searchForm;
import com.example.animelistinanotherworld.user.User;
import com.example.animelistinanotherworld.user.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanQueryException;
import net.sandrohc.jikan.model.anime.Anime;
import net.sandrohc.jikan.query.random.RandomAnimeQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Controller
public class randomAnime {

    private final JwtService jwtService;
    private final UserRepository userRepository;


    public randomAnime(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }


    @RequestMapping(value = "/randomAnime", method = RequestMethod.GET)
    public ModelAndView getRandomPage(@ModelAttribute searchForm searchForm, Model model, HttpServletRequest request) throws JikanQueryException {

        model.addAttribute("searchForm", searchForm);


        Jikan jikan = new Jikan();
        RandomAnimeQuery randomAnimeQuery = new RandomAnimeQuery(jikan);

        Mono<Anime> randomAnimeMono = randomAnimeQuery.execute();
        Anime randomAnime = randomAnimeMono.block();

        //System.out.println(randomAnime.getMalId());


        ModelAndView modelAndView = new ModelAndView("randomAnime");
        modelAndView.addObject("randomAnime", randomAnime);
        animeIdForm animeIdForm = new animeIdForm();
        animeIdForm.setMalId(randomAnime.getMalId());
        model.addAttribute("animeIdForm", animeIdForm);


        boolean isLoggedIn = false;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    String token = cookie.getValue();
                    // Here, you have the token from the cookie
                    // You can use it to authenticate the user and perform actions accordingly
                    try{
                        User user = getUserFromToken(token);
                        if (user != null) {
                            isLoggedIn = true;
                        }
                    }catch (ExpiredJwtException exception){

                    }

                    break;
                }
            }
        }

        model.addAttribute("isLoggedIn", isLoggedIn);

        return modelAndView;
    }



    private User getUserFromToken(String token) {
        // Extract the username from the token
        String username = jwtService.extractUsername(token);

        // Retrieve the user details from the database based on the username
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElse(null);
    }

    @ExceptionHandler(JikanQueryException.class)
    public ModelAndView handleJikanQueryException(JikanQueryException ex) {
        return new ModelAndView("apiError");
    }


}
