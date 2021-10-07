// Callaghan Donnelly
// 10/6/2021
// Assignment 1


import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class driver {
    public ArrayList<Integer> indexList = new ArrayList<Integer>();

    public void swap (double[] arr, int i, int j) {
        double temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public int findSmallest (double[] arr, int start) {
        int smallest = start;
        for (int i = start+1; i < arr.length; i++){
            if (arr[i] < arr[smallest]) {
                smallest = i;
            }
        }
        return smallest;
    }

    public double[] selectSort(double[] arrayIn){
        for (int i = 0; i < arrayIn.length; i++){
            swap(arrayIn, i, findSmallest(arrayIn, i));
        }
        return arrayIn;
    }

    public double[] bubbleSort (double[] arr) {
        for (int i = 0; i < arr.length - 1; i++){
            for (int j = 0; j < arr.length - i - 1; j++){
                if (arr[j] > arr[j+1]){
                    swap(arr, j, j+1);
                }
            }
        }
        return arr;
    }

    public double[] insertionSort (double[] arr) {
        for (int i = 1; i < arr.length; i++) {
            double temp = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > temp) {
                arr[j+1] = arr[j];
                --j;
            }
        arr[j+1] = temp;
        }
        return arr;
    }

    public double[] get_left(double[] arr){
        int size = arr.length/2;
        double[] left = Arrays.copyOfRange(arr, 0, size);
        return left;
    }

    public double[] get_right(double[] arr){
        int size = arr.length/2;
        double[] right = Arrays.copyOfRange(arr, size, arr.length);
        return right;
    }

    public void merge(double[] arr, double[] left, double[] right){
        // essentially counter variables (but for indicies)
        int leftIndex = 0;
        int rightIndex = 0;

        // Merge arrays back into the original array
        for (int i = 0; i < arr.length; i++) {
            // if within the bounds of the sub-array, check the index to merge, and merge the correct one
            if (leftIndex < left.length && rightIndex < right.length) {
                if (left[leftIndex] < right[rightIndex]) {
                    arr[i] = left[leftIndex];
                    leftIndex++;
                } else {
                    arr[i] = right[rightIndex];
                    rightIndex++;
                }
            }
            else if (leftIndex < left.length) {
                // do left when right is done
                arr[i] = left[leftIndex];
                leftIndex++;
            }
            else if (rightIndex < right.length) {
                // do right when left is done
                arr[i] = right[rightIndex];
                rightIndex++;
            }
        }
    }

    public double[] mergesort(double[] arr) {
        if (arr.length > 1) {
            double[] left = get_left(arr);
            double[] right = get_right(arr);
            mergesort(left);
            mergesort(right);
            merge(arr, left, right);
        }
        return arr;
    }

    int partition (double[] arr, int left, int right) {
        int holder = 0;
        if (left < right) {
            int pivot = right;
            int bot = left;
            int top = right - 1;
            while (bot <= top) {
                while (bot < right && arr[bot] < arr[pivot]) {
                    ++bot;
                }
                while (top >= bot && arr[top] >= arr[pivot]) {
                    --top;
                }
                if (bot < top) {
                    swap(arr, bot, top);
                } else {
                    swap(arr, bot, pivot);
                }
            }
            holder = bot;
        }
        return holder;
    }

    public double[] quickSort (double[] arr, int bot, int top) {
        if (bot < top) {
            int p = partition(arr, bot, top);
            quickSort(arr, bot, p-1);
            quickSort(arr, p+1, top);
        }
        return arr;
    }

    public ArrayList<ArrayList<Double>> presort (double[] arr){
        ArrayList<ArrayList<Double>> primaryArray = new ArrayList<>();
        // track the index of which sub list should be getting modified
        int counter = 0;
        // worst case scenario is is it completely backwards and every number requires its own indexing
        ArrayList<Integer> secondary = new ArrayList<Integer>();
        for (int i = 0; i < arr.length; i++) {
            if (i == 0){
                primaryArray.add(new ArrayList<Double>());
                primaryArray.get(counter).add(arr[i]);
                secondary.add(0);
            }
            else if (arr[i] >= arr[i-1]){
                primaryArray.get(counter).add(arr[i]);
            }
            else {
                secondary.add(i - 1);
                counter++;
                primaryArray.add(new ArrayList<Double>());
                primaryArray.get(counter).add(arr[i]);
                secondary.add(i);
            }
        }
        // lazy (yet very effective) way to fix the bug leaving off the last ending value
        secondary.add(arr.length-1);

        // copy the index list to global space so I don't have to pass it
        // lmao turns out I don't actually need this list at all, so I'm just gonna leae it in this method, but not touch it anywhere else, whoops
        indexList.addAll(secondary);
        return primaryArray;
    }

    public ArrayList<Double> improvedMerge(ArrayList<ArrayList<Double>> dataList){
        // fastest way would probably be to multiprocess (assuming starting each needed JVM wouldn't take too long), and give the left side to one process and the right side to another, and then once its down to one array on each process, go back down to one process, and merge the last 2 arrays together
        // but I don't know how to multiprocess in Java, nor do I know that overhead for it, nor do I really want to spend 3 hours figuring all of that out
        // Setup the list to return with the 0 index already there, and then remove it from the data list
        ArrayList<Double> finalList = new ArrayList<Double>(dataList.get(0));
        dataList.remove(0);

        // My solution is essentially depending on the fact that the sub-arrays are mostly longer than 3, and pre-sorted
        // So check and see if the first in the new index is
        for (int i = 0; i < dataList.size(); i++) {
            int lastIndex = finalList.size() - 1;
            // since presorted, if the first of the list being merged in is larger than finalList, I don't have to check, I know they are all larger, so just add the whole thing at the end of finalList
            if (dataList.get(i).get(0) > finalList.get(lastIndex)){
                finalList.addAll(dataList.get(i));
            }
            else {
                // this part is kinda like binary search, but adding in, instead of cutting out
                for (int k = 0; k < dataList.get(i).size(); k++) {
                    int low = 0;
                    int high = finalList.size();
                    Double toAdd = dataList.get(i).get(k);
                    boolean addedFlag = false;

                    while (low < high && !addedFlag){
                        int mid = (low + high) / 2;

                        if (toAdd <= finalList.get(0)){
                            finalList.add(0, toAdd);
                            addedFlag = true;
                        }

                        else if (toAdd >= finalList.get(finalList.size() - 1)){
                            finalList.add(finalList.size(), toAdd);
                            addedFlag = true;
                        }

                        else if ((finalList.get(mid) < toAdd) && (toAdd <= finalList.get(mid + 1))){
                            finalList.add(mid + 1, toAdd);
                            addedFlag = true;
                        }

                        else if ((finalList.get(mid - 1) < toAdd) && (toAdd <= finalList.get(mid))){
                            finalList.add(mid, toAdd);
                            addedFlag = true;
                        }

                        else if (finalList.get(mid) < toAdd){
                            low = mid + 1;
                        }

                        else {
                            high = mid - 1;
                        }
                    }
                }
            }
        }

        return finalList;
    }

    public static void main(String[] args) {
        // TODO: uncomment one type of sort at a time to test
        // ^ this avoids the problem of refilling each array with unsorted data, as well as not having to sit for 30 minutes while selection and bubble sort set my cpu on fire
        long setupStart = System.currentTimeMillis();
        // parameters for the random numbers inside the arrays
        double minVal = 0;
        double maxVal = 200;

        // all of the needed arrays
        double[] testArray0 = new double[50000];
        double[] testArray1 = new double[100000];
        double[] testArray2 = new double[150000];
        double[] testArray3 = new double[200000];
        double[] testArray4 = new double[250000];
        double[] testArray5 = new double[300000];
        double[] testArray6 = new double[350000];
        double[] testArray7 = new double[400000];
        double[] testArray8 = new double[450000];
        double[] testArray9 = new double[500000];

        // loop outer for each increment of 50,000, and loop inner for each individual of those 50,000
        for (int i = 9; i >= 0; i--) {
            for (int k = 0; k < 50000; k++) {
                Random r = new Random();
                double randomValue = minVal + (maxVal - minVal) * r.nextDouble();

                // (i * 50,000) + 50,000 essentially, so that at bottom it will reach 49,999 index, and at top it will reach 499999
                int indexToEffect = (i * 50000) + k;
                // on 0 change all of them, then as i progresses, change one less each time
                if (i == 0){
                    testArray0[indexToEffect] = randomValue;
                    testArray1[indexToEffect] = randomValue;
                    testArray2[indexToEffect] = randomValue;
                    testArray3[indexToEffect] = randomValue;
                    testArray4[indexToEffect] = randomValue;
                    testArray5[indexToEffect] = randomValue;
                    testArray6[indexToEffect] = randomValue;
                    testArray7[indexToEffect] = randomValue;
                    testArray8[indexToEffect] = randomValue;
                    testArray9[indexToEffect] = randomValue;
                }
                else if (i == 1){
                    testArray1[indexToEffect] = randomValue;
                    testArray2[indexToEffect] = randomValue;
                    testArray3[indexToEffect] = randomValue;
                    testArray4[indexToEffect] = randomValue;
                    testArray5[indexToEffect] = randomValue;
                    testArray6[indexToEffect] = randomValue;
                    testArray7[indexToEffect] = randomValue;
                    testArray8[indexToEffect] = randomValue;
                    testArray9[indexToEffect] = randomValue;
                }
                else if (i == 2){
                    testArray2[indexToEffect] = randomValue;
                    testArray3[indexToEffect] = randomValue;
                    testArray4[indexToEffect] = randomValue;
                    testArray5[indexToEffect] = randomValue;
                    testArray6[indexToEffect] = randomValue;
                    testArray7[indexToEffect] = randomValue;
                    testArray8[indexToEffect] = randomValue;
                    testArray9[indexToEffect] = randomValue;
                }
                else if (i == 3){
                    testArray3[indexToEffect] = randomValue;
                    testArray4[indexToEffect] = randomValue;
                    testArray5[indexToEffect] = randomValue;
                    testArray6[indexToEffect] = randomValue;
                    testArray7[indexToEffect] = randomValue;
                    testArray8[indexToEffect] = randomValue;
                    testArray9[indexToEffect] = randomValue;
                }
                else if (i == 4){
                    testArray4[indexToEffect] = randomValue;
                    testArray5[indexToEffect] = randomValue;
                    testArray6[indexToEffect] = randomValue;
                    testArray7[indexToEffect] = randomValue;
                    testArray8[indexToEffect] = randomValue;
                    testArray9[indexToEffect] = randomValue;
                }
                else if (i == 5){
                    testArray5[indexToEffect] = randomValue;
                    testArray6[indexToEffect] = randomValue;
                    testArray7[indexToEffect] = randomValue;
                    testArray8[indexToEffect] = randomValue;
                    testArray9[indexToEffect] = randomValue;
                }
                else if (i == 6){
                    testArray6[indexToEffect] = randomValue;
                    testArray7[indexToEffect] = randomValue;
                    testArray8[indexToEffect] = randomValue;
                    testArray9[indexToEffect] = randomValue;
                }
                else if (i == 7){
                    testArray7[indexToEffect] = randomValue;
                    testArray8[indexToEffect] = randomValue;
                    testArray9[indexToEffect] = randomValue;
                }
                else if (i == 8){
                    testArray8[indexToEffect] = randomValue;
                    testArray9[indexToEffect] = randomValue;
                }
                else if (i == 9){
                    testArray9[indexToEffect] = randomValue;
                }
            }
        }

        long setupEnd = System.currentTimeMillis();
        long setupTime = setupEnd - setupStart;

        System.out.println("Setup time: " + setupTime + "ms");

        long startTime = System.currentTimeMillis();
        long endTime;
        long finishTime;

        // run the selection sorts
        /*
        // add a timer after each sort is run
        double[] item0 = new driver().selectSort(testArray0);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Select Sort 1: " + finishTime + "ms");
        double[] item1 = new driver().selectSort(testArray1);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Select Sort 2: " + finishTime + "ms");
        double[] item2 = new driver().selectSort(testArray2);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Select Sort 3: " + finishTime + "ms");
        double[] item3 = new driver().selectSort(testArray3);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Select Sort 4: " + finishTime + "ms");
        double[] item4 = new driver().selectSort(testArray4);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Select Sort 5: " + finishTime + "ms");
        double[] item5 = new driver().selectSort(testArray5);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Select Sort 6: " + finishTime + "ms");
        double[] item6 = new driver().selectSort(testArray6);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Select Sort 7: " + finishTime + "ms");
        double[] item7 = new driver().selectSort(testArray7);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Select Sort 8: " + finishTime + "ms");
        double[] item8 = new driver().selectSort(testArray8);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Select Sort 9: " + finishTime + "ms");
        double[] item9 = new driver().selectSort(testArray9);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Select Sort 10: " + finishTime + "ms");
        */

        // run the bubble sorts
        /*
        double[] item0 = new driver().bubbleSort(testArray0);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Bubble Sort 1: " + finishTime + "ms");
        double[] item1 = new driver().bubbleSort(testArray1);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Bubble Sort 2: " + finishTime + "ms");
        double[] item2 = new driver().bubbleSort(testArray2);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Bubble Sort 3: " + finishTime + "ms");
        double[] item3 = new driver().bubbleSort(testArray3);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Bubble Sort 4: " + finishTime + "ms");
        double[] item4 = new driver().bubbleSort(testArray4);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Bubble Sort 5: " + finishTime + "ms");
        double[] item5 = new driver().bubbleSort(testArray5);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Bubble Sort 6: " + finishTime + "ms");
        double[] item6 = new driver().bubbleSort(testArray6);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Bubble Sort 7: " + finishTime + "ms");
        double[] item7 = new driver().bubbleSort(testArray7);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Bubble Sort 8: " + finishTime + "ms");
        double[] item8 = new driver().bubbleSort(testArray8);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Bubble Sort 9: " + finishTime + "ms");
        double[] item9 = new driver().bubbleSort(testArray9);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Bubble Sort 10: " + finishTime + "ms");
        */

        // run the insertion sorts
        /*
        double[] item0 = new driver().insertionSort(testArray0);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Insertion Sort 1: " + finishTime + "ms");
        double[] item1 = new driver().insertionSort(testArray1);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Insertion Sort 2: " + finishTime + "ms");
        double[] item2 = new driver().insertionSort(testArray2);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Insertion Sort 3: " + finishTime + "ms");
        double[] item3 = new driver().insertionSort(testArray3);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Insertion Sort 4: " + finishTime + "ms");
        double[] item4 = new driver().insertionSort(testArray4);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Insertion Sort 5: " + finishTime + "ms");
        double[] item5 = new driver().insertionSort(testArray5);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Insertion Sort 6: " + finishTime + "ms");
        double[] item6 = new driver().insertionSort(testArray6);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Insertion Sort 7: " + finishTime + "ms");
        double[] item7 = new driver().insertionSort(testArray7);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Insertion Sort 8: " + finishTime + "ms");
        double[] item8 = new driver().insertionSort(testArray8);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Insertion Sort 9: " + finishTime + "ms");
        double[] item9 = new driver().insertionSort(testArray9);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Insertion Sort 10: " + finishTime + "ms");
        */

        // run the merge sorts
        /*
        double[] item0 = new driver().mergesort(testArray0);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Merge Sort 1: " + finishTime + "ms");
        double[] item1 = new driver().mergesort(testArray1);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Merge Sort 2: " + finishTime + "ms");
        double[] item2 = new driver().mergesort(testArray2);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Merge Sort 3: " + finishTime + "ms");
        double[] item3 = new driver().mergesort(testArray3);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Merge Sort 4: " + finishTime + "ms");
        double[] item4 = new driver().mergesort(testArray4);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Merge Sort 5: " + finishTime + "ms");
        double[] item5 = new driver().mergesort(testArray5);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Merge Sort 6: " + finishTime + "ms");
        double[] item6 = new driver().mergesort(testArray6);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Merge Sort 7: " + finishTime + "ms");
        double[] item7 = new driver().mergesort(testArray7);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Merge Sort 8: " + finishTime + "ms");
        double[] item8 = new driver().mergesort(testArray8);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Merge Sort 9: " + finishTime + "ms");
        double[] item9 = new driver().mergesort(testArray9);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Merge Sort 10: " + finishTime + "ms");
        */

        // run the quick sort
        /*
        double[] item0 = new driver().quickSort(testArray0, 0, testArray0.length - 1);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Quick Sort 1: " + finishTime + "ms");
        double[] item1 = new driver().quickSort(testArray1, 0, testArray1.length - 1);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Quick Sort 2: " + finishTime + "ms");
        double[] item2 = new driver().quickSort(testArray2, 0, testArray2.length - 1);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Quick Sort 3: " + finishTime + "ms");
        double[] item3 = new driver().quickSort(testArray3, 0, testArray3.length - 1);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Quick Sort 4: " + finishTime + "ms");
        double[] item4 = new driver().quickSort(testArray4, 0, testArray4.length - 1);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Quick Sort 5: " + finishTime + "ms");
        double[] item5 = new driver().quickSort(testArray5, 0, testArray5.length - 1);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Quick Sort 6: " + finishTime + "ms");
        double[] item6 = new driver().quickSort(testArray6, 0, testArray6.length - 1);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Quick Sort 7: " + finishTime + "ms");
        double[] item7 = new driver().quickSort(testArray7, 0, testArray7.length - 1);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Quick Sort 8: " + finishTime + "ms");
        double[] item8 = new driver().quickSort(testArray8, 0, testArray8.length - 1);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Quick Sort 9: " + finishTime + "ms");
        double[] item9 = new driver().quickSort(testArray9, 0, testArray9.length - 1);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Quick Sort 10: " + finishTime + "ms");
        */

        // run the Improved Merge Sort (Part 2)
        /*
        ArrayList<ArrayList<Double>> item0 = new driver().presort(testArray0);
        ArrayList<Double> thing0 = new driver().improvedMerge(item0);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Improved Merge Sort 1: " + finishTime + "ms");
        ArrayList<ArrayList<Double>> item1 = new driver().presort(testArray1);
        ArrayList<Double> thing1 = new driver().improvedMerge(item1);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Improved Merge Sort 2: " + finishTime + "ms");
        ArrayList<ArrayList<Double>> item2 = new driver().presort(testArray2);
        ArrayList<Double> thing2 = new driver().improvedMerge(item2);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Improved Merge Sort 3: " + finishTime + "ms");
        ArrayList<ArrayList<Double>> item3 = new driver().presort(testArray3);
        ArrayList<Double> thing3 = new driver().improvedMerge(item3);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Improved Merge Sort 4: " + finishTime + "ms");
        ArrayList<ArrayList<Double>> item4 = new driver().presort(testArray4);
        ArrayList<Double> thing4 = new driver().improvedMerge(item4);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Improved Merge Sort 5: " + finishTime + "ms");
        ArrayList<ArrayList<Double>> item5 = new driver().presort(testArray5);
        ArrayList<Double> thing5 = new driver().improvedMerge(item5);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Improved Merge Sort 6: " + finishTime + "ms");
        ArrayList<ArrayList<Double>> item6 = new driver().presort(testArray6);
        ArrayList<Double> thing6= new driver().improvedMerge(item6);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Improved Merge Sort 7: " + finishTime + "ms");
        ArrayList<ArrayList<Double>> item7 = new driver().presort(testArray7);
        ArrayList<Double> thing7 = new driver().improvedMerge(item7);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Improved Merge Sort 8: " + finishTime + "ms");
        ArrayList<ArrayList<Double>> item8 = new driver().presort(testArray8);
        ArrayList<Double> thing8 = new driver().improvedMerge(item8);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Improved Merge Sort 9: " + finishTime + "ms");
        ArrayList<ArrayList<Double>> item9 = new driver().presort(testArray9);
        ArrayList<Double> thing9 = new driver().improvedMerge(item9);
        endTime = System.currentTimeMillis();
        finishTime = endTime - startTime;
        System.out.println("Improved Merge Sort 10: " + finishTime + "ms");
        */

    }
}
