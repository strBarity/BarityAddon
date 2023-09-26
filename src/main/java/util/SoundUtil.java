package main.java.util;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundUtil {
    private SoundUtil() {

    }
    public static void playChord(@NotNull Player p, Sound sound, float pitch) {
        p.playSound(p.getLocation(), sound, (float) 1 / 3, pitch);
        float k = (float) (12 * Math.log(pitch) / Math.log(2));
        p.playSound(p.getLocation(), sound, (float) 1/3, (float) Math.pow(2, k + 4));
        p.playSound(p.getLocation(), sound, (float) 1/3, (float) Math.pow(2, k + 3));

    }
}
