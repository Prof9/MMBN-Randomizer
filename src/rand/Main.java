package rand;

import java.awt.EventQueue;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import rand.gui.MainFrame;

public class Main {
	public static final String VERSION = "v1.1 BETA";
	private static final boolean DEBUG = false;
	private static final int RESULT_SUCCESS = 0;
	private static final int RESULT_WARNING = 1;
	private static final int RESULT_ERROR = 2;
	private static final int RESULT_FATAL = 3;

	public static void main(String[] args) {
		// Debug parameters
		if (DEBUG && args.length == 0) {
			args = new String[3];
			args[0] = "roms\\Rockman EXE 5 - Team of Blues (J).gba";
			args[1] = "out5pj.gba";
			args[2] = "" + 0x12345678;
		}

		if (!DEBUG) {
			try {
				run(args);
			} catch (Exception ex) {
				System.err.println("FATAL ERROR: " + ex.getMessage());
				System.exit(RESULT_FATAL);
			}
		} else {
			run(args);
		}
	}

	private static void run(String[] args) {
		if (args.length == 0) {
			runGUI();
		} else {
			System.exit(runCMD(args));
		}
	}

	private static void runGUI() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame mainFrame = new MainFrame();
				mainFrame.setLocationByPlatform(true);
				mainFrame.setVisible(true);
			}
		});
	}

	private static int runCMD(String[] args) {
		int result = RESULT_SUCCESS;
		System.out.println("MMBN Randomizer " + Main.VERSION);
		System.out.println("By Prof. 9");
		System.out.println("https://github.com/prof9/mmbn-randomizer/");
		System.out.println();

		// Get input path.
		if (args.length <= 0) {
			System.err.println("ERROR: Input path not specified.");
			return RESULT_ERROR;
		}
		String inPath = args[0];

		// Get seed.
		int seed = new Random().nextInt();
		if (args.length > 2) {
			seed = toSeed(args[2]);
		}

		// Get output path.
		String outPath;
		if (args.length > 1) {
			outPath = args[1];
		} else {
			outPath = String.format("Randomized-%08X.gba", seed);
		}

		// Load input ROM.
		ByteStream rom;
		try {
			rom = readFile(inPath);
		} catch (IOException ex) {
			System.err.println("ERROR: Could not read input ROM.");
			return RESULT_ERROR;
		}

		// Get randomizer context.
		RandomizerContext context = ContextSelector.getAppropriateContext(rom);
		if (context == null) {
			System.err.println("ERROR: Unsupported ROM ID.");
			return RESULT_ERROR;
		}

		// Run randomizer.
		System.out.println("Starting...");
		context.setSeed(seed);
		context.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				System.out.println((String) arg);
			}
		});
		context.randomize(rom);

		// Write output ROM.
		try {
			writeFile(outPath, rom);
		} catch (IOException ex) {
			System.err.println("ERROR: Could not write output ROM.");
			return RESULT_ERROR;
		}

		System.out.println("Done!");
		return result;
	}

	public static int toSeed(String seedString) {
		try {
			return Integer.parseInt(seedString);
		} catch (NumberFormatException ex) {
			return seedString.hashCode();
		}
	}

	public static ByteStream readFile(String inPath) throws IOException {
		Path in = Paths.get(inPath);
		ByteStream rom = new ByteStream(in, 0x08000000);
		return rom;
	}

	public static void writeFile(String outPath, ByteStream rom) throws IOException {
		Files.write(Paths.get(outPath), rom.toBytes());
	}

	public static String getRomId(ByteStream rom) {
		rom.setRealPosition(0x0000AC);
		return new String(rom.readBytes(4), StandardCharsets.US_ASCII);
	}
}
