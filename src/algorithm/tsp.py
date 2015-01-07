'''
Script creates a list representing an adjacency matrix
which is used to construct a path representing an optimal
tour of the graph. 
'''
adjMatrix = []

def tsp():
	test = "Let's go"
	createMatrix(javaGraph)
	minLength = float("inf")
	start = Node(0 , [] , 0)
	start.calcBound()
	pq = NodeQueue()	
	pq.put(start)
	while not pq.empty():
		parent = pq.get()
		#Check for promising
		if parent.bound < minLength: 
			#considers all
			for i in range(0 , len(parent.freeNodes)):
				child = Node(parent.level + 1 , list(parent.path) , parent.freeNodes[i])
				#Path complete, check for updated solution
				if child.level == len(adjMatrix) - 2:
					child.path.append( child.freeNodes[0] )
					child.path.append( child.path[0] )
					if child.length() < minLength:
						minLength = child.length()
						minPath = list(child.path)
				else:
					child.calcBound()
					if child.bound < minLength:
						pq.put(child)

	# Prints path to capture a return from the intrepeters set output
	print minPath

# Creates a 2d list from the list of edges
def createMatrix(graph):
	global adjMatrix
	matrixSize = len(graph.getVertices())

	# Initializes the adj matrix
	adjMatrix = [ [float("inf") for x in range(matrixSize)] for x in range(matrixSize)]

	# Fills in the matrix with existing edge weight
	edgeList = graph.getEdges()
	for edge in edgeList:
		adjMatrix[edge.parentNode.vertexID][edge.destinationNode.vertexID] = edge.weight

class Node:
	def __init__(self , l , parentPath , index):
		self.level = l
		self.path = parentPath
		self.path.append(index)
		self.bound = 0
		self.freeNodes = []
		#Makes array of all nodes not used which represent children
		for allIndex in range(0 , len(adjMatrix)):
			inUse = False
			for usedIndex in range(0 , len(self.path)):
				if allIndex == self.path[usedIndex]:
					inUse = True
					break;
			if not inUse:
				self.freeNodes.append(allIndex)


	#length + all minimums of all free nodes
	def calcBound(self):
		newBound = self.length()

		#add all the minimum costs to leave each unused vertex
		for nodeFrom in self.freeNodes:
			minimum = float("inf")
			for nodeTo in self.freeNodes:
				minimum = min(minimum , adjMatrix[nodeFrom][nodeTo])
			newBound += min(minimum , adjMatrix[nodeFrom][0])

		#Add the minimum cost to leave the last vertex in path
		minimum = float("inf")	
		for nodeTo in self.freeNodes:
			minimum = min(minimum , adjMatrix[self.path[len(self.path) - 1]][nodeTo])		
		self.bound = newBound + minimum
						
	#Sums all the edges along the path
	def length(self):
		length = 0
		for i in range(0 , (len(self.path) - 1)):
			length += adjMatrix[ self.path[i] ][ self.path[i +1] ]
		return length

# Basic priority queue that uses the Node class
# variables for sorting. It's a min heap!
class NodeQueue:
	def __init__(self):
		self.queue = [0]
		self.heapsize = 0

	# Returns the node with the highest priority
	def get(self):
		# Get node with highest priority
		minNode = self.queue[1]

		# Replace the first element with the last element
		self.queue[1] = self.queue[self.heapsize]
		del self.queue[self.heapsize]
		self.heapsize -= 1

		# Restore heap property by sifting down
		i = 1
		while( i < self.heapsize ):
			minIndex = i
			# Left child is smaller than new root
			if 2*i < self.heapsize and self.queue[minIndex].bound > self.queue[2*i].bound:
				minIndex = 2*i
			# Right child is smaller than new root and left child
			if 2*i+1 < self.heapsize and self.queue[minIndex].bound > self.queue[2*i+1].bound:
				minIndex = 2*i+1
			# Heap property is restored
			if minIndex != i:
				tmp = self.queue[i]
				self.queue[i] = self.queue[minIndex]
				self.queue[minIndex] = tmp
				i = minIndex
			# No change signified the heap property is restored
			else:
				break
		return minNode
			


	# Adds the node into the queue based on the bound
	def put(self , node):
		# Add node to last spot in heap
		self.heapsize += 1
		self.queue.append(node)

		#Restore heap property by sifting new node up
		i = self.heapsize
		while(i > 1):
			# If the current node is greater than or equal to parent we are done
			if self.queue[i].bound >= self.queue[i/2].bound:
				break
			# No break, so heap must be restoredd
			temp = self.queue[i]
			self.queue[i] = self.queue[i/2]
			self.queue[i/2] = temp
			# Move index to parent/swapped node and check again
			i = i/2

	# Returns true if the queue is empty
	def empty(self):
		return self.heapsize < 1

tsp()

