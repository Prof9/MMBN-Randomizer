package rand;

import java.awt.EventQueue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import mmbn.bn6.BN6RandomizerContext;
import rand.gui.MainFrame;

public class Main {
    private static final boolean DEBUG = true;
    private static final int RESULT_SUCCESS = 0;
    private static final int RESULT_WARNING = 1;
    private static final int RESULT_ERROR = 2;
    private static final int RESULT_FATAL = 3;
    
    public static void main(String[] args) {
        try {
            // Debug parameters
            if (DEBUG && args.length == 0) {
                args = new String[3];
                args[0] = "roms\\Mega Man Battle Network 6 - Cybeast Falzar (U).gba";
                args[1] = "out.gba";
                args[2] = "" + 0x12345678;
            }

            if (args.length == 0) {
                runGUI();
            } else {
                System.exit(runCMD(args));
            }
        }
        catch (Exception ex) {
            System.err.println("FATAL ERROR: " + ex.getMessage());
            System.exit(RESULT_FATAL);
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

        // Get input path.
        if (args.length <= 0) {
            System.err.println("ERROR: Input path not specified.");
            return RESULT_ERROR;
        }
        String inPath = args[0];

        // Get seed.
        int seed = new Random().nextInt();
        if (args.length > 2) {
            try {
                seed = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException ex) {
                // Do nothing
                System.err.println("WARNING: Could not parse specified seed. "
                        + "Using random seed " + seed + " instead.");
                result = RESULT_WARNING;
            }
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
            Path in = Paths.get(inPath);
            rom = new ByteStream(in, 0x08000000);
        }
        catch (IOException ex) {
            System.err.println("ERROR: Could not read input ROM.");
            return RESULT_ERROR;
        }

        // Run randomizer.
        System.out.println("Starting...");
        BN6RandomizerContext context = new BN6RandomizerContext(seed);
        context.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println((String)arg);
            }
        });
        context.randomize(rom);
        

        // Write output ROM.
        try {
            Files.write(Paths.get(outPath), rom.toBytes());
        }
        catch (IOException ex) {
            System.err.println("ERROR: Could not write output ROM.");
            return RESULT_ERROR;
        }

        return result;
    }
}
