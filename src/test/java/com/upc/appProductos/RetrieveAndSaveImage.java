package com.upc.appProductos;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RetrieveAndSaveImage {

    private static final String DB_URL = "jdbc:postgresql://pg-186ceb7-test-proyect.k.aivencloud.com:28011/defaultdb";
    private static final String DB_USER = "avnadmin";
    private static final String DB_PASSWORD = "";
    private static final String OUTPUT_DIR = "imagenes"; // Carpeta donde se guardarán las imágenes

    public static void main(String[] args) throws IOException {

        //Notar que readLine() nos obliga a declarar IOException
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //Ya tenemos el "lector"

        System.out.println("Por favor ingrese el codigo de la imagen");//Se pide un dato al usuario

        String entrada = br.readLine();

        // El ID o nombre del archivo que deseas recuperar
        int imageId = Integer.parseInt(entrada); // Cambia esto al ID de la imagen que deseas recuperar

        String outputFilePath = OUTPUT_DIR + File.separator + "imagen_recuperada.jpg";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT image_data FROM images WHERE id = ?")) {

            // Establecer el ID de la imagen que deseas recuperar
            pstmt.setInt(1, imageId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                byte[] imgBytes = rs.getBytes("image_data");

                // Crear la carpeta si no existe
                File outputDir = new File(OUTPUT_DIR);
                if (!outputDir.exists()) {
                    outputDir.mkdirs();
                }

                // Guardar la imagen en la carpeta del proyecto
                try (OutputStream os = new FileOutputStream(outputFilePath)) {
                    os.write(imgBytes);
                    System.out.println("Imagen guardada en: " + outputFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Error al guardar la imagen.");
                }
            } else {
                System.out.println("No se encontró la imagen con el ID: " + imageId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al recuperar la imagen.");
        }
    }
}
