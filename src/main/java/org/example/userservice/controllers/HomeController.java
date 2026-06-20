package org.example.userservice.controllers;

import org.example.userservice.entity.User;
import org.example.userservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController
{
    @GetMapping(value = "/rest/home")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello World");
    }
}