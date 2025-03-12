package test;

import java.net.URL;

public class trst2 {
    public static void main(String[] args) {
        URL url = trst2.class.getClassLoader().getResource("Employe.hbm.xml");
        if (url == null) {
            System.out.println("❌ Employe.hbm.xml introuvable !");
        } else {
            System.out.println("✅ Fichier trouvé à : " + url);
        }
    }
}
