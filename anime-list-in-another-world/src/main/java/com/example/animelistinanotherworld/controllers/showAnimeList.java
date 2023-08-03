package com.example.animelistinanotherworld.controllers;

import com.example.animelistinanotherworld.AuthenticationRequest;
import com.example.animelistinanotherworld.animeIdForm;
import com.example.animelistinanotherworld.jwt.JwtService;
import com.example.animelistinanotherworld.list.AnimeListDetails;
import com.example.animelistinanotherworld.list.AnimeListRepository;
import com.example.animelistinanotherworld.list.Status;
import com.example.animelistinanotherworld.list.animelist;
import com.example.animelistinanotherworld.searchForm;
import com.example.animelistinanotherworld.user.User;
import com.example.animelistinanotherworld.user.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanQueryException;
import net.sandrohc.jikan.model.anime.Anime;
import net.sandrohc.jikan.query.anime.AnimeQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class showAnimeList {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AnimeListRepository animeListRepository;

    @RequestMapping(value = "/wholeList", method = RequestMethod.GET)
    public ModelAndView getTotalList(HttpServletRequest request, @ModelAttribute searchForm searchForm, Model model) throws JikanQueryException {

        User validUser = checkValidUser(request);

        if(validUser!=null){
            List<animelist> animeEntries = animeListRepository.findByUser_Id(validUser.getId());
            Jikan jikan = new Jikan();
            List<AnimeListDetails> animeDetailsList = new ArrayList<>();
            for (animelist animeEntry : animeEntries) {
                int animeId = animeEntry.getAnimeId();

                Anime anime = new AnimeQuery(jikan,animeId).execute().block();

                if (anime != null) {

                    AnimeListDetails animeListDetails = new AnimeListDetails();
                    animeListDetails.setAnime(anime);
                    animeListDetails.setWatchStatus(animeEntry.getStatus().toString());
                    //gets the current primary id for the selected anime and user for deletion or to move to status
                    animeListDetails.setAnimeId(animeEntry.getId());
                    animeListDetails.setMalId(animeId);

                    animeDetailsList.add(animeListDetails);
                }
            }

            if(animeDetailsList.size()==0){


                return new ModelAndView("noAnimeError");

            }

            return new ModelAndView("wholeList","animeDetailsList",animeDetailsList);

        }

        model.addAttribute("searchForm", searchForm);
        model.addAttribute("AuthenticationRequest", new AuthenticationRequest());
        return new ModelAndView("/login");


    }



    @PostMapping("/deleteAnimeFromList")
    public String deleteAnimeFromList(HttpServletRequest request, @RequestParam("id") int id){

        User validUser = checkValidUser(request);
        Optional<animelist> animeToDelete = animeListRepository.findById(id);

        if(animeToDelete.isPresent()){

            animelist animelist = animeToDelete.get();
            animeListRepository.delete(animelist);
        }

        return "redirect:/profile";
    }



    @RequestMapping(value = "/watchList", method = RequestMethod.GET)
    public ModelAndView getOnlyWatchList(HttpServletRequest request, @ModelAttribute searchForm searchForm, Model model) throws JikanQueryException {

        User validUser = checkValidUser(request);

        if(validUser!=null){
            List<animelist> animeEntries = animeListRepository.findByUser_Id(validUser.getId());
            Jikan jikan = new Jikan();
            List<AnimeListDetails> animeDetailsList = new ArrayList<>();
            for (animelist animeEntry : animeEntries) {
                int animeId = animeEntry.getAnimeId();
                Status status = animeEntry.getStatus();
                if(status==Status.WATCHING){

                    Anime anime = new AnimeQuery(jikan, animeId).execute().block();

                    if (anime != null) {

                        AnimeListDetails animeListDetails = new AnimeListDetails();
                        animeListDetails.setAnime(anime);
                        animeListDetails.setWatchStatus(animeEntry.getStatus().toString());
                        //gets the current primary id for the selected anime and user for deletion or to move to status
                        animeListDetails.setAnimeId(animeEntry.getId());
                        animeListDetails.setMalId(animeId);

                        animeDetailsList.add(animeListDetails);
                    }
                }
            }

            if(animeDetailsList.size()==0){


                return new ModelAndView("noAnimeError");

            }

            return new ModelAndView("wholeList","animeDetailsList",animeDetailsList);

        }

        model.addAttribute("searchForm", searchForm);
        model.addAttribute("AuthenticationRequest", new AuthenticationRequest());
        return new ModelAndView("/login");


    }


    @RequestMapping(value = "/watchedList", method = RequestMethod.GET)
    public ModelAndView getOnlyWatchedList(HttpServletRequest request, @ModelAttribute searchForm searchForm, Model model) throws JikanQueryException {

        User validUser = checkValidUser(request);

        if(validUser!=null){
            List<animelist> animeEntries = animeListRepository.findByUser_Id(validUser.getId());
            Jikan jikan = new Jikan();
            List<AnimeListDetails> animeDetailsList = new ArrayList<>();
            for (animelist animeEntry : animeEntries) {
                int animeId = animeEntry.getAnimeId();
                Status status = animeEntry.getStatus();
                if(status==Status.WATCHED){

                    Anime anime = new AnimeQuery(jikan, animeId).execute().block();

                    if (anime != null) {

                        AnimeListDetails animeListDetails = new AnimeListDetails();
                        animeListDetails.setAnime(anime);
                        animeListDetails.setWatchStatus(animeEntry.getStatus().toString());
                        //gets the current primary id for the selected anime and user for deletion or to move to status
                        animeListDetails.setAnimeId(animeEntry.getId());
                        animeListDetails.setMalId(animeId);

                        animeDetailsList.add(animeListDetails);
                    }
                }
            }

            if(animeDetailsList.size()==0){


                return new ModelAndView("noAnimeError");

            }

            return new ModelAndView("wholeList","animeDetailsList",animeDetailsList);

        }

        model.addAttribute("searchForm", searchForm);
        model.addAttribute("AuthenticationRequest", new AuthenticationRequest());
        return new ModelAndView("/login");


    }


    @RequestMapping(value = "/planList", method = RequestMethod.GET)
    public ModelAndView getOnlyPlannedList(HttpServletRequest request, @ModelAttribute searchForm searchForm, Model model) throws JikanQueryException {

        User validUser = checkValidUser(request);

        if(validUser!=null){
            List<animelist> animeEntries = animeListRepository.findByUser_Id(validUser.getId());
            Jikan jikan = new Jikan();
            List<AnimeListDetails> animeDetailsList = new ArrayList<>();
            for (animelist animeEntry : animeEntries) {
                int animeId = animeEntry.getAnimeId();
                Status status = animeEntry.getStatus();
                if(status==Status.PLANNING){

                    Anime anime = new AnimeQuery(jikan, animeId).execute().block();

                    if (anime != null) {

                        AnimeListDetails animeListDetails = new AnimeListDetails();
                        animeListDetails.setAnime(anime);
                        animeListDetails.setWatchStatus(animeEntry.getStatus().toString());
                        //gets the current primary id for the selected anime and user for deletion or to move to status
                        animeListDetails.setAnimeId(animeEntry.getId());
                        animeListDetails.setMalId(animeId);

                        animeDetailsList.add(animeListDetails);
                    }
                }
            }

            if(animeDetailsList.size()==0){


                return new ModelAndView("noAnimeError");

            }

            return new ModelAndView("wholeList","animeDetailsList",animeDetailsList);

        }
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("AuthenticationRequest", new AuthenticationRequest());
        return new ModelAndView("/login");


    }

    @RequestMapping(value = "/getSelectedAnime/{malId}", method = RequestMethod.GET)
    public ModelAndView getSelectedAnime(HttpServletRequest request, @PathVariable int malId, Model model) throws JikanQueryException {

        System.out.println("malId: " + malId);
        Jikan jikan = new Jikan();
        int animeId = malId;
        Anime anime = new AnimeQuery(jikan, malId).execute().block();
        ModelAndView modelAndView = new ModelAndView("animePage");
        modelAndView.addObject("randomAnime", anime);
        User validUser = checkValidUser(request);
        boolean isLoggedIn = false;


        animeIdForm animeIdForm = new animeIdForm();
        model.addAttribute("animeIdForm", animeIdForm);
        animeIdForm.setMalId(malId);
        model.addAttribute("isLoggedIn", isLoggedIn);




        return modelAndView;

    }


    private User checkValidUser(HttpServletRequest request){
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
                            return user;
                        }
                    }catch (ExpiredJwtException exception){
                        return null;
                    }

                    break;
                }
            }
        }

        return null;

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
