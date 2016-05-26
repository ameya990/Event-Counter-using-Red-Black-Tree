//The program implements an event counter using a Red-Black tree.
public class RBTree {

	EventNode root;
    // the below boolean variables are used in the inner class EventNode to represent the node color
	private static final boolean RED = true;
	private static final boolean BLACK = false;

	private int tMin = -1;
	private int tMax = -1;

	public class EventNode {
		int id; // unique ID for each event
		int count; // count is the number of active events for each unique ID. It is always greater than 0.
		int descendantsEventCount; // this the summation of all the event's counts in all the nodes of the subtree rooted at this node
		EventNode parent, left, right;
		boolean isRed; // I have taken a boolean approach to store the node color

		EventNode(int id, int count) {
			this.id = id;
			this.count = count;
			this.descendantsEventCount = count;
			this.isRed = RED; // a node is asigned Red color whenever it is first created
		}
	}

	//
	//Increase the count of the event theID by m. If
	//theID is not present, insertBST it. Print the count
	//of theID after the addition.
	//
	void increase(int eventID, int m) {
		EventNode event = findEventNode(eventID);
		if (event != null) {
			event.count = event.count + m;
			event.descendantsEventCount = event.descendantsEventCount + m;
			EventNode ancestor = event.parent;

			while (ancestor != null) {
				ancestor.descendantsEventCount = ancestor.descendantsEventCount + m;
				ancestor = ancestor.parent;
			}
		} else {
			insertBST(eventID, m);
			event = findEventNode(eventID);
		}
		System.out.println(event.count);
	}

	//
	//Decrease the count of theID by m. If theID’s
	//count becomes less than or equal to 0, remove
	//theID from the counter. Print the count of
	//theID after the deletion, or 0 if theID is
	//removed or not present.
	//
	void reduce(int eventID, int m) {
		EventNode event = findEventNode(eventID);
		if (event != null) {
			if (event.count <= m) {
				int deletedEventCount = event.count;
				deleteBST(eventID);
				EventNode ancestor = event.parent;

				while (ancestor != null) {
					ancestor.descendantsEventCount = ancestor.descendantsEventCount - deletedEventCount;
					ancestor = ancestor.parent;
				}
				// Printing zero beacause count became less than or equal to zero.
				System.out.println(0);
			} else {
				event.count = event.count - m;
				event.descendantsEventCount = event.descendantsEventCount - m;
				EventNode ancestor = event.parent;

				while (ancestor != null) {
					ancestor.descendantsEventCount = ancestor.descendantsEventCount - m;
					ancestor = ancestor.parent;
				}
				System.out.println(event.count);
			}
		} else {
			// theID is not present. Print zero.
			System.out.println(0);
		}
	}

	//
	//Print the count of theID. If not present, print
	//0.
	//
	void count(int eventID) {
		EventNode event = findEventNode(eventID);
		if (event != null) {
			System.out.println(event.count);
		} else {
			System.out.println(0);
		}
	}

	EventNode next(int eventID, boolean printWorthy) {
		EventNode event = findEventNode(eventID);
		if (event != null) {
			EventNode eventSuccessor = nodeSuccessor(event);
			if (eventSuccessor != null) {
				if (printWorthy) {
					System.out.println(eventSuccessor.id + " " + eventSuccessor.count);
				}
				return eventSuccessor;
			} else {
				EventNode ancestor = event.parent;
				boolean isFound = false;
				while (ancestor != null) {
					if (ancestor.id > eventID) {
						if (printWorthy) {
							System.out.println(ancestor.id + " " + ancestor.count);
						}
						isFound = true;
						break;
					}
					ancestor = ancestor.parent;
				}
				if (!isFound) {
					if (printWorthy) {
						System.out.println("0 0");
					}
					return null;
				} else {
					return ancestor;
				}
			}
		} else {
			// the given ID is absent, therefore, find the next best fit
			if (eventID > tMax) {
				if (printWorthy) {
					System.out.println("0 0");
				}
				return null;
			}
			int leftBestFitID = tMin;
			EventNode leftNode = null;
			if (eventID <= tMin) {
				leftNode = findEventNode(tMin);
			} else {
				while (!(eventID <= leftBestFitID)) {
					leftNode = next(leftBestFitID, false);
					leftBestFitID = leftNode.id;
				}
			}
			if (printWorthy) {
				if (leftNode == null) {
					System.out.println("0 0");
				} else {
					System.out.println(leftNode.id + " " + leftNode.count);
				}
			}
			return leftNode;
		}
	}

