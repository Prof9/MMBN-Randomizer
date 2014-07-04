package rand.prov;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rand.Rom;
import rand.lib.BattleChip;
import rand.lib.ChipLibrary;

public class TraderProvider extends RomProvider {
    private final ChipLibrary library;
    
    public TraderProvider(ChipLibrary library) {
        this.library = library;
    }
    
    @Override
    protected void randomizeData(Random rng, int position, DataEntry data) {
        byte[] bytes = data.getBytes();
        
        // Get some libraries.
        List<Integer> stdChips = this.library.query(
                BattleChip.Library.STANDARD);
        List<Integer> megaChips = this.library.query(BattleChip.Library.MEGA);
        List<Integer> gigaChips = this.library.query(BattleChip.Library.GIGA);
        // Merge the libraries.
        List<Integer> allChips = new ArrayList<>();
        allChips.addAll(stdChips);
        allChips.addAll(megaChips);
        allChips.addAll(gigaChips);
        
        // Add chips until the trader is full.
        int chipCount = bytes.length / 6;
        for (int i = 0; i < chipCount; i++) {
            int start = i * 6;
            
            // Pick a random chip to add.
            int r = rng.nextInt(allChips.size());
            int chipIndex = allChips.get(r);
            BattleChip chip = this.library.getElement(chipIndex);
            
            // Null all the codes.
            for (int j = 0; j < 4; j++) {
                bytes[start + 2 + j] = -1;
            }            
            
            // Insert the chip index.
            bytes[start] = (byte)(chipIndex & 0xFF);
            bytes[start + 1] = (byte)((chipIndex >> 8) & 0xFF);
            
            // Insert the chip codes.
            byte[] codes = chip.getCodes();
            System.arraycopy(codes, 0, bytes, start + 2, codes.length);
            
            // Remove the chip from the selectable chips.
            allChips.remove((Integer)chipIndex);
        }        
        
        data.setBytes(bytes);
    }

    @Override
    public void execute(Rom rom) {
        rom.advance(4);
        int traderPtr = rom.readPtr();
        rom.pushPosition();
        rom.setPosition(traderPtr);
        
        // See how many chips there are.
        int start = rom.getPosition();
        while (rom.hasNext()) {
            // Read the chip and its codes.
            int chipIndex = rom.readUInt16();
            byte[] codes = rom.readBytes(4);
            
            // Stop on chip 0.
            if (chipIndex == 0) {
                rom.advance(-6);
                break;
            }
        }
        
        // Register the whole trader block.
        int size = rom.getPosition() - start;
        rom.advance(-size);
        registerData(rom, size);
        
        rom.popPosition();
        rom.advance(4);
    }
}
