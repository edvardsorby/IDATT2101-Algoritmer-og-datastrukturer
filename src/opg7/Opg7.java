package opg7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Opg7 {
    public static void main(String[] args) throws IOException {
        Graf graf = new Graf();

        String grafNavn = "vg3";
        graf.ny_vgraf(new BufferedReader(new FileReader("src/opg7/" + grafNavn + ".txt")), grafNavn);


        int startNode = 1;
        graf.kjoerDijkstra(startNode);


    }
}

class Node {
    Kant kant1;
    Object d; //Andre nodedata
    int navn;

    public Node(int navn) {
        this.navn = navn;
    }

    @Override
    public String toString() {
        return "[Node " + navn + "]";
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
    int N;
    int K;
    Node[] node;
    String grafNavn;
    int len;

    public void ny_vgraf(BufferedReader br, String grafNavn) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        node = new Node[N];

        for (int i = 0; i < N; ++i) {
            node[i] = new Node(i);
        }

        K = Integer.parseInt(st.nextToken());

        for (int i = 0; i < K; ++i) {
            st = new StringTokenizer(br.readLine());
            int fra = Integer.parseInt(st.nextToken());
            int til = Integer.parseInt(st.nextToken());
            int vekt = Integer.parseInt(st.nextToken());
            Vkant k = new Vkant(node[til], (Vkant) node[fra].kant1, vekt);
            node[fra].kant1 = k;
        }
        this.grafNavn = grafNavn;
    }

    public void fiks_heap(int i, Node[] pri) {
        int m = venstre(i);
        if(m < len) {
            int h = m + 1;
            if (h < len && ((Forgj) pri[h].d).dist < ((Forgj) pri[m].d).dist) {
                m = h;
            }
            if (((Forgj) pri[m].d).dist < ((Forgj) pri[i].d).dist) {
                bytt(pri, i, m);
                fiks_heap(m, pri);
            }
        }
    }

    int venstre(int i) {
        return (i << 1) + 1;
    }

    public static void bytt(Node[] n, int i, int j) {
        Node k = n[j];
        n[j] = n[i];
        n[i] = k;
    }

    public Node hent_min(Node[] pri) {
        Node min = pri[0];
        pri[0] = pri[--len];
        fiks_heap(0, pri);
        return min;
    }

    public void lag_priko(Node[] pri, Node s) {
        for (int i = 0; i < N; i++) {
            pri[i] = node[i];
        }
        int i = len/2;
        while (i-- > 0) {
            fiks_heap(i, pri);
        }
    }

    public void initforgj(Node s) {
        for (int i = N; i-- > 0;) {
            node[i].d = new Forgj();
        }
        ((Forgj) s.d).dist = 0;
    }

    void forkort(Node n, Vkant k, Node[] pri) {
        Forgj nd = (Forgj) n.d;
        Forgj md = (Forgj) k.til.d;
        if (md.dist > nd.dist + k.vekt) {
            md.dist = nd.dist + k.vekt;
            md.forgj = n;

            int i = len / 2;
            while (i-- > 0) fiks_heap(i, pri);
        }
    }

    void dijkstra(Node s) {
        len = N;
        initforgj(s);
        Node[] pri = new Node[N];
        lag_priko(pri, s);
        for (int i = N; i > 1; --i) {
            Node n = hent_min(pri);
            for (Vkant k = (Vkant) n.kant1; k != null; k = (Vkant) k.neste) {
                forkort(n, k, pri);
            }
        }
    }

    void kjoerDijkstra(int startNode) {
        dijkstra(node[startNode]);

        System.out.println("Dijkstras algoritme på " + grafNavn);
        System.out.println("Node   forgjenger   distanse");

        for (int i = 0; i < N; i++) {
            Forgj node = ((Forgj) this.node[i].d);
            String linje = "   " + this.node[i].navn;

            if (this.node[i].navn == startNode) {
                linje += "        start";
            } else if (node.forgj == null) {
                linje += "             ";
            } else {
                linje += "            " + node.forgj.navn;
            }

            if (node.dist == Forgj.uendelig) {
                linje += "  nåes ikke";
            } else {
                linje += "          " + node.dist;
            }

            System.out.println(linje);
        }
    }

}

class Forgj {
    int dist;
    Node forgj;
    static int uendelig = 1000000000;

    public Forgj() {
        dist = uendelig;
    }
    public int finn_dist() {
        return dist;
    }
    public Node finn_forgj() {
        return forgj;
    }
}