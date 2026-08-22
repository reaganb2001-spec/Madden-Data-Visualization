import java.io.FileWriter;
import java.util.LinkedList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;



public class Team
{
    String name;
    int wins;
    int losses;
    LinkedList<Player> players;
    LinkedList<Championship> championshipsIn;
    double legacy;

    public Team(String name, int wins, int losses, LinkedList<Player> players, LinkedList<Championship> championshipsIn)
    {
        this.name=name;
        this.wins=wins;
        this.losses=losses;
        this.players=players;
        this.championshipsIn=championshipsIn;
        updateLegacy();
    }

    public void updateLegacy()
    {
        this.legacy= (((double) this.wins)/(((double) this.losses)+ (double)this.wins))*(3* ((double) this.wins) + .5*((double) this.losses));
    }

    public Championship getChampByNum(int num)
    {
        for(Championship champ : this.championshipsIn)
        {
            if(num==champ.champNum)
            {
                return champ;
            }
        }
        Championship fakeChamp = new Championship("0", "0", "0", "0", 0);
        return fakeChamp;
    }


    public void getDynasties(int initialChamp, FileWriter output)
    {
        /*
        Todo
        output dynasty stuff to file
        Account for a team beating themselves

        misc: mvp and dpotg counts
         */

        int initialAppearance = initialChamp; //this.championshipsIn.getFirst().champNum;
        LinkedList<Integer> dynasty = new LinkedList<Integer>();

        try {
            for (Championship c : this.championshipsIn) {
                if (c.champNum > initialAppearance) {
                    if (c.champNum - initialAppearance <= 7) {
                        if (!dynasty.contains(initialAppearance)) {
                            dynasty.add(initialAppearance);
                        }
                        dynasty.add(c.champNum);
                        initialAppearance = c.champNum;
                        if(this.championshipsIn.indexOf(c)==this.championshipsIn.size()-1)
                        {
                            if (dynasty.size() > 2) {
                                output.write("Dynasty: \n");
                                System.out.println("Dynasty: ");
                                for (int dC : dynasty) {
                                    if(getChampByNum(dC).winner.equals(this.name))
                                    {
                                        output.write("W"+dC + " ");
                                        System.out.print("W"+dC + " ");
                                    }
                                    else
                                    {
                                        output.write("L"+dC + " ");
                                        System.out.print("L"+dC + " ");
                                    }
                                }
                                output.write("\n");
                                System.out.println();
                            }
                        }
                    } else {
                        //print out dynasty that was created
                        initialAppearance = c.champNum;
                        if (dynasty.size() > 2) {
                            output.write("Dynasty: \n");
                            System.out.println("Dynasty: ");
                            for (int dC : dynasty) {
                                if(getChampByNum(dC).winner.equals(this.name))
                                {
                                    output.write("W"+dC + " ");
                                    System.out.print("W"+dC + " ");
                                }
                                else
                                {
                                    output.write("L"+dC + " ");
                                    System.out.print("L"+dC + " ");
                                }
                            }
                            output.write("\n");
                            System.out.println();
                        }
                        getDynasties(initialAppearance, output);
                        break;
                    }
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Trouble outputting data");
        }
    }
}