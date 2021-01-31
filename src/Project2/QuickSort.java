package Project2;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuickSort {
    public void quickSort(List<Transaction> A){
        quickSort(A, 0, A.size() -1);
    }

    public void quickSort(List<Transaction> A, int low, int high) {
        if (low < high+1){
            int p = partition(A, low, high);
            quickSort(A, low, p-1);
            quickSort(A, p+1, high);
        }
    }

    private void swap(List<Transaction> A, int index1, int index2){
        Collections.swap(A, index1, index2);
    }

    private int getPivot(int low, int high){
        Random rand = new Random();
        return rand.nextInt((high-low) +1) +low;
    }

    private int partition(List<Transaction> A, int low, int high){
        swap(A, low, getPivot(low, high));
        int border = low +1;
        for(int i = border; i <= high; i++){
            if(Double.parseDouble(A.get(i).getID()) < Double.parseDouble(A.get(low).getID())){
                swap(A, i, border++);
            }
        }
        swap(A, low, border-1);
        return  border-1;
    }
}