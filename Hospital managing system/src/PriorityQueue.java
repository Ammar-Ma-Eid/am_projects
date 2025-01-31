import java.util.ArrayList;
import java.util.List;

public class PriorityQueue {
    private List<PatientPriority> heap;

    public PriorityQueue() {
        heap = new ArrayList<>();
    }

    public void insert(PatientPriority patient) {
        heap.add(patient);
        bubbleUp(heap.size() - 1);
    }

    public PatientPriority extractMin() {
        if (heap.isEmpty()) {
            return null;
        }
        PatientPriority root = heap.get(0);
        PatientPriority last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            bubbleDown(0);
        }
        return root;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void remove(PatientPriority patient) {
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i).getEmail().equals(patient.getEmail())) {
                heap.set(i, heap.get(heap.size() - 1));
                heap.remove(heap.size() - 1);
                bubbleDown(i);
                break;
            }
        }
    }

    public List<PatientPriority> getAllSorted() {
        List<PatientPriority> sortedList = new ArrayList<>(heap);
        for (int i = 0; i < sortedList.size() - 1; i++) {
            for (int j = i + 1; j < sortedList.size(); j++) {
                if (sortedList.get(i).getInjuryLevel() > sortedList.get(j).getInjuryLevel()) {
                    PatientPriority temp = sortedList.get(i);
                    sortedList.set(i, sortedList.get(j));
                    sortedList.set(j, temp);
                }
            }
        }
        return sortedList;
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(index).getInjuryLevel() < heap.get(parentIndex).getInjuryLevel()) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    private void bubbleDown(int index) {
        int size = heap.size();
        while (index < size) {
            int leftChildIndex = 2 * index + 1;
            int rightChildIndex = 2 * index + 2;
            int smallestIndex = index;

            if (leftChildIndex < size && heap.get(leftChildIndex).getInjuryLevel() < heap.get(smallestIndex).getInjuryLevel()) {
                smallestIndex = leftChildIndex;
            }
            if (rightChildIndex < size && heap.get(rightChildIndex).getInjuryLevel() < heap.get(smallestIndex).getInjuryLevel()) {
                smallestIndex = rightChildIndex;
            }
            if (smallestIndex != index) {
                swap(index, smallestIndex);
                index = smallestIndex;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        PatientPriority temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