	EventNode previous(int eventID, boolean printWorthy) {
		EventNode event = findEventNode(eventID);
		if (event != null) {
			EventNode eventPredecessor = nodePredecessor(event);
			if (eventPredecessor != null) {
				if (printWorthy) {
					System.out.println(eventPredecessor.id + " " + eventPredecessor.count);
				}
				return eventPredecessor;
			} else {
				EventNode ancestor = event.parent;
				boolean isFound = false;
				while (ancestor != null) {
					if (ancestor.id < eventID) {
						if (printWorthy) {
							System.out.println(ancestor.id + " " + ancestor.count);
						}
						isFound = true;
						break;
					}
					ancestor = ancestor.parent;
				}
				if (!isFound) {
					if (printWorthy) {
						System.out.println("0 0");
					}
					return null;
				} else {
					return ancestor;
				}
			}
		} else {
			// the given ID is absent, therefore, find the next best fit
			if (eventID < tMin) {
				if (printWorthy) {
					System.out.println("0 0");
				}
				return null;
			}
			int leftBestFitID = tMax;
			EventNode rightNode = null;
			if (eventID >= tMax) {
				rightNode = findEventNode(tMax);
			} else {
				while (!(eventID >= leftBestFitID)) {
					rightNode = previous(leftBestFitID, false);
					leftBestFitID = rightNode.id;
				}
			}
			if (printWorthy) {
				if (rightNode == null) {
					System.out.println("0 0");
				} else {
					System.out.println(rightNode.id + " " + rightNode.count);
				}
			}
			return rightNode;
		}
	}

	EventNode getLeftNodeRange(int ID) {
		EventNode leftNode = findEventNode(ID);
		if (leftNode == null) {
			// the given ID is absent, therefore, find the next best fit
			if (ID > tMax) {
				return null;
			}
			int leftBestFitID = tMin;

			if (ID <= tMin) {
				leftNode = findEventNode(tMin);
			} else {
				while (!(ID <= leftBestFitID)) {
					leftNode = next(leftBestFitID, false);
					leftBestFitID = leftNode.id;
				}
			}
			return leftNode;
		} else {
			return leftNode;
		}
	}

	EventNode getRightNodeRange(int ID) {
		EventNode rightNode = findEventNode(ID);
		if (rightNode == null) {
			// the given ID is absent, therefore, find the next best fit
			if (ID < tMin) {
				return null;
			}
			int RightBestFitID = tMax;

			if (ID >= tMax) {
				rightNode = findEventNode(tMax);
			} else {
				while (!(ID >= RightBestFitID)) {
					rightNode = previous(RightBestFitID, false);
					RightBestFitID = rightNode.id;
				}
			}
			return rightNode;
		} else {
			return rightNode;
		}
	}

