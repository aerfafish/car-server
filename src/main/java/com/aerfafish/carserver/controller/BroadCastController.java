package com.aerfafish.carserver.controller;


import com.aerfafish.carserver.socket.ServerWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/test")
@Component
public class BroadCastController {



    @Autowired
    ServerWebSocket webSocket;



    @RequestMapping(value = "/broadcast")
    public void broadcast(){
        webSocket.broadcast();
    }
}
