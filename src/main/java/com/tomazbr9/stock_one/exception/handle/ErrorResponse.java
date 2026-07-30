package com.tomazbr9.stock_one.exception.handle;

import java.time.LocalDateTime;

public record ErrorResponse (
    int status,
    String message,
    String path,
    LocalDateTime timestamp
){}
