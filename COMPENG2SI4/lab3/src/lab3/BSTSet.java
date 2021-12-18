package lab3;



public class BSTSet {
	private TNode root;
	//time O(1)
	public BSTSet() {
		root = null;
	}
	//time depend on time for add()
	//time T(n) =  n*T(add())
	//for non bonus add: worse O(n^2), best O(1), avergae O(nlogn)
	//with bonus: O(n^2)average
	public BSTSet(int[] input) {
		//if input nothing, empty tree
		if(input.length == 0||input == null){
			root = null;//error handling
			return; 
		}
		for(int i = 0;i<input.length;i++) {
			add(input[i]);
		}
	}

	
	//worse O(n) best O(1) average O(logn)
	public boolean isIn(int v) {
		if(root == null)
			return false;//empty absolute not in it
		TNode tem = root;
		do {
			if(v<tem.element)
				tem = tem.left; // less than go left
			else if (v>tem.element)
				tem = tem.right; //more than go right
			else
				return true; //found 
		}while(tem!=null); //break if not found
		return false;
	}
	
	
	/*
	//without bonus worse O(n) best O(1) average O(logn)
	public void add(int v) {
		if(root == null)
			root = new TNode(v,null,null);//empty tree just insert at root
		else {
			TNode tem = root;
			TNode parent;
			do {
				if(v<tem.element) {
					parent = tem;
					tem = tem.left; // less than go left
				}
				else if (v>tem.element) {
					parent = tem;
					tem = tem.right; //more than go right
				}
				else
					return; //if value already exist just finish this insertion
			}while(tem!=null); //loop end when reach the position
			if(v> parent.element)
				parent.right = new TNode(v,null,null);
			else
				parent.left = new TNode(v,null,null);
		}
		
	}*/
	
