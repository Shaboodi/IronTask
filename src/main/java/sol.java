import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Gal on 18/07/2017.
 */

public class sol {

    public static void main(String[] args) {

        int[] arr = {5, 5, 2, 2, 2, 2};
        //{1,1,3,4,4,5}
        //{1,2,2,2,5}
        //{4,3,2,1,5,5,}
        //int [] arr1 = {1,2,2,2,2,2,3};
        //      1,2,3,4,5,6,7 = 28
        int[] arr2 = new int[10];
        ArrayList<Integer> Aarr = new ArrayList<Integer>();
        int k = 0;


        System.out.println("we will build histogram from input arr \n " + Arrays.toString(arr));
        for (int i = 0; i < arr.length; i++) {
            arr2[arr[i]]++;

        }

        for (int i = 0; i < arr2.length; i++) {
            System.out.println("The number of occurrences of number  " + i + " in arr is : " + arr2[i]);

        }

        System.out.println("======================= \n");

        System.out.println("we build new  sorted arry from the histogram \n");

        for (int i = 0; i < arr2.length; i++) {
            if (arr2[i] > 1) {

                for (int j = arr2[i]; j > 0; j--) {

                    Aarr.add(k, i);
                    k++;
                }
            } else {
                if (arr2[i] != 0) {
                    Aarr.add(k, i);

                    k++;
                }
            }

        }

        for (int i = 0; i < Aarr.size(); i++) {
            System.out.print(Aarr.get(i));
        }

        System.out.println("\n \nwe send the sorted arry to the algo that return the minimum sum \n");
        System.out.println("minimum sum is : " + sumMinimumDuplicate(Aarr));

    }


    public static int sumMinimumDuplicate(ArrayList<Integer> arr) {

        int sum = arr.get(0);
        int lastNumberToAdd = arr.get(0);

        for (int i = 1; i < arr.size(); i++) {
            if (lastNumberToAdd >= arr.get(i)) {
                arr.set(i, lastNumberToAdd + 1);
            }

            sum += arr.get(i);
            lastNumberToAdd = arr.get(i);
        }

        return sum;
    }


}
