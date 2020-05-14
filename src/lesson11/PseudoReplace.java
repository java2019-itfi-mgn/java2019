package lesson11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;

public class PseudoReplace {
	private static final String		ENCODING = "-encoding:";
	
	private static void printUsageAndExit() {	// <1>
		System.err.println("Usage: java -jar PseudoReplace.jar <regular_template> <replacement_value> [-encoding:<encoding>]");
		System.exit(128);
	}

	private static void convertData(final BufferedReader brdr
			, final PrintStream out
			, final String pattern
			, final String replacement) throws IOException {
		int		lineCount = 0;	// <2>
		long	start = System.currentTimeMillis();
		String	line;
		
		while ((line = brdr.readLine()) != null) {	// <3>
			out.println(line.replaceAll(pattern,replacement));
			lineCount++;
		}
		out.flush();	// <4>
		System.err.println("Conversion ended, "+lineCount
				+" lines processed, duration="
				+(System.currentTimeMillis()-start));
	}
	
	public static void main(final String[] args) {
		String	pattern, replacement, encoding = System.getProperty("file.encoding");	// <5>
		
		if (args.length == 0) {		// <6>
			printUsageAndExit();
		}
		else if (args.length < 2) {
			System.err.println("Mandatory arguments are missing!");
			printUsageAndExit();
		}
		else {
			pattern = args[0];
			replacement = args[1];
			if (args.length > 3) {
				System.err.println("Extra arguments in the command string!");
				printUsageAndExit();
			}
			else if (args.length == 3) {
				if (!args[2].startsWith(ENCODING)) {
					System.err.println("Unknown argument ["+args[2]+"] in the command string!");
					printUsageAndExit();
				}
				else {
					encoding = args[2].substring(ENCODING.length());
				}
			}
			
			try(final Reader rdr = new InputStreamReader(System.in,encoding);	// <7>
				final BufferedReader brdr = new BufferedReader(rdr)) {
				
				convertData(brdr,System.out,pattern,replacement);
			} catch (IOException e) {	// <8>
				System.err.println("I/O error converting data: "+e.getMessage());
				System.exit(129);
			}
		}
	}
}