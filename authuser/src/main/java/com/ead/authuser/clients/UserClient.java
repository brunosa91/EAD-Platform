package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.ResponsePageDto;
import com.ead.authuser.service.UtilsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class UserClient {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UtilsService utilsService;

    public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable) {
        List<CourseDto> searchResult = null;
        ResponseEntity<ResponsePageDto<CourseDto>> result = null;
       String url = utilsService.createUrl(userId,pageable);
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);

        try {

            ParameterizedTypeReference<ResponsePageDto<CourseDto>> responseType =
                    new ParameterizedTypeReference<ResponsePageDto<CourseDto>>() {
                    };
            result = restTemplate
                    .exchange(url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();

            log.debug("Response Number of Elements: {} ", searchResult.size());

        } catch (HttpStatusCodeException e) {

            log.error("Error request /courses {} ", e);
        }
        log.info("Ending Request /courses userId {} ", userId);

        return result.getBody();
    }

}
