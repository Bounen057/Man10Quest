package man10.red.man10quest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.util.List;

public class GUIClickEvents {
    private Man10Quest pl;
    public GUIClickEvents(Man10Quest pl) {
        this.pl = pl;
    }
    public void click_event(InventoryClickEvent e) {
        if(e.getWhoClicked().getInventory() == e.getClickedInventory()){
            return;
        }
        if(e.getCurrentItem() == null){
            return;
        }

        /******************************************************************
         *
         * Event - 行動 関係
         *
        ********************************************************************/
        if (e.getInventory().getName().equalsIgnoreCase(pl.PluginName + "§7§lEvent設定:Event 選択")) {
            e.setCancelled(true);
            String delete_sql_where = e.getCurrentItem().getItemMeta().getDisplayName().substring(4);

            pl.delete_sql = "DELETE FROM events WHERE name='" + delete_sql_where + "'";
            pl.others_method.option_delete(e.getCurrentItem(), (Player) e.getWhoClicked());
        }


        ////////////////////////////////
        // 設定 - option
        ////////////////////////////////
        if (e.getInventory().getName().equalsIgnoreCase(pl.PluginName + "§7§lCategory 設定")) {
            e.setCancelled(true);
            ItemStack[] item = {e.getCurrentItem()};
            String delete_sql_where = pl.others_method.itemStackArrayToBase64(item);

            pl.delete_sql = "DELETE FROM category_data WHERE item='" + delete_sql_where + "';";
            pl.others_method.option_delete(e.getCurrentItem(), (Player) e.getWhoClicked());
        }
        /******************************************************************
         *
         * Quests - クエスト 関係
         *
         ********************************************************************/
        //////////////////////////////
        // Quest - カテゴリ選択
        //////////////////////////////
        if (e.getInventory().getName().equalsIgnoreCase(pl.PluginName + "§7§lCategory 選択")) {
            e.setCancelled(true);
            ItemStack[] item = {e.getCurrentItem()};
            String where_quest = pl.others_method.itemStackArrayToBase64(item);

            String sql = "SELECT * FROM category_data;";
            ResultSet category_data = pl.mysql.query(sql);
            try {
                while (category_data.next()) {
                    if (category_data.getString("item").equalsIgnoreCase(where_quest)) {
                        pl.quest.open_quest((Player) e.getWhoClicked(), category_data.getString("name"));
                        return;
                    }
                }
            } catch (Exception e_null) {
            }
        }
        //////////////////////////////
        // 設定(1) - Category 選択
        //////////////////////////////
        if (e.getInventory().getName().equalsIgnoreCase(pl.PluginName + "§7§lQuest設定:Category 選択(1)")) {
            e.setCancelled(true);
            ItemStack[] item = {e.getCurrentItem()};
            String where_quest = pl.others_method.itemStackArrayToBase64(item);

            String sql = "SELECT * FROM category_data;";
            ResultSet category_data = pl.mysql.query(sql);
            try {
                while (category_data.next()) {
                    if (category_data.getString("item").equalsIgnoreCase(where_quest)) {
                        pl.quest_option.open_quest_option((Player) e.getWhoClicked(), category_data.getString("name"));
                        return;
                    }
                }
            } catch (Exception e_null) {
            }
        }
        //////////////////////////////
        // 設定(2) - Quest 選択
        //////////////////////////////
        if (e.getInventory().getName().equalsIgnoreCase(pl.PluginName + "§7§lQuest設定:Quest 選択(2)")) {
            e.setCancelled(true);
            ItemStack[] item = {e.getCurrentItem()};
            ItemMeta itemMeta = item[0].getItemMeta();

            if(itemMeta == null){
                return;
            }
            List<String> item_lore = itemMeta.getLore();
            int size = item_lore.size();
            item_lore.remove(size - 1);
            item_lore.remove(size - 2);
            item_lore.remove(size - 3);
            itemMeta.setLore(item_lore);
            item[0].setItemMeta(itemMeta);

            Bukkit.broadcastMessage("" + item_lore.size());
            e.getWhoClicked().getInventory().addItem(item[0]);
            String where_quest = pl.others_method.itemStackArrayToBase64(item);

            String sql = "SELECT * FROM quest_data WHERE item='" + where_quest + "';";
            ResultSet quest_data = pl.mysql.query(sql);
            try {
                while (quest_data.next()) {
                    pl.quest_option.option_quest((Player)e.getWhoClicked(),quest_data.getString("name"));
                }
            } catch (Exception ev) {
            }
        }
        ////////////////////////////
        // 設定(3) - Quest 設定
        ////////////////////////////
        if(e.getInventory().getName().equalsIgnoreCase(pl.PluginName + "§7§lQuest設定:Quest 操作")){
            e.setCancelled(true);
            /** 消去 **/
            if(e.getCurrentItem().getType()== Material.BARRIER){
                pl.quest_player.pt_delete((Player)e.getWhoClicked(),pl.questOption_selecting.get(e.getWhoClicked().getUniqueId()));
                e.getWhoClicked().closeInventory();
            }

            /** 報酬 **/
            if(e.getCurrentItem().getType()== Material.CHEST){
                String quest_name = pl.questOption_selecting.get(e.getWhoClicked().getUniqueId());
                String sql = "SELECT * FROM quest_data WHERE name='" + quest_name + "';";
                ResultSet quest_data = pl.mysql.query(sql);
                try {
                    while (quest_data.next()) {
                        pl.quest_option.select_item((Player)e.getWhoClicked(),quest_name);
                    }
                } catch (Exception ev) {
                }
            }
        }

        if(e.getInventory().getName().equalsIgnoreCase(pl.PluginName + "§a§lアイテムを入れてください")){
            if(e.getSlot() >=9) {
                e.setCancelled(true);
                if (e.getSlot() == 13) {
                    String[] item_str = new String[9];
                    ItemStack[] item_array = new ItemStack[1];
                    for(int i=0;i < 9;i++){
                        item_array[0] = e.getInventory().getItem(i);
                        item_str[i] = pl.others_method.itemStackArrayToBase64(item_array);
                    }
                    String sql = "UPDATE quest_data SET reward='"+pl.others_method.ArrayToSQL(item_str)+"' WHERE name='"+pl.questReward_selecting.get(e.getWhoClicked().getUniqueId())+"';";
                    pl.mysql.execute(sql);
                    e.getWhoClicked().sendMessage(pl.PluginName+"§a§l報酬の設定が完了しました!");
                    ((Player)e.getWhoClicked()).closeInventory();
                }
            }
        }

        if (e.getInventory().getName().equalsIgnoreCase(pl.PluginName + "§c§l設定を消去してもよろしいですか?")) {
            e.setCancelled(true);
            pl.others_method.mySQL_delete((Player) e.getWhoClicked(), e.getRawSlot(), pl.delete_sql);

            pl.Man10Quest_commands.reload();
            e.getWhoClicked().closeInventory();
        }

        if(e.getInventory().getName().equalsIgnoreCase(pl.PluginName)){
            e.setCancelled(true);
            if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§c§l戻る")){
                pl.quest_category.select_category((Player)e.getWhoClicked());
            }
        }
    }
}
