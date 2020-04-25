package pl.devgroup.restapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.devgroup.restapi.service.TrackService;

import java.io.IOException;

@Controller
public class TrackController {

    private TrackService trackService;

    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @RequestMapping(value = "/allTracks", method = RequestMethod.GET)
    public ModelAndView getTracks() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("trackList", trackService.getTracks());
        modelAndView.setViewName("tracks");
        return modelAndView;
    }

    @RequestMapping(value = "/getTrack/{trackId}", method = RequestMethod.GET)
    public ModelAndView getTrack(@PathVariable("trackId") String trackId) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("trackDetails", trackService.getTrack(trackId));
        modelAndView.setViewName("track");
        return modelAndView;
    }

}
