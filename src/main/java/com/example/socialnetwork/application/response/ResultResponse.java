package com.example.socialnetwork.application.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ResultResponse (
        int status,
        String message,
        Object result,
        Instant timestamp
){
}