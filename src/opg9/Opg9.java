package opg9;

import java.io.*;
import java.util.*;

import static java.lang.System.currentTimeMillis;

public class Opg9 {
    public static void main(String[] args) throws IOException {
        Graf graf = new Graf();

        String grafNavn = "src/opg9/Norden";
        String nodeFil = "src/opg9/noder.txt";
        String kantFil = "src/opg9/kanter.txt";

        System.out.println("Leser inn kartdata...");
        graf.nyGraf(grafNavn, nodeFil, kantFil);
        graf.lesInteressepunkter();


        int[] landemerker = {3643361, 2413034, 3109952, 310877, 3746969};

        // Eventuell preprosessering av data. Må gjøres før ALT-algoritmen benyttes.
        //System.out.println("Preprosesserer...");
        //graf.preprosesser(landemerker);

        System.out.println("Leser inn preprosessert data...");
        graf.lesTilOgFraLandemerker();


        boolean kjoerer = true;
        while (kjoerer) {

            int valg = 0;
            System.out.println("\n***** Navigasjonsprogram *****\n");
            System.out.println("1. Finn rute med Dijkstras algoritme");
            System.out.println("2. Finn rute med ALT-algoritmen");
            System.out.println("3. Finn interessepunkter");
            System.out.println("4. Avslutt");
            System.out.println("\nVennligst velg et tall mellom 1 og 4.");
            Scanner input = new Scanner(System.in);

            if (input.hasNextInt()) {
                valg = input.nextInt();
            } else {
                System.out.println("Du må skrive inn et tall");
            }

            input = new Scanner(System.in);

            switch (valg) {
                case 1 -> {

                    System.out.println("\nStartnode:");
                    String stringInput = input.nextLine();
                    int startNode = Integer.parseInt(stringInput);

                    System.out.println("\nMålnode:");
                    stringInput = input.nextLine();
                    int maalNode = Integer.parseInt(stringInput);

                    System.out.println("\nKjører Dijkstras algoritme fra " + graf.noder[startNode].getNavn() + " til " + graf.noder[maalNode].getNavn() + "...\n");

                    long startTid = currentTimeMillis();
                    graf.dijkstra(graf.noder[startNode], graf.noder[maalNode]);
                    long sluttTid = currentTimeMillis();

                    System.out.println("Reisetid: " + centiSekOmregning(graf.noder[maalNode].distFraStart));
                    System.out.println("Tidsbruk for algoritmen: " + (sluttTid - startTid) + " ms");

                    // Eventuell opptegning av rute:
                    //graf.tegnRute(graf.noder[maalNode], 0);
                    //System.out.println("Kjørerute lagret i \"rute.txt\"");

                    System.out.println("\nTrykk enter for å fortsette");
                    input.nextLine();
                }
                case 2 -> {

                    System.out.println("\nStartnode:");
                    String stringInput = input.nextLine();
                    int startNode = Integer.parseInt(stringInput);

                    System.out.println("\nMålnode:");
                    stringInput = input.nextLine();
                    int maalNode = Integer.parseInt(stringInput);


                    System.out.println("\nKjører ALT-algoritmen fra " + graf.noder[startNode].getNavn() + " til " + graf.noder[maalNode].getNavn() + "...\n");
                    long startTid = currentTimeMillis();
                    graf.alt(graf.noder[startNode], graf.noder[maalNode]);
                    long sluttTid = currentTimeMillis();

                    System.out.println("Reisetid: " + centiSekOmregning(graf.noder[maalNode].distFraStart));
                    System.out.println("Tidsbruk for algoritmen: " + (sluttTid - startTid) + " ms");

                    // Eventuell opptegning av rute:
                    //graf.tegnRute(graf.noder[maalNode], 0);
                    //System.out.println("Kjørerute lagret i \"rute.txt\"");

                    System.out.println("\nTrykk enter for å fortsette");
                    input.nextLine();

                }
                case 3 -> {

                    System.out.println("\nAngi node som utgangspunkt:");
                    String stringInput = input.nextLine();
                    int node = Integer.parseInt(stringInput);

                    System.out.println("\nAngi kode for interessepunktet du vil finne:");
                    System.out.println("""
                            1 Stedsnavn
                            2 Bensinstasjon
                            4 Ladestasjon
                            8 Spisested
                            16 Drikkested
                            32 Overnattingssted""");
                    stringInput = input.nextLine();
                    int kode = Integer.parseInt(stringInput);

                    System.out.println("\nAngi antall interessepunkter du vil finne:");
                    stringInput = input.nextLine();
                    int antall = Integer.parseInt(stringInput);

                    System.out.println("\nFinner de " + antall + " nærmeste interessepunktene til " + graf.noder[node].getNavn());
                    System.out.println("\nKoordinater til stedene: ");
                    graf.finnInteressepunkter(graf.noder[node], kode, antall);

                    System.out.println("\nTrykk enter for å gå videre");
                    input.nextLine();
                }
                case 4 -> kjoerer = false;
                default -> System.exit(0);
            }
        }
    }


