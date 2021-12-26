/**
 * 
 * Percolation
 * 
 * @author Timothy Altonji
 */

public class Percolation {

	private boolean[][] grid;          // gridSize by gridSize grid of sites; 
	                                   // true = open site, false = closed or blocked site
	private WeightedQuickUnionFind wquFind; // 
	private int 		gridSize;      // gridSize by gridSize is the size of the grid/system 
	private int         gridSquared;

	/**
	* Constructor.
	* Initializes all instance variables
	*/
	public Percolation (int n) {
		gridSize 	  = n;
		gridSquared   = gridSize * gridSize;
		wquFind       = new WeightedQuickUnionFind(gridSquared + 2);
		grid          = new boolean[gridSize][gridSize];   // every site is initialized to closed/blocked
	} 

	/**
	* Getter method for GridSize 
	* @return integer representing the size of the grid.
	*/
	public int getGridSize () {
		return gridSize;
	}

	/**
	 * Returns the grid array
	 * @return grid array
	 */
	public boolean[][] getGridArray () {
		return grid;
	}

	//helper method
	private boolean isOpen(int i, int j) {
		return grid[i][j];
	}

	/**
	* Open the site at postion (x,y) on the grid to true and add an edge
	* to any open neighbor (left, right, top, bottom) and/or top/bottom virtual sites
	* Note: diagonal sites are not neighbors
	*
	* @param row grid row
	* @param col grid column
	* @return void
	*/
	public void openSite (int row, int col) {
		
		if (row < 0 || row > gridSize || col < 0 || col > gridSize) 
		{
            throw new java.lang.IndexOutOfBoundsException();
        }
		int i = row;
		int j = col;
		grid[i][j] = true;
		
		if (i - 1 >= 0 && isOpen(i - 1, j)) //down
		{
			wquFind.union(to1D(i , j),to1D(i - 1, j));
		}
		if (i + 1 < gridSize && isOpen(i + 1, j)) //up
		{
			wquFind.union(to1D(i, j),to1D(i + 1, j));
		}
		if (j - 1 >= 0 && isOpen(i, j - 1)) //left
		{
			wquFind.union(to1D(i, j),to1D(i, j - 1));
		}
		if (j + 1 < gridSize && isOpen(i, j + 1)) //right
		{
			wquFind.union(to1D(i, j),to1D(i, j + 1));
		}

	}

	//helper method to derive a single site position
	private int to1D (int row, int col) {
		int pos = (row * gridSize) + col + 1; // Indeces
		return pos;
	}

	/**
	* Check if the system percolates (any top and bottom sites are connected by open sites)
	* @return true if system percolates, false otherwise
	*/
	public boolean percolationCheck () {
		for (int i = (gridSize * (gridSize - 1)) - 1; i < (gridSize * gridSize); i++)
        {
            for (int j = 0; j < gridSize; j++)
            {
                if (wquFind.find(i) == wquFind.find(j)) 
					return true;
            }
        }
		return false;
	}

	/**
	 * Iterates over the grid array openning every site. 
	 * Starts at [0][0] and moves row wise 
	 * @param probability
	 * @param seed
	 */
	public void openAllSites (double probability, long seed) {

		// Setting the same seed before generating random numbers ensure that
		// the same numbers are generated between different runs
		StdRandom.setSeed(seed); 
		for (int row = 0; row < grid.length; row++) 
		{ 
			for (int col = 0; col < grid[row].length; col++) 
			{ 
				if(StdRandom.uniform() <= probability)
					openSite(row, col);
				else 
				{ 
					continue;
				}
			}
		}
	}
	
	/**
	* Open up a new window and display the current grid using StdDraw library.
	* The output will be colored based on the grid array. Blue for open site, black for closed site.
	* @return: void 
	*/
	public void displayGrid () {
		double blockSize = 0.9 / gridSize;
		double zeroPt =  0.05+(blockSize/2), x = zeroPt, y = zeroPt;

		for ( int i = gridSize-1; i >= 0; i-- ) 
		{
			x = zeroPt;
			for ( int j = 0; j < gridSize; j++) 
			{
				if ( grid[i][j] ) 
				{
					StdDraw.setPenColor( StdDraw.BOOK_LIGHT_BLUE );
					StdDraw.filledSquare( x, y ,blockSize/2);
					StdDraw.setPenColor( StdDraw.BLACK);
					StdDraw.square( x, y ,blockSize/2);		
				} else {
					StdDraw.filledSquare( x, y ,blockSize/2);
				}
				x += blockSize; 
			}
			y += blockSize;
		}
	}

	/**
	* Main method
	*/
	public static void main ( String[] args ) {

		double p = 0.47;
		Percolation pl = new Percolation(5);

		/* 
		 * Setting a seed before generating random numbers ensure that
		 * the same numbers are generated between runs.
		 */
		long seed = System.currentTimeMillis();
		pl.openAllSites(p, seed);

		System.out.println("The system percolates: " + pl.percolationCheck());
		pl.displayGrid();
	}
}