package utez.edu.mx.unidad3.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class ClaveGenerator {
    public static String generateClave(Long id){
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy", new Locale("es", "MX"));
        String fecha = sdf.format(System.currentTimeMillis());
        String random = String.format("%04d", ThreadLocalRandom.current().nextInt(1,10000));

        return "C" + id + "-" + fecha + "-" + random;
    }

    public static void main (String[] args) {
        System.out.println(generateClave(1L));
    }

    public static String generateWarehouseClave(String cedeClave, Long idWarehouse){
        return null;
    }
}
