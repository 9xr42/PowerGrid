public class PowerPlant implements Comparable{
    private int identity;
    private String resourceType;
    private int amount;
    private int citiesPowered;
    private boolean roundThree;

    public PowerPlant(int iden, String rType, int amo, int cPowered, boolean rTres)
    {
        setIdentity(iden);
        setResource(rType);
        setAmount(amo);
        setCitiesPowered(cPowered);
        setRoundThree(rTres);
    }

    public int compareTo(Object obj)
    {
        PowerPlant temp = (PowerPlant) obj;
        return ((Integer)getIdentity()).compareTo((Integer)temp.getIdentity());
    }

    public int getIdentity()
    {
        return identity;
    }

    public void setIdentity(int iden)
    {
        identity = iden;
    }

    public String getResource()
    {
        return resourceType;
    }

    public void setResource(String rType)
    {
        resourceType = rType;
    }

    public int getAmount()
    {
        return amount;
    }

    public void setAmount(int amo)
    {
        amount = amo;
    }

    public int getCitiesPowered()
    {
        return citiesPowered;
    }

    public void setCitiesPowered(int cPowered)
    {
        citiesPowered = cPowered;
    }

    public boolean isRoundThree()
    {
        return roundThree;
    }

    public void setRoundThree(boolean rTres)
    {
        roundThree = rTres;
    }
}