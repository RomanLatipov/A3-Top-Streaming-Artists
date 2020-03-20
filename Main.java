import java.io.*;
import java.util.*;
//Movie Node
class Movie {
	//the movies statistics
	private String title;
  	private String releaseYear;
  	private String[] genres; // optional
  	private int movieId; //optional

  	Movie leftChild;
  	Movie rightChild;
  	//
  	Movie(String title, String releaseYear, String[] genres, int movieId) {
  		this.title = title;
  		this.releaseYear = releaseYear;
  		this.genres = genres;
  		this.movieId = movieId;
  	}
  	//since title is private the movie node needs a getter method when comparing mvoie titles
  	public String getTitle() {
  		return title;
  	}

 	public String toString() {
 		//since some movies don't have a release year and/or genres, our sorting algorithm creates a Movie node with release year/genre as null
 		if (releaseYear == null)
  			return title + " has no release date. Its movie id is: " + movieId + "and its genres are " + Arrays.toString(genres);
  		else if (genres == null)
  			return title + " was released in " + releaseYear + ". Its movie id is: " + movieId + " and it has no genres listed.";
  		else if (genres == null && releaseYear == null)
  			return title + " has no release date. Its movie id is: " + movieId + " and it has no genres listed.";
  		else
  			return title + " was released in " + releaseYear + ". Its movie id is: " + movieId + " and its genres are " + Arrays.toString(genres);
  	}
}
class BinaryTree {
	Movie root;
	public void addMovie(String title, String releaseYear, String[] genres, int movieId) {
		Movie newMovie = new Movie(title, releaseYear, genres, movieId);
		// If there is no root newMovie becomes the root
		if (root == null) {
			root = newMovie;
		}
		else {
			//as we traverse the tree, root is the first one we start with
			Movie focusNode = root;
			Movie parent;
			while (true) {
				parent = focusNode;
				//checks if the new node goes to the left of the parent node
				if (title.compareToIgnoreCase(focusNode.getTitle())<0) {
					//switch focus to the left child
					focusNode = focusNode.leftChild;
					//if the left child has no children then it places the new node to the left of it
					if (focusNode == null) {
						parent.leftChild = newMovie;
						return;
					}
				}
				//this put the new node to the right
				else {
					//switch focus to the right child
					focusNode = focusNode.rightChild;
					//if the right child has no children then it places the new node to the right of it
					if (focusNode == null) {
						parent.rightChild = newMovie;
						return;
					}
				}
			}
		}
	}
	//this method prints all the nodes in ascending order
	//I did get this online, I do understand how it works for the 
	//most part but its too complicated for me to come up on my own
	public void inOrder(Movie focusNode, PrintStream output) {
		if (focusNode != null) {
			inOrder(focusNode.leftChild, output);
			output.println(focusNode);
			inOrder(focusNode.rightChild, output);
		}
	}
	//this method prints a subset of our tree between the starting string and the ending string
	//Like the prevoius method I did  not come up with this on my own, I just managed to fit it 
	//into my own program
	public void subSet(Movie focusNode, String start, String end, PrintStream output) {
		if (focusNode == null) { 
           	return; 
        } 
        if (start.compareToIgnoreCase(focusNode.getTitle())<0) { 
           	subSet(focusNode.leftChild, start, end, output); 
        } 
        if (start.compareToIgnoreCase(focusNode.getTitle())<=0 && end.compareToIgnoreCase(focusNode.getTitle())>=0) { 
           	output.println(focusNode); 
        }
        if (end.compareToIgnoreCase(focusNode.getTitle())>0) { 
           	subSet(focusNode.rightChild, start, end, output);
        }
    }
}
public class Main {
	public static void moviesToBinaryTree(BinaryTree tree) throws IOException {
		//Scanner sc traverses through the csv file line by line
		BufferedReader br = new BufferedReader(new FileReader("movies.csv"));
		Scanner sc = new Scanner(br);
		while (sc.hasNext()) {
			//each line in the csv file become a string for us to work with
			String temp = sc.nextLine();
			//the first set of numbers prior to the first "," are used for the movie id
			String tempId = temp.substring(0, temp.indexOf(","));
			//the movie id string is then turned into an int
			int id = Integer.valueOf(tempId);
			String[] genres;
			//some movies don't have genres listed thus this part of the algorithm checks if it has genres
			//and sets String[] genres as null
			if (temp.substring(temp.lastIndexOf(",")).contains("no genres listed")) {
				genres = null;
			}
			//if a movie does have genres then it takes the substring and splits
			//the string by "|" and turns it into a string array
			else {
				String genre = temp.substring(temp.lastIndexOf(",")+1);
      			genres = genre.split("\\|");
			}
			String year = "";
			String title = "";
			//since some movies don't have a release year then this part of the algorithm checks
			//for the existence of a ")" since all dates are written between parentheses, whilst
			//also taking a substtring after the first "," and setting it as the title
      		if ((temp.substring(temp.lastIndexOf(",")-3, temp.lastIndexOf(","))).contains(")")) {
      			year = temp.substring(temp.lastIndexOf("(")+1, temp.lastIndexOf(")"));
      			//some movie title are between "" thus this part of the algorithm checks for
      			//them and tries to work between them
      			if (temp.contains("\"")) {
      				year = temp.substring(0, temp.lastIndexOf(",")-1);
      				title = temp.substring(temp.indexOf(",")+2, year.lastIndexOf("(")-1);
      				year = year.substring(year.lastIndexOf("(")+1, year.lastIndexOf(")"));
      			}
      			else {
      				year = temp.substring(0, temp.lastIndexOf(","));
      				title = temp.substring(temp.indexOf(",")+1, year.lastIndexOf("(")-1);
      				year = year.substring(year.lastIndexOf("(")+1, year.lastIndexOf(")"));
      			}
      		}
      		//if a movie doesn't have a release year then this part just sets the year as null
      		//and sets the title as a substring between the first and last ","
      		else {
      			year = null;
      			if (temp.contains("\""))
      				title = temp.substring(temp.indexOf(",")+2, temp.lastIndexOf(",")-1);
      			else
      				title = temp.substring(temp.indexOf(",")+1, temp.lastIndexOf(",")-1);
      		}
      		//crrates a movie node and gives it a title, release year, genres, and id
      		tree.addMovie(title, year, genres, id);
      	}
    }
    public static void main(String[] args) throws IOException {
    	BinaryTree tree = new BinaryTree();
    	PrintStream output = new PrintStream(new FileOutputStream("output.txt"));
		moviesToBinaryTree(tree);
		//this call for the program to print the entire tree in ascending order
		//tree.inOrder(tree.root, output);
		//this is an example of how you would go about printing a subset in the binary tree
		tree.subSet(tree.root, "Bug's Life", "Harry Potter and the Chamber of Secrets", output);
		//tree.subSet(tree.root, "Back to the Future", "Hulk");
		//tree.subSet(tree.root, "Toy Story", "WALL·E");
	}
}
