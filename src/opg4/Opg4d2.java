package opg4;

import java.util.Scanner;

public class Opg4d2 {
    public static void main(String[] args) {

        Tre binærtSøketre = new Tre();


        /* Eksempelnoder: */
        String[] nyeOrd = {"hode", "ben", "legg", "albue", "hake", "tå", "arm", "tann"};
        for (String ord : nyeOrd) binærtSøketre.settInn(new Ord(ord));


        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Legg til nytt ord: ('exit' for å avslutte)");
            String nyttOrd = in.nextLine();

            if (nyttOrd.equalsIgnoreCase("exit")) {
                System.out.println("Avslutter...");
                break;
            }

            binærtSøketre.settInn(new Ord(nyttOrd));
            binærtSøketre.skrivUtTre();
        }
    }
}

class TreNode {
    Ord element;
    TreNode venstre;
    TreNode høyre;
    TreNode forelder;

    public TreNode(Ord e, TreNode f, TreNode v, TreNode h) {
        this.element = e;
        this.forelder = f;
        this.venstre = v;
        this.høyre = h;
    }
}

class Tre {
    TreNode rot;

    public Tre() {
        rot = null;
    }

    void settInn(Ord e) {

        String nøkkel = ((Element)e).finnNøkkel();
        TreNode n = rot;

        if (rot == null) {
            rot = new TreNode(e, null, null, null);
            return;
        }

        String sml = null;
        TreNode f = null;

        while (n != null) {
            f = n;
            sml = ((Element)(n.element)).finnNøkkel();
            if (nøkkel.compareToIgnoreCase(sml) < 0) {
                n = n.venstre;
            } else {
                n = n.høyre;
            }
        }

        if (nøkkel.compareToIgnoreCase(sml) < 0) {
            f.venstre = new TreNode(e, f,null,null);
        } else {
            f.høyre = new TreNode(e, f,null,null);
        }
    }

    public void skrivUtTre() {
        int antOrd = 0;
        StringBuilder utskrift = new StringBuilder();

        Kø kø = new Kø(10);
        kø.leggIKø(rot);
        while (!kø.tom()) {
            TreNode denne = (TreNode) (kø.nesteIKø());

            StringBuilder node = new StringBuilder();

            if (denne != null) {

                String ord = denne.element.toString();

                // Regner ut marginer til noden:
                if (antOrd == 0) printNode(node, ord, 64);
                else if (antOrd >= 1 && antOrd <= 2) printNode(node, ord, 32);
                else if (antOrd >= 3 && antOrd <= 6) printNode(node, ord, 16);
                else if (antOrd >= 7 && antOrd <= 15) printNode(node, ord, 8);

                // Legger til nodens barn i køa:
                kø.leggIKø(denne.venstre);
                kø.leggIKø(denne.høyre);
            } else {

                // Fyller eventuelle tomrom:
                if (antOrd > 0 && antOrd < 3) node.append(" ".repeat(32));
                else if (antOrd > 2 && antOrd < 7) node.append(" ".repeat(16));
                else if (antOrd >= 7 && antOrd < 15) node.append(" ".repeat(8));

                // Legger til null i køa for å fylle inn tomrom:
                if (antOrd < 16) {
                    kø.leggIKø(null);
                    kø.leggIKø(null);
                }
            }

            // Legger til gjeldende node i utskriften:
            utskrift.append(node);

            antOrd++;

            // Bytter linje for å få riktig fasong på treet:
            if (antOrd == 1 || antOrd == 3 || antOrd == 7 || antOrd == 15) utskrift.append("\n");

        }
        // Skriver ut hele treet til slutt:
        System.out.println(utskrift);
    }

    // Funksjon som brukes for å regne ut riktig marginer til hver node:
    private static void printNode(StringBuilder node, String ord, int plasser) {
        int margin = (plasser - ord.length())/2;
        node.append(" ".repeat(Math.max(0, margin)));
        node.append(ord);
        node.append(" ".repeat(Math.max(0, margin)));
    }
}

interface Element {
    String finnNøkkel();
}

class Ord implements Element {
    String nøkkel;

    public Ord(String nøkkel) {
        this.nøkkel = nøkkel;
    }

    @Override
    public String finnNøkkel() {
        return nøkkel;
    }

    @Override
    public String toString() {
        return nøkkel;
    }
}

class Kø {
    private Object[] tab;
    private int start = 0;
    private int slutt = 0;
    private int antall = 0;

    public Kø(int str) {
        tab = new Object[str];
    }

    public boolean tom() {
        return antall == 0;
    }

    public boolean full() {
        return antall == tab.length;
    }

    public void leggIKø(Object e) {
        if (full()) return;
        tab[slutt] = e;
        slutt = (slutt+1)%tab.length;
        ++antall;
    }

    public Object nesteIKø() {
        if (!tom()) {
            Object e = tab[start];
            start = (start+1)%tab.length;
            --antall;
            return e;
        }
        else return null;
    }
}