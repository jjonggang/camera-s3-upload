package com.example.aicctvstream.controller.cctv;


import com.example.aicctvstream.dto.file.ImageFileResponseDto;
import com.example.aicctvstream.service.aws.AwsS3Service;
import com.example.aicctvstream.webSocket.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.TextMessage;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1")
public class CctvController {

    private final AwsS3Service awsS3Service;
    private final WebSocketHandler webSocketHandler;

    @PostMapping("/file/stream")
    public String streamUpload(@RequestParam("file") MultipartFile file) throws Exception{
        ImageFileResponseDto imageFileResponseDto = awsS3Service.upload2(file, "stream");
        webSocketHandler.sendMessageToAll(new TextMessage(imageFileResponseDto.getFileUrl()));
        TimeUnit.MILLISECONDS.sleep(100);
//        awsS3Service.deleteFile(imageFileResponseDto.getFileName());
        log.info("삭제완료");
        return imageFileResponseDto.getFileUrl();
    }

}
