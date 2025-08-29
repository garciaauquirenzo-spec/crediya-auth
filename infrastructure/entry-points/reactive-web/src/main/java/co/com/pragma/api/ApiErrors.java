package co.com.pragma.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@Component
final class ApiErrors {
    static Mono<ServerResponse> badRequest(String message) {
        return json(HttpStatus.BAD_REQUEST, message);
    }
    static Mono<ServerResponse> conflict(String message) {
        return json(HttpStatus.CONFLICT, message);
    }
    private static Mono<ServerResponse> json(HttpStatus status, String message) {
        return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", status.value(),
                        "error", status.getReasonPhrase(),
                        "message", message));
    }
    private ApiErrors() {}
}
