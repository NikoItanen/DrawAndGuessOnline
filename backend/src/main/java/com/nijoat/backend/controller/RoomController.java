package com.nijoat.backend.controller;

import com.nijoat.backend.model.Room;
import com.nijoat.backend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// RestController-annotaatio ilmoittaa, että tämä luokka on osa REST API:a ja palvelee HTTP-pyyntöjä.
@RestController

// RequestMapping määrittelee peruspolun kaikille luokan käsittelymetodeille.
@RequestMapping("/api/rooms")
public class RoomController {
    
    // Automaattisesti injektoi RoomService-luokan ilmentymän Springin kontekstista.
    @Autowired
    private RoomService roomService;

    // GetMapping-annotaatio määrittelee, että tämä metodi vastaa HTTP GET -pyyntöihin.
    @GetMapping
    public List<String> getAllRooms() {
        // Kutsuu RoomService-luokan metodia, joka palauttaa listan kaikista huoneista.
        return roomService.getAllRooms();
    }
}
