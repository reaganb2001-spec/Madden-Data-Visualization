
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Main
{
    public static void main(String[] args)
    {
        LinkedList<Team> teams = new LinkedList<Team>();
        LinkedList<Player> players = new LinkedList<Player>();

        LinkedList<Team> sortTeams = new LinkedList<Team>();
        LinkedList<Player> sortPlayers = new LinkedList<Player>();

        try
        {
            File maddenFile = new File("maddenChampions.txt");
            Scanner scanner = new Scanner(maddenFile);

            int champNum=1;
            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                String winnerName = line.substring(0, line.indexOf(" "));
                String loserName = line.substring(line.indexOf(" ")+6, line.indexOf("."));
                String mvpName = line.substring(line.indexOf(".")+2, line.lastIndexOf("."));
                String dpotgName = line.substring(line.lastIndexOf(".")+2, line.length());

                Championship thisChampionship = new Championship(winnerName, loserName, mvpName, dpotgName, champNum);

                if(checkTeams(teams, winnerName))
                {
                    getTeam(teams, winnerName).championshipsIn.add(thisChampionship);
                    getTeam(teams, winnerName).wins++;
                }
                else
                {
                    LinkedList<Player> newPlayers = new LinkedList<Player>();
                    LinkedList<Championship> newChamps = new LinkedList<Championship>();
                    Team winningTeam = new Team(winnerName, 1, 0, newPlayers, newChamps);
                    teams.add(winningTeam);
                    winningTeam.championshipsIn.add(thisChampionship);
                }

                if(checkTeams(teams, loserName))
                {
                    if(!checkChamps(getTeam(teams, loserName).championshipsIn, thisChampionship))
                    {
                        getTeam(teams, loserName).championshipsIn.add(thisChampionship);
                    }
                    getTeam(teams, loserName).losses++;
                }
                else
                {
                    LinkedList<Player> newPlayers = new LinkedList<Player>();
                    LinkedList<Championship> newChamps = new LinkedList<Championship>();
                    Team losingTeam = new Team(loserName, 0, 1, newPlayers, newChamps);
                    teams.add(losingTeam);
                    losingTeam.championshipsIn.add(thisChampionship);
                }

                if(checkPlayers(players, mvpName))
                {
                    getPlayer(players, mvpName).mvps++;
                }
                else
                {
                    Player mvpPlayer = new Player(mvpName, getTeam(teams, winnerName), 1, 0);
                    getTeam(teams, winnerName).players.add(mvpPlayer);
                    players.add(mvpPlayer);
                }

                if(checkPlayers(players, dpotgName))
                {
                    getPlayer(players, dpotgName).dpotgs++;
                }
                else
                {
                    Player dpPlayer = new Player(dpotgName, getTeam(teams, winnerName), 0, 1);
                    getTeam(teams, winnerName).players.add(dpPlayer);
                    players.add(dpPlayer);
                }
                champNum++;
            }
            scanner.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        }

        try
        {
            FileWriter output = new FileWriter("maddenData.txt");

            // Handling for Young and Jackson
            for(Team t : teams)
            {
                if(t.name.equals("Browns"))
                {
                    for(Player p : t.players)
                    {
                        if(p.name.equals("Young"))
                        {
                            p.dpotgs++;
                        }
                    }
                }
            }

            for(Team aTeam : teams)
            {


                System.out.println("Team Name: " + aTeam.name);
                output.write("Team Name: " + aTeam.name+"\n");
                aTeam.updateLegacy();
                System.out.println("Legacy: " + aTeam.legacy+ "\n");
                output.write("Legacy: " + aTeam.legacy+ "\n");
                System.out.println("Wins: " + aTeam.wins);
                output.write("Wins: " + aTeam.wins+"\n");

                for(Championship i : aTeam.championshipsIn)
                {
                    if(aTeam.name.equals(i.winner))
                    {
                        System.out.print(i.champNum+" ");
                        output.write(i.champNum+" ");
                    }
                }
                System.out.println("\nLoses: " + aTeam.losses);
                output.write("\nLoses: " + aTeam.losses+"\n");

                for(Championship j : aTeam.championshipsIn)
                {
                    if(aTeam.name.equals(j.loser))
                    {
                        System.out.print(j.champNum+" ");
                        output.write(j.champNum+" ");
                    }
                }
                System.out.println();
                System.out.println();
                output.write("\n\n");

                for(Player aPlayer : aTeam.players)
                {
                    if(!aPlayer.name.equals("NA"))
                    {
                        System.out.println("Player Name: " + aPlayer.name);
                        output.write("Player Name: " + aPlayer.name+"\n");
                        aPlayer.updateLegacy();
                        System.out.println("Legacy: " + aPlayer.legacy + "\n");
                        output.write("Legacy: " + aPlayer.legacy + "\n");
                        System.out.println("MVPs: " + aPlayer.mvps + "  DPOTGs: " + aPlayer.dpotgs);
                        output.write("MVPs: " + aPlayer.mvps + "  DPOTGs: " + aPlayer.dpotgs+"\n");
                        System.out.println();
                        output.write("\n");
                    }
                }
                System.out.println();
                System.out.println();
                output.write("\n");
                output.write("\n");
            }

            // Sort Teams by legacy  -  includes wins,losses, other stats. Best player too.
            double legacyTotal = 0;
            LinkedList<Team> legacySortTeams = new LinkedList<>();
            for(Team team : teams)
            {
                legacyTotal = legacyTotal + team.legacy;
                legacySortTeams.add(team);
            }

            int teamNum =1;
            while(legacySortTeams.size()>0)
            {
                double maxLeg = 0.0;
                Team[] legTeam = new Team[1];
                for (Team team : legacySortTeams) {
                    if (team.legacy > maxLeg) {
                        maxLeg = team.legacy;
                        legTeam[0] = team;
                    }
                }
                output.write(teamNum + " - Team Name: " + legTeam[0].name + "  Legacy: " + maxLeg + "\n");
                System.out.print(teamNum + " - Team Name: " + legTeam[0].name + "  Legacy: " + maxLeg + "\n");
                legacySortTeams.remove(legTeam[0]);
                teamNum++;
            }

            output.write("\n\n");
            System.out.print("\n\n");
            // now rank all the players
            double playerLegacyTotal = 0;
            int playerCount = 0;
            LinkedList<Player> legacySortPlayers = new LinkedList<>();
            for(Player player : players)
            {
                playerLegacyTotal = playerLegacyTotal + player.legacy;
                playerCount++;
                legacySortPlayers.add(player);
            }

            int playerNum =1;
            while(legacySortPlayers.size()>0)
            {
                double maxLeg = 0.0;
                Player[] legPlayer = new Player[1];
                for (Player player : legacySortPlayers) {
                    if (player.legacy > maxLeg) {
                        maxLeg = player.legacy;
                        legPlayer[0] = player;
                    }
                }
                output.write(playerNum + " - Player Name: " + legPlayer[0].name + "  Legacy: " + maxLeg + "\n");
                System.out.print(playerNum + " - Player Name: " + legPlayer[0].name + "  Legacy: " + maxLeg + "\n");
                legacySortPlayers.remove(legPlayer[0]);
                playerNum++;
            }


            double averageLegacy = legacyTotal/32;
            double averagePlayerLegacy = playerLegacyTotal/playerCount;
            output.write("\n");
            System.out.println();
            output.write("Average Team Legacy: " + averageLegacy+"\n");
            System.out.println("Average Team Legacy: " + averageLegacy);
            output.write("Average Player Legacy: " + averagePlayerLegacy+"\n");
            System.out.println("Average Player Legacy: " + averagePlayerLegacy);


            // List out top 10 MVP counts
//            output.write("\n\n");
//            System.out.print("\n\n");
//            // now rank all the players
//            LinkedList<Player> mvpSortPlayers = new LinkedList<>();
//            for(Player player : players)
//            {
//                mvpSortPlayers.add(player);
//            }
//
//            int pNum =1;
//            while(mvpSortPlayers.size()>0)
//            {
//                int maxMVPs = 0;
//                Player[] mvpPlayer = new Player[1];
//                for (Player player : mvpSortPlayers) {
//                    if (player.mvps > maxMVPs) {
//                        maxMVPs = player.mvps;
//                        mvpPlayer[0] = player;
//                    }
//                }
//                if(mvpPlayer[0].mvps>0)
//                {
//                    output.write(pNum + " - Player Name: " + mvpPlayer[0].name + "  MVPs: " + maxMVPs + "\n");
//                    System.out.print(pNum + " - Player Name: " + mvpPlayer[0].name + "  MVPs: " + maxMVPs + "\n");
//                    mvpSortPlayers.remove(mvpPlayer[0]);
//                    pNum++;
//                }
//            }

            // List out top 10 DPOG counts



            // List out dynasties
            output.write("\n");
            System.out.println();
            output.write("Listing out all dynasties:\n\n");
            System.out.println("Listing out all dynasties\n");
            for(Team dTeam : teams)
            {
                output.write(dTeam.name+"\n");
                System.out.println(dTeam.name);
                dTeam.getDynasties(dTeam.championshipsIn.getFirst().champNum, output);
                output.write("\n");
                System.out.println();
            }

            output.close();
        }
        catch (IOException e)
        {
            System.out.println("Trouble outputting data");
        }

    }

    // returns true if player is already initialized
    public static boolean checkPlayers(LinkedList<Player> players, String playerName)
    {
        for(Player player : players)
        {
            if(player.name.equals(playerName))
            {
                return true;
            }
        }
        return false;
    }

    // return true if team is already initialized
    public static boolean checkTeams(LinkedList<Team> teams, String teamName)
    {
        for(Team team : teams)
        {
            if(team.name.equals(teamName))
            {
                return true;
            }
        }
        return false;
    }

    // retrun true if Championship is already in the given championship list
    public static boolean checkChamps(LinkedList<Championship> champs, Championship aShip)
    {
        for(Championship game : champs)
        {
            if(aShip.champNum==game.champNum)
            {
                return true;
            }
        }
        return false;
    }

    public static Player getPlayer(LinkedList<Player> players, String playerName)
    {
        for(Player player : players)
        {
            if(player.name.equals(playerName))
            {
                return player;
            }
        }
        return null;
    }

    public static Team getTeam(LinkedList<Team> teams, String teamName)
    {
        for(Team team : teams)
        {
            if(team.name.equals(teamName))
            {
                return team;
            }
        }
        return null;
    }
}