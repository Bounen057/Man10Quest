package man10.red.man10quest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class quest_open {
    private Man10Quest pl;

    public quest_open(Man10Quest pl) {
        this.pl = pl;
    }


    /*************************************************************************
     *
     *  カテゴリ別にクエストを表示
     *
     *************************************************************************/

    public void open_quest(Player p,String category) {
        // インベントリ GUI セット
        Inventory inv = Bukkit.createInventory(null, 54, pl.PluginName);//invの設定

        /**************************************************************
         *
         * プレイヤーの情報を取り出す
         *
         **************************************************************/
        String sql = "SELECT * FROM player_data WHERE uuid='" + p.getUniqueId() + "';";
        ResultSet player_data = pl.mysql.query(sql);

        //変数
        String[] player_pt_sql={};
        String[] player_pt={""};
        int number = 0;
        try {
            while (player_data.next()) {
                number++;
                player_pt_sql = pl.others_method.SQLToArray(player_data.getString("quest_pt"));

                //代入
                System.arraycopy(player_pt_sql,0,player_pt,0,player_pt_sql.length);
            }
        } catch (Exception e) {
        }

        /////////////////////////////////////
        // プレイヤーのデータが存在しない時に作る
        /////////////////////////////////////
        if (number == 0) {
            sql = "SELECT * FROM player_data WHERE uuid='template';";
            player_data = pl.mysql.query(sql);
            try {
                while (player_data.next()) {
                    String[] player_pt_template = pl.others_method.SQLToArray("" + player_data.getString("quest_pt"));
                    //String[] player_reward_template = pl.others_method.SQLToArray("" + player_data.getString("quest_reward"));
                    //String[] player_finish_template = pl.others_method.SQLToArray("" + player_data.getString("finish_date"));
                                          
                    sql = "INSERT INTO player_data " +
                            "(uuid,mcid,quest_pt) " +
                            "VALUES(" +
                            "'"+p.getUniqueId()+"'," +
                            "'"+p.getName()+"'," +
                            "'"+pl.others_method.ArrayToSQL(player_pt_template)+"'" +
                            //"'"+pl.others_method.ArrayToSQL(player_reward_template)+"'," +
                            //"'"+pl.others_method.ArrayToSQL(player_finish_template)+"'" +
                            ");";
                    pl.mysql.execute(sql);

                    p.sendMessage(pl.PluginName+"§a§lデータが存在しなかったので作成しました");
                }
            } catch (Exception e) {
            }
            return;
        }



        /************************************************
         *
         * クエスト アイテム化してinvに設置
         *
         *************************************************/
        //変数
        ItemStack[] item_getBase64;//アイテムの情報
        ItemStack item;//getBase64 + プレイヤーの情報
        ItemMeta itemMeta;//item用
        int quest_number=0;
        int inv_number=0;
        List<String> lore = new ArrayList<>();

        sql = "SELECT * FROM quest_data WHERE category='"+category+"'";
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
                    lore.add("§e§lQuest point》" + player_pt_sql[quest_number] + "/" + quest_data.getInt("pt"));
                    lore.add("§a§lCategory》 " + quest_data.getString("category"));
                }else{
                    lore.add("");
                    lore.add("§e§lQuest point》" + player_pt_sql[quest_number] + "/" + quest_data.getInt("pt"));
                    lore.add("§a§lCategory》" + quest_data.getString("category"));
                }
                if(Integer.parseInt(player_pt_sql[quest_number]) == quest_data.getInt("pt")){
                    itemMeta.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    lore.add("§e§lクリア済");
                }
                itemMeta.setLore(lore);
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
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
        ///////////////////////////
        //  修飾アイテムの設置
        ///////////////////////////
        item = new ItemStack(Material.STAINED_GLASS_PANE);
        item.setDurability((short) 3);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(" ");
        item.setItemMeta(itemMeta);
        for (int i = 0; i < 9; i++) {
            inv.setItem(45 + i, item);
        }

        item = new ItemStack(Material.STAINED_GLASS_PANE);
        item.setDurability((short) 14);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§c§l戻る");
        item.setItemMeta(itemMeta);
        for (int i = 0; i < 3; i++) {
            inv.setItem(48 + i, item);
        }

        p.openInventory(inv);
    }
}
