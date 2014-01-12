import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class BitMap {
	  private int[][] bits;
	    private int  columns;
	    private int rows;

	    /**
	     * Get the Rows value.
	     * @return the Rows value.
	     */
	    public int getRows() {
	        return rows;
	    }


	    /**
	     * Get the Columns value.
	     * @return the Columns value.
	     */
	    public int getColumns() {
	        return columns;
	    }

	    public boolean isSet(int i, int j){
	        return (bits[i][j]!=0);
	    }

	    public void flip(int i, int j){
	    
	            bits[i][j]=1;
	        
	    }


	    BitMap(){
	        this(0,0);
	    }
	    BitMap(int rows, int cols){
	        bits=new int[rows][cols];
	        for(int i=0;i<rows;i++){
	        	for(int j=0;j<cols;j++){
	        		bits[i][j]=0;
	        	}
	        }
	    }

	    BitMap(String filename) throws IOException{
	        readFile(filename);
	    }

	    public void readFile(String filename) throws IOException{
	                BufferedReader infile=  new BufferedReader(new FileReader(filename));

	        String firstLine = infile.readLine();

	        if (!firstLine.equals("P1"))
	            throw new IOException("File format error");

	        StringTokenizer tokens = new StringTokenizer(infile.readLine(), " ");

	        columns=Integer.parseInt(tokens.nextToken());
	        rows=Integer.parseInt(tokens.nextToken());

	        bits=new int[rows][columns];

	        for (int i=0; i< rows; i++){
	            String line=infile.readLine();

	            for (int j=0; j<columns; j++){
	                bits[i][j]=line.charAt(j)-'0';
	            }
	        }
	    }
	    private boolean isIsolated(int row,int col,int threshold){
	        int found=0;

	        if (bits[row][col]==0) {
	            return false;
	        }

	        for (int i=row-1; i<=row+1; i++){
	            for (int j=col-1; j<= col+1; j++){
	                if (i>=0 && j>=0 && i< rows && j<columns) {
	                    found +=bits[i][j];
	                }
	            }
	        }
	        return (found<=threshold);
	    }
	    public void filter(int threshold){
	        for (int i=0; i< rows; i++){
	            for (int j=0; j<columns; j++){
	                if (isIsolated(i,j,threshold)){
	                    bits[i][j]=0;
	                }
	            }
	        }
	    }
	    public String toString(){

	        String result="";

	        for (int i=0; i< rows; i++){
	            for (int j=0; j<columns; j++){
	                if (bits[i][j]==1)
	                    result += "*";
	                else
	                    result += " ";
	            }
	            result +="\n";
	        }
	        return result;
	    }
}
