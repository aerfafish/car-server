package com.aerfafish.carserver.config;


import com.aerfafish.carserver.socket.ClientWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

@Component
public class ServerConfigurator  extends ServerEndpointConfig.Configurator {

    private final static Logger log = LoggerFactory.getLogger(ServerConfigurator.class);

    /**
     * token鉴权认证 临时写死123
     *
     * @param originHeaderValue
     * @return
     */
    @Override
    public boolean checkOrigin(String originHeaderValue) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String token = request.getParameter("token");
        log.info("---->>>>>  token {" + token + "}");
        if ("yanshuangshigehanhan".equals(token)) {
            return super.checkOrigin(originHeaderValue);
        } else {
            return false;
        }
    }

    /**
     * Modify the WebSocket handshake response
     * 修改websocket 返回值
     *
     * @param sec
     * @param request
     * @param response
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        super.modifyHandshake(sec, request, response);
    }

}
