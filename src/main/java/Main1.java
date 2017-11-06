import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Gal on 16/07/2017.
 */
public class Main1 {

    public static int getMinimumUniqueSum1(int[] arr) {
        Arrays.sort(arr);
        int sum = arr[0];
        int lastNumberToAdd = arr[0];

        for(int i = 1; i < arr.length; i++ ) {
            if(lastNumberToAdd >= arr[i]) {
                arr[i] = lastNumberToAdd+1;
            }
            sum += arr[i];
            lastNumberToAdd = arr[i];
        }
        return sum;
    }

    public static int getMinimumUniqueSum(int[] arr) {
        Map<Integer,Integer> entries;
        do {
            entries = getFilteredMap(arr);
            for(Map.Entry<Integer,Integer> entry : entries.entrySet()) {
                int toUpdate = entry.getValue() - 1;
                int updateCount = 0;
                for(int i = 0; i < arr.length; i++) {
                    if (arr[i] == entry.getKey() && updateCount < toUpdate) {
                        updateCount++;
                        arr[i] = arr[i] + updateCount;
                    }
                }
            }
        } while (!entries.isEmpty());
        return Arrays.stream(arr).sum();
    }


    private static Map<Integer,Integer> getFilteredMap(int[] arr) {
        Map<Integer, Integer> map = new HashMap<>();
        Arrays.stream(arr).forEach(num -> map.merge(num, 1, (a, b) -> b + 1)).filter(e -> e.getValue()>1).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    public static void main(String[] args) {
        int[] array = new int[]{1,2,2,3,4,5,6,6,7};
        int[] array1 = new int[]{1,2,7,3,4,6,6,6,6};
        int[] array2 = new int[]{1,2,2,4,5,6,6};
        int[] array3 = new int[]{2,2,2};
        int[] array4 = new int[]{2,4,2,5,5,4,3,2,6,3};
        int[] array5 = new int[]{1,2,3};
        int[] array6 = new int[]{19,17, 20, 21 , 20};
        int[] array7 = new int[]{5,5,3,1,4};
        System.out.println(getMinimumUniqueSum1(array1));
        System.out.println("=======");
        array = new int[]{1,2,2,3,4,5,6,6,7};
        array1 = new int[]{1,2,7,3,4,6,6,6,6};
        array2 = new int[]{1,2,2,4,5,6,6};
        array3 = new int[]{2,2,2};
        array4 = new int[]{2,4,2,5,5,4,3,2,6,3};
        array5 = new int[]{1,2,3};
        array6 = new int[]{19,17, 20, 21 , 20};
        array7 = new int[]{5,5,3,1,4};
        System.out.println(getMinimumUniqueSum(array1));

    }

}
