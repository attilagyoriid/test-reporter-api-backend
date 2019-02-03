package com.ericsson.eea.rv.testreporter.testreporter.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class DetailedResponseMessage {

    private Date timestamp;
    private String message;
    private List<String> details;
}
