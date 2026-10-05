package automation;

public enum BrowserType
{
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge"),
    SAFARI("safari");

    private final String descriptor;

    BrowserType(String str)
    {
        this.descriptor = str;
    }

    public String toString()
    {
        return this.descriptor;
    }

}
