package com.crm.miniCRM.model;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class LabelPdfView extends AbstractPdfView {
    @Override
    protected void buildPdfDocument(Map<String, Object> model,
                                    Document document, PdfWriter writer,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        List<PersonAddress> personAddresses = (List<PersonAddress>) model.get("personaddress");
        Map<Long,String> personMap = (Map<Long, String>) model.get("personMap");
        Map<Long, String> addressMap = (Map<Long, String>) model.get("addressMap");


        Table table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();

        for (PersonAddress p: personAddresses) {
            table.addCell(personMap.get(p.getId().getPerson_ID()) + "\n" + addressMap.get(p.getId().getAddress_ID()));
        }

        document.add(table);
    }
}
