package Logger;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Constants.Constants.INVARIANTES;

public class Logger {
    private PrintWriter pw;
    private final String file_path;
    public Logger(String file_path) {
        this.file_path = file_path;
        try {
            FileWriter file = new FileWriter(file_path);
            pw = new PrintWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * añade transicion disparada a log.
     * @param s . string de transicion a añadir.
     */
    public void log(String s){
        pw.printf(s);
        pw.flush();
    }

    public void logn(String s){
        pw.printf(s);
        pw.println();
        pw.flush();
    }

    /**
     * Recorre el log en busca de las transiciones que forman los T-invariantes, y verifica si efectivamente el log
     * se queda sin transiciones o no.
     * @param regex . Expresion regular utilizada
     * @param replace . Grupos a reemplazar
     * @return true , si no quedo ningun caracter en el log ,false en caso contrario.
     */
    public boolean validateLog(String regex,String replace){
        String log;
        try {
            log = getLog();
        } catch (FileNotFoundException e) {
            return false;
        }

        Pattern pattern = Pattern.compile(regex);   // creo el objeto de la regex
        Matcher matcher = pattern.matcher(log);     // busco los patrones en el log
        String out = matcher.replaceAll(replace);   // reemplazo los grupos
        String prev = "";
        while (true){
            // reemplazo y busco con la regex hasta que no se pueda mas
            matcher = pattern.matcher(out);
            out = matcher.replaceAll(replace);
            // si la salida es igual a la anterior ==> no puedo reemplazar mas y corto el bucle
            if (Objects.equals(out,prev))
                break;
            prev = out;
        }

        // verifico si sobraron transiciones
        if (!Objects.equals(out, "")){
            System.out.println("LOG VALIDATION FAILED");
            System.out.println(out);
            return false;
        }

        // No sobro ninguna transicion
        System.out.println("LOG VALIDATION SUCCESS");
        return true;
    }

    public int[] contarInvariantes(String regex, String replace){
        String log;
        try {
            log = getLog();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No se pudo leer el log");
        }

        int[] cuentas = new int[INVARIANTES.length];
        Arrays.fill(cuentas, 0);

        Pattern pattern = Pattern.compile(regex);   // creo el objeto de la regex
        Matcher matcher = pattern.matcher(log);     // busco los patrones en el log

        while (true){
            // reemplazo y busco con la regex hasta que no se pueda mas

            String out = matcher.replaceAll(replace);   // reemplazo los grupos

            String resto = procesar_resultado(out, cuentas);

            // si la salida es igual a la anterior ==> no puedo reemplazar mas y corto el bucle
            if (Objects.equals(out,resto))
                break;

            matcher = pattern.matcher(resto);
        }

        return cuentas;
    }

    private String procesar_resultado(String result, int[] cuentas){
        String resto = "";
        String[] result_split = result.split("-");

        for (String inv : result_split) {

            // verifico si es un invariante y aumento la cuenta
            boolean isInvariante = false;

            for (int i = 0; i<cuentas.length; i++){
                if (!inv.equals(INVARIANTES[i]))
                    continue;

                cuentas[i]++;
                isInvariante = true;
                break;
            }

            // si no es un invariante concateno al resto
            if (!isInvariante)
                resto += inv;
        }

        return resto;
    }

    public String getLog() throws FileNotFoundException {
        File file = new File(file_path);
        String log = "";

        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            log += scanner.nextLine();
        }

        return log;
    }

}