	/*
	//without bonus worse O(n) best O(1) average O(log n)
	public boolean remove(int v) {
		if(root == null)
			return false; // empty tree has nothing to remove
		if(root.element == v&&root.left==null&&root.right ==null) {
			root = null;  //when there is only root, become empty
			return true;
		}
		try {
			root = delete(v,root);
			return true;//if no error means success 
			
		}
		catch (IllegalArgumentException e) {
			return false; // if the element not found
		}
	}
	//helper function
	private TNode delete(int v, TNode t) {
		if(t == null)
			throw new IllegalArgumentException();//element not found
		if(v<t.element)
			t.left = delete(v,t.left);//if smaller go left
		else if(v>t.element)
			t.right = delete(v,t.right);//if smaller go right
		//situation when have two child
		else if(t.right!=null&&t.left !=null) {
			//find the left most at right child
			TNode tem = t.right,parent = t;
			while(tem.left!=null) {
				parent = tem;
				tem = tem.left;
			}
			
			//if the right of t is just the min, replave with right child
			if(parent.element == t.element) {
				t.element = tem.element;
				t.right = tem.right;
			}
			//swtich that value into t and delete the left most value
			else {
				t.element = tem.element;
				parent.left = null;
			}
			
		}
		//situation when one or zero child
		else
			t = (t.right!=null)? t.right :t.left ;
		return t;
	}*/
/* bonus helper function---------------------------------
 * different type of rotation
 */
	//O(1)
	private TNode RRrotate(TNode t) {
		TNode tem = t.right; //make t.right be subroot
		if(tem.left!=null)
			t.right = tem.left; //left child of tem become right child of t
		else
			t.right = null; //if no left child, just break the bond between tem and tem.left
		tem.left = t; //rotate left
		
		return tem;
	}
	private TNode LLrotate(TNode t) {
		TNode tem = t.left;
		if(tem.right!=null)
			t.left = tem.right;
		else
			t.left = null;
		tem.right = t; //rotate right
		
		return tem;
	}
	private TNode RLrotate(TNode t) {
		t.right = LLrotate(t.right);
		TNode tem = RRrotate(t);
		return tem;
	}
	private TNode LRrotate(TNode t) {
		t.left = RRrotate(t.left);
		TNode tem = LLrotate(t);
		return tem;
	}
	//bonus add --------------------------
	//add visit each node position O(logn), check height for this specific node = 2^k (k is height of this subnode)
	//run time = 2^0+2^1+....2^(log2(n))  therefore T(n) = O(n)
	public void add(int v) {
		if(!isIn(v)) //if already exist break
			this.root = add(v,root);//modify whole root
	}
	private TNode add(int v, TNode t) {
		//at position add node
		if(t==null)
			t = new TNode(v,null,null);
		//when less than this node go left
		else if(v<t.element) {
			t.left = add(v,t.left);
			
			//check if the tree is not balanced
			if(height(t.left)-height(t.right)==2) {
				//if inserting at left do LL rotation
				if(v<t.left.element)
					t = LLrotate(t);
				//if inserting at right do LR rotation
				else
					t = LRrotate(t);
			}
		}
		//when larger than this node go right
		else if(v>t.element) {
			t.right = add(v,t.right);
			
			//check if the tree is not balanced
			if(height(t.left)-height(t.right)==-2) {
				//if inserting at left do RL rotation
				if(v<t.right.element)
					t = RLrotate(t);
				//if inserting at right do RR rotation
				else
					t = RRrotate(t);
			}
		}
		return t;
	}
	//bonus remove--------------------------
	//remove visit each node position O(logn), check height for this specific node = 2^k (k is height of this subnode)
	//run time = 2^0+2^1+....2^(log2(n))  therefore T(n) = O(n)
	public boolean remove(int v) {
		if (root == null || !isIn(v))
			return false; // empty or not exist cannot remove
		this.root = remove(v,root);
		return true;
	}
	public TNode remove(int v, TNode t) {
		//if not found
		if(t==null)
			return null;
		else if(v<t.element) {
			t.left = remove(v,t.left);
			
			//check if the tree is not balanced
			if(height(t.left)-height(t.right)==-2) {
				//find where node is delete
				if(height(t.right.left)>height(t.right.right))
					t = RLrotate(t);
				else
					t = RRrotate(t);
			}
		}
		//when larger than this node go right
		else if(v>t.element) {
			t.right = remove(v,t.right);
			
			//check if the tree is not balanced
			if(height(t.left)-height(t.right)==2) {
				//find where node is delete
				if(height(t.left.left)>height(t.left.right))
					t = LLrotate(t);
				else
					t = LRrotate(t);
			}
		}
		//deleting process
		//situation when have two child
			else if(t.right!=null&&t.left !=null) {
				//find the left most at right child
				TNode tem = t.right,parent = t;
				while(tem.left!=null) {
					parent = tem;
					tem = tem.left;
				}
				//if the right of t is just the min, replace it with the right
				if(parent.element == t.element) {
					t.element = tem.element;
					t.right = tem.right;
				}
				//swtich that value into t and delete the left most value
				else {
					t.element = tem.element;
					parent.left = null;
				}
			}
			//situation when one or zero child
			else
				t = (t.right!=null)? t.right :t.left ;
			return t;
		
	}
	
	
//bonus end ----------------------------------------------
	
	//helper function
	public TNode copy(TNode t) {
		if(t!=null) {
			TNode tem = new TNode(t.element,null,null);
			tem.left = copy(t.left);
			tem.right = copy(t.right);
			return tem;
		}
		return null;
	}
	
	//if both size n
	//without bonus best O(1) worse O(n^2) avergae O(nlogn)
	//with  bonus O(n^2)
	public BSTSet union(BSTSet s) {
		BSTSet result = new BSTSet();
		if(root == null && s.root== null)
			return result;
		if(root == null)
			result.root = copy(s.root); //if this empty, return s
		else if(s.root == null)
			result.root = copy(root); // if s empty, return this 
		else {
			result.root = copy(root);//copy this first
			union(result,s.root); //add s to result
			
		}
		
		return result;
	}
	//helper function
	private void union(BSTSet result, TNode t) {
		if(t!=null) {
			result.add(t.element);
			union(result,t.left);//add value at the left
			union(result,t.right); //add value at the right
		}
	}
	
