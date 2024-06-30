/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.practica2;
import static com.mycompany.practica2.EscanearDirectorioMultihilo.escanearDirectorio;
import java.util.Scanner;
/**
 *
 * @author Mauricio Prendas
 */
public class Practica2 {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java EscanearDirectorioMultihilo <ruta_directorio>");
            return;
        }

        String rutaDirectorio = args[0];

        escanearDirectorio(rutaDirectorio);
    }
}
