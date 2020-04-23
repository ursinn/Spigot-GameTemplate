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

import dev.ursinn.spigot.gamelib.enums.GameStatsEnum;
import dev.ursinn.spigot.gamelib.helpers.GamePlayerHelper;
import dev.ursinn.spigot.gametemplate.Main;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!Main.getInstance().devBuild) {
            if (e.getPlayer().hasPermission("template.admin")) {
                if (Main.getInstance().updateChecker.isUpdate())
                    p.sendMessage("An update for " + Main.getInstance().getDescription().getName() + " is available");
            }
        }
        if (!Main.getInstance().getOptions().isServerMode())
            return;
        e.setJoinMessage("§a» " + p.getName() + " §7hat §7den §7Server §7betreten.");
        Main.getInstance().players.add(p.getUniqueId());
        Main.getInstance().getServerAPI().updateOnline(Main.getInstance().players.size());
        Main.getInstance().kills.put(p.getUniqueId(), Main.getInstance().getStatsAPI().getStats(p.getUniqueId().toString(), GameStatsEnum.KILLS));
        Main.getInstance().deaths.put(p.getUniqueId(), Main.getInstance().getStatsAPI().getStats(p.getUniqueId().toString(), GameStatsEnum.DEATHS));
        p.teleport(Main.getInstance().getMap().getSpawn());
        p.setGameMode(GameMode.ADVENTURE);
        p.setFoodLevel(20);
        p.setHealth(20.0D);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (!Main.getInstance().getOptions().isServerMode() && !Main.getInstance().players.contains(p.getUniqueId()))
            return;
        Main.getInstance().players.remove(p.getUniqueId());
        Main.getInstance().getServerAPI().updateOnline(Main.getInstance().players.size());
        Main.getInstance().kills.remove(p.getUniqueId());
        Main.getInstance().deaths.remove(p.getUniqueId());

        if (Main.getInstance().getOptions().isServerMode())
            e.setQuitMessage("§c« " + p.getName() + " §7hat §7den §7Server §7verlassen.");
        else {
            GamePlayerHelper playerHelper = Main.getInstance().playerHelper.get(p.getUniqueId());
            p.setGameMode(playerHelper.getGameMode());
            p.getInventory().clear();
            p.getInventory().setContents(playerHelper.getInventory().getContents());
            p.getInventory().setArmorContents(playerHelper.getInventory().getArmorContents());
        }
    }

}