	//
	//Print the total count for IDs between ID1 and
	//ID2 inclusively. Note, ID1 ≤ ID2
	//
	void inRange(int ID1, int ID2) {
		if (ID1 == ID2) {
			EventNode node = findEventNode(ID1);
			if (node == null) {
				System.out.println(0);
				return;
			} else {
				System.out.println(node.count);
				return;
			}
		} else {
			EventNode leftNode = getLeftNodeRange(ID1);
			EventNode rightNode = getRightNodeRange(ID2);
			if (leftNode == null || rightNode == null || (leftNode.id > rightNode.id)) {
				System.out.println(0);
				return;
			} else if (leftNode == rightNode) {
				System.out.println(leftNode.count);
				return;
			}
			ID1 = leftNode.id;
			ID2 = rightNode.id;
			EventNode smallestCommonAncestor = lowestCommonAncestor(ID1, ID2);
			int totalInRangeCount = 0;

			if (smallestCommonAncestor != leftNode && smallestCommonAncestor != rightNode) {
				totalInRangeCount = smallestCommonAncestor.count + leftNode.count
						+ getDescendantsEventCount(leftNode.right) + rightNode.count
						+ getDescendantsEventCount(rightNode.left);

				{
					EventNode prev = leftNode;
					EventNode ancestor = leftNode.parent;
					while (ancestor != smallestCommonAncestor) {
						if (ancestor.id >= leftNode.id) { // we're adding ancestor's count to the totalInRangeCount
							totalInRangeCount = totalInRangeCount + ancestor.count;
							if (prev == ancestor.left) {
								totalInRangeCount = totalInRangeCount + getDescendantsEventCount(ancestor.right);
							}

						}
						prev = ancestor;
						ancestor = ancestor.parent;
					}
				}
				{
					EventNode prev = rightNode;
					EventNode ancestor = rightNode.parent;
					while (ancestor != smallestCommonAncestor) {
						if (ancestor.id <= rightNode.id) { // we're adding ancestor's count to the totalInRangeCount
							totalInRangeCount = totalInRangeCount + ancestor.count;
							if (prev == ancestor.right) {
								totalInRangeCount = totalInRangeCount + getDescendantsEventCount(ancestor.left);
							}

						}
						prev = ancestor;
						ancestor = ancestor.parent;
					}
				}
			} else {
				// An ancestor node selected is least common. So we are considering just a single branch of it

				if (leftNode == smallestCommonAncestor) {
					// Only right tree of the leftNode is taken into consideration.
					totalInRangeCount = leftNode.count + rightNode.count + getDescendantsEventCount(rightNode.left);
					EventNode prev = rightNode;
					EventNode ancestor = rightNode.parent;
					while (ancestor != smallestCommonAncestor) {
						if (ancestor.id <= rightNode.id) { // we're adding ancestor's count to the totalInRangeCount
							totalInRangeCount = totalInRangeCount + ancestor.count;
							if (prev == ancestor.right) {
								totalInRangeCount = totalInRangeCount + getDescendantsEventCount(ancestor.left);
							}

						}
						prev = ancestor;
						ancestor = ancestor.parent;
					}
				} else {
					// consider only the left tree if right node is the common ancestor
					totalInRangeCount = rightNode.count + leftNode.count + getDescendantsEventCount(leftNode.right);
					EventNode prev = leftNode;
					EventNode ancestor = leftNode.parent;
					while (ancestor != smallestCommonAncestor) {
						if (ancestor.id >= leftNode.id) { // we're adding ancestor's count to the totalInRangeCount
							totalInRangeCount = totalInRangeCount + ancestor.count;
							if (prev == ancestor.left) {
								totalInRangeCount = totalInRangeCount + getDescendantsEventCount(ancestor.right);
							}

						}
						prev = ancestor;
						ancestor = ancestor.parent;
					}
				}
			}
			System.out.println(totalInRangeCount);
		}
	}

	EventNode lowestCommonAncestor(int leftID, int rightID) {
		EventNode temp = root;
		while (temp != null) {
			if (temp.id < leftID && temp.id < rightID) {
				temp = temp.right;
			} else if (temp.id > leftID && temp.id > rightID) {
				temp = temp.left;
			} else if (temp.id >= leftID && temp.id <= rightID) {
				break;
			} else {
				// the desired range is not present
				break;
			}
		}
		return temp;
	}

	//
	// A temporary NULL sentinal node is created and attached to the parent node
	// for rebalancing purposes while deletion and deleted thereafter.
	//
	EventNode createNullSentinel(EventNode parent, boolean onRight) {
		EventNode nullSentinel = new EventNode(-1, -1);
		nullSentinel.descendantsEventCount = -1;
		nullSentinel.isRed = BLACK;
		nullSentinel.parent = parent;
		if (onRight) {
			parent.right = nullSentinel;
		} else {
			parent.left = nullSentinel;
		}
		return nullSentinel;
	}


	void cleanNullSentinel(EventNode node) {
		if (node.id == -1) {
			removeAllNodeReferences(node);
		}
	}

	//insert method of Binary search tree
	void insertBST(int key, int count) {
		EventNode newNode = new EventNode(key, count);
		if (root != null) {
			EventNode parent = null, temp = root;
			while (temp != null) {
				parent = temp;
				if (key < temp.id) {
					temp.descendantsEventCount += newNode.count; // this increase count of newNode's ancestors when we traverse down
					temp = temp.left;
				} else {
					temp.descendantsEventCount += newNode.count; // this increase count of newNode's ancestors when we traverse down
					temp = temp.right;
				}
			}
			if (key < parent.id) {
				parent.left = newNode;
			} else {
				parent.right = newNode;
			}
			newNode.parent = parent;
			if (newNode.id > tMax) {
				tMax = newNode.id;
			}
			if (newNode.id < tMin) {
				tMin = newNode.id;
			}
		} else {
			root = newNode;
			tMin = newNode.id;
			tMax = newNode.id;
		}
		insertRBTCase1(newNode);
	}

