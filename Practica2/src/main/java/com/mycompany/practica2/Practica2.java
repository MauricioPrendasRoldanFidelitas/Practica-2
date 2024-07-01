/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.practica2;
import static com.mycompany.practica2.EscanearDirectorioMultihilo.escanearDirectorio;
import java.io.File;
/**
 *
 * @author Mauricio Prendas
 */
public class Practica2 {

   public static void main(String[] args) {
        String rutaDirectorio = System.getProperty("user.dir") + File.separator + "bitacora";

        escanearDirectorio(rutaDirectorio);
    }
}
