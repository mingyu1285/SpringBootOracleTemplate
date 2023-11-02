package kopo.poly.controller;

import kopo.poly.service.IOcrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/ocr")
@RequiredArgsConstructor
@Controller

public class OcrController {
    private final IOcrService ocrService;

    //업로드되는 파일이 저장되는 기본 폴더 설정(자바에서 경로는 /로 표현)
    final private String FILE_UPLOAD_SAVE_PATH = "c:/upload"; // C:\\upload 폴더에 저장함

    /**
     * 이미지 인식을 위한 파일 업로드 화면 호출
     */
    @GetMapping(value = "uploadImage")
    public String uploadImage(){
        log.info(this.getClass().getName()+".uploadImage!");
        return "ocr/uploadImage";
    }

}
