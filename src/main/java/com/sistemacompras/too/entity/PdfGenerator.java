package com.sistemacompras.too.entity;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.sistemacompras.too.service.DetalleOrdenDeCompraService;
import org.springframework.beans.factory.annotation.Autowired;

public class PdfGenerator {
    private OrdenDeCompra ordenDeCompra;
    @Autowired
    private DetalleOrdenDeCompraService detalleOrdenDeCompraService;

    public PdfGenerator(OrdenDeCompra ordenDeCompra) {
        this.ordenDeCompra = ordenDeCompra;
    }


    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Producto", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Cantidad", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Precio", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("SubTotal", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table, List<DetalleOrdenDeCompra> detalleOrdenDeCompras) {
        for (int i = 0; i < detalleOrdenDeCompras.size(); i++) {
            table.addCell(String.valueOf(detalleOrdenDeCompras.get(i).getIdProductoProveedor().getNombreProductoProveedor()));
            table.addCell(detalleOrdenDeCompras.get(i).getCantidad().toString());
            table.addCell(detalleOrdenDeCompras.get(i).getPrecio().toString());
            table.addCell(String.valueOf(detalleOrdenDeCompras.get(i).getPrecio() * detalleOrdenDeCompras.get(i).getCantidad()));
        }
    }

    public void export(HttpServletResponse response, OrdenDeCompra ordenDeCompra) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("Orden de compra", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
        table.setSpacingBefore(10);

        //Obteniendo los detalles de la orden de compra
        List<DetalleOrdenDeCompra> detalleOrdenDeCompras = detalleOrdenDeCompraService.listAllbyIdOrderCompra(ordenDeCompra.getIdOrdenDeCompra());
        writeTableHeader(table);
        writeTableData(table, detalleOrdenDeCompras);
        document.add(table);
        document.close();

    }
}
