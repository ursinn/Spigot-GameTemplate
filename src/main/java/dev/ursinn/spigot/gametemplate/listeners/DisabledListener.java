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

import dev.ursinn.spigot.gametemplate.Main;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class DisabledListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!Main.getInstance().getOptions().isServerMode() && !Main.getInstance().players.contains(p.getUniqueId()))
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (!Main.getInstance().getOptions().isServerMode() && !Main.getInstance().players.contains(p.getUniqueId()))
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (!Main.getInstance().getOptions().isServerMode() && !Main.getInstance().players.contains(p.getUniqueId()))
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        if (!Main.getInstance().getOptions().isServerMode() && !Main.getInstance().players.contains(p.getUniqueId()))
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        HumanEntity entity = e.getEntity();
        if (!(entity instanceof Player))
            return;
        Player p = (Player) entity;
        if (!Main.getInstance().getOptions().isServerMode() && !Main.getInstance().players.contains(p.getUniqueId()))
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent e) {
        if (Main.getInstance().getOptions().isServerMode() || e.getWorld().getName().equals(Main.getInstance().getMap().getSpawn().getWorld().getName()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onLeave(LeavesDecayEvent e) {
        if (Main.getInstance().getOptions().isServerMode() || e.getBlock().getWorld().getName().equals(Main.getInstance().getMap().getSpawn().getWorld().getName()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onGrow(BlockGrowEvent e) {
        if (Main.getInstance().getOptions().isServerMode() || e.getBlock().getWorld().getName().equals(Main.getInstance().getMap().getSpawn().getWorld().getName()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onIgnite(BlockIgniteEvent e) {
        if (Main.getInstance().getOptions().isServerMode() || e.getBlock().getWorld().getName().equals(Main.getInstance().getMap().getSpawn().getWorld().getName()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onPhysics(BlockPhysicsEvent e) {
        if (Main.getInstance().getOptions().isServerMode() || e.getBlock().getWorld().getName().equals(Main.getInstance().getMap().getSpawn().getWorld().getName()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        HumanEntity entity = e.getWhoClicked();
        if (!(entity instanceof Player))
            return;
        Player p = (Player) entity;
        if (!Main.getInstance().getOptions().isServerMode() && !Main.getInstance().players.contains(p.getUniqueId()))
            return;
        e.setCancelled(true);
    }

}