    private static String centiSekOmregning(int hundredeler) {

        int sekunder = hundredeler/100;

        int s = sekunder % 60;
        int h = sekunder / 60;
        int m = h % 60;
        h = h / 60;

        return h + " timer, " + m + " minutter og " + s + " sekunder";
    }
}

class Node {
    Kant kant1; // Nodens første kant
    int nodeNr;
    String navn; // Nodens navn hvis den er et interessepunkt
    int interessepunktKode; // Koden som brukes for å identifisere typen interessepunkt
    double breddegrad;
    double lengdegrad;
    boolean funnet;
    int distFraStart; // Brukes av Dijkstra og ALT
    int distTilMaal; // Brukes av ALT
    Node forgjenger; // Nodens forgjenger under korteste-vei-søk

    public Node(int nodeNr) {
        this.nodeNr = nodeNr;
        this.funnet = false;
    }

    public String getNavn() {
        if (navn != null) return navn;
        else return "Node " + nodeNr;
    }

    public int getSumAvAvstander() {
        return distFraStart + distTilMaal;
    }

    @Override
    public String toString() {
        return "[Node " + nodeNr + "]";
    }
}

class Kant {
    Kant neste;
    Node til;
    public Kant(Node n, Kant nst) {
        til = n;
        neste = nst;
    }
}

class Vkant extends Kant {
    int vekt;
    public Vkant(Node n, Vkant nst, int vkt) {
        super(n, nst);
        vekt = vkt;
    }
}
class Graf {
    int N; // Antall noder
    int K; // Antall kanter
    Node[] noder; // List med alle nodene i grafen
    String grafNavn;
    int antallLandemerker;
    int[][] fraLandemerker; // Tabell med avstander fra alle landemerkene
    int[][] tilLandemerker; // Tabell med avstander til alle landemerkene

