package pl.cpiechowiak.reportsgenerator.service;

import com.itextpdf.text.*;
import org.springframework.stereotype.Service;
import pl.cpiechowiak.reportsgenerator.shared.model.ReportResponse;

@Service
public class PdfReportService {

    public void addSummary(Document document, ReportResponse reportResponse) throws DocumentException {
        Paragraph header = new Paragraph();
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLDITALIC);
        header.add(new Paragraph("Podsumowanie materiału:", font));
        header.setAlignment(Element.ALIGN_TOP);
        document.add(header);
        document.add(new Paragraph(reportResponse.summary()));
    }

    public void addHomework(Document document, ReportResponse reportResponse) throws DocumentException {
        Paragraph header = new Paragraph();
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLDITALIC);
        header.add(new Paragraph("Praca domowa do wykonania::", font));
        header.setAlignment(Element.ALIGN_TOP);
        document.add(header);
        document.add(new Paragraph(reportResponse.homework()));
    }
}