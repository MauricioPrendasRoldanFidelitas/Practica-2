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
    private static final String OUTPUT_FILE = "reporte_hallazgos.txt";

    public static void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            System.out.println(fileEntry.getName());
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            }
        }
    }

    public static void escanearDirectorio(String rutaDirectorio) {
        File directorio = new File(rutaDirectorio);
        System.out.println("Directorio: " + directorio.getAbsolutePath());
        listFilesForFolder(directorio);
        if (!directorio.isDirectory()) {
            LOGGER.log(Level.SEVERE, "La ruta proporcionada no es un directorio.");
            return;
        }

        File[] subdirectorios = directorio.listFiles(File::isDirectory);
        if (subdirectorios == null) {
            LOGGER.log(Level.SEVERE, "No se encontraron subdirectorios.");
            return;
        }

        try (FileWriter archivoSalidaWriter = new FileWriter(OUTPUT_FILE)) {
            for (File subdirectorio : subdirectorios) {
                String nombreSubdirectorio = subdirectorio.getName();
                if (nombreSubdirectorio.matches("^[a-zA-Z]+_\\d{4}$")) {
                    StringBuilder reporte = procesarDirectorio(subdirectorio);
                    if (reporte.length() > 0) {
                        archivoSalidaWriter.write(nombreSubdirectorio + "\n");
                        archivoSalidaWriter.write(reporte.toString());
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ocurrió un error al escribir en el archivo de salida.", e);
        }

        System.out.println("Procesamiento completado.");
    }

    private static StringBuilder procesarDirectorio(File directorio) {
        StringBuilder reporte = new StringBuilder();

        File[] archivos = directorio.listFiles();
        if (archivos == null) {
            return reporte;
        }

        for (File archivo : archivos) {
            if (archivo.isDirectory()) {
                reporte.append(procesarDirectorio(archivo));
            } else if (archivo.getName().endsWith(".txt")) {
                leerYProcesarArchivo(archivo, reporte);
            }
        }

        return reporte;
    }

    private static void leerYProcesarArchivo(File archivo, StringBuilder reporte) {
        try (Scanner lector = new Scanner(archivo)) {
            if (lector.hasNextLine()) {
                String fecha = lector.nextLine();
                if (fecha.startsWith("# Reporte del ")) {
                    reporte.append(fecha).append("\n");

                    while (lector.hasNextLine()) {
                        String linea = lector.nextLine();
                        if (linea.startsWith("- ")) {
                            reporte.append(linea).append("\n");
                        }
                    }

                    reporte.append("\n");
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "No se encontró el archivo: " + archivo.getAbsolutePath(), e);
        }
    }


}
