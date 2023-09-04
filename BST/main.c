/* COP 3502C Assignment 4
This program is written by: Arian Tashakkor*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "leak_detector_c.h"

#define MAXLEN 30

typedef struct itemNode {
  char name[MAXLEN];
  int count;
  struct itemNode *left, *right;
} itemNode;

typedef struct treeNameNode {
  char treeName[MAXLEN];
  struct treeNameNode *left, *right;
  itemNode *theTree;
} treeNameNode;

treeNameNode *buildTreeByItemFromInput(treeNameNode *root, int size);
treeNameNode *handleCommand(treeNameNode *root, int Q);
void traverse_in_traverse(treeNameNode *root);
void deleteTree(treeNameNode *root);
treeNameNode *createTreeNameNode(char *treeName);
treeNameNode *
buildNameTree(treeNameNode *root,
              int count); // Based on the data in the file, it will insert
// them to the name tree and then finally return the root of the name tree
void printOrderByItemFormat(
    treeNameNode *root); // this function takes the root of the name tree
// and prints the data of the name tree and the corresponding item trees in the
// format shown in the sample output. You can call other function from this
// function as needed.
treeNameNode *searchNameNode(treeNameNode *root, char treeName[50]); // This
// function takes a name string and search this name in the name tree and
// returns that node. This function will help you a lot to go to the item tree.
itemNode *searchItemNode(itemNode *root, char itemName[MAXLEN]);
itemNode *createItemNode(char *treeName, char *itemName, int count);
treeNameNode *insertNameNode(treeNameNode *root, treeNameNode *newNode);
itemNode *insertItemNode(itemNode *treeRoot, itemNode *newNode);

int main() {
  atexit(report_mem_leak);
  treeNameNode *root = NULL;
  int N, I, Q;
  scanf("%d %d %d", &N, &I, &Q);
  root = buildNameTree(root, N);
  root = buildTreeByItemFromInput(root, I);
  traverse_in_traverse(root);
  root = handleCommand(root, Q);
  deleteTree(root);
  return 0;
}

void deleteItem(itemNode *root) {
  if (root != NULL) {
    deleteItem(root->left);
    deleteItem(root->right);
    free(root);
  }
}

void findTheCommandFromInput(treeNameNode *root) {
  char name[MAXLEN];
  char nameItem[MAXLEN];
  scanf("%s %s", name, nameItem);
  treeNameNode *node = searchNameNode(root, name);
  if (node == 0)
    printf("%s does not exist\n", name);
  else {
    itemNode *tempItemNode;
    tempItemNode = searchItemNode(node->theTree, nameItem);
    if (tempItemNode == 0)
      printf("%s not found in %s\n", nameItem, node->treeName);
    else
      printf("%d %s found in %s\n", tempItemNode->count, tempItemNode->name,
             node->treeName);
  }
}

int countItemBefore(itemNode *root, char names[MAXLEN]) {
  int cnt = 0;
  if (root == NULL)
    return 0;
  else if (strcmp(root->name, names) < 0) {
    cnt++;
    cnt += countItemBefore(root->left, names);
    cnt += countItemBefore(root->right, names);
  } else {
    cnt += countItemBefore(root->left, names);
  }
  return cnt;
}

void handleBeforeCommand(treeNameNode *root) {
  char name[MAXLEN];
  char nameItem[MAXLEN];
  scanf("%s %s", name, nameItem);
  treeNameNode *tmp = searchNameNode(root, name);
  itemNode *node = searchItemNode(tmp->theTree, nameItem);
  int count = 0;
  count = countItemBefore(tmp->theTree, nameItem);
  printf("item before %s: %d\n", node->name, count);
}

int getHeightTree(itemNode *root) {
  int leftH = 0;
  int rightH = 0;
  if (root == NULL)
    return -1;
  leftH = getHeightTree(root->left);
  rightH = getHeightTree(root->right);
  if (leftH > rightH)
    return 1 + leftH;
  else
    return 1 + rightH;
}

void handleBalanceCommand(treeNameNode *root) {
  char names[MAXLEN];
  scanf("%s", names);
  treeNameNode *nodes = searchNameNode(root, names);
  int left_height = getHeightTree(nodes->theTree->left);
  int right_height = getHeightTree(nodes->theTree->right);
  int diff = abs(left_height - right_height);
  if (diff >= 2) {
    printf("%s: left height %d, right height %d, difference %d, not balanced\n",
           nodes->treeName, left_height, right_height, diff);
  } else
    printf("%s: left height %d, right height %d, difference %d, balanced\n",
           nodes->treeName, left_height, right_height, diff);
}

int getTotalCount(itemNode *root) {
  int sum = 0;
  if (root != NULL) {
    sum += getTotalCount(root->left);
    sum += getTotalCount(root->right);
    return sum + root->count;
  }
  return 0;
}

void handleCountCommand(treeNameNode *root) {
  char names[MAXLEN];
  scanf("%s", names);
  treeNameNode *node = searchNameNode(root, names);
  int sum = getTotalCount(node->theTree);
  printf("%s count %d\n", node->treeName, sum);
}

treeNameNode *getMinNodeByName(treeNameNode *root) {
  treeNameNode *node = root;
  while (node && node->left != NULL)
    node = node->left;
  return node;
}

itemNode *getMinNodeByItem(itemNode *root) {
  itemNode *node = root;
  while (node && node->left != NULL)
    node = node->left;
  return node;
}

itemNode *deleteItemNode(itemNode *root, char names[MAXLEN]) {
  if (root == NULL)
    return root;
  if (strcmp(names, root->name) < 0)
    root->left = deleteItemNode(root->left, names);
  else if (strcmp(names, root->name) > 0)
    root->right = deleteItemNode(root->right, names);
  else {
    if (root->left == NULL) {
      itemNode *tmp = root->right;
      free(root);
      return tmp;
    } else if (root->right == NULL) {
      itemNode *tmp = root->left;
      free(root);
      return tmp;
    }
    itemNode *tmp = getMinNodeByItem(root->right);
    strcpy(root->name, tmp->name);
    root->count = tmp->count;
    root->right = deleteItemNode(root->right, tmp->name);
  }
  return root;
}

void handleDeleteCommand(treeNameNode *root) {
  char name[MAXLEN];
  char nameItem[MAXLEN];

  scanf("%s %s", name, nameItem);

  treeNameNode *node = searchNameNode(root, name);
  itemNode *item = searchItemNode(node->theTree, nameItem);

  char deleteNames[MAXLEN];

  strcpy(deleteNames, item->name);
  node->theTree = deleteItemNode(node->theTree, nameItem);

  printf("%s deleted from %s\n", deleteNames, node->treeName);
}

treeNameNode *deleteNameNode(treeNameNode *root, char names[MAXLEN]) {
  if (root == NULL)
    return root;
  if (strcmp(names, root->treeName) < 0)
    root->left = deleteNameNode(root->left, names);
  else if (strcmp(names, root->treeName) > 0)
    root->right = deleteNameNode(root->right, names);
  else {
    if (root->left == NULL) {
      treeNameNode *tmp = root->right;
      free(root);
      return tmp;
    } else if (root->right == NULL) {
      treeNameNode *tmp = root->left;
      free(root);
      return tmp;
    }
    treeNameNode *tmp = getMinNodeByName(root->right);
    strcpy(root->treeName, tmp->treeName);
    root->right = deleteNameNode(root->right, tmp->treeName);
  }
  return root;
}

void handleDeleteNameCommand(treeNameNode *root) {
  char names[MAXLEN];
  scanf("%s", names);
  treeNameNode *node = searchNameNode(root, names);
  char treeNameFromTree[MAXLEN];
  strcpy(treeNameFromTree, node->treeName);
  deleteItem(node->theTree);
  root = deleteNameNode(root, names);
  printf("%s deleted\n", treeNameFromTree);
}

void handleReduceCommand(treeNameNode *root) {
  char names[MAXLEN];
  char nameItem[MAXLEN];
  int reduce;
  scanf("%s %s %d", names, nameItem, &reduce);
  treeNameNode *node = searchNameNode(root, names);
  itemNode *items = searchItemNode(node->theTree, nameItem);
  items->count -= reduce;
  if (items->count <= 0) {
    printf("%s reduced\n", items->name);
    node->theTree = deleteItemNode(node->theTree, nameItem);
  }else
  printf("%s reduced\n", items->name);
}

treeNameNode *handleCommand(treeNameNode *root, int Q) {
  for (int i = 0; i < Q; i++) {
    char querie[MAXLEN];

    scanf("%s", querie);

    if (strcmp(querie, "search") == 0) {
      findTheCommandFromInput(root);
    } else if (strcmp(querie, "item_before") == 0) {
      handleBeforeCommand(root);
    } else if (strcmp(querie, "height_balance") == 0) {
      handleBalanceCommand(root);
    } else if (strcmp(querie, "count") == 0) {
      handleCountCommand(root);
    } else if (strcmp(querie, "reduce") == 0) {
      handleReduceCommand(root);
    } else if (strcmp(querie, "delete") == 0) {
      handleDeleteCommand(root);
    } else if (strcmp(querie, "delete_name") == 0) {
      handleDeleteNameCommand(root);
    }
  }
  return root;
}

treeNameNode *buildNameTree(treeNameNode *root, int count) {
  treeNameNode *tmp;
  for (int i = 0; i < count; i++) {
    char name[MAXLEN];
    // read tree node name
    scanf("%s", name);
    tmp = createTreeNameNode(name);
    // insert to tree
    root = insertNameNode(root, tmp);
  }
  return root;
}

itemNode *searchItemNode(itemNode *root, char itemName[MAXLEN]) {
  if (root == NULL)
    return 0;
  else {
    if (strcmp(root->name, itemName) == 0) {
      return root;
    } else if (strcmp(root->name, itemName) > 0)
      return searchItemNode(root->left, itemName);
    else
      return searchItemNode(root->right, itemName);
  }
}

treeNameNode *searchNameNode(treeNameNode *root, char treeName[MAXLEN]) {
  if (root == NULL)
    return 0;
  else {
    if (strcmp(root->treeName, treeName) == 0) {
      return root;
    } else if (strcmp(root->treeName, treeName) > 0)
      return searchNameNode(root->left, treeName);
    else
      return searchNameNode(root->right, treeName);
  }
}

treeNameNode *createTreeNameNode(char *treeName) {
  treeNameNode *temp = (treeNameNode *)malloc(sizeof(treeNameNode));
  strcpy(temp->treeName, treeName);
  temp->left = NULL;
  temp->right = NULL;
  temp->theTree = NULL;

  return temp;
}

itemNode *createItemNode(char *treeName, char *itemName, int count) {
  itemNode *item = (itemNode *)malloc(sizeof(itemNode));
  strcpy(item->name, itemName);
  item->count = count;
  item->left = NULL;
  item->right = NULL;

  return item;
}

treeNameNode *insertNameNode(treeNameNode *root, treeNameNode *newNode) {

  if (root == NULL)
    return newNode; // if this is an empty tree, just return the node
  else {
    if (strcmp(newNode->treeName, root->treeName) <= 0) {
      // if there is an element to the left of root, call again
      if (root->left != NULL) {
        root->left = insertNameNode(root->left, newNode);
      } else {
        root->left = newNode;
      }
    } else {
      if (root->right != NULL) {
        root->right = insertNameNode(root->right, newNode);
      } else
        root->right = newNode;
    }
  }
  return root;
}

itemNode *insertItemNode(itemNode *treeRoot, itemNode *newNode) {

  if (treeRoot == NULL) {
    return newNode;
  } else {
    // treeRoot name > newNode name
    if (strcmp(treeRoot->name, newNode->name) < 0) {
      // if an element exists to the left of it's 'root' then call again
      if (treeRoot->right != NULL) {
        treeRoot->right = insertItemNode(treeRoot->right, newNode);
      } else {
        treeRoot->right = newNode;
      }
    }
    // treeRoot name < newNode name OR they are equal strings
    else {
      // if an element exists to the right of it's 'root' then call again
      if (treeRoot->left != NULL) {
        treeRoot->left = insertItemNode(treeRoot->left, newNode);
      } else {
        treeRoot->left = newNode;
      }
    }
  }
  return treeRoot;
}

treeNameNode *buildTreeByItemFromInput(treeNameNode *root, int size) {
  itemNode *item;
  for (int j = 0; j < size; j++) {
    char name[MAXLEN];
    char nameItem[MAXLEN];
    int count;
    scanf("%s %s %d\n", name, nameItem, &count);
    item = createItemNode(name, nameItem, count);
    treeNameNode *temp = searchNameNode(root, name);
    temp->theTree = insertItemNode(temp->theTree, item);
  }
  return root;
}

void deleteTree(treeNameNode *root) {
  if (root != NULL) {
    if (root->theTree != NULL)
      deleteItem(root->theTree);
    deleteTree(root->left);
    deleteTree(root->right);
    free(root);
  }
}

void displayInOrderNameTree(treeNameNode *root) {
  if (root != NULL) {
    displayInOrderNameTree(root->left);
    printf("%s ", root->treeName);
    displayInOrderNameTree(root->right);
  }
}

void printTreeInOrderByItem(itemNode *root) {
  if (root != NULL) {
    printTreeInOrderByItem(root->left);
    printf("%s ", root->name);
    printTreeInOrderByItem(root->right);
  }
}

void printOrderByItemFormat(treeNameNode *root) {
  if (root != NULL) {
    printOrderByItemFormat(root->left);
    printf("\n===%s===\n", root->treeName);
    printTreeInOrderByItem(root->theTree);
    printOrderByItemFormat(root->right);
  }
}

// shown in output
void traverse_in_traverse(treeNameNode *root) {
  displayInOrderNameTree(root);
  printf("\n");
  printOrderByItemFormat(root);
}

void freeTree(itemNode *root) {
  if (root != NULL) {
    freeTree(root->left);
    freeTree(root->right);
    free(root);
  }
}