import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {


        /*
        Primer paso leer archivo config y guarda en un mapa
        */
        HashMap<String, Integer> config = new HashMap<>();

        Scanner sc = new Scanner(new File("config.txt"));

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] partes = line.split("=");
            config.put(partes[0], Integer.parseInt(partes[1]));
        }
        sc.close(); 

        /*
        ******************************************************************
        */

        /*
        Sacar parametros del mapa en variables
        */
        int ni = config.get("ni");
        int baseEventos = config.get("baseEventos");
        int nc = config.get("nc");
        int ns = config.get("ns");
        int tam1 = config.get("tam1");
        int tam2 = config.get("tam2");


        /*
        ******************************************************************
        */

        /*
        eventos totales
        */
        int totalEventosEsperados = 0;
        for (int i = 1; i <= ni; i++) {
            totalEventosEsperados += baseEventos * i;
        }


        /*
        ******************************************************************
        */

        /*C
        rear buzones recordar! -1 es para capacidad ilimitada los tam2 son para servidores
        y cada uno tiene uno propio 
        */
        Buzon buzonEntrada = new Buzon(-1);       
        Buzon buzonAlertas = new Buzon(-1);       
        Buzon buzonClasificacion = new Buzon(tam1);

        Buzon[] buzonesServidores = new Buzon[ns];
        for (int i = 0; i < ns; i++) {
            buzonesServidores[i] = new Buzon(tam2);
        }


        /*
        ******************************************************************
        */

        /*
        Crear monitor de los clasificadores
        */
        MonitorClasificadores controlClasificadores = new MonitorClasificadores(nc);


        /*
        ******************************************************************
        */

        /*
        Crear sensores
        */
        SensoresIoT[] sensores = new SensoresIoT[ni];
        for (int i = 0; i < ni; i++) {
            sensores[i] = new SensoresIoT(i + 1, baseEventos, ns, buzonEntrada);
        }

         
        /*
        ******************************************************************
        */

        /*
        Crear broker
        */
        BrokerYAnalizador broker = new BrokerYAnalizador(
                buzonEntrada,
                buzonAlertas,
                buzonClasificacion,
                totalEventosEsperados
        );


         /*
        ******************************************************************
        */


        /*
        Crear administrador
        */
        Administrador administrador = new Administrador(
                buzonAlertas,
                buzonClasificacion,
                nc
        );

         /*
        ******************************************************************
        */

        /*
        Crear Clasificadores
        */
        Clasificador[] clasificadores = new Clasificador[nc];
        for (int i = 0; i < nc; i++) {
            clasificadores[i] = new Clasificador(
                    i + 1,
                    buzonClasificacion,
                    buzonesServidores,
                    controlClasificadores
            );
        }

        /*
        ******************************************************************
        */

        /* 
        Crear servidores 
        */ 
        ServidoresDeDespliegue[] servidores = new ServidoresDeDespliegue[ns];
        for (int i = 0; i < ns; i++) {
            servidores[i] = new ServidoresDeDespliegue(i + 1, buzonesServidores[i]);
        }


        /*
        ******************************************************************
        */

        /*
        start los threads
        */
        for (int i = 0; i < ni; i++) {
            sensores[i].start();
        }

        broker.start();
        administrador.start();

        for (int i = 0; i < nc; i++) {
            clasificadores[i].start();
        }

        for (int i = 0; i < ns; i++) {
            servidores[i].start();
        }

        /*
        ******************************************************************
        */

        /*
        Join de todos los thread
        */
        try {
            for (int i = 0; i < ni; i++) {
                sensores[i].join();
            }

            broker.join();
            administrador.join();

            for (int i = 0; i < nc; i++) {
                clasificadores[i].join();
            }

            for (int i = 0; i < ns; i++) {
                servidores[i].join();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("El hilo principal fue interrumpido.");
        }

        System.out.println("Sistema IoT finalizado correctamente.");

    }
}


