/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica2;
import java.io.File;//biblioteca para los archivos
import java.io.FileWriter; //biblioteca para escribir en los archivos
import java.io.IOException; //excepcion a la hora de leer archivos
import java.io.FileNotFoundException; //exccepcion de archivo no encontrado
import java.util.Scanner; //escaner para escribir en los archivos y demas
import java.util.logging.Logger; //loggers para escribir errores
import java.util.logging.Level;

public class EscanearDirectorioMultihilo {
    private static final Logger LOGGER = Logger.getLogger(EscanearDirectorioMultihilo.class.getName());
    private static final String OUTPUT_FILE = "reporte_hallazgos.txt";

    public static void listFilesForFolder(final File folder) {//metodo recursivo que lista los archivos dentro de uno o varios directorios
        for (final File fileEntry : folder.listFiles()) {
            System.out.println(fileEntry.getName());
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            }
        }
    }

    public static void escanearDirectorio(String rutaDirectorio) { //metodo que escanea el directorio especificado
        File directorio = new File(rutaDirectorio);
        System.out.println("Directorio: " + directorio.getAbsolutePath());
        listFilesForFolder(directorio);
        if (!directorio.isDirectory()) { //si la ruta no es un directorio se muestra el mensaje
            LOGGER.log(Level.SEVERE, "La ruta proporcionada no es un directorio.");
            return;
        }

        File[] subdirectorios = directorio.listFiles(File::isDirectory);
        if (subdirectorios == null) { //si no existen subdirectorios se muestra el mensaje
            LOGGER.log(Level.SEVERE, "No se encontraron subdirectorios.");
            return;
        }

        try (FileWriter archivoSalidaWriter = new FileWriter(OUTPUT_FILE)) { //este metodo escribe el archivo de salida
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
        StringBuilder reporte = new StringBuilder(); //se procesan los archivos de los directorios

        File[] archivos = directorio.listFiles();
        if (archivos == null) {
            return reporte;
        }

        for (File archivo : archivos) {
            if (archivo.isDirectory()) { //recursivamente se procesan los archivos del subdirectorio
                reporte.append(procesarDirectorio(archivo));
            } else if (archivo.getName().endsWith(".txt")) {
                leerYProcesarArchivo(archivo, reporte); //se leen los archivos .txt
            }
        }

        return reporte;
    }

 private static void leerYProcesarArchivo(File archivo, StringBuilder reporte) {
        try (Scanner lector = new Scanner(archivo)) { //este metodo construye el reporte_hallazgos.txt
            boolean encabezadoEscrito = false;
            while (lector.hasNextLine()) {
                String linea = lector.nextLine();
                if (!encabezadoEscrito && linea.startsWith("# Reporte del ")) {
                    reporte.append(linea).append("\n");
                    encabezadoEscrito = true;
                } else if (linea.startsWith("- ")) {
                    reporte.append(linea).append("\n");
                }
            }

            if (encabezadoEscrito) {
                reporte.append("\n");
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "No se encontró el archivo: " + archivo.getAbsolutePath(), e);
        }
    }


}