	//if both size n
	//without bonus best O(1) worse O(n^2) avergae O(nlogn)
	//with  bonus O(n^2)
	public BSTSet intersection(BSTSet s) {
		BSTSet result = new BSTSet();
		if(root == null || s.root== null)
			return result; //if one empty, intersection will be empty
		else {
			result.root = copy(root);//copy this first
			intersection(result,s,root); //check and only keep same value
			
		}
		return result;
	}
	//helper function
	private void intersection(BSTSet result,BSTSet s, TNode t) {
		if(t!=null) {
			//remove only when the element does not appear in s
			if(!s.isIn(t.element)){
				result.remove(t.element);
			}
			intersection(result,s,t.left);//remove value at the left
			intersection(result,s,t.right); //remove value at the right
		}
		
	}
	
	//if both size n
	//without bonus best O(1) worse O(n^2) avergae O(nlogn)
	//with  bonus O(n^2)
	public BSTSet difference(BSTSet s) {
		BSTSet result = new BSTSet();
		if(root == null )
			return result;//if this empty, return null
		else if(s.root == null)
			result.root = copy(root); // if s empty, return this 
		else {
			result.root = copy(root);//copy this first
			difference(result,s.root); //remove s from this
			
		}
		return result;
	}
	//helper function
	private void difference(BSTSet result, TNode t) {
		if(t!=null) {
			result.remove(t.element);
			difference(result,t.left);//remove value at the left
			difference(result,t.right); //remove value at the right
		}
	}
	
	// runtime O(n)
	public int size() {
		if(root == null)
			return 0;
		else {
			return size(root);
		}
	}
	//helper
	private int size(TNode t) {
		if(t==null) {
			return 0;//break until reach nothing
		}
		int left = size(t.left); //count value on the left
		int right = size(t.right);//count value on the right
		return (1+left+right);//size on this node is all left plus all right plus this
	}
	
	//runtime O(n)
	public int height() {
		if(root == null)
			return -1;
		else {
			return height(root);
		}
	}
	//helper height of this sub tree
	private int height(TNode t) {
		if(t==null) {
			//since height start at -1
			return -1;//break until reach nothing
		}
		int left = height(t.left); //count value on the left
		int right = height(t.right);//count value on the right
		return (1+(left>right?left:right));//choose the longest line
	}
	
	//runtime O(n)
	public void printBSTSet() {
		if(root == null)
			System.out.println("The set is empty");
		else {
			System.out.println("The set elements are: ");
			printBSTSet(root);
			System.out.print("\n");
		}
	}
	
	public void printBSTSet(TNode t) {
		if(t!=null) {
			printBSTSet(t.left);
			System.out.print(" "+t.element +" ");
			printBSTSet(t.right);
		}
	}
	
	//runtime O(n)
	public void printNonRec() {
		if(root==null) {
			System.out.println("Empty BST");
			return;
		}
		myStack<TNode> S = new myStack<TNode>();
		TNode t = root;
		//when there is value not visit or stack not empty
		while(t!=null || !S.isEmpty()) {
			//if not visit all value visit until the left most in this branch
			while(t!=null) {
				S.push(t);
				t =t.left;
			}
			TNode currentNode = S.pop();
			System.out.print(" "+currentNode.element +" ");//print value at current node
			t = currentNode.right;
		}
		System.out.print("\n");
	}
	
	//runtime O(n)
	public void printLevelOrder() {
		if(root==null) {
			System.out.println("Empty BST");
			return;
		}
		myQueue<TNode> Q = new myQueue<TNode>();
		Q.enqueue(root);
		TNode t;
		//store value in level order
		do {
			t = Q.dequeue();
			if(t.left!=null)//if left have no child not enqueue
				Q.enqueue(t.left);
			if(t.right!=null)//if right have no child not enqueue
				Q.enqueue(t.right);
			System.out.print(" "+t.element +" ");//print element
		}while(!Q.isEmpty());
		System.out.print("\n");
		
	}
	
	public TNode getRoot() {
		return root;
	}
	
}
