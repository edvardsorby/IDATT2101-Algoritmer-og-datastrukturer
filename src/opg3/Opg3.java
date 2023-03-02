package opg3;

import java.util.Random;

import static java.lang.System.currentTimeMillis;

public class Opg3 {
    public static void main(String[] args) throws Exception {

        // Velger antall tall i listene:
        int n = 100000000;

        int[] list = new int[n];
        int[] list2 = new int[n];

        /* Utgangspunkt med tilfeldige tall: */
        Random random = new Random();

        for (int i = 0; i < list.length; i++) {
            list[i] = list2[i] = random.nextInt(0, list.length);
        }


        /* Utgangspunkt med mange duplikater: */

        /*
        for (int i = 0; i < list.length-2; i+=4) {
            list[i] = list[i+2] = list2[i] = list2[i+2] = random.nextInt(0, list.length-1);
            list[i+1] = list[i+3] = list2[i+1] = list2[i+3] = random.nextInt(0, list.length-1);
        }
         */



        /* Utgangspunkt med tabell sortert fra før: */

        /*
        for (int i = 0; i < list.length; i++) {
            list[i] = list2[i] = i;
        }
         */

        long startTime;
        long endTime;

        /* Quicksort: */

        int startSum = checkSum(list);

        // Starter tiden og utfører sorteringen med quicksort:
        startTime = currentTimeMillis();
        quicksort(list, 0, list.length-1);
        endTime = currentTimeMillis();

        double time = endTime - startTime;
        System.out.println("Tid brukt på quicksort: " + time + " ms");

        int endSum = checkSum(list);

        // Tester for å sjekke om sorteringen ble riktig:
        if (startSum != endSum) throw new Exception("Listen har mistet tall!");
        if (!checkSorting(list)) throw new Exception("Sorteringen er feil!");



        /* Dual pivot quicksort: */

        startSum = checkSum(list2);

        // Starter tiden og utfører sorteringen:
        startTime = currentTimeMillis();
        dualPivotQuickSort(list2, 0, list2.length-1);
        endTime = currentTimeMillis();

        time = endTime - startTime;
        System.out.println("Tid brukt på dual pivot quicksort: " + time + " ms");

        endSum = checkSum(list2);

        // Tester for å sjekke om sorteringen ble riktig:
        if (startSum != endSum) throw new Exception("Listen har mistet tall!");
        if (!checkSorting(list2)) throw new Exception("Sorteringen er feil!");

    }

    public static void quicksort(int[] t, int v, int h) {
        if (h - v > 2) {
            int delepos = splitt(t, v, h);
            quicksort(t, v, delepos-1);
            quicksort(t, delepos+1, h);
        } else median3sort(t, v, h);
    }


    public static int median3sort(int[] t, int v, int h) {
        int m = (v + h) / 2;
        if (t[v] > t[m]) bytt(t, v, m);
        if (t[m] > t[h]) {
            bytt(t, m, h);
            if (t[v] > t[m]) bytt(t, v, m);
        }
        return m;
    }

    public static int splitt(int[] t, int v, int h) {
        int iv, ih;
        int m = median3sort(t, v, h);
        int dv = t[m];
        bytt(t, m, h-1);
        for (iv = v, ih = h - 1;;) {
            while (t[++iv] < dv);
            while (t[--ih] > dv);
            if (iv >= ih) break;
            bytt(t, iv, ih);
        }
        bytt(t, iv, h-1);
        return iv;
    }

    public static void bytt(int[] t, int i, int j) {
        int k = t[j];
        t[j] = t[i];
        t[i] = k;
    }


    static void swap(int[] arr, int i, int j)
    {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static void dualPivotQuickSort(int[] arr, int low, int high) {

        if (low < high) {

            // Bytte av pivot for å unngå skjevdeling:
            int t1 = arr[low+(high-low)/3];
            int t2 = arr[high-(high-low)/3];
            arr[low+(high-low)/3] = arr[low];
            arr[high-(high-low)/3] = arr[high];
            arr[low] = t1;
            arr[high] = t2;

            // piv[] stores left pivot and right pivot.
            // piv[0] means left pivot and
            // piv[1] means right pivot
            int[] piv;
            piv = partition(arr, low, high);

            dualPivotQuickSort(arr, low, piv[0] - 1);
            dualPivotQuickSort(arr, piv[0] + 1, piv[1] - 1);
            dualPivotQuickSort(arr, piv[1] + 1, high);
        }
    }

    static int[] partition(int[] arr, int low, int high)
    {
        if (arr[low] > arr[high])
            swap(arr, low, high);

        // p is the left pivot, and q
        // is the right pivot.
        int j = low + 1;
        int g = high - 1, k = low + 1,
                p = arr[low], q = arr[high];

        while (k <= g)
        {

            // If elements are less than the left pivot
            if (arr[k] < p)
            {
                swap(arr, k, j);
                j++;
            }

            // If elements are greater than or equal
            // to the right pivot
            else if (arr[k] >= q)
            {
                while (arr[g] > q && k < g)
                    g--;

                swap(arr, k, g);
                g--;

                if (arr[k] < p)
                {
                    swap(arr, k, j);
                    j++;
                }
            }
            k++;
        }
        j--;
        g++;

        // Bring pivots to their appropriate positions.
        swap(arr, low, j);
        swap(arr, high, g);

        // Returning the indices of the pivots
        // because we cannot return two elements
        // from a function, we do that using an array.
        return new int[] { j, g };
    }

    private static int checkSum(int[] list) {
        int sum = 0;
        for (int n : list) {
            sum += n;
        }
        return sum;
    }
    
    private static boolean checkSorting(int[] list) {
        for (int i = 0; i < list.length-2; i++) {
            if (!(list[i+1] >= list[i])) {
                return false;
            }
        }
        return true;
    }
}
