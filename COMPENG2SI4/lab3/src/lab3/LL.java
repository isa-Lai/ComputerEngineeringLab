package lab3;

public class LL<E> {
	E element;
	LL<E> next;
	LL(E i, LL<E> nextlist){
		element = i;next = nextlist;
	}
}
