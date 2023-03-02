package opg6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Opg6 {
    public static void main(String[] args) throws IOException {
        Graph graph = new Graph();

        String graphName = "ø6g6"; // Grafen/fila som skal lastes inn

        graph.new_ugraph(new BufferedReader(new FileReader("src/opg6/" + graphName+".txt")), graphName);


        graph.findStronglyConnectedComponents();
    }
}

class Edge {
    Edge next;
    Vertex to;

    public Edge(Vertex v, Edge nxt) {
        to = v;
        next = nxt;
    }
}

class Vertex {
    Edge edge1;
    Object d; //Other vertex/node data
    int vertexId;
    int finishTime;

    @Override
    public String toString() {
        return "Node " + vertexId;
    }

}

class Graph {
    int V;
    int E;
    Vertex[] vertex;
    String graphName;

    String graphPrint;

    public void new_ugraph(BufferedReader br, String graphName) throws IOException {
        this.graphName = graphName;

        StringTokenizer st = new StringTokenizer(br.readLine());
        V = Integer.parseInt(st.nextToken());

        vertex = new Vertex[V];
        for (int i = 0; i < V; i++) {
            vertex[i] = new Vertex();
        }
        E = Integer.parseInt(st.nextToken());

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(br.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            Edge e = new Edge(vertex[to], vertex[from].edge1);
            vertex[from].edge1 = e;
        }

        for (int i = 0; i < this.vertex.length; i++) {
            this.vertex[i].vertexId = i;
        }
    }

    public void dfs_init() {
        for (int i = V; i-- > 0;) {
            vertex[i].d = new Dfs_predecessor();
        }
        Dfs_predecessor.null_time();
    }

    public void df_search(Vertex v) {
        Dfs_predecessor nd = (Dfs_predecessor) v.d;
        nd.found_time = Dfs_predecessor.read_time();

        for (Edge e = v.edge1; e != null; e = e.next) {
            Dfs_predecessor md = (Dfs_predecessor)e.to.d;

            if (md.found_time == 0) {
                md.pred = v;
                md.dist = nd.dist + 1;

                df_search(e.to);
            }
        }

        graphPrint += (v.vertexId + " ");

        nd.finish_time = Dfs_predecessor.read_time();
        v.finishTime = nd.finish_time;
    }

    public void dfs(Vertex s) {
        dfs_init();
        ((Dfs_predecessor) s.d).dist = 0;
        df_search(s);
    }

    public void findStronglyConnectedComponents() {
        // Kjører først DFS på grafen
        dfs_init();
        for (int i = 0; i < V; i++) {
            if (vertex[i].finishTime == 0) df_search(vertex[i]);
        }

        // Sorterer så nodene i grafen på synkende ferdig-tid
        Arrays.sort(vertex, (o1, o2) -> Integer.compare(o2.finishTime, o1.finishTime));


        // Lager så den omvendte (transponerte) grafen
        this.transpose();

        // Kjører så DFS på den omvendte grafen, med nodene med høyest ferdig-tid først
        dfs_init();

        for (Vertex v : vertex) v.finishTime = 0;
        int componentNo = 0;


        graphPrint = "Komponent    Noder i komponenten";

        for (int i = 0; i < V; i++) {
            if (vertex[i].finishTime == 0) {
                graphPrint += ("\n" + ++componentNo + "            ");
                df_search(vertex[i]);
            }
        }

        // Printer til slutt resultatet
        System.out.println("Grafen " + graphName + " har " + componentNo + " sterkt sammenhengende komponenter.");
        if (V < 100) System.out.println(graphPrint);
    }


    public void transpose() {
        int[][] edges = new int[E][2];
        int[] vertexIds = new int[V];

        int j = 0;
        for (int i = 0; i < V; i++) {
            for (Edge e = vertex[i].edge1; e != null; e = e.next) {
                edges[j][0] = e.to.vertexId;
                edges[j][1] = vertex[i].vertexId;
                j++;
            }
            vertexIds[i] = vertex[i].vertexId;
        }

        vertex = new Vertex[V];
        for (int i = 0; i < V; i++) {
            vertex[i] = new Vertex();
            vertex[i].vertexId = vertexIds[i];
        }

        for (int i = 0; i < E; i++) {
            int from = edges[i][0];
            int to = edges[i][1];
            Edge e = new Edge(vertex[findVertexPositionById(to)], vertex[findVertexPositionById(from)].edge1);
            vertex[findVertexPositionById(from)].edge1 = e;
        }
    }

    public int findVertexPositionById(int vertexId) {
        for (int i = 0; i < V; i++) {
            if (vertex[i].vertexId == vertexId) {
                return i;
            }
        }
        return -1;
    }
}

class Predecessor {
    int dist;
    Vertex pred;
    static int infinity = 1000000000;

    public Predecessor() {
        dist = infinity;
    }

}

class Dfs_predecessor extends Predecessor {
    int found_time;
    int finish_time;
    static int time;
    static void null_time() {
        time = 0;
    }
    static int read_time() {
        return ++time;
    }
}