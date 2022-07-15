//package demo;
//
//import com.blue.basic.common.reactive.AccessGetterForReactive;
//import com.blue.basic.model.base.BlueResponse;
//import com.blue.context.holder.ReactiveContextHolder;
//import com.blue.member.service.inter.MemberBasicService;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Mono;
//
//import static com.blue.basic.common.reactive.ReactiveCommonFunctions.generate;
//import static com.blue.basic.constant.base.ResponseElement.OK;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.web.reactive.function.server.ServerResponse.ok;
//
///**
// * member api handler
// *
// * @author liuyunfei
// */
//@SuppressWarnings("JavaDoc")
//@Component
//public final class MemberApiHandler {
//
//    private final MemberBasicService memberBasicService;
//
//    public MemberApiHandler(MemberBasicService memberBasicService) {
//        this.memberBasicService = memberBasicService;
//    }
//
//    /**
//     * get member info by access
//     *
//     * @param serverRequest
//     * @return
//     */
//    public Mono<ServerResponse> selectMemberInfo(ServerRequest serverRequest) {
//        return ReactiveContextHolder.getServerHttpRequest()
//                .flatMap(AccessGetterForReactive::getAccessReact)
//                .flatMap(ai ->
//                        memberBasicService.selectMemberInfoMonoByPrimaryKeyWithAssert(ai.getId())
//                                .flatMap(mbi ->
//                                        ok()
//                                                .contentType(APPLICATION_JSON)
//                                                .body(generate(OK.code, mbi, serverRequest), BlueResponse.class))
//                );
//    }
//
//}
