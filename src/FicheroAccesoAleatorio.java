
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author antoniocruz
 */
public class FicheroAccesoAleatorio {

    private File f;
    private HashMap<String, Integer> campos;
    private long longitudRegistro;
    private long numReg = 0;

    FicheroAccesoAleatorio(String nomFich, HashMap<String, Integer> campos) throws IOException {
        this.campos = campos;
        this.f = new File(nomFich);
        longitudRegistro = 0;
        for (Map.Entry<String, Integer> entry : campos.entrySet()) {
            longitudRegistro = entry.getValue();
            if (f.exists()) {
                this.numReg = f.length() / this.longitudRegistro;
            }
        }
    }

    public long getNumReg() {
        return numReg;
    }

    public void insertar(Map<String, String> reg) throws IOException {
        insertar(reg, this.numReg++);
    }

    public void insertar(Map<String, String> reg, long pos) throws IOException {
        try (RandomAccessFile faa = new RandomAccessFile(f, "rws")) {
            faa.seek(pos * this.longitudRegistro);
            for (Map.Entry<String, Integer> campo : campos.entrySet()){
                String nomCampo = campo.getKey();
                Integer longCampo = campo.getValue();
                String valorCampo = reg.get(nomCampo);
                if (valorCampo == null) {
                    valorCampo
                            = "";
                }
                String valorCampoForm = String.format("%1$-" + longCampo + "s", valorCampo);
                faa.write(valorCampoForm.getBytes("UTF-8"), 0, longCampo);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HashMap<String, Integer> campos = new HashMap<>();
        campos.put("DNI", 9);
        campos.put("NOMBRE", 32);
        campos.put("CP", 5);
        try {
            FicheroAccesoAleatorio faa = new FicheroAccesoAleatorio("fic_acceso_aleat.dat", campos);
            Map reg = new HashMap();
            reg.put("DNI", "56789012B");
            reg.put("NOMBRE", "SAMPER");
            reg.put("CP", "29730");
            faa.insertar(reg);
            reg.clear();
            reg.put("DNI", "89012345E");
            reg.put("NOMBRE", "ROJAS");
            faa.insertar(reg);
            reg.clear();
            reg.put("DNI", "23456789D");
            reg.put("NOMBRE", "DORCE");
            reg.put("CP", "13700");
            faa.insertar(reg);
            reg.clear();
            reg.put("DNI", "78901234X");
            reg.put("NOMBRE", "NADALES");
            reg.put("CP", "44126");
            faa.insertar(reg, 1);
        } catch (IOException e) {
            System.err.println("Error de E / S: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}