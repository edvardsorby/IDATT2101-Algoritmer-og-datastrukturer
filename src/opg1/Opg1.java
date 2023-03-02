package opg1;

import java.util.Arrays;
import java.util.Random;

import static java.lang.System.currentTimeMillis;

public class Opg1 {
    public static void main(String[] args) {

        // Eksempel fra boka:
        //int[] sharePriceChanges = {-1,3,-9,2,2,-1,2,-1,-5};

        // Tilfeldig genererte tall:

        int[] sharePriceChanges = new int[10000];
        Random random = new Random();
        for (int i = 0; i < sharePriceChanges.length; i++) {
            sharePriceChanges[i] = random.nextInt(-10,11);
        }
        System.out.println(Arrays.toString(sharePriceChanges));






        System.out.println("Kursendringer: " + Arrays.toString(sharePriceChanges));
        int[] sharePrices = new int[sharePriceChanges.length];

        int shareValue = 0;
        System.out.println("Startverdi: " + shareValue);

        int lowestValueDay = 0;
        int highestValueDay = 0;
        int largestProfit = 0;

        for (int i = 0; i < sharePrices.length; i++) {
            sharePrices[i] = shareValue += sharePriceChanges[i];
        }
        System.out.println("Aksjeverdier: " + Arrays.toString(sharePrices));

        long startTime = currentTimeMillis();

        for (int i = 0; i < sharePrices.length-1; i++) {
            for (int j = i+1; j < sharePrices.length; j++) {

                int profit = sharePrices[j] - sharePrices[i];

                if (profit > largestProfit) {
                    largestProfit = profit;
                    lowestValueDay = i;
                    highestValueDay = j;
                }

            }
        }

        long endTime = currentTimeMillis();

        System.out.println("\nStarttid: " + startTime);
        System.out.println("Sluttid: " + endTime);
        System.out.println("Tid brukt: " + (endTime - startTime) + " ms.");

        System.out.println("\nHøyest mulig fortjeneste: " + largestProfit);
        System.out.println("Beste kjøpsdato: dag " + (lowestValueDay+1) + " (verdi: " + sharePrices[lowestValueDay] + ")");
        System.out.println("Beste salgsdato: dag " + (highestValueDay+1) + " (verdi: " + sharePrices[highestValueDay] + ")");


    }
}
