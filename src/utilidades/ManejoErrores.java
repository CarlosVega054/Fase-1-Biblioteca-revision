package utilidades;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ManejoErrores {
    // Guardar errores 
    public static void guardarError(String mensaje) {
        
        System.err.println("ERROR: " + mensaje);

        System.err.println("ERROR: " + mensaje); 
        
        try (FileWriter fw = new FileWriter("errores.txt", true);
             PrintWriter pw = new PrintWriter(fw)) {
             
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fecha = sdf.format(new Date());
            
            pw.println("[" + fecha + "] " + mensaje);
            
        } catch (IOException e) {
            System.out.println("No se pudo guardar el error en el archivo txt.");
        }
    }
}
