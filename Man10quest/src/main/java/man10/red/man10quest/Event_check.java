package man10.red.man10quest;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.Bukkit.dispatchCommand;

import java.sql.ResultSet;
public class Event_check extends BukkitRunnable {
    ////////////////////////////////
    //      コンストラクタ
    ////////////////////////////////
    private Man10Quest pl;
    int time=0;

    public Event_check(Man10Quest pl, int time) {
        this.pl = pl;
        this.time = time;
    }

    ////////////////////////////////
    //      Event 判定
    ////////////////////////////////

    @Override
    public void run() {
        boolean isEventComplete = true;
        time++;
        ////////////////////////////////////////////////
        //   条件 適合してるかチェック
        ////////////////////////////////////////////////
        for (Player p : Bukkit.getOnlinePlayers()) {
            /**
             *
             *  プレイヤー 場所を判定 - place event.
             *
             **/
            if(time % 1 == 0) {
                for (int i = 0; i < pl.event_place_x.size(); i++) {

                    isEventComplete = true;
                    if (!(pl.event_place_world.get(i).equalsIgnoreCase(p.getWorld().getName()))) {
                        isEventComplete = false;
                    }
                    if (!(pl.event_place_server.get(i).equalsIgnoreCase(p.getServer().getServerName()))) {
                        isEventComplete = false;
                    }
                    if (!(pl.event_place_x.get(i) - pl.event_place_radius.get(i) <= p.getLocation().getX() && pl.event_place_x.get(i) + pl.event_place_radius.get(i) >= p.getLocation().getX())) {
                        isEventComplete = false;
                    }
                    if (!(pl.event_place_z.get(i) - pl.event_place_radius.get(i) <= p.getLocation().getZ() && pl.event_place_z.get(i) + pl.event_place_radius.get(i) >= p.getLocation().getZ())) {
                        isEventComplete = false;
                    }
                    if (!(pl.event_place_y.get(i) - pl.event_place_height.get(i) <= (p.getLocation().getY()) && pl.event_place_y.get(i) + pl.event_place_height.get(i) >= p.getLocation().getY())) {
                        isEventComplete = false;
                    }
                    if (isEventComplete) {
                        String command = pl.event_place_command.get(i);
                        command = command.replace("{player}",p.getName());
                        command = command.replace("&","§");
                        try {
                            p.getServer().dispatchCommand(Bukkit.getConsoleSender(),command);
                        }catch (Exception e){
                            Bukkit.broadcastMessage(pl.PluginName+"§c§lERROR:コンソールから打てません!");
                        }
                    }
                }
            }
        }
    }
}
