package ru.omon4412.minibank.middle.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String error;
    private String path;
    private int status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private ZonedDateTime timestamp;

    public static ApiError from(String e, HttpStatus httpStatus, String path) {
        return ApiError.builder().error(e).status(httpStatus.value()).path(path).timestamp(ZonedDateTime.now()).build();
    }
}
