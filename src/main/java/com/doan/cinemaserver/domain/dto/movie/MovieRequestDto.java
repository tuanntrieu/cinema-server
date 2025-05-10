package com.doan.cinemaserver.domain.dto.movie;

import com.doan.cinemaserver.constant.CommonConstant;
import com.doan.cinemaserver.constant.ErrorMessage;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequestDto {

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    private String name;


    private String actors;

    private String trailer;
    private String director;

    private int duration = CommonConstant.ZERO_INT_VALUE;

    @Lob
    private String description;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    private String language;

    private Boolean isSub;

    private int ageLimit = CommonConstant.ZERO_INT_VALUE;;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private List<Long> movieTypeId =new ArrayList<>();

}
