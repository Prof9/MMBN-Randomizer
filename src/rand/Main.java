package rand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import rand.lib.ChipLibrary;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[2];
            args[0] = "roms\\Mega Man Battle Network 6 - Cybeast Falzar (U).gba";
            args[1] = "out.gba";
        }
        
        int result = 0;
        try {
            System.out.println("Starting");
            Path in = Paths.get(args[0]);
            ByteStream rom = new ByteStream(in, 0x08000000);

            Random rng = new Random();
            RandomizerContext context = new RandomizerContext();
            ChipLibrary library = context.randomizeChips(rom);
            context.randomizeBattles(rom);
            context.randomizeFolders(rom, library);
            context.randomizeRewards(rom, library);
            context.randomizeTraders(rom, library);
            
            Files.write(Paths.get(args[1]), rom.toBytes());
        }
        catch (IOException ex) {
            // whatever
            result = 1;
        }
        System.exit(result);
    }
}
