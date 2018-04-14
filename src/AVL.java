import java.util.LinkedList;

public class AVL<T> {
	private AVLNode<T> root;
	
	public AVL(){
		root = null;
	}
	
	/**
	 * Insert new Node to the tree.
	 * @param key
	 * @param data
	 */
	public void insert(int key, T data){
		if(root == null)
			root = new AVLNode<>(key,data);
		else {
			root.insert(key,data);
			AVLNode<T> temp = root;
			while(temp.getFather() !=null){ // in case the root changed on insert
				temp = temp.getFather();
			}
			this.root = temp;
		}
	}

	public T search(int key){
			AVLNode<T> temp = root;
			while (temp !=null){
				if(temp.getKey()>key)
					temp = temp.getLeftChild();
				else if(temp.getKey()<key)
					temp = temp.getRightChild();
				else 
					return (T)temp.getData();
			}
			return null;
		}
	/**
	 * Search the Node with the key 'key'. 
	 * @param key
	 * @return
	 */
	public AVLNode<T> searchNode(int key){
		AVLNode<T> temp = root;
		while (temp !=null && temp.getKey()!=key){
			if(temp.getKey()>key)
				temp = temp.getLeftChild();
			else if(temp.getKey()<key)
				temp = temp.getRightChild();
		}
		if(temp!= null &&temp.getKey()==key)
			return temp;
		return null;
	}
	/**
	 * 
	 * @return the root of the tree
	 */
	public AVLNode<T> getRoot(){
		return this.root;
	}
	
	public AVLNode<T> Minimum(){
		if(root !=null)
			return root.Minimum();
		return null;
	}
	
	/**
	 * get the Node with the maximum key of the tree.
	 * @return
	 */
	public AVLNode<T> Maximum(){
		if(root!=null)
			return root.Maximum();
		return null;
	}
	
	private AVLNode<T> searchAtleastKey(int key) {
		if (root == null)
				return null;
		AVLNode<T> temp = root;
		while (temp != null && temp.getKey() != key) {
			if (temp.getKey() > key && temp.getLeftChild() != null)
				temp = temp.getLeftChild();
			else if (temp.getKey() < key && temp.getRightChild() != null)
				temp = temp.getRightChild();
			else break;
		}
		if(temp.getKey()>=key)
			return temp;
		else{
			AVLNode<T> temp_successor = temp.getSuccessor();
			if(temp_successor != null && temp_successor.getKey()>key)
				return temp_successor;		
		}
		return null;
	}
	/**
	 * return an array of Nodes' data in the Range [min,max]
	 * @param minKey
	 * @param maxKey
	 * @return
	 */
	public Object[] getRenge(int minKey, int maxKey){
		LinkedList<Object> list = new LinkedList<Object>();
		AVLNode<T> minNode = searchAtleastKey(minKey);
		while (minNode != null && minNode.getKey() <= maxKey) {
				list.add(minNode.getData());
				minNode=minNode.getSuccessor();
			}
		return list.toArray();
	}
	
		/**
	 * Delete a node with whe key 'key' 
	 * @param key
	 * @
	 */
	public void delete(int key){
	root = 	root.delete(key);

	}
	
	public int getStepsToMIn(){
		return root.getStepsToMIn();
	}

}

	