	int findMinimum() {
		if (root == null) {
			return -1;
		} else {
			EventNode prev = root, temp = root.left;
			while (temp != null) {
				prev = temp;
				temp = temp.left;
			}
			return prev.id;
		}
	}

	int findMaximum() {
		if (root == null) {
			return -1;
		} else {
			EventNode prev = root, temp = root.right;
			while (temp != null) {
				prev = temp;
				temp = temp.right;
			}
			return prev.id;
		}
	}

	//if the node does not exist, return zero, else return count
	int getDescendantsEventCount(EventNode node) {
		if (node != null) {
			return node.descendantsEventCount;
		} else {
			return 0;
		}
	}

	// return null if the node with given ID is not found, else return the eventNode
	EventNode findEventNode(int ID) {
		if (root != null) {
			// First find the node
			EventNode node = root;
			while (node != null && node.id != ID) {
				if (ID < node.id) {
					node = node.left;
				} else {
					node = node.right;
				}
			}
			if (node != null) {
				return node;
			} else {
				return null;
			}
		}
		return null;
	}

	// the Binary search tree delete method
	void deleteBST(int key) {
		EventNode node = findEventNode(key);

		if (node != null) {
			deleteEventNode(node);
		}
	}


	void deleteEventNode(EventNode node) {
		if (node != null) {
			int deletedKeyID = node.id;
			if (node.left != null && node.right != null) {
				// If the node has two children, replace the node with its
				// predecessor, and  then make a recursive call to deleteEventNode(predecessor)
				// recursively.
				EventNode predecessor = nodePredecessor(node);
				replaceEventNode(node, predecessor);
				deleteEventNode(predecessor);
			} else {
				// If only one child, call delete fix up if a Black node is being deleted.
				// (if it's red, then no RBT properties are violated)
				boolean moreFixesNeeded = false;
				EventNode child = null;
				if (node.isRed == BLACK) {
					moreFixesNeeded = !deleteFix1(node);
				}
				if (node.right != null) {
					child = node.right;

					if (node.parent == null) {
						root = child;
						moreFixesNeeded = false;
					} else if (node.parent.right == node) {
						node.parent.right = child;
					} else {
						node.parent.left = child;
					}
				} else {
					child = node.left;

					if (node.parent == null) {
						root = child;
						moreFixesNeeded = false;
					} else if (node.parent.right == node) {
						if (child == null) {
							child = createNullSentinel(node.parent, true); // adding a dummy sentinel for rebalancing
						}
						node.parent.right = child;
					} else {
						if (child == null) {
							child = createNullSentinel(node.parent, false); /// adding a dummy sentinel for rebalancing
						}
						node.parent.left = child;
					}
				}
				child.parent = node.parent;
				if (moreFixesNeeded) {
					//if the replacement is not root and was previously black
					deleteRBT2(child);
					cleanNullSentinel(child);
				}
				cleanNullSentinel(child);
			}
			if (deletedKeyID == tMin) {
				tMin = findMinimum();
			}
			if (deletedKeyID == tMax) {
				tMax = findMaximum();
			}
		}
	}


	//Node is the new root and RB Tree properties are preserved

	void deleteRBT1(EventNode node) {
		if (node.parent != null) {
			deleteRBT2(node);
		} else {
			root = node;
		}
	}

	//If child replacing deleted node is not current root and was previously black

	void deleteRBT2(EventNode node) {
		EventNode sibNode = sibling(node);
		if (sibNode.isRed == RED) {
			node.parent.isRed = RED;
			sibNode.isRed = BLACK;
			if (node == node.parent.left) {
				leftRotate(node.parent);
			} else {
				rightRotate(node.parent);
			}
		}
		deleteRBT3(node);
	}

	// Parent and both the children are black
	void deleteRBT3(EventNode node) {
		EventNode sibNode = sibling(node);
		if (node.parent.isRed == BLACK && sibNode.isRed == BLACK
				&& (sibNode.left == null || sibNode.left.isRed == BLACK)
				&& (sibNode.right == null || sibNode.right.isRed == BLACK)) {

			sibNode.isRed = RED;
			deleteRBT1(node.parent);
		} else {
			deleteRBT4(node);
		}
	}

