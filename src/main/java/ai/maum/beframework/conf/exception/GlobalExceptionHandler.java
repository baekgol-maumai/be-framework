package ai.maum.beframework.conf.exception;

import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.BaseResponse;
import ai.maum.beframework.codemessage.SystemCodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 전역 예외 처리 핸들러
 * @author baekgol@maum.ai
 */
@Component
@Slf4j
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {
    GlobalExceptionHandler(ErrorAttributes errorAttributes, ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        super.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), request -> ServerResponse.ok()
                .bodyValue(request.attribute("ExceptionHandlingWebHandler.handledException")
                        .map(e -> {
                            final Exception ex = (Exception)e;

                            if(!(ex instanceof BaseException be) || be.getInfo() != SystemCodeMsg.UNAUTHORIZED)
                                log.error("{}", ex.getMessage());

                            if(ex instanceof BaseException) return BaseResponse.failure(((BaseException)ex).getInfo());
                            else if(ex instanceof DataIntegrityViolationException)
                                return BaseResponse.failure(SystemCodeMsg.DATA_SEARCH_FAILURE);

                            return BaseResponse.failure(ex.getMessage());
                        })
                        .orElse(BaseResponse.failure())));
    }
}
