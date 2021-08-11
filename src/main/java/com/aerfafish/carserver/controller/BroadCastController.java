package com.aerfafish.carserver.controller;


import com.aerfafish.carserver.socket.ServerWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;

@RequestMapping(value = "/test")
@Component
public class BroadCastController {

    @RequestMapping(value = "/broadcast")
    public void broadcast(){
        ServerWebSocket.broadcast(" hi ".getBytes(StandardCharsets.UTF_8));
    }
}
