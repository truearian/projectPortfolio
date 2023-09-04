package TwoFourTree;
import java.util.Scanner;

import javax.swing.event.TreeWillExpandListener;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

public class TwoFourTree {
    private class TwoFourTreeItem {
        int values = 1;
        int value1 = 0; // always exists.
        int value2 = 0; // exists iff the node is a 3-node or 4-node.
        int value3 = 0; // exists iff the node is a 4-node.
        boolean isLeaf = true;
        TwoFourTreeItem parent = null; // parent exists iff the node is not root.
        TwoFourTreeItem leftChild = null; // left and right child exist iff the node is a non-leaf.
        TwoFourTreeItem rightChild = null;
        TwoFourTreeItem centerChild = null; // center child exists iff the node is a non-leaf 3-node.
        TwoFourTreeItem centerLeftChild = null; // center-left and center-right children exist iff the node is a
                                                // non-leaf 4-node.
        TwoFourTreeItem centerRightChild = null;

        // public TwoFourTreeItem makeParentChild(){
        // if()
        // }

        public TwoFourTreeItem split(int value) {
            if (values != 3)
                return null;
            if (parent == null) { // If the node being split is the root
                TwoFourTreeItem newRoot = new TwoFourTreeItem(value2);
                TwoFourTreeItem newLeftChild = new TwoFourTreeItem(value1);
                TwoFourTreeItem newRightChild = new TwoFourTreeItem(value3);

                if (!isLeaf) {
                    // the root has child nodes, remember 234 trees have the same
                    // height, and split nodes are 4 nodes, so they have all child
                    // ren possible. re route children nodes to the new node objects
                    newLeftChild.leftChild = this.leftChild;
                    newLeftChild.rightChild = this.centerLeftChild;
                    newRightChild.leftChild = this.centerRightChild;
                    newRightChild.rightChild = this.rightChild;
                    newLeftChild.leftChild.parent = newLeftChild;
                    newLeftChild.rightChild.parent = newLeftChild;
                    newRightChild.rightChild.parent = newRightChild;
                    newRightChild.leftChild.parent = newRightChild;
                    newLeftChild.isLeaf = false;
                    newRightChild.isLeaf = false;

                    // new root set up (middle value)
                    newRoot.leftChild = newLeftChild;
                    newRoot.rightChild = newRightChild;
                    newRoot.isLeaf = false;
                    newRoot.leftChild.parent = newRoot;
                    newRoot.rightChild.parent = newRoot;

                    // which way to traverse...
                    if (value < value2) {
                        return newRoot.leftChild;
                    } else if (value > value2)
                        return newRoot.rightChild;

                    // this should not happen
                    return newRoot;
                } else {

                    // if not a leaf, then just set up newRoot childs
                    newRoot.leftChild = newLeftChild;
                    newRoot.rightChild = newRightChild;
                    newRoot.isLeaf = false;

                    newRoot.leftChild.parent = newRoot;
                    newRoot.rightChild.parent = newRoot;

                    if (value < value2) {
                        return newRoot.leftChild;
                    } else if (value > value2)
                        return newRoot.rightChild;

                    return newRoot;
                }
                // not a root else...
            } else {
                if (parent.isTwoNode()) {
                    // if the parent of this object is 2 node
                    if (isRightChild()) {
                        // this object is the right child of a parent
                        // so our middle value goes in the parent's value2 since
                        // it is guaranteed to be bigger. we also have to set this
                        // object's value1 as center child of the parent
                        parent.values++;
                        parent.value2 = value2;
                        TwoFourTreeItem parentRightChild = new TwoFourTreeItem(value3);
                        TwoFourTreeItem parentCenterChild = new TwoFourTreeItem(value1);
                        if (!isLeaf) {
                            // this object is not a leaf, so reroute its children to the
                            // new center child and right child
                            parentRightChild.leftChild = this.centerRightChild;
                            parentRightChild.rightChild = this.rightChild;
                            parentRightChild.leftChild.parent = parentRightChild;
                            parentRightChild.rightChild.parent = parentRightChild;
                            parentRightChild.isLeaf = false;
                            parent.rightChild = parentRightChild;
                            parent.rightChild.parent = this.parent;

                            parentCenterChild.leftChild = this.leftChild;
                            parentCenterChild.rightChild = this.centerLeftChild;
                            parentCenterChild.leftChild.parent = parentCenterChild;
                            parentCenterChild.rightChild.parent = parentCenterChild;
                            parentCenterChild.isLeaf = false;
                            parent.centerChild = parentCenterChild;
                            parent.centerChild.parent = this.parent;

                            // which node to return. center or right or left
                            if (parent.value1 < value && parent.value2 > value) {
                                return parent.centerChild;
                            } else if (parent.value1 > value)
                                return parent.leftChild;
                            else if (parent.value2 > value)
                                return parent.rightChild;

                            // should not happen
                            return parent;
                        } else {
                            // if this object is leaf
                            parent.rightChild = parentRightChild;
                            parent.centerChild = parentCenterChild;
                            parent.rightChild.parent = parent;
                            parent.centerChild.parent = parent;

                            if (parent.value1 < value && parent.value2 > value) {
                                return parent.centerChild;
                            } else if (parent.value1 > value)
                                return parent.leftChild;
                            else if (parent.value2 > value)
                                return parent.rightChild;

                            return parent;
                        }
                    } else if (isLeftChild()) {
                        // the only difference here is we are making a new left child
                        // still making a new center child, and value3 of this object
                        // will be in center child
                        parent.values++;
                        parent.value2 = parent.value1;
                        parent.value1 = value2;
                        TwoFourTreeItem parentLeftChild = new TwoFourTreeItem(value1);
                        TwoFourTreeItem parentCenterChild = new TwoFourTreeItem(value3);
                        if (!isLeaf) {
                            parentLeftChild.leftChild = this.leftChild;
                            parentLeftChild.rightChild = this.centerLeftChild;
                            parentLeftChild.rightChild.parent = parentLeftChild;
                            parentLeftChild.leftChild.parent = parentLeftChild;
                            parentLeftChild.isLeaf = false;
                            parent.leftChild = parentLeftChild;
                            parent.leftChild.parent = this.parent;

                            parentCenterChild.leftChild = this.centerRightChild;
                            parentCenterChild.rightChild = this.rightChild;
                            parentCenterChild.leftChild.parent = parentCenterChild;
                            parentCenterChild.rightChild.parent = parentCenterChild;
                            parentCenterChild.isLeaf = false;
                            parent.centerChild = parentCenterChild;
                            parent.centerChild.parent = parent;

                            if (parent.value1 < value && parent.value2 > value) {
                                return parent.centerChild;
                            } else if (parent.value1 > value)
                                return parent.leftChild;
                            else if (parent.value2 > value)
                                return parent.rightChild;

                            return parent;
                        } else {
                            parent.leftChild = parentLeftChild;
                            parent.centerChild = parentCenterChild;
                            parent.leftChild.parent = parent;
                            parent.centerChild.parent = parent;

                            if (parent.value1 < value && parent.value2 > value) {
                                return parent.centerChild;
                            } else if (parent.value1 > value)
                                return parent.leftChild;
                            else if (parent.value2 > value)
                                return parent.rightChild;

                            return parent;
                        }
                    }
                    // if the parent is three node we have to figure out what
                    // child this is and connect accordingly
                } else if (parent.isThreeNode()) {
                    // if its a right child of the parent
                    // WE HAVE TO NULL THE CURRENT CENTER CHILD OF THE PARENT FOR ALL PARENT THREE
                    // NODE CASES
                    // for right child we need a new center right child and right child
                    if (isRightChild()) {
                        parent.values++;
                        parent.value3 = value2;
                        TwoFourTreeItem parentCenterRightChild = new TwoFourTreeItem(value1);
                        TwoFourTreeItem parentRightChild = new TwoFourTreeItem(value3);
                        TwoFourTreeItem parentCenterLeftChild = parent.centerChild;
                        if (!isLeaf) {
                            // not leaf connect children
                            parentRightChild.leftChild = this.centerRightChild;
                            parentRightChild.rightChild = this.rightChild;
                            parentRightChild.leftChild.parent = parentRightChild;
                            parentRightChild.rightChild.parent = parentRightChild;
                            parentRightChild.isLeaf = false;
                            parent.rightChild = parentRightChild;
                            parent.rightChild.parent = parent;

                            parentCenterRightChild.leftChild = this.leftChild;
                            parentCenterRightChild.rightChild = this.centerLeftChild;
                            parentCenterRightChild.leftChild.parent = parentCenterRightChild;
                            parentCenterRightChild.rightChild.parent = parentCenterRightChild;
                            parentCenterRightChild.isLeaf = false;
                            parent.centerRightChild = parentCenterRightChild;
                            parent.centerRightChild.parent = parent;

                            parent.centerChild = null;
                            parent.centerLeftChild = parentCenterLeftChild;
                            parent.centerLeftChild.parent = parent;

                            if (parent.value1 > value) {
                                return parent.leftChild;
                            } else if (parent.value3 < value)
                                return parent.rightChild;
                            else if (parent.value1 < value && parent.value2 > value)
                                return parent.centerLeftChild;
                            else if (parent.value2 < value && parent.value3 > value)
                                return parent.centerRightChild;

                            return parent;
                        } else {
                            parent.centerRightChild = parentCenterRightChild;
                            parent.rightChild = parentRightChild;
                            parent.centerRightChild.parent = parent;
                            parent.rightChild.parent = parent;
                            parent.centerChild = null;
                            parent.centerLeftChild = parentCenterLeftChild;
                            parent.centerLeftChild.parent = parent;

                            if (parent.value1 > value) {
                                return parent.leftChild;
                            } else if (parent.value3 < value)
                                return parent.rightChild;
                            else if (parent.value1 < value && parent.value2 > value)
                                return parent.centerLeftChild;
                            else if (parent.value2 < value && parent.value3 > value)
                                return parent.centerRightChild;

                            return parent;
                        }
                    } else if (isLeftChild()) {
                        parent.values++;
                        int temp = parent.value1;
                        int temp2 = parent.value2;
                        parent.value1 = this.value2;
                        parent.value2 = temp;
                        parent.value3 = temp2;
                        TwoFourTreeItem parentLeftChild = new TwoFourTreeItem(value1);
                        TwoFourTreeItem parentCenterLeftChild = new TwoFourTreeItem(value3);
                        TwoFourTreeItem parentCenterRightChild = parent.centerChild;
                        if (!isLeaf) {
                            // not leaf...
                            parentLeftChild.leftChild = this.leftChild;
                            parentLeftChild.rightChild = this.centerLeftChild;
                            parentLeftChild.leftChild.parent = parentLeftChild;
                            parentLeftChild.rightChild.parent = parentLeftChild;
                            parentLeftChild.isLeaf = false;
                            parent.leftChild = parentLeftChild;
                            parent.leftChild.parent = parent;

                            parentCenterLeftChild.leftChild = this.centerRightChild;
                            parentCenterLeftChild.rightChild = this.rightChild;
                            parentCenterLeftChild.leftChild.parent = parentCenterLeftChild;
                            parentCenterLeftChild.rightChild.parent = parentCenterLeftChild;
                            parentCenterLeftChild.isLeaf = false;
                            parent.centerLeftChild = parentCenterLeftChild;
                            parent.centerLeftChild.parent = parent;

                            parent.centerChild = null;
                            parent.centerRightChild = parentCenterRightChild;
                            parent.centerRightChild.parent = parent;

                            if (parent.value1 > value) {
                                return parent.leftChild;
                            } else if (parent.value3 < value)
                                return parent.rightChild;
                            else if (parent.value1 < value && parent.value2 > value)
                                return parent.centerLeftChild;
                            else if (parent.value2 < value && parent.value3 > value)
                                return parent.centerRightChild;

                            return parent;
                        } else {
                            parent.leftChild = parentLeftChild;
                            parent.centerLeftChild = parentCenterLeftChild;
                            parent.leftChild.parent = parent;
                            parent.centerLeftChild.parent = parent;

                            parent.centerChild = null;
                            parent.centerRightChild = parentCenterRightChild;
                            parent.centerRightChild.parent = parent;

                            if (parent.value1 > value) {
                                return parent.leftChild;
                            } else if (parent.value3 < value)
                                return parent.rightChild;
                            else if (parent.value1 < value && parent.value2 > value)
                                return parent.centerLeftChild;
                            else if (parent.value2 < value && parent.value3 > value)
                                return parent.centerRightChild;

                            return parent;
                        }
                    } else if (isCenterChild()) {
                        // if this object is center child we need a new center left/right child for the
                        // parent
                        parent.values++;
                        int temp = parent.value2;
                        parent.value2 = value2;
                        parent.value3 = temp;
                        TwoFourTreeItem parentCenterLeftChild = new TwoFourTreeItem(value1);
                        TwoFourTreeItem parentCenterRightChild = new TwoFourTreeItem(value3);
                        if (!isLeaf) {
                            // not leaf...
                            parentCenterLeftChild.leftChild = this.leftChild;
                            parentCenterLeftChild.rightChild = this.centerLeftChild;
                            parentCenterLeftChild.leftChild.parent = parentCenterLeftChild;
                            parentCenterLeftChild.rightChild.parent = parentCenterLeftChild;
                            parentCenterLeftChild.isLeaf = false;
                            parent.centerLeftChild = parentCenterLeftChild;
                            parent.centerLeftChild.parent = parent;

                            parentCenterRightChild.leftChild = this.centerRightChild;
                            parentCenterRightChild.rightChild = this.rightChild;
                            parentCenterRightChild.leftChild.parent = parentCenterRightChild;
                            parentCenterRightChild.rightChild.parent = parentCenterRightChild;
                            parentCenterRightChild.isLeaf = false;
                            parent.centerRightChild = parentCenterRightChild;
                            parent.centerLeftChild.parent = parent;

                            parent.centerChild = null;

                            if (parent.value1 > value) {
                                return parent.leftChild;
                            } else if (parent.value3 < value)
                                return parent.rightChild;
                            else if (parent.value1 < value && parent.value2 > value)
                                return parent.centerLeftChild;
                            else if (parent.value2 < value && parent.value3 > value)
                                return parent.centerRightChild;

                            return parent;
                        } else {
                            parent.centerLeftChild = parentCenterLeftChild;
                            parent.centerLeftChild.parent = parent;
                            parent.centerRightChild = parentCenterRightChild;
                            parent.centerRightChild.parent = parent;

                            parent.centerChild = null;

                            if (parent.value1 > value) {
                                return parent.leftChild;
                            } else if (parent.value3 < value)
                                return parent.rightChild;
                            else if (parent.value1 < value && parent.value2 > value)
                                return parent.centerLeftChild;
                            else if (parent.value2 < value && parent.value3 > value)
                                return parent.centerRightChild;

                            return parent;
                        }

                    }

                }
            }
            return null;
        }

