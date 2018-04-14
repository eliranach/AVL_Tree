public class AVLNode<T> {
	private AVLNode<T> father;
	private AVLNode<T> left;
	private AVLNode<T> right;
	private T data;
	private int height;
	private int key;
	private AVLNode<T> successor;
	private AVLNode<T> predecessor;

	public AVLNode(int key, T data) {
		this.key = key;
		this.data = data;

	}



	// constructor
	public AVLNode(int key, T data, AVLNode<T> father) {
		this.key = key;
		this.data = data;
		this.father = father;
		height = 0;

	}

	public AVLNode<T> getLeftChild() {
		return left;
	}

	public AVLNode<T> getRightChild() {
		return right;
	}

	public AVLNode<T> getFather() {
		return father;
	}

	public int getKey() {
		return key;
	}

	public T getData() {
		return data;
	}

	private static int getHeight(AVLNode node) {
		int ans = -1;
		if (node == null)
			return ans;
		int rightHeight = -1;
		int leftHeight = -1;
		if (node.left != null)
			leftHeight = node.left.height;
		if (node.right != null)
			rightHeight = node.right.height;

		return Math.max(rightHeight, leftHeight) + 1;

	}

	@Override
	public String toString() {

		String s = "";

		if (getLeftChild() != null) {
			s += "(";
			s += getLeftChild().toString();
			s += ")";
		}
		s += getKey();

		if (getRightChild() != null) {
			s += "(";
			s += getRightChild().toString();
			s += ")";
		}

		return s;
	}

	/**
	 * Insert new Node to sub-Tree of Node this.
	 * 
	 * @param key
	 * @param data
	 */

	public void insert(int key, T data) {
		AVLNode<T> y = null;
		AVLNode<T> x = this; // X <- T.root
		while (x != null) {
			y = x;
			if (key <= x.key)
				x = x.left;
			else
				x = x.right;
		}
		boolean heightChanged = false; // for later balancing, if will be requested
		AVLNode<T> z = new AVLNode<T>(key, data, y);
		if (key <= y.key) { // the new AVLNode will be y.left
			y.left = z;
			y.predecessor = z;
			z.successor = y;
			z.setPredecessor();
			if (y.right == null)
				heightChanged = true;
		} else {
			y.right = z;
			y.successor = z;
			z.predecessor = y;
			z.setSuccessor();
			if (y.left == null)
				heightChanged = true;
		}
		if (heightChanged == true) {
			y.height++; // update the height of parent 'y'
			checkBalance(z); // check if the balancing of the tree violated
		}
	}

	/**
	 * Update the height of the nodes after insert/delete. when the balance violated
	 * it fix it
	 * 
	 * @param AVLNode
	 *            z, a Node in the AVL-Tree.
	 */
	private static AVLNode checkBalance(AVLNode z) {
		AVLNode y = null;
		AVLNode root = null;
		AVLNode x = null;
		int m_case = 0;
		boolean nonBalancing = false;
		int rightHeight = 0;
		int leftHeight = 0;
		while (z != null && nonBalancing == false) {
			leftHeight = getHeight(z.left);
			rightHeight = getHeight(z.right);
			z.height = Math.max(rightHeight, leftHeight) + 1; // update the height if needed
			if ((rightHeight - leftHeight) > 1) { // right side's height is bigger by 2
				nonBalancing = true;
				y = z.right;
				if (getHeight(y.right) > getHeight(y.left)) { // case 2 , y is z.right & x is y.right
					m_case = 2;
					x = y.right;
				} else { // y.left.h > y.right.h
					m_case = 3;
					x = y.left;
				}
			} else if ((leftHeight - rightHeight) > 1) {// left side's height is bigger by 2
				nonBalancing = true;
				y = z.left;
				if (getHeight(y.right) > getHeight(y.left)) { // case 4 , y is z.left & x is y.right
					m_case = 4;
					x = y.right;
				} else { // y.left.h > y.right.h
					m_case = 1;
					x = y.left;
				}

			} else{
				root = z;
				z = z.father;
			}
		}

		if (nonBalancing == true) {
			doBalance(x, y, z, m_case);
			if (m_case == 1 || m_case == 2)// the root of the sub-tree will be 'y'
				root = checkBalance(y); // continue update the height on the upper nodes
			else
				// if x is the root of the 'new' sub-tree
				root =  checkBalance(x);// continue update the height on the upper nodes

		}
		return root;
	}

	/**
	 * Fix the balance of AVL tree.
	 * 
	 * @param AVLNode
	 *            x which is the bigger child of y.
	 * @param AVLNode
	 *            y which is the bigger child of z.
	 * @param AVLNode
	 *            z which is not balancing
	 * @param int m_case to fit the fix to the case.
	 */
	private static void doBalance(AVLNode x, AVLNode y, AVLNode z, int m_case) {
		switch (m_case) {
		case 1: // left left -> right_rotation(z)
			rotation_right(x, y, z);
			z.height = z.height - 2;
			break;
		case 2: // right right -> left_roatation(z)
			rotation_left(x, y, z);
			z.height = z.height - 2;
			break;
		case 3:
			rotation_right(null, x, y);
			rotation_left(y, x, z);
			y.height--;
			z.height = z.height - 2;
			x.height++;
			break;
		case 4:
			rotation_left(null, x, y);
			rotation_right(y, x, z);
			y.height--;
			z.height = z.height - 2;
			x.height++;
			break;
		default:
			break;
		}

	}

	/**
	 * Make a rotation left on z. After this rotation, 'y' will be the root of
	 * the sub-tree
	 * 
	 * @param AVLNode
	 *            x
	 * @param AVLNode
	 *            y
	 * @param AVLNode
	 *            z
	 */
	private static void rotation_left(AVLNode x, AVLNode y, AVLNode z) {
		if (y.left != null)
			switchChild(y.left, z, 2); // y.right -> z.left
		else
			switchChild(null, z, 2); // y.right -> z.left
		changeParentPointers(z, y); // z.father -> y.father
		y.left = z; // need to become z to y.right
		z.father = y;
	}

	/**
	 * Make a rotation right on z. After this rotation, 'y' will be the root of
	 * the sub-tree
	 * 
	 * @param AVLNode
	 *            x
	 * @param AVLNode
	 *            y
	 * @param AVLNode
	 *            z
	 */
	private static void rotation_right(AVLNode x, AVLNode y, AVLNode z) {
		if (y.right != null)
			switchChild(y.right, z, 1); // y.right -> z.left
		else
			switchChild(null, z, 1); // y.right -> z.left
		changeParentPointers(z, y); // z.father -> y.father
		y.right = z; // need to become z to y.right
		z.father = y;

	}

	/**
	 * Change the pointers between 2 AVLNodes. the second will exchange as the
	 * new child of the old's father
	 * 
	 * @param AVLNode
	 *            oldChild
	 * @param AVLNode
	 *            newChild
	 */
	private static void changeParentPointers(AVLNode oldChild, AVLNode newChild) {
		if (oldChild.father != null) {
			AVLNode parant = oldChild.father;

			if (parant.right == oldChild)
				parant.right = newChild;
			else
				parant.left = newChild;

			newChild.father = parant;
		} else
			// oldChild.Father = NULL
			newChild.father = null;
	}

	/**
	 * Make an AVLNode T, which is child of 'y' to be a Child of AVLNode 'z'
	 * 
	 * @param T
	 * @param z
	 * @param m_case
	 */
	private static void switchChild(AVLNode T, AVLNode z, int m_case) {
		if (T != null) {
			AVLNode y = T.father;
			changeParentPointers(y, T); // change y.child to z.child
		} else if (T == null && z != null) { // T = NULL
			if (m_case == 1)
				z.left = null;
			else
				// case 2
				z.right = null;
		}

	}

	/**
	 * 
	 * @return the Node with the min key in sub-tree
	 */
	public AVLNode<T> Minimum() {
		AVLNode temp = this;
		while (temp.left != null) {
			temp = temp.left;
		}
		return temp;
	}

	/**
	 * 
	 * @return the Node with the max key in sub-tree
	 */
	public AVLNode<T> Maximum() {
		AVLNode temp = this;
		while (temp.right != null) {
			temp = temp.right;
		}
		return temp;
	}

	/**
	 * 
	 * @return the Node with the greatest key after the key of this.
	 */
	public AVLNode<T> getPredecessor() {
		return this.predecessor;
	}

	/**
	 * 
	 * @return the Node with the smallest key which greater than key of this.
	 */
	public AVLNode<T> getSuccessor() {
		return this.successor;
	}

	/**
	 * Find the Node which is predecessor of this and set it as this
	 * predecessor.
	 */
	private void setPredecessor() {
		AVLNode p = this.father;
		AVLNode temp = this;
		while (p != null && temp == p.left) {
			temp = p;
			p = p.father;
		}
		if (p == null) {
			this.predecessor = null;
			return;
		}
		this.predecessor = p;
		p.successor = this;
	}

	/**
	 * Find the Node which is predecessor of this and set it as this Successor.
	 */
	private void setSuccessor() {
		AVLNode p = this.father;
		AVLNode temp = this;
		while (p != null && temp == p.right) {
			temp = p;
			p = p.father;
		}
		if (p == null) {
			this.successor = null;
			return;
		}
		this.successor = p;
		p.predecessor = this;
	}

	public boolean search(int key) {
		boolean ans = false;
		AVLNode<T> current = this;
		while (current != null && !ans) {
			if (current.key == key)
				ans = true;
			else if (key > current.key)
				current = current.right;
			else
				current = current.left;
		}
		return ans;
	}

	public AVLNode<T> find(int key) {
		AVLNode<T> ans = null;
		AVLNode<T> current = this;
		while (current != null && ans == null) {
			if (current.key == key)
				ans = current;
			else if (key > current.key)
				current = current.right;
			else
				current = current.left;
		}
		return ans;
	}



	public AVLNode<T> delete(int key) {
		AVLNode<T> toDelete = find(key);
		if (toDelete != null) {
			int numOfChild = 0;
			if (toDelete.left != null)
				numOfChild++;
			if (toDelete.right != null)
				numOfChild++;
			if (numOfChild == 0) { // leaf case
				if (toDelete.father != null && toDelete == toDelete.father.left) { // change predecessor for the father
					toDelete.father.setPredecessor(); // treat successor
					toDelete.father.left = null;
					if (toDelete.predecessor != null)// treat predecessor
						toDelete.predecessor = toDelete.successor;
				} else if (toDelete.father != null && toDelete == toDelete.father.right) { // change successor for the father
					toDelete.father.setSuccessor();// treat predecessor
					toDelete.father.right = null;
					if (toDelete.successor != null)// treat successor
						toDelete.successor = toDelete.predecessor;
				}
				return (AVLNode<T>)(checkBalance(toDelete.father));
			} else if (numOfChild == 1) {
				AVLNode<T> child = (toDelete.left != null) ? toDelete.left : toDelete.right;
				changeParentPointers(toDelete, child);
				if (child.predecessor == toDelete) { // right child
					child.predecessor = toDelete.predecessor;
					// the successor is the same as was before
					if(toDelete.predecessor!=null)
						toDelete.predecessor.successor = child;
				}
				else {// left child
					child.successor = toDelete.successor;
					// the predecessor is the same as was before
					if(toDelete.successor!=null)
						toDelete.successor.predecessor = child;
				}
				return (AVLNode<T>)(checkBalance(child));
				
			}
			else { // numOfChild = 2   
				return 	deleteNodeWith2Childs(toDelete);

			}
		}
		return this;
		
	}

	private static AVLNode deleteNodeWith2Childs(AVLNode toDelete) {
		AVLNode ans;
		AVLNode toSwitch = toDelete.successor;
		ans = toSwitch.delete(toSwitch.key);
		if(toDelete.father == null) // switch is new root
			ans =toSwitch;
		changeParentPointers(toDelete, toSwitch);
		toSwitch.predecessor = toDelete.predecessor;
		toDelete.predecessor.successor = toSwitch;
		if(toSwitch != toDelete.right){
			toSwitch.right = toDelete.right;
			toDelete.right.father = toSwitch;	
		}
		toSwitch.left = toDelete.left;
		toDelete.left.father = toSwitch;
		return ans;
	}
	
	public int getHeight(){
		return this.height;
	}

	public int getStepsToMIn() {
		int ans =0;
		AVLNode<T> c = this;
		while(c.left!=null){
			ans++;
			c =c.left;
		}
		return ans;
	}
}
