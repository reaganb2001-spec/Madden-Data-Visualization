
public class Player
{
    String name;
    Team team;
    int mvps;
    int dpotgs;
    double legacy;

    public Player(String name, Team team, int mvps, int dpotgs)
    {
        this.name=name;
        this.team=team;
        this.mvps=mvps;
        this.dpotgs=dpotgs;
        updateLegacy();
    }

    public void updateLegacy()
    {
        this.legacy=3*this.mvps + 2*this.dpotgs + 2*this.team.wins + .5*this.team.losses;
    }
}