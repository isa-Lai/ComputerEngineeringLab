package lab3;

public class myStack<E> {
	LL<E> head;
	public myStack() {
		head = null;
	}
	public boolean isEmpty() {
		return (head==null);
	}
	public void push(E v) {
		head = new LL<E>(v,head);
	}
	public E pop() {
		if(isEmpty())
			return null; // empty stack situation
		E tem = head.element;
		head = head.next;
		return tem;
	}
	public E top() {
		if(isEmpty())
			return null; // empty stack situation
		return head.element;
	}
}