    public void nyGraf(String grafNavn, String nodeFil, String kantFil) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(nodeFil))) {

            StringTokenizer st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            noder = new Node[N];

            for (int i = 0; i < N; ++i) {
                st = new StringTokenizer(br.readLine());
                noder[i] = new Node(i);
                noder[i].nodeNr = Integer.parseInt(st.nextToken());
                noder[i].breddegrad = Double.parseDouble(st.nextToken());
                noder[i].lengdegrad = Double.parseDouble(st.nextToken());
            }
        } catch (IOException e) {
            throw new IOException(e);
        }



        try (BufferedReader br = new BufferedReader(new FileReader(kantFil))) {

            StringTokenizer st = new StringTokenizer(br.readLine());

            K = Integer.parseInt(st.nextToken());

            for (int i = 0; i < K; ++i) {
                st = new StringTokenizer(br.readLine());
                int fra = Integer.parseInt(st.nextToken());
                int til = Integer.parseInt(st.nextToken());
                int vekt = Integer.parseInt(st.nextToken());
                noder[fra].kant1 = new Vkant(noder[til], (Vkant) noder[fra].kant1, vekt);
            }
        } catch (IOException e) {
            throw new IOException(e);
        }


        this.grafNavn = grafNavn;
    }

    public void initforgj(Node s) {
        for (int i = N; i-- > 0;) {
            noder[i].distFraStart = 1000000000;
            noder[i].forgjenger = null;
            noder[i].funnet = false;
        }
        s.distFraStart = 0;
    }

    void forkort(Node n, Vkant k, PriorityQueue<Node> pri) {
        Node n1 = k.til;
        if (n1.distFraStart > n.distFraStart + k.vekt) {
            n1.distFraStart = n.distFraStart + k.vekt;
            n1.forgjenger = n;

            pri.remove(k.til);
            pri.add(k.til);
        }
    }

    void dijkstra(Node start, Node maal) {
        int teller = 0;
        initforgj(start);
        PriorityQueue<Node> pri = new PriorityQueue<>(new DijkstraComparator());
        pri.add(start);
        start.funnet = true;
        while (!pri.isEmpty()) {
            Node n = pri.poll();
            for (Vkant k = (Vkant) n.kant1; k != null; k = (Vkant) k.neste) {
                if (!k.til.funnet) {
                    pri.add(k.til);
                    k.til.funnet = true;
                }
                forkort(n, k, pri);
            }
            if (n.equals(maal)) break;
            teller++;
        }
        System.out.println("Antall noder behandlet: " + teller);
    }

    void alt(Node start, Node maal) {
        int teller = 0;
        initforgj(start);
        PriorityQueue<Node> pri = new PriorityQueue<>(new ALTComparator());
        pri.add(start);
        start.funnet = true;
        while (!pri.isEmpty()) {
            Node n = pri.poll();
            for (Vkant k = (Vkant) n.kant1; k != null; k = (Vkant) k.neste) {
                if (!k.til.funnet) {
                    k.til.distTilMaal = estimerAvstandTilMaal(k.til, maal);
                    pri.add(k.til);
                    k.til.funnet = true;
                }
                forkort(n, k, pri);
            }
            if (n.equals(maal)) break;
            teller++;
        }
        System.out.println("Antall noder behandlet: " + teller);
    }

    int estimerAvstandTilMaal(Node n, Node maal) {

        int nodeIndeks = n.nodeNr;
        int maalIndeks = maal.nodeNr;

        int estimat = 0;

        for (int i = 0; i < antallLandemerker; i++) {

            int estimat1 = fraLandemerker[i][maalIndeks] - fraLandemerker[i][nodeIndeks];

            if (estimat1 < 0) estimat1 = 0;

            int estimat2 = tilLandemerker[i][nodeIndeks] - tilLandemerker[i][maalIndeks];

            if (estimat1 > estimat || estimat2 > estimat) {
                estimat = Math.max(estimat2, estimat1);
            }
        }

        return estimat;
    }

    void preprosesser(int[] landemerker) {

        antallLandemerker = landemerker.length;

        fraLandemerker = new int[antallLandemerker][N];

        for (int i = 0; i < antallLandemerker; i++) {
            System.out.println("Kalkulerer avstander fra landemerke " + i + "...");
            dijkstra(noder[landemerker[i]], null);

            for (int j = 0; j < N; j++) {
                fraLandemerker[i][j] = noder[j].distFraStart;
            }
        }

        try (FileWriter fileWriter = new FileWriter("src/opg9/fraLandemerker.txt")) {
            fileWriter.write(antallLandemerker + "  " + N + "\n");


            for (int i = 0; i < N; i++) {
                for (int j = 0; j < antallLandemerker; j++) {
                    fileWriter.write(fraLandemerker[j][i] + "    ");
                }
                fileWriter.write("\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Graf transponert = getTransponert();

        tilLandemerker = new int[antallLandemerker][N];


        for (int i = 0; i < antallLandemerker; i++) {
            System.out.println("Kalkulerer avstander til landemerke " + i + "...");
            transponert.dijkstra(transponert.noder[landemerker[i]], null);

            for (int j = 0; j < N; j++) {
                tilLandemerker[i][j] = transponert.noder[j].distFraStart;
            }
        }

        try (FileWriter fileWriter = new FileWriter("src/opg9/tilLandemerker.txt")) {
            fileWriter.write(antallLandemerker + "  " + N + "\n");


            for (int i = 0; i < N; i++) {
                for (int j = 0; j < antallLandemerker; j++) {
                    fileWriter.write(tilLandemerker[j][i] + "    ");
                }
                fileWriter.write("\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    void lesTilOgFraLandemerker() throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader("src/opg9/tilLandemerker.txt"))) {

            StringTokenizer st = new StringTokenizer(br.readLine());
            antallLandemerker = Integer.parseInt(st.nextToken());
            N = Integer.parseInt(st.nextToken());

            tilLandemerker = new int[antallLandemerker][N];

            for (int i = 0; i < N; ++i) {
                st = new StringTokenizer(br.readLine());

                for (int j = 0; j < antallLandemerker; j++) {
                    tilLandemerker[j][i] = Integer.parseInt(st.nextToken());
                }
            }
        } catch (IOException e) {
            throw new IOException(e);
        }


        try (BufferedReader br = new BufferedReader(new FileReader("src/opg9/fraLandemerker.txt"))) {

            StringTokenizer st = new StringTokenizer(br.readLine());
            antallLandemerker = Integer.parseInt(st.nextToken());
            N = Integer.parseInt(st.nextToken());

            fraLandemerker = new int[antallLandemerker][N];

            for (int i = 0; i < N; ++i) {
                st = new StringTokenizer(br.readLine());

                for (int j = 0; j < antallLandemerker; j++) {
                    fraLandemerker[j][i] = Integer.parseInt(st.nextToken());
                }
            }
        } catch (IOException e) {
            throw new IOException(e);
        }

    }


    public Graf getTransponert() {

        Graf transponert = new Graf();
        transponert.N = N;
        transponert.K = K;
        transponert.noder = new Node[N];

        for (int i = 0; i < N; i++) {
            transponert.noder[i] = new Node(this.noder[i].nodeNr);
            transponert.noder[i].breddegrad = this.noder[i].breddegrad;
            transponert.noder[i].lengdegrad = this.noder[i].lengdegrad;
        }

        int[][] tempKanter = new int[K][3];

        int j = 0;
        for (int i = 0; i < N; i++) {
            for (Vkant k = (Vkant) this.noder[i].kant1; k != null; k = (Vkant) k.neste) {
                tempKanter[j][0] = this.noder[i].nodeNr;
                tempKanter[j][1] = k.til.nodeNr;
                tempKanter[j][2] = k.vekt;
                j++;
            }
        }

        for (int i = 0; i < K; i++) {
            int til = tempKanter[i][0];
            int fra = tempKanter[i][1];
            int vekt = tempKanter[i][2];

            transponert.noder[fra].kant1 = new Vkant(transponert.noder[til], (Vkant) transponert.noder[fra].kant1, vekt);

        }

        transponert.grafNavn = this.grafNavn;

        return transponert;
    }

    public void lesInteressepunkter() throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader("src/opg9/interessepkt.txt"))) {

            StringTokenizer st = new StringTokenizer(br.readLine());
            int antall = Integer.parseInt(st.nextToken());

            for (int i = 0; i < antall; i++) {
                st = new StringTokenizer(br.readLine());

                int indeks = Integer.parseInt(st.nextToken());
                int kode = Integer.parseInt(st.nextToken());
                String navn = st.nextToken();

                noder[indeks].interessepunktKode = kode;
                noder[indeks].navn = navn;
            }

        } catch (IOException e) {
            throw new IOException(e);
        }

    }

    public void finnInteressepunkter(Node start, int kode, int antall) {

        Node[] interessepunkter = new Node[antall];
        int indeks = 0;

        initforgj(start);
        PriorityQueue<Node> pri = new PriorityQueue<>(new DijkstraComparator());
        pri.add(start);
        start.funnet = true;
        while (!pri.isEmpty()) {
            Node n = pri.poll();
            for (Vkant k = (Vkant) n.kant1; k != null; k = (Vkant) k.neste) {
                if (!k.til.funnet) {
                    pri.add(k.til);
                    k.til.funnet = true;
                }
                forkort(n, k, pri);
            }
            if ((n.interessepunktKode & kode) == kode) {
                interessepunkter[indeks++] = n;

                if (indeks == antall) break;
            }
        }

        for (Node n : interessepunkter) {
            System.out.println(n.breddegrad + ", " + n.lengdegrad);
        }
    }

    public void tegnRute(Node maal, int punktFrekvens) {

        try (FileWriter fileWriter = new FileWriter("src/opg9/rute.txt")) {

            Node n = maal;

            fileWriter.write("lat, lng\n");

            int counter = punktFrekvens;
            for (Node forgjenger = n.forgjenger; forgjenger != null; forgjenger = forgjenger.forgjenger) {


                if (counter == punktFrekvens) {
                    fileWriter.write(forgjenger.breddegrad + ", " + forgjenger.lengdegrad + "\n");
                    counter = 0;
                } else {
                    counter++;
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

class DijkstraComparator implements Comparator<Node> {
    public int compare(Node n, Node o) {
        int thisDist = n.distFraStart;
        int otherDist = o.distFraStart;

        return Integer.compare(thisDist, otherDist);
    }
}
class ALTComparator implements Comparator<Node> {
    public int compare(Node n, Node o) {
        int thisDist = n.getSumAvAvstander();
        int otherDist = o.getSumAvAvstander();

        return Integer.compare(thisDist, otherDist);
    }
}