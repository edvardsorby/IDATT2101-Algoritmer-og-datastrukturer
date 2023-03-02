package opg2;

import static java.lang.System.currentTimeMillis;

public class Opg2 {

    public static void main(String[] args) {

        System.out.println("Testdata:");
        System.out.println("2^12 = " + exponentiation(2,12));
        System.out.println("2^12 = " + exponentiation2(2,12));
        System.out.println("2^12 = " + exponentiation3(2,12));
        System.out.println("3^14 = " + exponentiation(3,14));
        System.out.println("3^14 = " + exponentiation2(3,14));
        System.out.println("3^14 = " + exponentiation3(3,14));


        System.out.println("\nTidtaking:");

        double start = currentTimeMillis();
        int rounds = 0;
        double time;
        double end;
        do {
            exponentiation(1.001, 500);
            end = currentTimeMillis();
            rounds++;
        } while (end - start < 1000);
        time = (end - start) / rounds;
        System.out.println("Millisekund pr. runde:" + time);
        System.out.println("Runder per sekund: " + rounds / 1000);
    }

    public static double exponentiation (double x, int n) {

        if (n == 0) {
            return 1;
        } else if (n > 0) {
            return x * exponentiation(x, n-1);
        } else {
            return 0;
        }
    }
    public static double exponentiation2 (double x, double n) {

        if (n == 0) {
            return 1;
        } else if (n % 2 != 0) {
            return x * exponentiation2(x*x,(n-1)/2);
        } else if (n % 2 == 0) {
            return exponentiation2(x*x,n/2);
        } else {
            return 0;
        }
    }

    public static double exponentiation3 (double x, double n) {
        return Math.pow(x,n);
    }
}
