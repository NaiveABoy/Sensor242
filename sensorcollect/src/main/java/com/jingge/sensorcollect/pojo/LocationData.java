package com.jingge.sensorcollect.pojo;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class LocationData {
    private String timestamp;

    private String value1;

    private String value2;

    private String value3;

    private String value4;

    private String value5;
}
