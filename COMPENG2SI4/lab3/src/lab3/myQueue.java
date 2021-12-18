package lab3;

public class myQueue<E> {
	LL<E> front, end;
	public myQueue() {
		front =new LL<E>(null,null);
		end = front;
	}
	public boolean isEmpty() {
		return(front == end);
	}
	public void enqueue(E v) {
		end.next = new LL<E>(v,null); //insert at the next and move to next
		end = end.next;
	}
	public E dequeue() {
		if(isEmpty())
			return null; //empty queue return -1
		else {
			if(end == front.next)
				end= front; //now queue is empty
			E tem = front.next.element; //value at second position
			front.next = front.next.next;
			return tem;
		}
	}
}
