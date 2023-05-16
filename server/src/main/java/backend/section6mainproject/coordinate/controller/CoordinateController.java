package backend.section6mainproject.coordinate.controller;

import backend.section6mainproject.coordinate.dto.CoordinateControllerDTO;
import backend.section6mainproject.coordinate.dto.CoordinateServiceDTO;
import backend.section6mainproject.coordinate.mapper.CoordinateMapper;
import backend.section6mainproject.coordinate.service.CoordinateService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class CoordinateController {
    private final CoordinateService coordinateService;
    private final SimpMessagingTemplate messagingTemplate;
    private final CoordinateMapper mapper;
    @MessageMapping("/walk-logs")
    public void publishCoordinate(@Valid @Payload CoordinateControllerDTO.Pub pub) {
        long walkLogId = Long.parseLong(SimpAttributesContextHolder.getAttributes().getAttribute("walkLogId").toString());
        CoordinateServiceDTO.Input input = mapper.controllerPubDTOTOServiceInputDTO(pub);
        input.setWalkLogId(walkLogId);
        CoordinateServiceDTO.Output coordinate = coordinateService.createCoordinate(input);
        messagingTemplate.convertAndSend("/sub/" + coordinate.getWalkLogId(), mapper.serviceOutputDTOToControllerSubDTO(coordinate));
    }

}
