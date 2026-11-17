package MAExtendedRigs;

/**
 * Immutable rest-layout dimensions and topology. Lengths are in model units,
 * multiplied by scale. Dimensions position connectors; they do not stretch
 * imported geometry. Zero optional segment/pair counts omit those joints.
 */
public final class MARigConfig {
    public final float scale, bodyLength, bodyWidth, bodyHeight, neckLength, headLength;
    public final float upperLegLength, lowerLegLength, footLength, armLength;
    public final float wingLength, tailLength, appendageLength, trunkLength, legSplay;
    public final int tailSegments, neckSegments, wingSegments, wingPairs, wingDigits, legPairs;
    public final int bodySegments, appendageCount, appendageSegments, antennaSegments, trunkSegments;
    public final boolean verticalBody;

    private MARigConfig(Builder b) {
        scale=positive("scale",b.scale); bodyLength=positive("bodyLength",b.bodyLength);
        bodyWidth=positive("bodyWidth",b.bodyWidth); bodyHeight=positive("bodyHeight",b.bodyHeight);
        neckLength=positive("neckLength",b.neckLength); headLength=positive("headLength",b.headLength);
        upperLegLength=positive("upperLegLength",b.upperLegLength);
        lowerLegLength=positive("lowerLegLength",b.lowerLegLength);
        footLength=positive("footLength",b.footLength); armLength=positive("armLength",b.armLength);
        wingLength=positive("wingLength",b.wingLength); tailLength=positive("tailLength",b.tailLength);
        appendageLength=positive("appendageLength",b.appendageLength);
        trunkLength=positive("trunkLength",b.trunkLength);
        if(!Float.isFinite(b.legSplay)||b.legSplay<0)throw new IllegalArgumentException("legSplay must be finite and nonnegative");
        legSplay=b.legSplay;
        tailSegments=count("tailSegments",b.tailSegments,0,128);
        neckSegments=count("neckSegments",b.neckSegments,0,32);
        wingSegments=count("wingSegments",b.wingSegments,1,16);
        wingPairs=count("wingPairs",b.wingPairs,0,8);
        wingDigits=count("wingDigits",b.wingDigits,0,5);
        legPairs=count("legPairs",b.legPairs,0,32);
        bodySegments=count("bodySegments",b.bodySegments,1,128);
        appendageCount=count("appendageCount",b.appendageCount,0,32);
        appendageSegments=count("appendageSegments",b.appendageSegments,1,64);
        antennaSegments=count("antennaSegments",b.antennaSegments,0,16);
        trunkSegments=count("trunkSegments",b.trunkSegments,0,64);
        verticalBody=b.verticalBody;
    }
    private static float positive(String n,float v) {
        if(!Float.isFinite(v)||v<=0)throw new IllegalArgumentException(n+" must be finite and positive");
        return v;
    }
    private static int count(String n,int v,int min,int max) {
        if(v<min||v>max)throw new IllegalArgumentException(n+" must be "+min+".."+max);
        return v;
    }
    public static Builder builder(){return new Builder();}
    public Builder toBuilder(){return new Builder(this);}
    public static final class Builder {
        private float scale=1,bodyLength=1.4f,bodyWidth=.7f,bodyHeight=.6f,neckLength=.45f,headLength=.5f;
        private float upperLegLength=.6f,lowerLegLength=.55f,footLength=.35f,armLength=1;
        private float wingLength=1.6f,tailLength=1.2f,appendageLength=1,trunkLength=1.1f,legSplay=0;
        private int tailSegments=3,neckSegments=1,wingSegments=3,wingPairs=0,wingDigits=0,legPairs=3;
        private int bodySegments=8,appendageCount=6,appendageSegments=3,antennaSegments=2,trunkSegments=0;
        private boolean verticalBody;
        public Builder(){}
        private Builder(MARigConfig c){
            scale=c.scale;bodyLength=c.bodyLength;bodyWidth=c.bodyWidth;bodyHeight=c.bodyHeight;
            neckLength=c.neckLength;headLength=c.headLength;upperLegLength=c.upperLegLength;
            lowerLegLength=c.lowerLegLength;footLength=c.footLength;armLength=c.armLength;
            wingLength=c.wingLength;tailLength=c.tailLength;appendageLength=c.appendageLength;
            trunkLength=c.trunkLength;legSplay=c.legSplay;tailSegments=c.tailSegments;
            neckSegments=c.neckSegments;wingSegments=c.wingSegments;wingPairs=c.wingPairs;wingDigits=c.wingDigits;
            legPairs=c.legPairs;bodySegments=c.bodySegments;appendageCount=c.appendageCount;
            appendageSegments=c.appendageSegments;antennaSegments=c.antennaSegments;
            trunkSegments=c.trunkSegments;verticalBody=c.verticalBody;
        }
        public Builder scale(float v){scale=v;return this;}
        public Builder body(float length,float width,float height){bodyLength=length;bodyWidth=width;bodyHeight=height;return this;}
        public Builder neck(int segments,float length){neckSegments=segments;neckLength=length;return this;}
        public Builder headLength(float v){headLength=v;return this;}
        public Builder legs(float upper,float lower,float foot){upperLegLength=upper;lowerLegLength=lower;footLength=foot;return this;}
        public Builder legSplay(float v){legSplay=v;return this;}
        public Builder armLength(float v){armLength=v;return this;}
        public Builder tail(int segments,float length){tailSegments=segments;tailLength=length;return this;}
        public Builder wings(int pairs,int segments,float length){wingPairs=pairs;wingSegments=segments;wingLength=length;return this;}
        public Builder wingDigits(int v){wingDigits=v;return this;}
        public Builder legPairs(int v){legPairs=v;return this;}
        public Builder bodySegments(int v){bodySegments=v;return this;}
        public Builder appendages(int count,int segments,float length){appendageCount=count;appendageSegments=segments;appendageLength=length;return this;}
        public Builder antennaSegments(int v){antennaSegments=v;return this;}
        public Builder trunk(int segments,float length){trunkSegments=segments;trunkLength=length;return this;}
        public Builder verticalBody(boolean v){verticalBody=v;return this;}
        public MARigConfig build(){return new MARigConfig(this);}
    }
}

