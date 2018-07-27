package man10.red.man10quest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class quest_option {
    private Man10Quest pl;

    public quest_option(Man10Quest pl) {
        this.pl = pl;
    }
    ///////////////////////////////////////////////////////////
    //
    // 設定:カテゴリを選択
    //
    ///////////////////////////////////////////////////////////
    public void quest_option_category(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, pl.PluginName + "§7§lQuest設定:Category 選択(1)");//invの設定
        //////////////////////////////////
        //  アイテム 設置
        //////////////////////////////////
        //変数
        ItemStack[] Confirmation_item = new ItemStack[1];
        ItemMeta Confirmation_itemMeta;
        int i = 10;

        String sql = "SELECT * FROM category_data;";
        ResultSet category_data = pl.mysql.query(sql);
        try {
            while (category_data.next()) {
                ///////////////
                //アイテムの取得
                //////////////
                Confirmation_item = pl.others_method.itemStackArrayFromBase64(category_data.getString("item"));
                ///////////////
                //アイテムの設置
                //////////////
                inv.setItem(i, Confirmation_item[0]);
                i++;
                if (i % 8 == 1) {
                    i = i + 2;
                }
            }
        } catch (Exception e) {
        }

        p.openInventory(inv);
    }


    ///////////////////////////////////////////////////////////
    //
    // 設定:クエストを選択
    //
    ///////////////////////////////////////////////////////////
    public void open_quest_option(Player p, String category) {
        // インベントリ GUI セット
        Inventory inv = Bukkit.createInventory(null, 54, pl.PluginName + "§7§lQuest設定:Quest 選択(2)");//invの設定


        /************************************************
         *
         * クエスト アイテム化してinvに設置
         *
         *************************************************/
        //変数
        ItemStack[] item_getBase64;//アイテムの情報
        ItemStack item;//getBase64 + プレイヤーの情報
        ItemMeta itemMeta;//item用
        int quest_number = 0;
        int inv_number = 0;
        List<String> lore = new ArrayList<>();

        String sql = "SELECT * FROM quest_data WHERE category='" + category + "'";
        ResultSet quest_data = pl.mysql.query(sql);

        try {
            while (quest_data.next()) {
                //////////////////////
                // アイテムの情報取得
                //////////////////////
                quest_number = quest_data.getInt("number");
                item_getBase64 = pl.others_method.itemStackArrayFromBase64(quest_data.getString("item"));
                item = item_getBase64[0];
                itemMeta = item.getItemMeta();

                ///////////////////
                // lore
                ///////////////////
                lore = itemMeta.getLore();
                if(lore == null){
                    lore = new ArrayList<>();
                    lore.add("");
                    lore.add("§a§lQuest Name:" + quest_data.getString("name"));
                    lore.add("§a§lCategory:" + quest_data.getString("category"));
                }else{
                    lore = itemMeta.getLore();
                    lore.add("");
                    lore.add("§a§lQuest Name:" + quest_data.getString("name"));
                    lore.add("§a§lCategory:" + quest_data.getString("category"));
                }

                itemMeta.setLore(lore);

                /////////////////////
                // GUIに設置
                /////////////////////
                item.setItemMeta(itemMeta);
                inv.setItem(inv_number, item);
                inv_number++;
            }

            quest_data.close();
        } catch (Exception e) {
        }
        p.openInventory(inv);
    }

    ///////////////////////////////////////////////////////////
    //
    // 設定:クエストを操作
    //
    ///////////////////////////////////////////////////////////
    public void option_quest(Player p,String quest_name) {
        pl.questOption_selecting.put(p.getUniqueId(), quest_name);

        Inventory inv = Bukkit.createInventory(null, 27, pl.PluginName + "§7§lQuest設定:Quest 操作");//invの設定
        int quest_number = 0;
        ItemStack[] item_getBase64 = {};
        ItemStack item;
        ItemMeta itemMeta;
        List<String> lore = new ArrayList<>();

        String sql = "SELECT * FROM quest_data WHERE name='" + quest_name + "'";
        ResultSet quest_data = pl.mysql.query(sql);

        try {
            while (quest_data.next()) {
                /////////////////////////
                // クエストのアイテムの設置
                /////////////////////////
                quest_number = quest_data.getInt("number");
                item_getBase64 = pl.others_method.itemStackArrayFromBase64(quest_data.getString("item"));
                item = item_getBase64[0];
                itemMeta = item.getItemMeta();

                inv.setItem(13, item);

                /////////////////////////
                // "報酬の変更"の設置
                /////////////////////////
                item = new ItemStack(Material.CHEST);
                itemMeta = item.getItemMeta();

                itemMeta.setDisplayName("§a§l報酬の変更");
                lore.clear();
                lore.add("§3§l- クリアした時の報酬が変更できます");
                lore.add("§3§l- 報酬のアイテムの設定は9つまでです!");
                itemMeta.setLore(lore);

                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

                item.setItemMeta(itemMeta);
                inv.setItem(11, item);

                /////////////////////////
                // "消去"の設置
                /////////////////////////
                item = new ItemStack(Material.BARRIER);
                itemMeta = item.getItemMeta();

                itemMeta.setDisplayName("§c§l消去");

                lore.clear();
                lore.add("§4§l- クエストを消去します");
                lore.add("§4§l- 一度消去したら元には戻せません");
                itemMeta.setLore(lore);

                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

                item.setItemMeta(itemMeta);
                inv.setItem(15, item);
            }

            quest_data.close();
        } catch (Exception e) {
        }
        p.openInventory(inv);
    }

    ///////////////////////////////////////////////////////////
    //
    // 設定:アイテムを選択
    //
    ///////////////////////////////////////////////////////////
    public void select_item(Player p, String quest_name) {
        pl.questReward_selecting.put(p.getUniqueId(), quest_name);
        Inventory inv = Bukkit.createInventory(null, 18, pl.PluginName + "§a§lアイテムを入れてください");

        ItemStack[] in_item = {};
        String sql = "SELECT * FROM quest_data WHERE name='"+quest_name+"';";
        ResultSet quest_data = pl.mysql.query(sql);
        try {
            while (quest_data.next()){
                if(pl.others_method.SQLToArray(quest_data.getString("reward"))!=null) {
                    String[] item_base64 = pl.others_method.SQLToArray(quest_data.getString("reward"));
                    for (int i = 0; i < 9; i++) {
                        String[] item_array = {item_base64[i]};
                        if (!(item_array[0].equalsIgnoreCase("rO0ABXcEAAAAAXA="))) {
                            ItemStack[] item = pl.others_method.itemStackArrayFromBase64(item_array[0]);
                            inv.setItem(i, item[0]);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }


        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE);
        item.setDurability((short) 3);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("");
        item.setItemMeta(itemMeta);
        for (int i = 0; i < 9; i++) {
            inv.setItem(9 + i, item);
        }

        item = new ItemStack(Material.EMERALD);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§a§l決定");
        item.setItemMeta(itemMeta);
        inv.setItem(13, item);

        p.openInventory(inv);
    }
}
