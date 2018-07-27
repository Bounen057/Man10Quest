package man10.red.man10quest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;

public class quest_category {
    private Man10Quest pl;

    public quest_category(Man10Quest pl) {
        this.pl = pl;
    }
    ///////////////////////////////////////////////////////////
    // * カテゴリを選択 *
    // アイテムをクリックでカテゴリ別のクエストを表示
    // /mq
    ///////////////////////////////////////////////////////////
    public void select_category(Player p){
        Inventory inv = Bukkit.createInventory(null, 45, pl.PluginName + "§7§lCategory 選択");//invの設定
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
                if(i % 8 == 1){
                    i=i+2;
                }
            }
        } catch (Exception e) {
        }
        p.openInventory(inv);
    }



    ///////////////////////////////////////////////////////////
    // * クエストを追加 *
    // アイテムを右手に持つ
    // /mq category set <名前>
    ///////////////////////////////////////////////////////////
    public void category_set(String name, ItemStack item, Player p) {
        ItemStack[] category_item = {item};
        boolean isDO = true;

        String sql = "SELECT * FROM category_data;";
        ResultSet category_data = pl.mysql.query(sql);
        try {
            while (category_data.next()) {
                if (pl.others_method.itemStackArrayToBase64(category_item).equalsIgnoreCase(category_data.getString("item"))) {
                    isDO = false;
                }
            }
        } catch (Exception e) {
        }
        if (isDO) {
            sql = "INSERT INTO category_data " +
                    "(name,item) " +
                    "VALUES(" +
                    "'" + name + "'" +
                    ",'" + pl.others_method.itemStackArrayToBase64(category_item) + "');";

            pl.mysql.execute(sql);
            p.sendMessage(pl.PluginName + "§a§l登録しました!");
        } else {
            p.sendMessage(pl.PluginName + "§c§lすでに手持ちのアイテムは存在しています!");
        }
    }


    ///////////////////////////////////////////////////////////
    // * クエスト一覧を表示 *
    // /mq category list
    ///////////////////////////////////////////////////////////
    public void category_list(Player p) {
        String sql = "SELECT * FROM category_data;";
        ResultSet category_data = pl.mysql.query(sql);
        p.sendMessage("§e§l-" + pl.PluginName + "§6§l カテゴリー一覧§e§l-------------------------");
        try {
            while (category_data.next()) {
                p.sendMessage("§a§- §7§l" + category_data.getString("name"));
            }
        } catch (Exception e) {
        }
    }


    ///////////////////////////////////////////////////////////
    // * クエスト 設定 *
    // /mq category option
    ///////////////////////////////////////////////////////////
    public void category_option(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, pl.PluginName + "§7§lCategory 設定");//invの設定
        //////////////////////////////////
        //  アイテム 設置
        //////////////////////////////////
        //変数
        ItemStack[] Confirmation_item = new ItemStack[1];
        ItemMeta Confirmation_itemMeta;
        int i = 0;

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
            }
        } catch (Exception e) {
        }
        p.openInventory(inv);
    }
}
