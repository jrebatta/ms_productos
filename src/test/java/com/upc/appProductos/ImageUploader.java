package com.upc.appProductos;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImageUploader {

    // Configura tu URL de conexión, usuario y contraseña
    private static final String DB_URL = "jdbc:postgresql://pg-186ceb7-test-proyect.k.aivencloud.com:28011/defaultdb";
    private static final String DB_USER = "avnadmin";
    private static final String DB_PASSWORD = "";

    // Método para abrir el selector de archivos y obtener el archivo seleccionado
    private static File selectImageFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png"));
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    // Método para insertar una imagen en la base de datos
    public static void insertImage(File imageFile) {
        if (imageFile == null) {
            System.out.println("No se seleccionó ningún archivo.");
            return;
        }

        String sql = "INSERT INTO images (image_name, image_data) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             FileInputStream fis = new FileInputStream(imageFile)) {

            // Establecer parámetros
            pstmt.setString(1, imageFile.getName()); // Nombre del archivo
            pstmt.setBinaryStream(2, fis, (int) imageFile.length()); // Datos binarios de la imagen

            // Ejecutar la inserción
            pstmt.executeUpdate();
            System.out.println("Imagen insertada exitosamente.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.err.println("Error al insertar la imagen.");
        }
    }

    // Ejemplo de uso
    public static void main(String[] args) {
        File imageFile = selectImageFile(); // Abre el selector de archivos
        insertImage(imageFile); // Inserta la imagen seleccionada en la base de datos
    }
}
