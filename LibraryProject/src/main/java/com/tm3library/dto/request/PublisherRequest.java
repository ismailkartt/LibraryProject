package com.tm3library.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublisherRequest {

    @Size(min = 2, max = 50)
    @NotNull(message = "Please provide publisher name")
    private String name;
}
