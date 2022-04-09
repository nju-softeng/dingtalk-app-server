package com.softeng.dingtalk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class PatentController {
    @PutMapping("/patent")
    public  void addPatent(){
        
    }

    @DeleteMapping("/patent/{id}")
    public void deletePatent(@PathVariable int id){

    }
}
