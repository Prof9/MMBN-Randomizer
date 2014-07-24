package rand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import javax.swing.UIManager;
import mmbn.bn6.BN6RandomizerContext;
import rand.gui.MainFrame;

public class Main {
    private static final boolean DEBUG = false;
    
    @SuppressWarnings("UseSpecificCatch")
    public static void main(String[] args) {
        // Debug parameters
        if (DEBUG && args.length == 0) {
            args = new String[3];
            args[0] = "roms\\Mega Man Battle Network 6 - Cybeast Falzar (U).gba";
            args[1] = "out.gba";
            args[2] = "" + 0x12345678;
        }
        
        if (args.length == 0) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch (Exception ex) {
                System.err.println("WARNING: Could not set system look and feel.");
                // tough luck yo
            }
            MainFrame mainFrame = new MainFrame();
            mainFrame.setLocationByPlatform(true);
            mainFrame.setVisible(true);
        } else {
            int result = 0;

            // Get input path.
            if (args.length <= 0) {
                System.err.println("FATAL ERROR: Input path not specified.");
                System.exit(2);
            }
            String inPath = args[0];

            // Get seed.
            long seed = new Random().nextInt();
            if (args.length > 2) {
                try {
                    seed = Integer.parseInt(args[2]);
                }
                catch (NumberFormatException ex) {
                    // Do nothing
                    System.err.println("ERROR: Could not parse specified seed. "
                            + "Using random seed " + seed + " instead.");
                    result = 1;
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
                System.err.println("FATAL ERROR: Could not read input ROM.");
                System.exit(2);
                return;
            }

            // Run randomizer.
            System.out.println("Starting...");
            Random rng = new Random(seed);
            BN6RandomizerContext context = new BN6RandomizerContext(rng);
            context.randomize(rom);

            // Write output ROM.
            try {
                Files.write(Paths.get(outPath), rom.toBytes());
            }
            catch (IOException ex) {
                System.err.println("FATAL ERROR: Could not write output ROM.");
                System.exit(2);
                return;
            }

            System.exit(result);
        }
    }
}
