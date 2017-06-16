package main.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import main.java.Utils.FullStat;
import main.java.Utils.PEQuery;

class Main{

	private static String name = "DragonBox-Network";
	private static double version = 1.0;

	private static PEQuery cs_lobby = new PEQuery("dragonboxpe.net",19132);
	private static PEQuery cs_anni = new PEQuery("dragonboxpe.net",19133);
	private static PEQuery cs_battle = new PEQuery("dragonboxpe.net",19134);

	private static FullStat fs_lobby;
	private static FullStat fs_anni;
	private static FullStat fs_battle;

	private static boolean lobby_status;
	private static boolean anni_status;
	private static boolean battle_status;


	public static void main(String[] args){
		init();
	}

	public static void init(){
		JFrame frame = new JFrame(name);
		frame.setSize(250,360);
		frame.setBounds(450, 200, 500, 400);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(name + " ver" + version);
		frame.setResizable(false);
		frame.setVisible(true);

		JPanel panel = new JPanel();

		JLabel label = new JLabel(name);
		label.setPreferredSize(new Dimension(240,30));
		label.setFont(new Font("Century", Font.ITALIC, 24));

		/*Web API(DEPRECATED)*/
		/*String result = getResult("http://dragonboxpe.net/api/server.php?port=19132");

		User user = JSON.decode(result, User.class);
		String lobby_status = user.getStatus();
		String lobby_online = user.getOnline();
		String lobby_max = user.getMax();

		String result1 = getResult("http://dragonboxpe.net/api/server.php?port=19133");

		User user1 = JSON.decode(result1, User.class);
		String anni_status = user1.getStatus();
		String anni_online = user1.getOnline();
		String anni_max = user1.getMax();*/

		/* MCPE Query */
		ping(Port.LOBBY);
		ping(Port.ANNI);
		ping(Port.BATTLE);

		JLabel label2 = new JLabel("Loading...");
		label2.setPreferredSize(new Dimension(450,30));
		JLabel label3 = new JLabel("Loading...");
		label3.setPreferredSize(new Dimension(450,30));
		JLabel label4 = new JLabel("Loading...");
		label4.setPreferredSize(new Dimension(450,30));

		panel.add(label);
		panel.add(label2);
		panel.add(label3);
		panel.add(label4);
	    Container contentPane = frame.getContentPane();
	    contentPane.add(panel, BorderLayout.CENTER);
	    frame.validate();

	    /* Update */
	    while(true){
	    	/*
	    	result = getResult("http://dragonboxpe.net/api/server.php?port=19132");
	    	result1 = getResult("http://dragonboxpe.net/api/server.php?port=19133");
			user = JSON.decode(result, User.class);
			lobby_status = user.getStatus();
			lobby_online = user.getOnline();
			lobby_max = user.getMax();

			user1 = JSON.decode(result1, User.class);
			anni_status = user1.getStatus();
			anni_online = user1.getOnline();
			anni_max = user1.getMax();
			*/

	    	cs_lobby = new PEQuery("dragonboxpe.net",19132);
	    	cs_anni = new PEQuery("dragonboxpe.net",19133);
	    	cs_battle = new PEQuery("dragonboxpe.net",19134);

			ping(Port.LOBBY);
			ping(Port.ANNI);
			ping(Port.BATTLE);

			if(lobby_status){
				label2.setText("  LobbyServer     ServerStatus:Online     Online:"+fs_lobby.getData().get("numplayers")+"/"+fs_lobby.getData().get("maxplayers"));
				label2.setPreferredSize(new Dimension(450,30));
				label2.setBorder(new LineBorder(Color.GRAY, 1, true));
			}else{
				label2.setText("  LobbyServer     ServerStatus:Offline");
				label2.setPreferredSize(new Dimension(450,30));
				label2.setBorder(new LineBorder(Color.GRAY, 1, true));
			}

			if(anni_status){
				label3.setText("  AnniServer     ServerStatus:Online     Online:"+fs_anni.getData().get("numplayers")+"/"+fs_anni.getData().get("maxplayers"));
				label3.setPreferredSize(new Dimension(450,30));
				label3.setBorder(new LineBorder(Color.GRAY, 1, true));
			}else{
				label3.setText("  AnniServer     ServerStatus:Offline");
				label3.setPreferredSize(new Dimension(450,30));
				label3.setBorder(new LineBorder(Color.GRAY, 1, true));
			}

			if(battle_status){
				label4.setText("  BattleRoyalServer     ServerStatus:Online     Online:"+fs_battle.getData().get("numplayers")+"/"+fs_battle.getData().get("maxplayers"));
				label4.setPreferredSize(new Dimension(450,30));
				label4.setBorder(new LineBorder(Color.GRAY, 1, true));
			}else{
				label4.setText("  BattleRoyalServer     ServerStatus:Offline");
				label4.setPreferredSize(new Dimension(450,30));
				label4.setBorder(new LineBorder(Color.GRAY, 1, true));
			}

			panel.add(label2);
			panel.add(label3);
			panel.add(label4);
		    contentPane.add(panel, BorderLayout.CENTER);

	    	try {
				Thread.sleep(1800);
			} catch (InterruptedException e) {}

	    }
	}

	/* Check Online */
    public static void ping(Port option) {
    	if(option == Port.LOBBY){
            if (cs_lobby.hand("dragonboxpe.net")) {
            	lobby_status = true;
                fs_lobby = cs_lobby.fullStat();
            } else {
            	lobby_status = false;
            }
    	}else if(option == Port.ANNI){
            if (cs_anni.hand("dragonboxpe.net")) {
            	anni_status = true;
                fs_anni = cs_anni.fullStat();
            } else {
            	anni_status = false;
            }
    	}else if(option == Port.BATTLE){
            if (cs_battle.hand("dragonboxpe.net")) {
            	battle_status = true;
                fs_battle = cs_battle.fullStat();
            } else {
            	battle_status = false;
            }
    	}
    }

	/* Get Web API */
	public static String getResult(String urlString){
		String result = "";
		try{
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.connect();
		BufferedReader in = new BufferedReader(new InputStreamReader(
		con.getInputStream()));
		String tmp = "";
		while ((tmp = in.readLine()) != null) {
		result += tmp;
		}
		in.close();
		con.disconnect();
		}catch(Exception e){
		e.printStackTrace();
		}
		return result;
		}
}
