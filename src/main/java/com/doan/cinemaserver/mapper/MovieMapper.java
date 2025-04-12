package com.doan.cinemaserver.mapper;

import com.doan.cinemaserver.domain.dto.movie.MovieRequestDto;
import com.doan.cinemaserver.domain.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mappings({
            @Mapping(target = "super.createdDate",ignore = true),
            @Mapping(target = "super.lastModifiedDate",ignore = true)
    })
    public Movie toMovie(MovieRequestDto dto);
}
