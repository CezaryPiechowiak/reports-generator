package pl.cpiechowiak.reportsgenerator.api;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.cpiechowiak.reportsgenerator.service.PdfReportService;
import pl.cpiechowiak.reportsgenerator.shared.model.ReportResponse;
import pl.cpiechowiak.reportsgenerator.service.SubtitleDownloaderService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
public class SubtitleDownloaderEndpoint {

    private final SubtitleDownloaderService subtitleDownloaderService;
    private final PdfReportService pdfReportService;

    public SubtitleDownloaderEndpoint(SubtitleDownloaderService subtitleDownloaderService, PdfReportService pdfReportService) {
        this.subtitleDownloaderService = subtitleDownloaderService;
        this.pdfReportService = pdfReportService;
    }

    @GetMapping("/report")
    public ReportResponse getReport(@RequestParam("videoId") String videoId) throws IOException, InterruptedException {
       return subtitleDownloaderService.getChatResponse(videoId);
    }

    @GetMapping("/report/generate")
    public ResponseEntity<byte[]> getReportAsPdf(@RequestParam("videoId") String videoId) throws DocumentException, IOException, InterruptedException {
        ReportResponse reportResponse = subtitleDownloaderService.getChatResponse(videoId);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        document.open();
        pdfReportService.addSummary(document, reportResponse);
        pdfReportService.addHomework(document, reportResponse);
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "report.pdf");

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }
}