        public TwoFourTreeItem findRoot() {
            if (parent != null)
                return this.parent.findRoot();
            return this;
        }

        public boolean isRightChild() {
            if (this == parent.rightChild)
                return true;
            return false;
        }

        public boolean isLeftChild() {
            if (this == parent.leftChild)
                return true;
            return false;
        }

        public boolean isCenterChild() {
            if (this == parent.centerChild)
                return true;
            return false;
        }

        public boolean isCenterLeftChild() {
            if (this == parent.centerLeftChild)
                return true;
            return false;
        }

        public boolean isCenterRightChild() {
            if (this == parent.centerRightChild)
                return true;
            return false;
        }

        public boolean isTwoNode() {
            if (values == 1)
                return true;
            return false;
        }

        public boolean isThreeNode() {
            if (values == 2)
                return true;
            return false;
        }

        public boolean isFourNode() {
            if (values == 3)
                return true;
            return false;
        }

        public boolean isRoot() {
            if (parent == null)
                return true;
            return false;
        }

        public boolean hasLeftChild() {
            if (leftChild != null)
                return true;
            return false;
        }

        public boolean hasRightChild() {
            if (rightChild != null)
                return true;
            return false;
        }

        public TwoFourTreeItem(int value1) {
            values = 1;
            this.value1 = value1;
            if (!hasLeftChild() && !hasRightChild()) {
                leftChild = null;
                rightChild = null;
            }
        }

        public TwoFourTreeItem(int value1, int value2) {
            values = 2;
            if (value1 < value2) {
                this.value1 = value1;
                this.value2 = value2;
            } else {
                this.value1 = value2;
                this.value2 = value1;
            }
            leftChild = null;
            centerChild = null;
            rightChild = null;
        }

        public TwoFourTreeItem(int value1, int value2, int value3) {
            values = 3;
            if (value1 < value2 && value2 < value3) {
                this.value1 = value1;
                this.value2 = value2;
                this.value3 = value3;
            } else if (value2 < value1 && value1 < value3) {
                this.value1 = value2;
                this.value2 = value1;
                this.value3 = value3;
            } else if (value3 < value1 && value1 < value2) {
                this.value1 = value3;
                this.value2 = value1;
                this.value3 = value2;
            } else if (value2 < value3 && value3 < value1) {
                this.value1 = value2;
                this.value2 = value3;
                this.value3 = value1;
            } else if (value3 < value2 && value2 < value1) {
                this.value1 = value3;
                this.value2 = value2;
                this.value3 = value1;
            } else if (value1 < value3 && value3 < value2) {
                this.value1 = value1;
                this.value2 = value3;
                this.value3 = value2;
            }
            leftChild = null;
            centerLeftChild = null;
            centerRightChild = null;
            rightChild = null;
        }

