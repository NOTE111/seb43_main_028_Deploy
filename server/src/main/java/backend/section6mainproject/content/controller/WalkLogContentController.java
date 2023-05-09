package backend.section6mainproject.content.controller;

import backend.section6mainproject.content.dto.WalkLogContentDTO;
import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.content.mapper.WalkLogContentMapper;
import backend.section6mainproject.content.service.WalkLogContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/walk-logs/{walk-log-id}/contents")
@Validated
@RequiredArgsConstructor
public class WalkLogContentController {
    private static final String WALK_LOG_CONTENT_DEFAULT_URL = "/walk-logs/{walk-log-id}/contents/";
    private final WalkLogContentService walkLogContentService;
    private final WalkLogContentMapper mapper;
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> postContent(@Positive @PathVariable("walk-log-id") long walkLogId,
                                         @Valid @RequestPart WalkLogContentDTO.Post content,
                                         @RequestPart MultipartFile contentImage) {
        content.setWalkLogId(walkLogId);
        WalkLogContent walkLogContent = walkLogContentService.createWalkLogContent(mapper.walkLogContentPostDTOToWalkLogContent(content), contentImage);
        URI uri = UriComponentsBuilder.newInstance()
                .path(WALK_LOG_CONTENT_DEFAULT_URL + walkLogContent.getWalkLogContentId())
                .build()
                .toUri();
        return ResponseEntity.created(uri).build();
    }

}