	// Parent is red but both the children are black
	void deleteRBT4(EventNode node) {
		EventNode sibNode = sibling(node);
		if (node.parent.isRed == RED && sibNode.isRed == BLACK
				&& (sibNode.left == null || sibNode.left.isRed == BLACK)
				&& (sibNode.right == null || sibNode.right.isRed == BLACK)) {

			sibNode.isRed = RED;
			node.parent.isRed = BLACK;
		} else {
			deleteRBT5(node);
		}
	}

	void deleteRBT5(EventNode node) {
		EventNode sibNode = sibling(node);
		if (sibNode.isRed == BLACK) {
			if (node == node.parent.left && (sibNode.right == null || sibNode.right.isRed == BLACK)
					&& (sibNode.left != null && sibNode.left.isRed == RED)) {

				sibNode.isRed = RED;
				sibNode.left.isRed = BLACK;
				rightRotate(sibNode);
			} else if (node == node.parent.right && (sibNode.left == null || sibNode.left.isRed == BLACK)
					&& (sibNode.right != null && sibNode.right.isRed == RED)) {
				sibNode.isRed = RED;
				sibNode.right.isRed = BLACK;
				leftRotate(sibNode);
			}
		}
		deleteRBT6(node);
	}

	void deleteRBT6(EventNode node) {
		EventNode sibNode = sibling(node);

		sibNode.isRed = node.parent.isRed;
		node.parent.isRed = BLACK;

		if (node == node.parent.left) {
			sibNode.right.isRed = BLACK;
			leftRotate(node.parent);
		} else {
			sibNode.left.isRed = BLACK;
			rightRotate(node.parent);
		}
	}

	// If the node to be deleted is black and has just a single red child , paint the child black.

	boolean deleteFix1(EventNode node) {
		if (node.isRed == BLACK && node.right != null && node.right.isRed == RED) {
			node.right.isRed = BLACK;
			return true;
		} else if (node.isRed == BLACK && node.left != null && node.left.isRed == RED) {
			node.left.isRed = BLACK;
			return true;
		}
		return false;
	}


	 // removes all reference to a node (to be deleted) the parent is set to NULL
	void removeAllNodeReferences(EventNode node) {
		if (node != null) {
			if (node == root) {
				root = null;
			} else {
				if (node.parent.left == node) {
					node.parent.left = null;
				} else {
					node.parent.right = null;
				}
			}
		}
	}

	// replaces a node
	void replaceEventNode(EventNode originalNode, EventNode newNode) {
		originalNode.id = newNode.id;
		originalNode.count = newNode.count;
	}

	//Returns the left-most child in the right
	 // subtree of a node.

	EventNode nodeSuccessor(EventNode node) {
		EventNode successor = null;
		if (node != null) {
			successor = node.right;
			while (successor != null && successor.left != null) {
				successor = successor.left;
			}
		}
		return successor;
	}

	//Returns the right-most child in the left
	// subtree of a node.
	EventNode nodePredecessor(EventNode node) {
		EventNode predecessor = null;
		if (node != null) {
			predecessor = node.left;
			while (predecessor != null && predecessor.right != null) {
				predecessor = predecessor.right;
			}
		}
		return predecessor;
	}

	EventNode grandParent(EventNode node) {
		if (node != null && node.parent != null && node.parent.parent != null) {
			return node.parent.parent;
		} else {
			return null;
		}
	}

	EventNode uncle(EventNode node) {
		if (node != null && node.parent != null && node.parent.parent != null) {
			if (node.parent == node.parent.parent.right) {
				return node.parent.parent.left;
			} else {
				return node.parent.parent.right;
			}
		} else {
			return null;
		}
	}

	EventNode sibling(EventNode node) {
		if (node != null && node.parent != null) {
			if (node == node.parent.right) {
				return node.parent.left;
			} else {
				return node.parent.right;
			}
		} else {
			return null;
		}
	}

	/*
	 * first node is black
	 */
	void insertRBTCase1(EventNode node) {
		if (node != null) {
			if (node.parent == null) {
				node.isRed = BLACK;
			} else {
				insertRBTCase2(node);
			}
		}
	}

	//Parent is black

	void insertRBTCase2(EventNode node) {
		if (node.parent.isRed == BLACK) {
			return;
		} else {
			insertRBTCase3(node);
		}
	}

	//Parent is red and the uncle is also red
	void insertRBTCase3(EventNode node) {
		EventNode uncle = uncle(node);
		if (uncle != null && uncle.isRed == RED) {
			node.parent.isRed = BLACK;
			uncle.isRed = BLACK;
			EventNode grandparent = grandParent(node);
			grandparent.isRed = RED;
			insertRBTCase1(grandparent);
		} else {
			insertRBTCase4(node);
		}
	}

