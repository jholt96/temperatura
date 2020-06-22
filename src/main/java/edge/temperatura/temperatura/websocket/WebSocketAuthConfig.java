package edge.temperatura.temperatura.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import edge.temperatura.temperatura.security.JwtUtils;
import edge.temperatura.temperatura.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketAuthConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
	private JwtUtils jwtUtils;
 
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketAuthConfig.class);

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                StompHeaderAccessor accessor =
                    MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                logger.info("************ STOMP COMMAND *****"+accessor.getCommand());
 
                logger.info("STOMP access destination "+accessor.getDestination());

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    if (accessor.getNativeHeader("Authorization") != null) {

                        String authToken = accessor.getFirstNativeHeader("Authorization");
                        logger.info(authToken);
                        
                        if (StringUtils.hasText(authToken) && authToken.startsWith("Bearer ")){
                            
                            authToken = authToken.substring(7, authToken.length());

                            String username = jwtUtils.getUserNameFromJwtToken(authToken);

                            if(username != null && jwtUtils.validateJwtToken(authToken)){

                                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                                                                                                                            userDetails.getAuthorities());
                                if(SecurityContextHolder.getContext().getAuthentication() == null){

                                    SecurityContextHolder.getContext().setAuthentication(authentication);
                                    accessor.setUser(authentication);
                                }                                                                                           
                            }
                        }
                    }
                }
                /*
                else if (StompCommand.SEND.equals(accessor.getCommand())){
                    boolean isSent = channel.send(message);
 
                    if(isSent)
                        return message;
                }else if (StompCommand.DISCONNECT.equals(accessor.getCommand())){
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                }*/
                return message;
            }
        });
    }
}