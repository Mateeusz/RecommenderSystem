package pl.devgroup.restapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.devgroup.restapi.ColaborativeFiltering.Matrix;
import pl.devgroup.restapi.ContentBased.Content;
import pl.devgroup.restapi.model.TrackDetails;
import pl.devgroup.restapi.service.TrackService;

import javax.validation.Valid;
import java.io.IOException;


@Controller
public class TrackController {

    private TrackService trackService;
    private Matrix matrix;
    private Content content;

    @Autowired
    public TrackController(TrackService trackService, Matrix matrix, Content content) {
        this.trackService = trackService;
        this.matrix = matrix;
        this.content = content;
    }

    @RequestMapping(value = "/allTracks", method = RequestMethod.GET)
    public ModelAndView getTracks(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("trackList", trackService.getTracks());
        modelAndView.addObject("activeUser", authentication.getName());
        modelAndView.setViewName("tracks");
        return modelAndView;
    }

    @RequestMapping(value = "/getTrack/{trackId}", method = RequestMethod.GET)
    public ModelAndView getTrack(@PathVariable("trackId") String trackId, Authentication authentication) throws IOException {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("trackDetails", trackService.getTrackWithRating(trackId, authentication.getName()));
        modelAndView.addObject("activeUser", authentication.getName());
        modelAndView.setViewName("track");
        return modelAndView;
    }

    @RequestMapping(value = "/getTrack/{trackId}", method = RequestMethod.POST)
    public ModelAndView setPoints(@Valid @ModelAttribute TrackDetails trackDetails, Authentication authentication) throws IOException {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("trackDetails", trackDetails);
        modelAndView.addObject("message", "Thank's for your vote!");
        modelAndView.addObject("activeUser", authentication.getName());

        TrackDetails trackDetails1 = trackService.getTrackWithRating(trackDetails.getTrackId(), authentication.getName());
        trackDetails.setArtist(trackDetails1.getArtist());
        trackDetails.setTimestamp(trackDetails1.getTimestamp());
        trackDetails.setSimilars(trackDetails1.getSimilars());
        trackDetails.setTags(trackDetails1.getTags());
        trackDetails.setTitle(trackDetails1.getTitle());

        trackService.saveRating(trackDetails, authentication.getName());
        modelAndView.setViewName("track");

        return modelAndView;
    }


    @RequestMapping(value = "/collaborativeFiltering", method = RequestMethod.GET)
    public ModelAndView test(Authentication authentication) throws IOException {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("recommendations", matrix.getRecommendations(authentication.getName()));
        modelAndView.addObject("activeUser", authentication.getName());

        modelAndView.setViewName("collaborativeFiltering");

        return modelAndView;
    }

    @RequestMapping(value = "/contentBased", method = RequestMethod.GET)
    public ModelAndView contentBased(Authentication authentication) throws IOException {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("activeUser", authentication.getName());
        modelAndView.addObject("recommendedTrack", content.createRecommendedList(authentication.getName()));

        modelAndView.setViewName("contentBased");

        return modelAndView;
    }
}