	void insertRBTCase4(EventNode node) {
		EventNode gParent = grandParent(node);
		EventNode parent = node.parent;
		if (gParent.left == parent && parent.right == node) {
			// left-rotate
			parent.right = node.left;
			if (parent.right != null) {
				parent.right.parent = parent;
			}
			parent.parent = node;
			node.left = parent;
			node.parent = gParent;
			gParent.left = node;
			node = node.left;
			int prevParentDescendantsEventCount = node.descendantsEventCount;
			node.descendantsEventCount -= node.parent.descendantsEventCount - getDescendantsEventCount(node.right);
			node.parent.descendantsEventCount = prevParentDescendantsEventCount;
		} else if (gParent.right == parent && parent.left == node) {
			// right-rotate
			parent.left = node.right;
			if (parent.left != null) {
				parent.left.parent = parent;
			}
			parent.parent = node;
			node.right = parent;
			node.parent = gParent;
			gParent.right = node;
			node = node.right;
			int prevParentDescendantsEventCount = node.descendantsEventCount;
			node.descendantsEventCount -= node.parent.descendantsEventCount - getDescendantsEventCount(node.left);
			node.parent.descendantsEventCount = prevParentDescendantsEventCount;
		}
		insertRBTCase5(node);
	}

	void insertRBTCase5(EventNode node) {
		EventNode gParent = grandParent(node);
		EventNode parent = node.parent;
		parent.isRed = BLACK;
		gParent.isRed = RED;
		if (parent.right == node) {
			leftRotate(gParent);
		} else {
			rightRotate(gParent);
		}
	}

	void leftRotate(EventNode node) {
		if (node != null && node.right != null) {
			EventNode rightChild = node.right;
			EventNode gParent = node.parent;
			node.right = rightChild.left;
			if (node.right != null) {
				node.right.parent = node;
			}
			node.parent = rightChild;
			rightChild.left = node;
			rightChild.parent = gParent;
			if (gParent != null) {
				if (node == gParent.left) {
					gParent.left = rightChild;
				} else {
					gParent.right = rightChild;
				}
			} else {
				root = rightChild;
			}
			int prevParentDescendantsEventCount = node.descendantsEventCount;
			node.descendantsEventCount -= node.parent.descendantsEventCount - getDescendantsEventCount(node.right);
			node.parent.descendantsEventCount = prevParentDescendantsEventCount;
		}
	}

	void rightRotate(EventNode node) {
		if (node != null && node.left != null) {
			EventNode leftChild = node.left, gParent = node.parent;
			node.left = leftChild.right;
			if (node.left != null) {
				node.left.parent = node;
			}
			node.parent = leftChild;
			leftChild.right = node;
			leftChild.parent = gParent;
			if (gParent != null) {
				if (node == gParent.left) {
					gParent.left = leftChild;
				} else {
					gParent.right = leftChild;
				}
			} else {
				root = leftChild;
			}
			int prevParentDescendantsEventCount = node.descendantsEventCount;
			node.descendantsEventCount -= node.parent.descendantsEventCount - getDescendantsEventCount(node.left);
			node.parent.descendantsEventCount = prevParentDescendantsEventCount;
		}
	}

	EventNode generateBST(EventNode arr[], int start, int end, int currHeight, int maxHeight) {
		if (start > end) {
			return null;
		}
		int mid = start + (end - start) / 2;
		EventNode node = arr[mid];
		node.left = generateBST(arr, start, mid - 1, currHeight + 1, maxHeight);
		node.right = generateBST(arr, mid + 1, end, currHeight + 1, maxHeight);
		if (node.left != null) {
			node.descendantsEventCount += node.left.descendantsEventCount;
			node.left.parent = node;
		}
		if (node.right != null) {
			node.descendantsEventCount += node.right.descendantsEventCount;
			node.right.parent = node;
		}
		if (currHeight == maxHeight) {
			node.isRed = RED;
		}
		return node;
	}

	void generateRBT(EventNode arr[], int n) {
		tMin = arr[0].id;
		tMax = arr[n - 1].id;
		root = generateBST(arr, 0, n - 1, 0, getMaxHeight(n));
	}

	public static int getMaxHeight(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		return 31 - Integer.numberOfLeadingZeros(n);
	}


}
