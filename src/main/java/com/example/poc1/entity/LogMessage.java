package com.example.poc1.entity;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogMessage {
    private String message;
    private String level;
    private String timestamp;

    public LogMessage(String message, String level) {
        this.message = message;
        this.level = level;
        this.timestamp = Instant.now().toString();
    }

}
