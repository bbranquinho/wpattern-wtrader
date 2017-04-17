package rddl.policy.waterloo;

import java.lang.Runtime;
import java.lang.Process;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.InterruptedException;

//NOTE: this class is a wrapper for matlab. If we are not using MATLAB, this class should not be used
//TODO: parameterize bin/matlab location, probably through some sort of properties file
//TODO: figure out how and when we're translating the domains, so we can pass in the sperseus-formatted domain file location here
class SPerseusPOMDPSolver{
	
	private static final String MATLAB_SOLVER = "matlabSPSolver";
	private static final String MATLAB_BIN_LOC = "/home/kyle/matlab/bin/matlab"; //FIXME
	
	class StreamPrinter{
		private InputStream inStream;
		private String contextMessageToUser;
		
		public StreamPrinter(InputStream inStream, String contextMessageToUser){
			this.inStream = inStream;
			this.contextMessageToUser = contextMessageToUser;
		}
		
		public void consumeAndPrintStream() throws IOException{
			System.out.println(this.contextMessageToUser);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
			try{
				String line = "";
				while ((line = reader.readLine()) != null) {
		            System.out.println(line);
		        }
			} finally{
				System.out.println("Finished printing " + this.contextMessageToUser + "\n");
				reader.close();
			}
		}
	}
	
	public void solvePOMDP(String problemFileLocation){
		String commandString = MATLAB_BIN_LOC + " -nodesktop -nosplash -r " + MATLAB_SOLVER + "('"
				+ problemFileLocation + "');";
		Runtime runtime = Runtime.getRuntime();
		try{
	        Process process = runtime.exec(commandString);

	        StreamPrinter inputStreamPrinter = new StreamPrinter(process.getInputStream(), "Input");
	        inputStreamPrinter.consumeAndPrintStream();
	        
	        StreamPrinter errorStreamPrinter = new StreamPrinter(process.getErrorStream(), "ERRORS?");
	        errorStreamPrinter.consumeAndPrintStream();
	        
	        process.waitFor();
	        assert (process.exitValue() == 0);
		} catch(IOException e){
			System.err.println("Could not execute MATLAB solver");
			System.err.println(e.toString());
		} catch (InterruptedException e) {
			System.err.print("Interrupted before solver process could finish");
			e.printStackTrace();
		} finally{
			System.out.println("Done solving " + problemFileLocation);
		}
	}
	
    public static void main(String[] args) throws IOException, InterruptedException{
        SPerseusPOMDPSolver pomdpSolver = new SPerseusPOMDPSolver();
        pomdpSolver.solvePOMDP("../files/boolean/spudd_sperseus/sysadmin_pomdp.sysp1.sperseus");
//        pomdpSolver.solvePOMDP("problems/coffee/coffee3po.txt");
  
        
        
    }
}
