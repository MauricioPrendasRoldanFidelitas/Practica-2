/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica2;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

public class EscanearDirectorioMultihilo {
    private static final Logger LOGGER = Logger.getLogger(EscanearDirectorioMultihilo.class.getName());
    private static String outputFilePath;

    public static void escanearDirectorio(String rutaDirectorio) {
        File directorio = new File(rutaDirectorio);
        if (!directorio.isDirectory()) {
            LOGGER.log(Level.SEVERE, "La ruta proporcionada no es un directorio.");
            return;
        }

        File[] subdirectorios = directorio.listFiles(File::isDirectory);
        if (subdirectorios == null) {
            LOGGER.log(Level.SEVERE, "No se encontraron subdirectorios.");
            return;
        }

        for (File subdirectorio : subdirectorios) {
            String nombreSubdirectorio = subdirectorio.getName();
            if (nombreSubdirectorio.matches("^[a-zA-Z]+_\\d{4}$")) {
                Thread hilo = new Thread(() -> procesarDirectorio(subdirectorio, nombreSubdirectorio));
                hilo.start();
                try {
                    hilo.join();
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "El hilo fue interrumpido.", e);
                }
            }
        }

        System.out.println("Procesamiento completado.");
    }

    private static void procesarDirectorio(File directorio, String nombreSubdirectorio) {
        File[] archivos = directorio.listFiles();
        if (archivos == null) {
            return;
        }

        for (File archivo : archivos) {
            if (archivo.isDirectory()) {
                procesarDirectorio(archivo, nombreSubdirectorio);
            } else if (archivo.getName().endsWith(".txt")) {
                leerYEscribirContenidoArchivo(archivo, nombreSubdirectorio);
            }
        }
    }

    private static void leerYEscribirContenidoArchivo(File archivo, String nombreSubdirectorio) {
        try (Scanner lector = new Scanner(archivo); FileWriter archivoSalidaWriter = new FileWriter(outputFilePath, true)) {
            archivoSalidaWriter.write(nombreSubdirectorio + "\n");
            archivoSalidaWriter.write("# Reporte del " + obtenerFechaSinFormato() + "\n");

            while (lector.hasNextLine()) {
                String lineaDatos = lector.nextLine();
                archivoSalidaWriter.write(lineaDatos + "\n");
            }
            archivoSalidaWriter.write("\n");
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "No se encontró el archivo: " + archivo.getAbsolutePath(), e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ocurrió un error al escribir en el archivo de salida.", e);
        }
    }

    private static String obtenerFechaSinFormato() {
        // Obtiene la fecha actual como cadena de caracteres sin formato
        long millis = System.currentTimeMillis();
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;
        long day = (millis / (1000 * 60 * 60 * 24)) % 30; // Aproximado
        long month = (millis / (1000 * 60 * 60 * 24 * 30)) % 12; // Aproximado
        long year = 1970 + (millis / (1000 * 60 * 60 * 24 * 365));

        return String.format("%02d/%02d/%04d", day, month + 1, year);
    }

    
}
