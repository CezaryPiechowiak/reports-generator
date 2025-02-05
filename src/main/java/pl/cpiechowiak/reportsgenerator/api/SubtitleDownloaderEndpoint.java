package pl.cpiechowiak.reportsgenerator.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.cpiechowiak.reportsgenerator.shared.model.ReportResponse;
import pl.cpiechowiak.reportsgenerator.service.SubtitleDownloaderService;

import java.io.IOException;

@RestController
public class SubtitleDownloaderEndpoint {

    private final SubtitleDownloaderService subtitleDownloaderService;

    public SubtitleDownloaderEndpoint(SubtitleDownloaderService subtitleDownloaderService) {
        this.subtitleDownloaderService = subtitleDownloaderService;
    }

    @GetMapping("/report")
    public ReportResponse getReport(@RequestParam("videoId") String videoId) throws IOException, InterruptedException {
       return subtitleDownloaderService.getChatResponse(videoId);
    }
}