/*
 * MIT License
 *
 * Copyright (c) 2020 Ursin Filli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package dev.ursinn.spigot.gametemplate.listeners;

import dev.ursinn.spigot.gamelib.events.GamePlayerQuitEvent;
import dev.ursinn.spigot.gamelib.helpers.GamePlayerHelper;
import dev.ursinn.spigot.gametemplate.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameQuitListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (!Main.getInstance().getOptions().isServerMode() && !Main.getInstance().players.contains(p.getUniqueId()))
            return;
        if (Main.getInstance().getOptions().isServerMode())
            e.setQuitMessage("§c« " + p.getName() + " §7hat §7den §7Server §7verlassen.");
        Bukkit.getPluginManager().callEvent(new GamePlayerQuitEvent(p));
    }

    @EventHandler
    public void onGameLeave(GamePlayerQuitEvent e) {
        Player p = e.getPlayer();
        Main.getInstance().players.remove(p.getUniqueId());
        Main.getInstance().getServerAPI().updateOnline(Main.getInstance().players.size());
        Main.getInstance().kills.remove(p.getUniqueId());
        Main.getInstance().deaths.remove(p.getUniqueId());

        if (Main.getInstance().getOptions().isServerMode())
            return;
        GamePlayerHelper playerHelper = Main.getInstance().playerHelper.get(p.getUniqueId());
        p.setGameMode(playerHelper.getGameMode());
        p.getInventory().clear();
        p.getInventory().setContents(playerHelper.getInventory().getContents());
        p.getInventory().setArmorContents(playerHelper.getInventory().getArmorContents());
        p.getActivePotionEffects().clear();
    }

}
