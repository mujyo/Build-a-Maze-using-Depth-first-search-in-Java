# Build-a-Maze-using-Depth-first-search-in-Java

A perfect maze is the simplest type of maze for a computer to generate and solve. It is defined as a maze which has one and only one path from any point in the maze to any other point in the maze. This means that the maze has no inaccessible sections, no circular paths, no open areas:
Each cell in the grid represents a location in the maze that has a potential exit to the north, south, east, and west. One way to look at the perfect maze is that each location is a hallway that twists and turns its way from one place to another. The challenge for this type of maze is to find the direct path from one place (the pink square) to another (the blue one).

In the non-perfect maze, each cell in the grid also represent a location in the maze, but there can be multiple paths between any two cells. This form is useful in several applications. Computer games, for instance, use this kind of maze to create a map of the world by giving locations in the maze different characteristics. For instance, a room maze categorizes locations in the maze as either rooms or hallways, where a hallway has exactly two doors while a room has 1, 3 or 4 doors. In this example of a room maze, rooms are pictured as circles, hallways are channels. Since locations at the top, bottom, left and right can "wrap" to the other side, we call this a wrapping room maze:

The placement of rooms, doors, and hallways in the grid is usually randomly selected with some constraint that is specified at the time of creation. In general, we start with a perfect maze (since it the simplest to build) and then additional pathways are added until the constraint is met.

Algorithm for building mazes
There are a number of algorithms for building perfect mazes including Kruskal's algorithm. and Prim's algorithm. Each of these requires starting with an undirected graph whose nodes represents locations in the maze, and whose edges represent walls between locations. For this assignment, you will implement a modification of Kruskal's algorithm.