        private void printIndents(int indent) {
            for (int i = 0; i < indent; i++)
                System.out.printf(" ");
        }

        public void printInOrder(int indent) {
            if (!isLeaf)
                leftChild.printInOrder(indent + 1);
            printIndents(indent);
            System.out.printf("%d\n", value1);
            if (isThreeNode()) {
                if (!isLeaf)
                    centerChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
            } else if (isFourNode()) {
                if (!isLeaf)
                    centerLeftChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
                if (!isLeaf)
                    centerRightChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value3);
            }
            if (!isLeaf)
                rightChild.printInOrder(indent + 1);
        }
    }

    TwoFourTreeItem root = null;

    public boolean addValue(int value) {

        if (root == null) {
            TwoFourTreeItem newRoot = new TwoFourTreeItem(value);
            root = newRoot;
            return true;
        }

        // if (hasValue(value)) {
        // System.out.println(value + " already exists, enter another number.");
        // return false;
        // }

        if (root.isRoot() && root.isLeaf) {
            /*
             * If the root is the only node in tree (is a leaf) and is a two node
             * Create a new 3 node and make it a root
             */
            if (root.isTwoNode()) {
                TwoFourTreeItem newNode = new TwoFourTreeItem(value, root.value1);
                root = newNode;
                return true;
            } /*
               * If the root is a three node and is a leaf
               * Place the new value
               */
            else if (root.isThreeNode()) {
                root = new TwoFourTreeItem(root.value1, value, root.value2);
                return true;
            } /*
               * If the root is a four node and is a leaf
               * Place the middle value as root and split
               * add in new value
               */
            else if (root.isFourNode()) {
                int middle = root.value2;
                TwoFourTreeItem newRoot = new TwoFourTreeItem(middle);
                TwoFourTreeItem leftChild = new TwoFourTreeItem(root.value1);
                TwoFourTreeItem rightChild = new TwoFourTreeItem(root.value3);
                root = newRoot;
                root.leftChild = leftChild;
                root.rightChild = rightChild;
                root.rightChild.parent = root;
                root.leftChild.parent = root;
                root.isLeaf = false;

                // add in new value
                if (root.value1 > value) {
                    // is smaller
                    TwoFourTreeItem newLeftChild = new TwoFourTreeItem(value, root.leftChild.value1);
                    root.leftChild = newLeftChild;
                    root.leftChild.parent = root;
                    return true;
                } else if (root.value1 < value) {
                    TwoFourTreeItem newRightChild = new TwoFourTreeItem(value, root.rightChild.value1);
                    root.rightChild = newRightChild;
                    root.rightChild.parent = root;
                    return true;
                } else
                    return false; // if equal
            }
        }
        // traverse through the tree to add node to leaf
        // but if the root is a 4 node and not a leaf split it
        TwoFourTreeItem tempAddNode = root;
        if (tempAddNode.isFourNode()) {
            tempAddNode = tempAddNode.split(value);
        }

        while (!tempAddNode.isLeaf || tempAddNode.isFourNode()) {
            if (tempAddNode.isTwoNode()) {
                if (tempAddNode.value1 < value) {
                    tempAddNode = tempAddNode.rightChild;
                    continue;
                } else if (tempAddNode.value1 > value) {
                    tempAddNode = tempAddNode.leftChild;
                    continue;
                } else
                    return false;
            } else if (tempAddNode.isThreeNode()) {
                if (tempAddNode.value1 > value) {
                    tempAddNode = tempAddNode.leftChild;
                    continue;
                } else if (tempAddNode.value1 < value && value < tempAddNode.value2) {
                    tempAddNode = tempAddNode.centerChild;
                    continue;
                } else if (tempAddNode.value2 < value) {
                    tempAddNode = tempAddNode.rightChild;
                    continue;
                } else
                    return false;
            } else if (tempAddNode.isFourNode()) {
                // split function to split and move childs properly based on the type of the
                // node
                // and which child to the parent this node is
                if (tempAddNode.value1 == value || tempAddNode.value2 == value || tempAddNode.value3 == value)
                    return false;
                tempAddNode = tempAddNode.split(value);
                continue;
            }
        }

        if (tempAddNode.isTwoNode()) {
            if (tempAddNode.value1 == value)
                return false;
            TwoFourTreeItem newNode = new TwoFourTreeItem(value, tempAddNode.value1);
            if (tempAddNode.isRightChild()) {
                tempAddNode.parent.rightChild = newNode;
                tempAddNode.parent.rightChild.parent = tempAddNode.parent;
                root = tempAddNode.findRoot();
                return true;
            } else if (tempAddNode.isLeftChild()) {
                tempAddNode.parent.leftChild = newNode;
                tempAddNode.parent.leftChild.parent = tempAddNode.parent;
                root = tempAddNode.findRoot();
                return true;
            } else if (tempAddNode.isCenterChild()) {
                tempAddNode.parent.centerChild = newNode;
                tempAddNode.parent.centerChild.parent = tempAddNode.parent;
                root = tempAddNode.findRoot();
                return true;
            } else if (tempAddNode.isCenterLeftChild()) {
                tempAddNode.parent.centerLeftChild = newNode;
                tempAddNode.parent.centerLeftChild.parent = tempAddNode.parent;
                root = tempAddNode.findRoot();
                return true;

            } else if (tempAddNode.isCenterRightChild()) {
                tempAddNode.parent.centerRightChild = newNode;
                tempAddNode.parent.centerRightChild.parent = tempAddNode.parent;
                root = tempAddNode.findRoot();
                return true;
            }
        } else if (tempAddNode.isThreeNode()) {
            if (tempAddNode.value1 == value || tempAddNode.value2 == value)
                return false;
            TwoFourTreeItem newNode = new TwoFourTreeItem(value, tempAddNode.value1, tempAddNode.value2);
            if (tempAddNode.isRightChild()) {
                tempAddNode.parent.rightChild = newNode;
                tempAddNode.parent.rightChild.parent = tempAddNode.parent;
                root = tempAddNode.findRoot();
                return true;
            } else if (tempAddNode.isLeftChild()) {
                tempAddNode.parent.leftChild = newNode;
                tempAddNode.parent.leftChild.parent = tempAddNode.parent;
                root = tempAddNode.findRoot();
                return true;
            } else if (tempAddNode.isCenterChild()) {
                tempAddNode.parent.centerChild = newNode;
                tempAddNode.parent.centerChild.parent = tempAddNode.parent;
                root = tempAddNode.findRoot();
                return true;
            } else if (tempAddNode.isCenterLeftChild()) {
                tempAddNode.parent.centerLeftChild = newNode;
                tempAddNode.parent.centerLeftChild.parent = tempAddNode.parent;
                root = tempAddNode.findRoot();
                return true;
            } else if (tempAddNode.isCenterRightChild()) {
                tempAddNode.parent.centerRightChild = newNode;
                tempAddNode.parent.centerRightChild.parent = tempAddNode.parent;
                root = tempAddNode.findRoot();
                return true;
            }
        }
        return false; // should not get here
    }

    public boolean hasValue(int value) {
        TwoFourTreeItem temp = root;

        // if temp is null return false
        if (temp == null) {
            return false;
        }

        // if root is a leaf check for values in each possible spot and return true if
        // found
        if (temp.isLeaf && (temp.value1 == value || temp.value2 == value || temp.value3 == value)) {
            return true;
        }

        // traverse and look for value in tree
        while (temp != null) {
            if (temp.isTwoNode()) {
                if (temp.value1 == value)
                    return true;
                else {
                    if (value < temp.value1)
                        temp = temp.leftChild;
                    else {
                        temp = temp.rightChild;
                    }

                }
            } else if (temp.isThreeNode()) {
                if (temp.value1 == value || temp.value2 == value || temp.value3 == value)
                    return true;
                else {
                    if (value < temp.value1)
                        temp = temp.leftChild;
                    else if (value > temp.value2)
                        temp = temp.rightChild;
                    else if (value > temp.value1 && value < temp.value2)
                        temp = temp.centerChild;
                }
            } else if (temp.isFourNode()) {
                if (temp.value1 == value || temp.value2 == value || temp.value3 == value)
                    return true;
                else {
                    if (value < temp.value1)
                        temp = temp.leftChild;
                    else if (value > temp.value3)
                        temp = temp.rightChild;
                    else if (value > temp.value1 && value < temp.value2)
                        temp = temp.centerLeftChild;
                    else if (value > temp.value2 && value < temp.value3)
                        temp = temp.centerRightChild;
                }
            }
        }

        return false;

    }

    /*
     * I HAVE TO MAKE SURE IM RECONNECTING ALL PARENT REFERENCES
     * FOR MOVING CHILDREN
     */

    private TwoFourTreeItem twoNodeRotation(TwoFourTreeItem node) {
        // this function is called when we have encountered a two node
        // we now need to do a rotation to 'grow' (merge) our 2-3-4 tree or fusion
        // if both siblings are 2 nodes

        if (node.parent.isTwoNode() && !node.parent.isRoot()) {
            return node.parent; // mistake??? shouldnt happen
        }

        // this case is when the child is of a root is 2 node and we encounter it
        // this means our left sibling is a 3 or 4 node so we do a rotation on the root
        // as an anchor
        if (node.parent.isTwoNode() && node.parent.isRoot() && node.isRightChild()
                && !node.parent.leftChild.isTwoNode()) {
            // the left child is guaranteed to be a 3 or 4 node because
            // of our merge on the way down rule
            if (node.parent.leftChild.isThreeNode()) {
                // sibling 3 node
                int siblingValue = node.parent.leftChild.value2;
                int parentValue = node.parent.value1;

                node.values++;
                node.centerChild = node.leftChild;
                node.leftChild = node.parent.leftChild.rightChild;
                if (node.leftChild != null)
                    node.leftChild.parent = node;

                node.parent.leftChild.values--;
                node.parent.leftChild.value2 = 0;
                node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
                node.parent.leftChild.rightChild.parent = node.parent.leftChild;
                node.parent.leftChild.centerChild = null;

                node.parent.value1 = siblingValue;
                node.value2 = node.value1;
                node.value1 = parentValue;
                return node;
            } else if (node.parent.leftChild.isFourNode()) {
                // 4 node sibling
                int siblingValue = node.parent.leftChild.value3;
                int parentValue = node.parent.value1;

                node.values++;
                node.centerChild = node.leftChild;
                if (node.centerChild != null)
                    node.centerChild.parent = node;
                node.leftChild = node.parent.leftChild.rightChild;
                if (node.leftChild != null)
                    node.leftChild.parent = node;

                node.parent.value1 = siblingValue;
                node.value2 = node.value1;
                node.value1 = parentValue;

                node.parent.leftChild.values--;
                node.parent.leftChild.value3 = 0;
                node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
                node.parent.leftChild.rightChild.parent = node.parent.leftChild;
                node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
                node.parent.leftChild.centerChild.parent = node.parent.leftChild;
                node.parent.leftChild.centerLeftChild = null;
                node.parent.leftChild.centerRightChild = null;

                return node;
            }
        }
        if (node.parent.isTwoNode() && node.parent.isRoot() && node.isLeftChild()
                && !node.parent.rightChild.isTwoNode()) {
            // rotate with right sibling
            if (node.parent.rightChild.isThreeNode()) {
                // sibling 3 node
                int siblingValue = node.parent.rightChild.value1;
                int parentValue = node.parent.value1;

                node.values++;
                node.centerChild = node.rightChild;
                node.rightChild = node.parent.rightChild.leftChild;
                if (node.rightChild != null)
                    node.rightChild.parent = node;

                node.parent.rightChild.values--;
                node.parent.rightChild.value1 = node.parent.rightChild.value2;
                node.parent.rightChild.value2 = 0;
                node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
                node.parent.rightChild.leftChild.parent = node.parent.rightChild;
                node.parent.rightChild.centerChild = null;

                node.parent.value1 = siblingValue;
                node.value2 = parentValue;
                return node;
            } else if (node.parent.rightChild.isFourNode()) {
                // 4 node sibling
                int siblingValue = node.parent.rightChild.value1;
                int parentValue = node.parent.value1;

                node.values++;
                node.centerChild = node.rightChild;
                node.rightChild = node.parent.rightChild.leftChild;
                if (node.rightChild != null)
                    node.rightChild.parent = node;

                node.parent.value1 = siblingValue;
                node.value2 = parentValue;

                node.parent.rightChild.values--;
                node.parent.rightChild.value1 = node.parent.rightChild.value2;
                node.parent.rightChild.value2 = node.parent.rightChild.value3;
                node.parent.rightChild.value3 = 0;
                node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
                node.parent.rightChild.leftChild.parent = node.parent.rightChild;
                node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
                node.parent.rightChild.centerChild.parent = node.parent.rightChild;
                node.parent.rightChild.centerLeftChild = null;
                node.parent.rightChild.centerRightChild = null;

                return node;
            }
        }
        if (node.isLeftChild()) {
            // the 2 node is a left child of the parent
            // we know there is a right sibling guarantteed in all 2-3-4 nodes for this case
            // but not a left sibling since it already is a left child of the parent
            if (node.parent.isThreeNode() && !node.parent.centerChild.isTwoNode()) {
                // if the parent is three node and...
                // if its right immediate sibling isnt a two node (center P child)

                int parentValue = node.parent.value1;
                node.values++;
                node.value2 = parentValue;
                node.centerChild = node.rightChild;
                node.rightChild = node.parent.centerChild.leftChild;
                if (node.rightChild != null)
                    node.rightChild.parent = node;

                int centerValue = node.parent.centerChild.value1;
                node.parent.value1 = centerValue;
                node.parent.centerChild.value1 = node.parent.centerChild.value2;

                // if center child is three node..
                if (node.parent.centerChild.isThreeNode()) {
                    // move values and children
                    node.parent.centerChild.value2 = 0;
                    node.parent.centerChild.leftChild = node.parent.centerChild.centerChild;
                    node.parent.centerChild.centerChild = null;
                } else if (node.parent.centerChild.isFourNode()) {
                    // its four node: move values and children
                    node.parent.centerChild.value2 = node.parent.centerChild.value3;
                    node.parent.centerChild.value3 = 0;
                    node.parent.centerChild.leftChild = node.parent.centerChild.centerLeftChild;
                    node.parent.centerChild.centerChild = node.parent.centerChild.centerRightChild;
                    node.parent.centerChild.centerLeftChild = null;
                    node.parent.centerChild.centerRightChild = null;
                }
                // edge case done now return node

                node.parent.centerChild.values--;
                return node;
            } else if (node.parent.isFourNode() && !node.parent.centerLeftChild.isTwoNode()) {
                // if the parent is a four node and its center left child isnt a two node
                int parentValue = node.parent.value1;
                node.values++;
                node.value2 = parentValue;
                node.centerChild = node.rightChild;
                node.rightChild = node.parent.centerLeftChild.leftChild;
                if (node.rightChild != null)
                    node.rightChild.parent = node;

                int centerLeftValue = node.parent.centerLeftChild.value1;
                node.parent.value1 = centerLeftValue;
                node.parent.centerLeftChild.value1 = node.parent.centerLeftChild.value2;

                // if center left child is three node..
                if (node.parent.centerLeftChild.isThreeNode()) {
                    // move values and children
                    node.parent.centerLeftChild.value2 = 0;
                    node.parent.centerLeftChild.leftChild = node.parent.centerLeftChild.centerChild;
                    node.parent.centerLeftChild.centerChild = null;
                } else if (node.parent.centerLeftChild.isFourNode()) {
                    // its four node: move values and children
                    node.parent.centerLeftChild.value2 = node.parent.centerLeftChild.value3;
                    node.parent.centerLeftChild.value3 = 0;
                    node.parent.centerLeftChild.leftChild = node.parent.centerLeftChild.centerLeftChild;
                    node.parent.centerLeftChild.centerChild = node.parent.centerLeftChild.centerRightChild;
                    node.parent.centerLeftChild.centerLeftChild = null;
                    node.parent.centerLeftChild.centerRightChild = null;
                }

                node.parent.centerLeftChild.values--;
                return node;
            } else {
                // guaranteed to have a parent w 3 or 4 nodes, its right sibling is a 2 node
                // so we now fuse
                if (node.parent.isThreeNode()) {
                    int rightSibling = node.parent.centerChild.value1;
                    int parentValue = node.parent.value1;

                    node.values += 2;
                    node.value2 = parentValue;
                    node.value3 = rightSibling;

                    node.centerLeftChild = node.rightChild;
                    node.centerRightChild = node.parent.centerChild.leftChild;
                    if (node.centerRightChild != null)
                        node.centerRightChild.parent = node;
                    node.rightChild = node.parent.centerChild.rightChild;
                    if (node.rightChild != null)
                        node.rightChild.parent = node;

                    node.parent.value1 = node.parent.value2;
                    node.parent.value2 = 0;
                    node.parent.values--;
                    node.parent.leftChild = node;
                    node.parent.centerChild = null;
                } else if (node.parent.isFourNode()) {
                    int rightSibling = node.parent.centerLeftChild.value1;
                    int parentValue = node.parent.value1;

                    node.values += 2;
                    node.value2 = parentValue;
                    node.value3 = rightSibling;

                    node.centerLeftChild = node.rightChild;
                    node.centerRightChild = node.parent.centerLeftChild.leftChild;
                    if (node.centerRightChild != null)
                        node.centerRightChild.parent = node;
                    node.rightChild = node.parent.centerLeftChild.rightChild;
                    if (node.rightChild != null)
                        node.rightChild.parent = node;

                    node.parent.value1 = node.parent.value2;
                    node.parent.value2 = node.parent.value3;
                    node.parent.value3 = 0;
                    node.parent.values--;
                    node.parent.leftChild = node;
                    node.parent.centerChild = node.parent.centerRightChild;
                    node.parent.centerLeftChild = null;
                    node.parent.centerRightChild = null;
                }
                return node;
            }
        } else if (node.isRightChild()) {
            // the 2 node is a right child of the parent
            // the only possibility is having a left sibling
            if (node.parent.isThreeNode() && !node.parent.centerChild.isTwoNode()) {

                int parentValue = node.parent.value2;
                node.values++;
                node.value2 = node.value1;
                node.value1 = parentValue;
                node.centerChild = node.leftChild;
                node.leftChild = node.parent.centerChild.rightChild;
                if (node.leftChild != null)
                    node.leftChild.parent = node;

                // if center child is three node..
                if (node.parent.centerChild.isThreeNode()) {
                    // move values and children
                    int centerChild = node.parent.centerChild.value2;
                    node.parent.value2 = centerChild;
                    node.parent.centerChild.value2 = 0;
                    node.parent.centerChild.rightChild = node.parent.centerChild.centerChild;
                    node.parent.centerChild.centerChild = null;
                } else if (node.parent.centerChild.isFourNode()) {
                    // its four node: move values and children
                    int centerChild = node.parent.centerChild.value3;
                    node.parent.value2 = centerChild;
                    node.parent.centerChild.value3 = 0;
                    node.parent.centerChild.centerChild = node.parent.centerChild.centerLeftChild;
                    node.parent.centerChild.rightChild = node.parent.centerChild.centerRightChild;
                    node.parent.centerChild.centerLeftChild = null;
                    node.parent.centerChild.centerRightChild = null;
                }
                // edge case done now return node
                node.parent.centerChild.values--;
                return node;
            } else if (node.parent.isFourNode() && !node.parent.centerRightChild.isTwoNode()) {
                // if the parent is a four node
                int parentValue = node.parent.value3;
                node.values++;
                node.value2 = node.value1;
                node.value1 = parentValue;
                node.centerChild = node.leftChild;
                node.leftChild = node.parent.centerRightChild.rightChild;
                if (node.leftChild != null)
                    node.leftChild.parent = node;

                if (node.parent.centerRightChild.isThreeNode()) {
                    // move values and children
                    int centerRightValue = node.parent.centerRightChild.value2;
                    node.parent.value3 = centerRightValue;
                    node.parent.centerRightChild.value2 = 0;
                    node.parent.centerRightChild.rightChild = node.parent.centerRightChild.centerChild;
                    node.parent.centerRightChild.centerChild = null;
                } else if (node.parent.centerRightChild.isFourNode()) {
                    // its four node: move values and children
                    int centerRightValue = node.parent.centerRightChild.value3;
                    node.parent.value3 = centerRightValue;
                    node.parent.centerRightChild.value3 = 0;
                    node.parent.centerRightChild.rightChild = node.parent.centerRightChild.centerRightChild;
                    node.parent.centerRightChild.centerChild = node.parent.centerRightChild.centerLeftChild;
                    node.parent.centerRightChild.centerLeftChild = null;
                    node.parent.centerRightChild.centerRightChild = null;
                }

                node.parent.centerRightChild.values--;
                return node;
            } else {
                // the parent is guaranteed to be more than 2 nodes
                // our left sibling is also a 2 node so we need to FUSE with left sib
                if (node.parent.isThreeNode()) {
                    int leftSibling = node.parent.centerChild.value1;
                    int parentValue = node.parent.value2;

                    node.values += 2;
                    node.value3 = node.value1;
                    node.value1 = leftSibling;
                    node.value2 = parentValue;

                    node.centerRightChild = node.leftChild;
                    node.centerLeftChild = node.parent.centerChild.rightChild;
                    if (node.centerLeftChild != null)
                        node.centerLeftChild.parent = node;
                    node.leftChild = node.parent.centerChild.leftChild;
                    if (node.leftChild != null)
                        node.leftChild.parent = node;

                    node.parent.value2 = 0;
                    node.parent.values--;
                    node.parent.rightChild = node;
                    node.parent.centerChild = null;
                } else if (node.parent.isFourNode()) {
                    int leftSibling = node.parent.centerRightChild.value1;
                    int parentValue = node.parent.value3;

                    node.values += 2;
                    node.value3 = node.value1;
                    node.value1 = leftSibling;
                    node.value2 = parentValue;

                    node.centerRightChild = node.leftChild;
                    node.centerLeftChild = node.parent.centerRightChild.rightChild;
                    if (node.centerLeftChild != null)
                        node.centerLeftChild.parent = node;
                    node.leftChild = node.parent.centerRightChild.leftChild;
                    if (node.leftChild != null)
                        node.leftChild.parent = node;

                    node.parent.value3 = 0;
                    node.parent.values--;
                    node.parent.rightChild = node;
                    node.parent.centerChild = node.parent.centerLeftChild;
                    node.parent.centerLeftChild = null;
                    node.parent.centerRightChild = null;
                }
                return node;
            }
        } else if (node.isCenterChild()) {
            // the 2 node is a center child of the parent
            // this can have a choice between left and right immediate sibling
            // we know the parent has to be a 3 node
            TwoFourTreeItem rightSiblingRef = node.parent.rightChild;
            TwoFourTreeItem leftSiblingRef = node.parent.leftChild;
            if (rightSiblingRef.isThreeNode()) {
                int parentValue = node.parent.value2;
                node.values++;
                node.value2 = parentValue;
                node.centerChild = node.rightChild;
                node.rightChild = rightSiblingRef.leftChild;
                if (node.rightChild != null)
                    node.rightChild.parent = node;

                node.parent.value2 = rightSiblingRef.value1;
                int right2 = node.parent.rightChild.value2;
                node.parent.rightChild.value1 = right2;
                node.parent.rightChild.value2 = 0;
                node.parent.rightChild.values--;

                node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
                node.parent.rightChild.centerChild = null;

                return node;
            } else if (rightSiblingRef.isFourNode()) {
                int parentValue = node.parent.value2;
                node.values++;
                node.value2 = parentValue;
                node.centerChild = node.rightChild;
                node.rightChild = rightSiblingRef.leftChild;
                if (node.rightChild != null)
                    node.rightChild.parent = node;

                node.parent.value2 = rightSiblingRef.value1;
                int right2 = node.parent.rightChild.value2;
                int right3 = node.parent.rightChild.value3;
                node.parent.rightChild.value1 = right2;
                node.parent.rightChild.value2 = right3;
                node.parent.rightChild.value3 = 0;
                node.parent.rightChild.values--;

                node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
                node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
                node.parent.rightChild.centerLeftChild = null;
                node.parent.rightChild.centerRightChild = null;

                return node;
            } else if (leftSiblingRef.isThreeNode()) {
                int parentValue = node.parent.value1;
                int node1 = node.value1;
                node.values++;
                node.value1 = parentValue;
                node.value2 = node1;
                node.centerChild = node.leftChild;
                node.leftChild = leftSiblingRef.rightChild;
                if (node.leftChild != null)
                    node.leftChild.parent = node;

                node.parent.value1 = leftSiblingRef.value2;
                node.parent.leftChild.value2 = 0;
                node.parent.leftChild.values--;

                node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
                node.parent.rightChild.centerChild = null;

                return node;
            } else if (leftSiblingRef.isFourNode()) {
                int parentValue = node.parent.value1;
                int node1 = node.value1;
                node.values++;
                node.value1 = parentValue;
                node.value2 = node1;
                node.centerChild = node.leftChild;
                node.leftChild = leftSiblingRef.rightChild;
                if (node.leftChild != null)
                    node.leftChild.parent = node;

                node.parent.value1 = leftSiblingRef.value3;
                node.parent.leftChild.value3 = 0;
                node.parent.leftChild.values--;

                node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
                node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
                node.parent.leftChild.centerLeftChild = null;
                node.parent.leftChild.centerRightChild = null;

                return node;
            } else {
                // both siblings are 2 nodes
                // for this tree we choose to FUSE with right sibling

                node.values += 2;
                int parentIntermediate = node.parent.value2;
                int rightSiblingValue = node.parent.rightChild.value1;

                node.value2 = parentIntermediate;
                node.value3 = rightSiblingValue;
                node.centerLeftChild = node.rightChild;
                node.centerRightChild = node.parent.rightChild.leftChild;
                if (node.centerRightChild != null)
                    node.centerRightChild.parent = node;
                node.rightChild = node.parent.rightChild.rightChild;
                if (node.rightChild != null)
                    node.rightChild.parent = node;

                node.parent.value2 = 0;
                node.parent.values--;
                node.parent.rightChild = node;
                node.parent.centerChild = null;

                return node;
            }
        } else if (node.isCenterLeftChild()) {
            TwoFourTreeItem rightSiblingRef = node.parent.centerRightChild;
            TwoFourTreeItem leftSiblingRef = node.parent.leftChild;
            if (rightSiblingRef.isThreeNode()) {
                int parentValue = node.parent.value2;
                node.values++;
                node.value2 = parentValue;
                node.centerChild = node.rightChild;
                node.rightChild = rightSiblingRef.leftChild;
                if (node.rightChild != null)
                    node.rightChild.parent = node;

                node.parent.value2 = rightSiblingRef.value1;
                int right2 = rightSiblingRef.value2;
                node.parent.centerRightChild.value1 = right2;
                node.parent.centerRightChild.value2 = 0;
                node.parent.centerRightChild.values--;

                node.parent.centerRightChild.leftChild = node.parent.centerRightChild.centerChild;
                node.parent.centerRightChild.centerChild = null;

                return node;
            } else if (rightSiblingRef.isFourNode()) {
                int parentValue = node.parent.value2;
                node.values++;
                node.value2 = parentValue;
                node.centerChild = node.rightChild;
                node.rightChild = rightSiblingRef.leftChild;
                if (node.rightChild != null)
                    node.rightChild.parent = node;

                node.parent.value2 = rightSiblingRef.value1;
                int right2 = rightSiblingRef.value2;
                int right3 = rightSiblingRef.value3;
                node.parent.centerRightChild.value1 = right2;
                node.parent.centerRightChild.value2 = right3;
                node.parent.centerRightChild.value3 = 0;
                node.parent.centerRightChild.values--;

                node.parent.centerRightChild.leftChild = node.parent.centerRightChild.centerLeftChild;
                node.parent.centerRightChild.centerChild = node.parent.centerRightChild.centerRightChild;
                node.parent.centerRightChild.centerLeftChild = null;
                node.parent.centerRightChild.centerRightChild = null;

                return node;
            } else if (leftSiblingRef.isThreeNode()) {
                int parentValue = node.parent.value1;
                int node1 = node.value1;
                node.values++;
                node.value1 = parentValue;
                node.value2 = node1;
                node.centerChild = node.leftChild;
                node.leftChild = leftSiblingRef.rightChild;
                if (node.leftChild != null)
                    node.leftChild.parent = node;

                node.parent.value1 = leftSiblingRef.value2;
                node.parent.leftChild.value2 = 0;
                node.parent.leftChild.values--;

                node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
                node.parent.rightChild.centerChild = null;

                return node;
            } else if (leftSiblingRef.isFourNode()) {
                int parentValue = node.parent.value1;
                int node1 = node.value1;
                node.values++;
                node.value1 = parentValue;
                node.value2 = node1;
                node.centerChild = node.leftChild;
                node.leftChild = leftSiblingRef.rightChild;
                if (node.leftChild != null)
                    node.leftChild.parent = node;

                node.parent.value1 = leftSiblingRef.value3;
                node.parent.leftChild.value3 = 0;
                node.parent.leftChild.values--;

                node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
                node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
                node.parent.leftChild.centerLeftChild = null;
                node.parent.leftChild.centerRightChild = null;

                return node;
            } else {
                // both siblings are 2 nodes
                // for this tree we choose to FUSE with right sibling

                node.values += 2;
                int parentIntermediate = node.parent.value2;
                int rightSiblingValue = rightSiblingRef.value1;

                node.value2 = parentIntermediate;
                node.value3 = rightSiblingValue;
                node.centerLeftChild = node.rightChild;
                node.centerRightChild = rightSiblingRef.leftChild;
                if (node.centerRightChild != null)
                    node.centerRightChild.parent = node;
                node.rightChild = rightSiblingRef.rightChild;
                if (node.rightChild != null)
                    node.rightChild.parent = node;

                node.parent.value2 = node.parent.value3;
                node.parent.value3 = 0;
                node.parent.values--;
                node.parent.centerChild = node;
                node.parent.centerLeftChild = null;
                node.parent.centerRightChild = null;

                return node;
            }
        } else if (node.isCenterRightChild()) {
            TwoFourTreeItem rightSiblingRef = node.parent.rightChild;
            TwoFourTreeItem leftSiblingRef = node.parent.centerLeftChild;
            if (rightSiblingRef.isThreeNode()) {
                int parentValue = node.parent.value3;
                node.values++;
                node.value2 = parentValue;
                node.centerChild = node.rightChild;
                node.rightChild = rightSiblingRef.leftChild;
                if (node.rightChild != null)
                    node.rightChild.parent = node;

                node.parent.value3 = rightSiblingRef.value1;
                int right2 = rightSiblingRef.value2;
                node.parent.rightChild.value1 = right2;
                node.parent.rightChild.value2 = 0;
                node.parent.rightChild.values--;

                node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
                node.parent.rightChild.centerChild = null;

                return node;
            } else if (rightSiblingRef.isFourNode()) {
                int parentValue = node.parent.value3;
                node.values++;
                node.value2 = parentValue;
                node.centerChild = node.rightChild;
                node.rightChild = rightSiblingRef.leftChild;
                if (node.rightChild != null)
                    node.rightChild.parent = node;

                node.parent.value3 = rightSiblingRef.value1;
                int right2 = rightSiblingRef.value2;
                int right3 = rightSiblingRef.value3;
                node.parent.rightChild.value1 = right2;
                node.parent.rightChild.value2 = right3;
                node.parent.rightChild.value3 = 0;
                node.parent.rightChild.values--;

                node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
                node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
                node.parent.rightChild.centerLeftChild = null;
                node.parent.rightChild.centerRightChild = null;

                return node;
            } else if (leftSiblingRef.isThreeNode()) {
                int parentValue = node.parent.value2;
                int node1 = node.value1;
                node.values++;
                node.value1 = parentValue;
                node.value2 = node1;
                node.centerChild = node.leftChild;
                node.leftChild = leftSiblingRef.rightChild;
                if (node.leftChild != null)
                    node.leftChild.parent = node;

                node.parent.value2 = leftSiblingRef.value2;
                node.parent.centerLeftChild.value2 = 0;
                node.parent.centerLeftChild.values--;

                node.parent.centerLeftChild.rightChild = node.parent.centerLeftChild.centerChild;
                node.parent.centerLeftChild.centerChild = null;

                return node;
            } else if (leftSiblingRef.isFourNode()) {
                int parentValue = node.parent.value2;
                int node1 = node.value1;
                node.values++;
                node.value1 = parentValue;
                node.value2 = node1;
                node.centerChild = node.leftChild;
                node.leftChild = leftSiblingRef.rightChild;
                if (node.leftChild != null)
                    node.leftChild.parent = node;

                node.parent.value2 = leftSiblingRef.value3;
                node.parent.centerLeftChild.value3 = 0;
                node.parent.centerLeftChild.values--;

                node.parent.centerLeftChild.rightChild = node.parent.centerLeftChild.centerRightChild;
                node.parent.centerLeftChild.centerChild = node.parent.centerLeftChild.centerLeftChild;
                node.parent.centerLeftChild.centerLeftChild = null;
                node.parent.centerLeftChild.centerRightChild = null;

                return node;
            } else {
                // both siblings are 2 nodes
                // we choose to FUSE with left sibling

                node.values += 2;
                int parentIntermediate = node.parent.value2;
                int leftSiblingValue = leftSiblingRef.value1;

                node.value3 = node.value1;
                node.value2 = parentIntermediate;
                node.value1 = leftSiblingValue;
                node.centerRightChild = node.leftChild;
                node.centerLeftChild = leftSiblingRef.rightChild;
                if (node.centerLeftChild != null)
                    node.centerLeftChild.parent = node;
                node.leftChild = leftSiblingRef.leftChild;
                if (node.leftChild != null)
                    node.leftChild.parent = node;

                node.parent.value2 = node.parent.value3;
                node.parent.value3 = 0;
                node.parent.values--;
                node.parent.centerChild = node;
                node.parent.centerLeftChild = null;
                node.parent.centerRightChild = null;

                return node.parent;
            }
        }

        return null;
    }

    private boolean isInternal(TwoFourTreeItem node) {
        // if it has a child return true if not return false
        if (node.hasLeftChild())
            return true;
        return false;
    }

    private TwoFourTreeItem traverseOnceToValue(TwoFourTreeItem temp, int value) {
        if (temp == null)
            return null;

        if (temp.isTwoNode()) {
            if (temp.value1 > value)
                temp = temp.leftChild;
            else if (temp.value1 < value)
                temp = temp.rightChild;
        } else if (temp.isThreeNode()) {
            if (temp.value1 > value)
                temp = temp.leftChild;
            else if (temp.value1 < value && temp.value2 > value)
                temp = temp.centerChild;
            else if (temp.value2 < value)
                temp = temp.rightChild;
        } else if (temp.isFourNode()) {
            if (temp.value1 > value)
                temp = temp.leftChild;
            else if (temp.value1 < value && temp.value2 > value)
                temp = temp.centerLeftChild;
            else if (temp.value2 < value && temp.value3 > value)
                temp = temp.centerRightChild;
            else if (temp.value3 < value)
                temp = temp.rightChild;
        }

        return temp;
    }

    private TwoFourTreeItem goToLeftChildofThisValue(TwoFourTreeItem temp, int value) {
        if (temp == null)
            return null;

        // this will happen if when the 2 node rotation passes up back its parent
        // node, this only happens in order to prevent a bug from happening in my
        // implementation
        // i pass the parent value up when I need to fuse (both siblings = 2 node)
        // so traverse once to find the value passed
        if ((value == temp.value1 || value == temp.value2 || value == temp.value3) == false) {
            temp = traverseOnceToValue(temp, value);
        }

        // we will not use the two node left child in this
        // operation because this function is called after a 2 node is
        // merged or fused, with the value of the previous 2 node being passed
        // so in theory our passed temp should not be a 2 node
        if (temp.isTwoNode() && value == temp.value1) {
            return temp.leftChild;
        } else if (temp.isThreeNode()) {
            if (value == temp.value1)
                return temp.leftChild;
            else if (value == temp.value2)
                return temp.centerChild;
        } else if (temp.isFourNode()) {
            if (value == temp.value1)
                return temp.leftChild;
            else if (value == temp.value2)
                return temp.centerLeftChild;
            else if (value == temp.value3)
                return temp.centerRightChild;
        }

        return null;
    }

    private TwoFourTreeItem successorLeafFinder(TwoFourTreeItem leaf, int value) {

        if (leaf.leftChild == null)
            return leaf;

        // we first have to check if the node passed initially
        // is a two node because it will have a left and right sibling
        // when we switch places of our delete value and successor value
        // it was the left most child of our delete value's successor
        // we need to traverse correctly when we do a 2 node rotation...
        // this complicates things but this is a simple solution
        if (leaf.isTwoNode()) {
            int saveVal = leaf.value1;
            leaf = twoNodeRotation(leaf);
            leaf = goToLeftChildofThisValue(leaf, saveVal);
        }

        while (leaf.leftChild != null) {
            if (leaf.isTwoNode()) {
                // rotate or merge on our way down
                leaf = twoNodeRotation(leaf);
                if (leaf.isTwoNode()) {
                    if (value < leaf.value1) {
                        leaf = leaf.leftChild;
                        continue;
                    } else if (value > leaf.value1) {
                        leaf = leaf.rightChild;
                        continue;
                    }
                } else if (leaf.isThreeNode()) {
                    if (leaf.value1 > value) {
                        leaf = leaf.leftChild;
                        continue;
                    } else if (leaf.value2 < value) {
                        leaf = leaf.rightChild;
                        continue;
                    } else if (leaf.value1 < value && leaf.value2 > value) {
                        leaf = leaf.centerChild;
                        continue;
                    }
                } else if (leaf.isFourNode()) {
                    if (leaf.value1 > value) {
                        leaf = leaf.leftChild;
                        continue;
                    } else if (leaf.value3 < value) {
                        leaf = leaf.rightChild;
                        continue;
                    } else if (leaf.value1 < value && leaf.value2 > value) {
                        leaf = leaf.centerLeftChild;
                        continue;
                    } else if (leaf.value2 < value && leaf.value3 > value) {
                        leaf = leaf.centerRightChild;
                        continue;
                    }
                }
            }
            // we have to check if the left child exists in case
            if (leaf.leftChild != null)
                leaf = leaf.leftChild;
        }
        return leaf;

    }

    // this is for root successor swapping
    private TwoFourTreeItem findSuccessor(TwoFourTreeItem findSuccessor, int value) {
        if (findSuccessor == null)
            return null;

        // traverse to the left most child , the right descendant is already passed
        while (findSuccessor.leftChild != null) {
            // if (findSuccessor.isTwoNode())
            // findSuccessor = twoNodeRotation(findSuccessor);
            findSuccessor = findSuccessor.leftChild;
        }

        // replace successor with value
        TwoFourTreeItem successor = findSuccessor;
        int successorValue = successor.value1;
        successor.value1 = value;

        // go back to the value we were looking the successor for and replace it with
        // the successor
        // THIS SHOULD THEORETICALLY ALWAYS WORK
        boolean replaced = false;
        while (successor.parent != null && !replaced) {
            successor = successor.parent;
            if (successor.value1 == value || successor.value2 == value || successor.value3 == value) {
                if (successor.value1 == value) {
                    successor.value1 = successorValue;
                    replaced = true;
                } else if (successor.value2 == value) {
                    successor.value2 = successorValue;
                    replaced = true;
                } else if (successor.value3 == value) {
                    successor.value3 = successorValue;
                    replaced = true;
                } else // not foundddd shouldnt get here
                    return null;
            }
        }

        // now return the successor node
        return successor;
    }

    private TwoFourTreeItem findSuccessor(TwoFourTreeItem findSuccessor) {
        int value = 0;
        if (findSuccessor == null)
            return null;

        // save the value we want to delete for swapping later in this function
        // must not be a root for this to work, we have a solution outside of this
        // function for root
        // swapping
        if (findSuccessor.parent != null) {
            if (findSuccessor.isCenterLeftChild() || findSuccessor.isCenterChild()
                    || (findSuccessor.parent.isTwoNode() && findSuccessor.isRightChild())) {
                value = findSuccessor.parent.value1;
            } else if (findSuccessor.isCenterRightChild()
                    || (findSuccessor.parent.isThreeNode() && findSuccessor.isRightChild())) {
                value = findSuccessor.parent.value2;
            } else {
                value = findSuccessor.parent.value3;
            }
        }

        // traverse to the left most child , the right descendant is already passed
        while (findSuccessor.leftChild != null) {
            // if (findSuccessor.isTwoNode())
            // findSuccessor = twoNodeRotation(findSuccessor);
            findSuccessor = findSuccessor.leftChild;
        }

        // replace successor
        TwoFourTreeItem successor = findSuccessor;
        int successorValue = successor.value1;
        successor.value1 = value;

        // go back to the value we were looking the successor for and replace it with
        // the successor
        // THIS SHOULD THEORETICALLY ALWAYS WORK
        boolean replaced = false;
        while (successor.parent != null && !replaced) {
            successor = successor.parent;
            if (successor.value1 == value || successor.value2 == value || successor.value3 == value) {
                if (successor.value1 == value) {
                    successor.value1 = successorValue;
                    replaced = true;
                } else if (successor.value2 == value) {
                    successor.value2 = successorValue;
                    replaced = true;
                } else if (successor.value3 == value) {
                    successor.value3 = successorValue;
                    replaced = true;
                } else // not foundddd shouldnt get here
                    return null;
            }
        }

        // now return the successor node
        return successor;
    }

    public boolean deleteValue(int value) {
        TwoFourTreeItem temp = root;
        boolean found = false;

        // if the value is in the root, swap with successor
        if (root.isTwoNode() && root.value1 == value && !root.isLeaf) {
            root = findSuccessor(root.rightChild, value);
            temp = root;
            temp = successorLeafFinder(temp.rightChild, value);
        } else if (root.isThreeNode() && (root.value1 == value || root.value2 == value) && !root.isLeaf) {
            if (root.value1 == value) {
                root = findSuccessor(root.centerChild, value);
                temp = root;
                temp = successorLeafFinder(temp.centerChild, value);
            } else if (root.value2 == value) {
                root = findSuccessor(root.rightChild, value);
                temp = root;
                temp = successorLeafFinder(temp.rightChild, value);
            }
        } else if (root.isFourNode() && (root.value1 == value || root.value2 == value || root.value3 == value)
                && !root.isLeaf) {
            if (root.value1 == value) {
                root = findSuccessor(root.centerLeftChild, value);
                temp = root;
                temp = successorLeafFinder(temp.centerLeftChild, value);
            } else if (root.value2 == value) {
                root = findSuccessor(root.centerRightChild, value);
                temp = root;
                temp = successorLeafFinder(temp.centerRightChild, value);
            } else if (root.value3 == value) {
                root = findSuccessor(root.rightChild, value);
                temp = root;
                temp = successorLeafFinder(temp.rightChild, value);
            }
        }

        // if the root is a two node and both its childs are two nodes we need to fuse
        // them together
        if (root.isTwoNode() && root.leftChild.isTwoNode() && root.rightChild.isTwoNode()) {
            temp = fuseRoot(root);
        }

        // traverse the tree to find value passed to be deleted
        while (!temp.isLeaf && !found) {
            if (temp.isTwoNode()) {
                if (!temp.isRoot()) {
                    temp = twoNodeRotation(temp);
                    if (temp.isThreeNode()) {
                        if (temp.value1 > value) {
                            temp = temp.leftChild;
                            continue;
                        } else if (temp.value2 < value) {
                            temp = temp.rightChild;
                            continue;
                        } else if (temp.value1 < value && temp.value2 > value) {
                            temp = temp.centerChild;
                            continue;
                        }
                    } else if (temp.isFourNode()) {
                        if (temp.value1 > value) {
                            temp = temp.leftChild;
                            continue;
                        } else if (temp.value3 < value) {
                            temp = temp.rightChild;
                            continue;
                        } else if (temp.value1 < value && temp.value2 > value) {
                            temp = temp.centerLeftChild;
                            continue;
                        } else if (temp.value2 < value && temp.value3 > value) {
                            temp = temp.centerRightChild;
                            continue;
                        }
                    }
                } else if (temp.value1 < value)
                    temp = temp.rightChild;
                else if (temp.value1 > value)
                    temp = temp.leftChild;
            } else if (temp.isThreeNode()) {
                if (temp.value1 == value || temp.value2 == value) {
                    found = true;
                } else if (temp.value1 > value) {
                    temp = temp.leftChild;
                } else if (temp.value2 < value) {
                    temp = temp.rightChild;
                } else if (temp.value1 < value && value < temp.value2) {
                    temp = temp.centerChild;
                }
            } else if (temp.isFourNode()) {
                if (temp.value1 == value || temp.value2 == value || temp.value3 == value) {
                    found = true;
                } else if (temp.value1 > value) {
                    temp = temp.leftChild;
                } else if (temp.value3 < value) {
                    temp = temp.rightChild;
                } else if (temp.value1 < value && value < temp.value2) {
                    temp = temp.centerLeftChild;
                } else if (temp.value2 < value && value < temp.value3) {
                    temp = temp.centerRightChild;
                }
            }
        }

        // since the iteration above stops at a leaf
        // or internal node we need to check if the value
        // was found at a leaf
        if ((temp.isLeaf || isInternal(temp)) && (temp.value1 == value || temp.value2 == value || temp.value3 == value))
            found = true;

        // check to see if the value is in an internal node:
        // NEEDS TO BE WORKED ON TO FUNCTION TO DELETE SUCCESSOR AFTER SWITCHING VALUES
        if (isInternal(temp) && found) {
            // if it is internal, then we need to do a switch with the successor
            // then recursively delete T
            if (temp.isThreeNode() && temp.value1 == value) {
                temp = findSuccessor(temp.centerChild);
                temp = successorLeafFinder(temp.centerChild, value);
            } else if (temp.isThreeNode() && temp.value2 == value) {
                temp = findSuccessor(temp.rightChild);
                temp = successorLeafFinder(temp.rightChild, value);
            } else if (temp.isFourNode() && temp.value1 == value) {
                temp = findSuccessor(temp.centerLeftChild);
                temp = successorLeafFinder(temp.centerLeftChild, value);
            } else if (temp.isFourNode() && temp.value2 == value) {
                temp = findSuccessor(temp.centerRightChild);
                temp = successorLeafFinder(temp.centerRightChild, value);
            } else if (temp.isFourNode() && temp.value3 == value) {
                temp = findSuccessor(temp.rightChild);
                temp = successorLeafFinder(temp.rightChild, value);
            }
        }

        // should never happen because the value should have been found
        if (found == false)
            return false;

        // if the value is in a leaf and its a two node we need to do a rotation in
        // order to remove
        // the value
        if (temp.isLeaf && temp.isTwoNode()) {
            temp = twoNodeRotation(temp);
            if (!temp.isLeaf)
                if (temp.isTwoNode()) {
                    if (!temp.isRoot()) {
                        temp = twoNodeRotation(temp);
                    } else if (temp.value1 < value)
                        temp = temp.rightChild;
                    else if (temp.value1 > value)
                        temp = temp.leftChild;
                } else if (temp.isThreeNode()) {
                    if (temp.value1 == value || temp.value2 == value) {
                        found = true;
                    } else if (temp.value1 > value) {
                        temp = temp.leftChild;
                    } else if (temp.value2 < value) {
                        temp = temp.rightChild;
                    } else if (temp.value1 < value && value < temp.value2) {
                        temp = temp.centerChild;
                    }
                } else if (temp.isFourNode()) {
                    if (temp.value1 == value || temp.value2 == value || temp.value3 == value) {
                        found = true;
                    } else if (temp.value1 > value) {
                        temp = temp.leftChild;
                    } else if (temp.value3 < value) {
                        temp = temp.rightChild;
                    } else if (temp.value1 < value && value < temp.value2) {
                        temp = temp.centerLeftChild;
                    } else if (temp.value2 < value && value < temp.value3) {
                        temp = temp.centerRightChild;
                    }
                }
        }

        // if the value IS in a leaf node GUARANTEED with at least 3 nodes, simply
        // remove the value
        if ((temp.isThreeNode()) && (temp.value1 == value || temp.value2 == value) && (temp.isLeaf)) {
            if (temp.value1 == value) {
                temp.values--;
                temp.value1 = temp.value2;
                temp.value2 = 0;
                root = temp.findRoot();
                return true;
            } else if (temp.value2 == value) {
                temp.values--;
                temp.value2 = 0;
                root = temp.findRoot();
                return true;
            }
        } else if ((temp.isFourNode()) && (temp.value1 == value || temp.value2 == value || temp.value3 == value)
                && (temp.isLeaf)) {
            if (temp.value1 == value) {
                temp.values--;
                temp.value1 = temp.value2;
                temp.value2 = temp.value3;
                temp.value3 = 0;
                root = temp.findRoot();
                return true;
            } else if (temp.value2 == value) {
                temp.values--;
                temp.value2 = temp.value3;
                temp.value3 = 0;
                root = temp.findRoot();
                return true;
            } else if (temp.value3 == value) {
                temp.values--;
                temp.value3 = 0;
                root = temp.findRoot();
                return true;
            }
        }

        return false;
    }

    private TwoFourTreeItem fuseRoot(TwoFourTreeItem fuseNode) {
        // this function is executed when the fuseNode passed is a root
        // and needs to be merged, but we also use this function in the odd chance
        // that during our traversal we encounter a root that needs to be rotated
        // since it does not have both children as 2 nodes
        // THAT WILL BE OUR BASE CASE

        // if one of the children isnt a two node
        if (!fuseNode.leftChild.isTwoNode() || !fuseNode.rightChild.isTwoNode()) {
            // ..
        }

        // fused node
        TwoFourTreeItem fused = new TwoFourTreeItem(fuseNode.leftChild.value1, fuseNode.value1,
                fuseNode.rightChild.value1);
        if (!fuseNode.leftChild.isLeaf) {
            // if the children are not leafs, reconnect references
            TwoFourTreeItem leftChild = fuseNode.leftChild;
            TwoFourTreeItem rightChild = fuseNode.rightChild;

            fused.leftChild = leftChild.leftChild;
            fused.centerLeftChild = leftChild.rightChild;
            fused.centerRightChild = rightChild.leftChild;
            fused.rightChild = rightChild.rightChild;

            fused.isLeaf = false;
            fused.leftChild.parent = fused;
            fused.centerLeftChild.parent = fused;
            fused.centerRightChild.parent = fused;
            fused.rightChild.parent = fused;

            return fused;
        } else {
            // else return fused
            return fused;
        }
    }

    public void printInOrder() {
        if (root != null)
            root.printInOrder(0);
    }

    public TwoFourTree() {
        
    }
}