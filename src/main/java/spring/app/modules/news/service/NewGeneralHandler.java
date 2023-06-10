package spring.app.modules.news.service;

import spring.app.modules.news.domain.New;
import spring.app.modules.news.dto.NewAllInfoDto;

import java.util.List;
import java.util.stream.Collectors;

public interface NewGeneralHandler {
    default NewAllInfoDto allInfoDto(New aNew) {
        return NewAllInfoDto.builder()
                .id(aNew.getIdNew())
                .name(aNew.getName())
                .publishDate(String.valueOf(aNew.getPublishDate()))
                .desc(aNew.getDescription())
                .emailUser(aNew.getCreatedBy().getEmail())
                .build();
    }

    default List<NewAllInfoDto> listToDto(List<New> news) {
        return news.stream().map(this::allInfoDto).collect(Collectors.toList());
    }
}
