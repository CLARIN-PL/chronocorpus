package pl.clarin.chronocorpus.document.entity;

public class Statistic {

    private int verbCount = 0;
    private int adjCount = 0;
    private int advCount = 0;
    private int nounCount = 0;

    public int getVerbCount() {
        return verbCount;
    }

    public int getValue(int pos) {
        if (pos == 1)
            return verbCount;
        if (pos == 2)
            return nounCount;
        if (pos == 3)
            return advCount;
        if (pos == 4)
            return adjCount;
        return 0;
    }

    public void addValue(byte pos, int val) {
        switch (pos) {
            case 0:
                break;
            case 1:
                this.addVerb(val);
                break;
            case 2:
                this.addNoun(val);
                break;
            case 3:
                this.addAdv(val);
                break;
            case 4:
                this.addAdj(val);
                break;
        }
    }

    private void addVerb(int v) {
        this.verbCount = verbCount + v;
    }

    private void addAdj(int v) {
        this.adjCount = adjCount + v;
    }

    private void addAdv(int v) {
        this.advCount = advCount + v;
    }

    private void addNoun(int v) {
        this.nounCount = nounCount + v;
    }

    public void setVerbCount(int verbCount) {
        this.verbCount = verbCount;
    }

    public int getAdjCount() {
        return adjCount;
    }

    public void setAdjCount(int adjCount) {
        this.adjCount = adjCount;
    }

    public int getAdvCount() {
        return advCount;
    }

    public void setAdvCount(int advCount) {
        this.advCount = advCount;
    }

    public int getNounCount() {
        return nounCount;
    }

    public void setNounCount(int nounCount) {
        this.nounCount = nounCount;
    }
}
