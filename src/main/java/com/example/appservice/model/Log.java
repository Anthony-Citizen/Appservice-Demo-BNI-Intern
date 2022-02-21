package com.example.appservice.model;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Log {
    @Id
    private String id;

    private String log_data;

    protected Date createdAt;

    public void setDate(){
        this.createdAt = new java.util.Date();
    }
}
