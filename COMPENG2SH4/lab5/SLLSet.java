
public class SLLSet {
    private int size;
    private SLLNode head;

    public SLLSet() {
        this.size = 0;
        this.head = null;
    }

    public SLLSet(int[] sortedArray) {
        this.size = sortedArray.length;//get length
        //access in each node and set value
        if (this.size!=0) {
            this.head = new SLLNode(sortedArray[0],null);
            SLLNode temp = this.head;
            for (int i=1; i<this.size;i++) {
            	temp.next = new SLLNode(sortedArray[i],null);
            	temp = temp.next;
            }
        }
        //for empty set
        else {
        	this.size = 0;
        	this.head=null;
        }

    }

    public int getSize() {
		
        return this.size;
    }

    public SLLSet copy() {
        SLLSet dcopy = new SLLSet();
        dcopy.size = this.size;//copy size
        if(this.head==null) return dcopy;
        
        SLLNode dtemp = new SLLNode (this.head.value ,null);//set head
        SLLNode temp = this.head.next;
        dcopy.head = dtemp;
        
        //access into each node and copy node
        while(temp!=null) {
        	dtemp.next = new SLLNode(temp.value ,null);
        	dtemp = dtemp.next;
        	temp = temp.next;
        }
        return dcopy;
    }

    public boolean isIn(int v) {
        SLLNode temp = this.head;
    	while(temp!=null) {
        	if(temp.value == v) {
        		return true;
        	}
        	temp = temp.next;

        }
        return false;
    }

    public void add(int v) {
        SLLNode output = this.head;
        boolean havevalue = this.isIn(v); //check if the element exist
        SLLNode temp;
        
        //if this set is empty just add to head
        //if the adding value is the head, add it first
        if (this.head==null) {
        	this.head = new SLLNode(v,null);
        	this.size+=1;
        }
        else if(this.head.next.value>v) {
        	temp = this.head;
        	this.head = new SLLNode (v,temp);
        	this.size+=1;
        }
        else if(havevalue==false){
        	//access into each node
        	//check the correct position 
	        while(output.next!=null) {
	        	if(output.next.value> v ) {
	        		break;
	        	}
	        	output = output.next;
	
	        }
	        
	        //if the value exist dont do anything.
	       
	        if (output.next!=null) {
	           	 temp = output.next;
	       	}
	       	else    temp = null;
            output.next = new SLLNode(v,temp);
	        this.size +=1;//size increase
	        
        }
    }

    public void remove(int v) {
    	SLLNode temp = this.head;
        if(this.isIn(v)) {
        	//situation when remove head
        	if(this.head.value==v) {
        		this.head = this.head.next;
        	}
        	
        	//situation of other position
        	else {
	        	while(temp.next.value!=v) {
		        	temp = temp.next;
		
		        }
	        	temp.next= temp.next.next;
        
        	}
        	this.size-=1; //size decrease
        }
    }

    public SLLSet union(SLLSet s) {


    	SLLSet output = new SLLSet();
        SLLNode temp1 = this.head;
        SLLNode temp2= s.head;
        SLLNode tempout;
        
        //set head first
        if(temp1 == null && temp2 == null) return output;
        else if (temp2 == null) {
        	output.head= new SLLNode(temp1.value,null);
        	temp1 = temp1.next;
        }
        else if (temp1 == null) {
        	output.head= new SLLNode(temp2.value,null);
        	temp2 = temp2.next;
        }
        else if ( temp1.value<temp2.value) {
        	output.head= new SLLNode(temp1.value,null);
        	temp1 = temp1.next;
        }
        else if ( temp1.value>temp2.value) {
        	output.head= new SLLNode(temp2.value,null);
        	temp2 = temp2.next;
        }
        else {
        	output.head= new SLLNode(temp2.value,null);
        	temp1 = temp1.next;
        	temp2 = temp2.next;
        }
        //add the remaining
        tempout = output.head;
        output.size+=1;
        while(temp1!=null && temp2!=null) {
        	
        		if (temp1.value<temp2.value) {
        			tempout.next= new SLLNode(temp1.value,null);
                	temp1 = temp1.next;
                }
                else if (temp1.value>temp2.value) {
                	tempout.next= new SLLNode(temp2.value,null);
                	temp2 = temp2.next;
                }
                else {
                	tempout.next= new SLLNode(temp1.value,null);
                	temp1 = temp1.next;
                	temp2 = temp2.next;
                }
        		tempout= tempout.next;
        		output.size+=1;
        	
        }
        
        //add remaining which does not need compare
        while(temp1!=null) {
        	tempout.next= new SLLNode(temp1.value,null);
        	temp1 = temp1.next;
        	tempout= tempout.next;
    		output.size+=1;
        }
        while(temp2!=null) {
        	tempout.next= new SLLNode(temp2.value,null);
        	temp2 = temp2.next;
        	tempout= tempout.next;
    		output.size+=1;
        }

        return output;
    }

    public SLLSet intersection(SLLSet s) {
    	SLLSet output = new SLLSet();
        SLLNode temp1 = this.head;
        SLLNode temp2;
        SLLNode tempout = output.head;
        SLLNode addvalue;
        boolean ishead = true;
        //compare each element in set
        while(temp1!=null) {
        	addvalue = null;
        	temp2 = s.head;
        	while (temp2!=null) {
        		if(temp1.value==temp2.value) {
        			addvalue = temp2;
        			break;
        		}
        		temp2= temp2.next;
        	}
        	
        	//add those element that is the same
        	if (ishead && addvalue!=null) {
        		output.head = new SLLNode(addvalue.value,null);
        		tempout = output.head;
        		output.size+=1;
        		ishead=false;
        	}
        	else if(addvalue!=null){
        		tempout.next = new SLLNode (addvalue.value,null);
        		tempout= tempout.next;
        		output.size +=1;
        	}
        	temp1=temp1.next;
        	
        }
        	

        return output;
    }

    public SLLSet difference(SLLSet s) {
        SLLSet output = this.copy();
        SLLSet removepart = this.intersection(s); // use intersection to check the same lement
        SLLNode temp = removepart.head;
        
        //remove all the same element
	    while (temp!=null) {
	      	output.remove(temp.value);
	       	temp=temp.next;
	    }

        return output;
    }

    public static SLLSet union(SLLSet[] sArray) {
        SLLSet ss = new SLLSet();
        for (int i=0;i<sArray.length;i++) {
        	ss = ss.union(sArray[i]);
        }
        

        return ss;
    }

    @Override
    public String toString() {
		if(this.size == 0) 
			return "";
		String result = new String("");
		SLLNode temp = this.head;
		result += (String.valueOf(temp.value));
		for (int i=0;i<this.size-1;i++) {
			temp = temp.next;
			result += (", "+String.valueOf(temp.value));
		}	
        return result;
    }
}
