package org.example.pojo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class YoungestOldestWorkers {
    private String text;
    private String name;
    private LocalDate birthday;
}
