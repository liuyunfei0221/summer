//package com.blue.message.router.api;
//
//import com.blue.message.acceptor.MessageAcceptor;
//import com.blue.message.handler.api.MessageHandler;
//import io.rsocket.core.RSocketServer;
//import io.rsocket.frame.decoder.PayloadDecoder;
//import io.rsocket.transport.netty.server.TcpServerTransport;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import reactor.core.Disposable;
//
///**
// * message server route
// *
// * @author liuyunfei
// */
//@Configuration
//public class MessageServerRoute {
//
//    private Disposable disposable;
//
//    @Bean
//    MessageHandler messageHandler(){
//        return new MessageHandler();
//    }
//
//    @Bean
//    MessageAcceptor messageAcceptor(MessageHandler messageHandler){
//        return new MessageAcceptor(messageHandler);
//    }
//
//    @Bean
//    RSocketServer messageServer(MessageAcceptor messageAcceptor) {
////        RSocketRequester.builder()
////                .rsocketStrategies(RSocketStrategies.builder().build())
////                .transport(WebsocketClientTransport.create("localhost", 6666))
//
//        RSocketServer rSocketServer = RSocketServer.create();
//        rSocketServer.acceptor(messageAcceptor);
//        rSocketServer.payloadDecoder(PayloadDecoder.ZERO_COPY);
//
//        disposable = rSocketServer.bind(TcpServerTransport.create("localhost", 6666)).subscribe();
//
//        return rSocketServer;
//    }
//
//}
