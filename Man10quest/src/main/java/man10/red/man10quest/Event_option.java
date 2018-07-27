package man10.red.man10quest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Event_option {
    private Man10Quest pl;

    public Event_option(Man10Quest pl) {
        this.pl = pl;
    }

    //////////////////////////////////////////////////////
    //  place events setting / 場所のイベントの設定画面(GUI)
    //////////////////////////////////////////////////////
    public void open_optionEvent(Player p,int page) {
        Inventory inv = Bukkit.createInventory(null, 54, pl.PluginName + "§7§lEvent設定:Event 選択");//invの設定

        ItemStack item=null;
        ItemMeta itemMeta=null;
        for (int i = 0; i < pl.event_place_x.size(); i++) {
            ///////////////////////////////////////////////
            //  場所イベント
            ///////////////////////////////////////////////
            if(pl.event_type.get(i).equalsIgnoreCase("place")) {
                item = new ItemStack(Material.GRASS);
                itemMeta = item.getItemMeta();

                itemMeta.setDisplayName("§a§l" + pl.event_place_name.get(i));
                List<String> lore = new ArrayList<String>();//アイテム Lore
                lore.add("§e§lx:§f§l" + pl.event_place_x.get(i) + "  §e§levent_place_y:§f§l" + pl.event_place_y.get(i) + " §e§levent_place_z:§f§l" + pl.event_place_z.get(i));
                lore.add("§e§lradius:§f§l" + pl.event_place_radius.get(i) + " §e§levent_place_height:§f§l" + pl.event_place_height.get(i));
                lore.add("§e§lserver:§f§l" + pl.event_place_server.get(i) + " §e§levent_place_world:§f§l" + pl.event_place_world.get(i));
                lore.add("§e§lcommand:§f§l'" + pl.event_place_command.get(i) +"'");
                itemMeta.setLore(lore);
            }

            item.setItemMeta(itemMeta);
            inv.setItem(i, item);
        }
        p.openInventory(inv);
    }


}
