package dev.grevend.count;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

import java.io.*;
import java.util.Objects;
import java.util.concurrent.Callable;

import static dev.grevend.count.Utils.lines;
import static picocli.CommandLine.Command;

/**
 * The count command entry class.
 *
 * @since sprint 1
 */
@Command(name = "count", version = "count 1.0", mixinStandardHelpOptions = true,
    description = "Count the human-readable characters, lines, or words from stdin or a file and write the number to stdout or a file.")
public class Count implements Callable<Integer> {

    @Spec
    private CommandSpec spec;

    @Parameters(index = "0", description = "Input file", arity = "0..1")
    private File inputFile;

    @Option(names = {"-m", "--method"}, description = "Counting method (default: chars)", arity = "0..1",
        defaultValue = "chars", showDefaultValue = CommandLine.Help.Visibility.NEVER)
    private CountingMethods method;

    /**
     * Main program entry point. Creates a command line instance and delegates the given program args to the count command.
     *
     * @param args the supplied program args
     *
     * @since sprint 1
     */
    public static void main(String[] args) {
        System.exit(new CommandLine(new Count()).execute(args));
    }

    /**
     * Reads text from the selected input stream, computes a count using the method option, and prints the value into the specified output stream.
     *
     * @return the command exit code
     *
     * @throws IOException if the input or output stream cannot be closed
     * @since sprint 1
     */
    @Override
    public Integer call() throws IOException {
        try (var reader = in(); var writer = out()) {
            writer.println(lines(reader).filter(Objects::nonNull).map(String::strip).flatMapToLong(method).sum());
        }
        return 0;
    }

    /**
     * Returns or constructs an input stream based on the selected command options.
     *
     * @return the input stream
     *
     * @throws FileNotFoundException if the input file is not found
     * @since sprint 1
     */
    protected BufferedReader in() throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(inputFile == null ? System.in : new FileInputStream(inputFile)));
    }

    /**
     * Returns or constructs an output stream based on the selected command options.
     *
     * @return the output stream
     *
     * @since sprint 1
     */
    private PrintWriter out() {
        return spec.commandLine().getOut();
    }

}
