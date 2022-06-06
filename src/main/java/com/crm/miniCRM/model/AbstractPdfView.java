package com.crm.miniCRM.model;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Map;

public abstract class AbstractPdfView extends AbstractView {

    public AbstractPdfView(){
        initView();
    }

    private void initView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model,
                                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        ByteArrayOutputStream baos = createTemporaryOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        buildPdfDocument(model, document, writer, request, response);
        document.close();
        writeToResponse(response, baos);
    }
    protected abstract void buildPdfDocument(Map<String, Object> model,
                                             Document document, PdfWriter writer, HttpServletRequest request,
                                             HttpServletResponse response) throws Exception;
}
