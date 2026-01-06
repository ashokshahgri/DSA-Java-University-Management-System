public class MyList<T> {
    private Node<T> head;

    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newNode;
        }
    }


    public void clear() { head = null; }

    public Node<T> getHead() { return head; }

    // --- LINEAR SEARCH (Standard) ---
    public boolean removeById(String id) {
        if(head == null) return false;

        boolean headMatch = false;
        if (head.data instanceof Person && ((Person)head.data).getId().equals(id)) headMatch = true;
        if (head.data instanceof Course && ((Course)head.data).getId().equals(id)) headMatch = true;

        if (headMatch) {
            head = head.next;
            return true;
        }

        Node<T> current = head;
        Node<T> prev = null;

        while(current != null) {
            boolean match = false;
            if (current.data instanceof Person && ((Person)current.data).getId().equals(id)) match = true;
            if (current.data instanceof Course && ((Course)current.data).getId().equals(id)) match = true;

            if(match) {
                if(prev != null) prev.next = current.next;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public T findById(String id) {
        Node<T> temp = head;
        while(temp != null) {
            if (temp.data instanceof Person && ((Person)temp.data).getId().equals(id)) return temp.data;
            if (temp.data instanceof Course && ((Course)temp.data).getId().equals(id)) return temp.data;
            temp = temp.next;
        }
        return null;
    }

    // --- BUBBLE SORT (Required for Binary Search) ---
    public void bubbleSort() {
        if (head == null || head.next == null) return;

        Node<T> current = head;
        Node<T> index = null;
        T temp;

        while (current != null) {
            index = current.next;
            while (index != null) {
                String id1 = "";
                String id2 = "";

                if (current.data instanceof Person) id1 = ((Person)current.data).getId();
                else if (current.data instanceof Course) id1 = ((Course)current.data).getId();

                if (index.data instanceof Person) id2 = ((Person)index.data).getId();
                else if (index.data instanceof Course) id2 = ((Course)index.data).getId();

                // Swap Logic (Swapping Data, not Pointers)
                if (id1.compareTo(id2) > 0) {
                    temp = current.data;
                    current.data = index.data;
                    index.data = temp;
                }
                index = index.next;
            }
            current = current.next;
        }
    }

    // --- NEW: BINARY SEARCH ---
    // Helper to find the middle node
    private Node<T> getMiddle(Node<T> start, Node<T> last) {
        if (start == null) return null;
        Node<T> slow = start;
        Node<T> fast = start.next;
        while (fast != last) {
            fast = fast.next;
            if (fast != last) {
                slow = slow.next;
                fast = fast.next;
            }
        }
        return slow;
    }

    public T binarySearch(String id) {
        // Binary search REQUIRES sorted data.

        Node<T> start = head;
        Node<T> last = null;

        do {
            Node<T> mid = getMiddle(start, last);
            if (mid == null) return null;

            String midId = "";
            if (mid.data instanceof Person) midId = ((Person)mid.data).getId();
            else if (mid.data instanceof Course) midId = ((Course)mid.data).getId();

            if (midId.equals(id)) {
                return mid.data; // FOUND
            }
            else if (midId.compareTo(id) < 0) {
                start = mid.next; // Go Right
            }
            else {
                last = mid; // Go Left
            }
        } while (last == null || last != start);

        return null; // Not Found
    }
}