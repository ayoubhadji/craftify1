package utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import entity.Evenement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QRCode {
    
    public static File generateQRCode(Evenement evenement, String participantName) throws WriterException, IOException {
        // bch ne9sm l'esm ta3 l participant l nom w prénom:
        String[] nameParts = participantName.split(" ", 2);
        String firstName = nameParts[0];
        String surname = nameParts.length > 1 ? nameParts[1] : "";

        // les detail ta3 l qr code fl affichage
        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String qrContent = String.format(
            "Event: %s\n" +
            "Description: %s\n" +
            "Price: %.2f DT\n" +
            "Event Start Date: %s\n" +
            "Participant Name: %s\n" +
            "Participant Surname: %s",
            evenement.getNom(),
            evenement.getDescription(),
            evenement.getPrix(),
            evenement.getDateDebut().format(dateFormatter), firstName, surname);

        // creation ta3 l QR code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);

        // Create downloads directory if it doesn't exist
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        Path downloadsPath = Paths.get(downloadDir);
        if (!Files.exists(downloadsPath)) {
            Files.createDirectories(downloadsPath);
        }

        // Create QR code fichier li bch nsobha
        String fileName = String.format("qr_code_%s_%s.png", 
            evenement.getNom().replaceAll("[^a-zA-Z0-9]", "_"),
            participantName.replaceAll("[^a-zA-Z0-9]", "_"));
        Path filePath = downloadsPath.resolve(fileName);
        
        // Write QR code to fichier png
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);
        
        return filePath.toFile();
    }
}
