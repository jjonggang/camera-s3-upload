package com.example.aicctvstream.controller.cctv;


import com.example.aicctvstream.dto.file.ImageFileResponseDto;
import com.example.aicctvstream.security.TokenProvider;
import com.example.aicctvstream.service.aws.AwsS3Service;
import com.example.aicctvstream.webSocket.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.TextMessage;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1")
public class CctvController {

    private final TokenProvider tokenProvider;
    private final AwsS3Service awsS3Service;
    private final WebSocketHandler webSocketHandler;

    @GetMapping("/auth/login")
    public String login(@RequestParam("password") String password) throws Exception{
        try{
            final String token = tokenProvider.create(password);
            return token;
        }catch (Exception e){
            return e.getMessage();
        }
    }

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
