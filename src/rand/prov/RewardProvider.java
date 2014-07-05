package rand.prov;

import java.util.List;
import java.util.Random;
import rand.Rom;
import rand.lib.BattleChip;
import rand.lib.ChipLibrary;

public class RewardProvider extends RomProvider {
    private enum RewardType {
        CHIP,
        ZENNY,
        BUGFRAG,
        HP
    }
    
    private final ChipLibrary library;
    
    public RewardProvider(final ChipLibrary library) {
        this.library = library;
    }
    
    @Override
    protected void randomizeData(Random rng, int position, DataEntry data) {        
        byte[] bytes = data.getBytes();
        int reward = (bytes[0] & 0xFF) + ((bytes[1] & 0xFF) << 8);
        reward &= 0x3FFF;
        
        switch ((RewardType)data.type()) {
            case CHIP:
                // Get the old battle chip.
                int oldReward = reward & 0x1FF;
                BattleChip oldChip = this.library.getElement(oldReward);
                
                // Randomize Standard chips only.
                if (oldChip.library() != BattleChip.Library.STANDARD) {
                    break;
                }
                
                // Choose a random new battle chip with the same library and
                // rarity.
                List<Integer> possibleChips
                        = this.library.query((byte)-1, oldChip.rarity(),
                                oldChip.rarity(), null, oldChip.library(), 1,
                                99);
                
                reward = possibleChips.get(rng.nextInt(possibleChips.size()));
                BattleChip chip = library.getElement(reward);
                
                // Choose a random code.
                byte[] codes = chip.getCodes();
                int code = codes[rng.nextInt(codes.length)];
                
                reward |= code << 9;
                break;
        }
        
        bytes[0] = (byte)(reward & 0xFF);
        bytes[1] = (byte)(reward >> 8);
        data.setBytes(bytes);
    }

    @Override
    public void execute(Rom rom) {
        System.out.println("Collected reward data at 0x"
                + String.format("%06X", rom.getPosition()));
        
        int reward = rom.readUInt16();
        
        RewardType type;
        if ((reward & 0xC000) == 0xC000) {
            type = RewardType.BUGFRAG;
        } else if ((reward & 0x8000) != 0) {
            type = RewardType.HP;
        } else if ((reward & 0x4000) != 0) {
            type = RewardType.ZENNY;
        } else {
            type = RewardType.CHIP;
        }
        
        rom.advance(-2);
        registerData(rom, type, 2);
    }
}
