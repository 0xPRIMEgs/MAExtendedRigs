package MAExtendedRigs;

/** Package-private construction helpers. No per-frame work lives here. */
final class Layouts {
    private Layouts(){}
    private static final class D {
        final MARigConfig c;final MARigLayout.Builder b;
        D(String root,MARigConfig c){this.c=c;b=MARigLayout.builder(root);}
        void add(String n,String p,float x,float y,float z){b.joint(n,p,x*c.scale,y*c.scale,z*c.scale);}
        void tip(String n,float x,float y,float z){b.tip(n,x*c.scale,y*c.scale,z*c.scale);}
        String chain(String pre,String p,int n,float x,float y,float z,float dx,float dy,float dz){
            float s=c.scale;return b.chain(pre,p,n,x*s,y*s,z*s,dx*s,dy*s,dz*s);
        }
        void head(String parent,float x,float y,float z){
            float step=c.neckLength/Math.max(1,c.neckSegments);
            String last=chain("neck",parent,c.neckSegments,x,y,z,0,step,0);
            add("head",last,c.neckSegments==0?x:0,c.neckSegments==0?y:step,c.neckSegments==0?z:0);
            tip("head",0,0,c.headLength);
            chain("trunk","head",c.trunkSegments,0,0,c.headLength,0,-c.trunkLength/Math.max(1,c.trunkSegments),0);
        }
        void tail(String parent,float x,float y,float z){
            chain("tail",parent,c.tailSegments,x,y,z,0,0,-c.tailLength/Math.max(1,c.tailSegments));
        }
        void leg(String pre,String parent,int side,float x,float y,float z){
            add(pre+"UpperLeg",parent,x,y,z);
            add(pre+"LowerLeg",pre+"UpperLeg",side*c.legSplay,-c.upperLegLength,0);
            add(pre+"Foot",pre+"LowerLeg",0,-c.lowerLegLength,0);tip(pre+"Foot",0,0,c.footLength);
        }
        void birdLeg(String pre,String parent,int side){
            add(pre+"Thigh",parent,side*c.bodyWidth*.35f,-c.bodyHeight*.5f,0);
            add(pre+"Calf",pre+"Thigh",side*c.legSplay,-c.upperLegLength,-c.upperLegLength*.2f);
            add(pre+"Foot",pre+"Calf",0,-c.lowerLegLength,c.upperLegLength*.2f);tip(pre+"Foot",0,0,c.footLength);
        }
        void wings(String parent){
            for(int pair=0;pair<c.wingPairs;pair++)for(int side:new int[]{-1,1}){
                String pre=(side<0?"left":"right")+"Wing"+pair+"_";
                float step=c.wingLength/c.wingSegments;
                String last=chain(pre,parent,c.wingSegments,side*c.bodyWidth*.5f,c.bodyHeight*.25f,-pair*c.bodyLength*.25f,side*step,0,0);
                for(int digit=0;digit<c.wingDigits;digit++){
                    float angle=(digit/(float)Math.max(1,c.wingDigits-1)-.5f)*1.2f;
                    chain((side<0?"left":"right")+"Wing"+pair+"Finger"+digit+"_",last,2,
                        side*step,0,0,side*step*(float)Math.cos(angle),0,step*(float)Math.sin(angle));
                }
            }
        }
        void antennae(String parent){
            for(int side:new int[]{-1,1})chain(side<0?"leftAntenna":"rightAntenna",parent,c.antennaSegments,
                side*c.headLength*.25f,c.headLength*.2f,c.headLength*.4f,
                side*c.headLength*.12f,c.headLength*.3f,c.headLength*.25f);
        }
        MARigLayout done(){return b.build();}
    }
    static MARigLayout biped(MARigConfig c){
        D d=new D("groin",c);
        d.add("abs","groin",0,c.bodyHeight*.5f,0);d.add("chest","abs",0,c.bodyHeight*.5f,0);
        d.head("chest",0,c.bodyHeight*.25f,0);
        for(int side:new int[]{-1,1}){
            String pre=side<0?"left":"right";d.birdLeg(pre,"groin",side);
            d.add(pre+"UpperArm","chest",side*c.bodyWidth*.5f,0,0);
            d.add(pre+"LowerArm",pre+"UpperArm",side*c.armLength*.55f,0,0);
            d.add(pre+"Hand",pre+"LowerArm",side*c.armLength*.45f,0,0);
            d.tip(pre+"Hand",side*c.footLength*.6f,0,0);
        }
        d.tail("groin",0,0,-c.bodyLength*.15f);d.wings("chest");return d.done();
    }
    static MARigLayout quadruped(MARigConfig c){
        D d=new D("pelvis",c);d.add("chest","pelvis",0,0,c.bodyLength);d.tip("pelvis",0,0,c.bodyLength);
        d.head("chest",0,c.bodyHeight*.35f,c.bodyLength*.1f);
        for(int side:new int[]{-1,1}){
            String lr=side<0?"Left":"Right";
            d.leg("front"+lr,"chest",side,side*c.bodyWidth*.5f,-c.bodyHeight*.5f,0);
            d.leg("rear"+lr,"pelvis",side,side*c.bodyWidth*.5f,-c.bodyHeight*.5f,0);
        }
        d.tail("pelvis",0,c.bodyHeight*.25f,-c.bodyLength*.15f);d.wings("chest");return d.done();
    }
    static MARigLayout bird(MARigConfig c){
        D d=new D("body",c);d.tip("body",0,0,c.bodyLength*.5f);
        d.head("body",0,c.bodyHeight*.5f,c.bodyLength*.25f);
        d.birdLeg("left","body",-1);d.birdLeg("right","body",1);
        d.wings("body");d.tail("body",0,0,-c.bodyLength*.5f);return d.done();
    }
    static MARigLayout insect(MARigConfig c){
        D d=new D("thorax",c);
        d.add("abdomen","thorax",0,0,-c.bodyLength*.5f);d.tip("abdomen",0,0,-c.bodyLength*.6f);
        d.add("head","thorax",0,0,c.bodyLength*.4f);d.tip("head",0,0,c.headLength);d.antennae("head");
        for(int pair=0;pair<c.legPairs;pair++)for(int side:new int[]{-1,1}){
            float z=c.legPairs==1?0:c.bodyLength*(.35f-.7f*pair/(c.legPairs-1));
            d.leg((side<0?"left":"right")+"Leg"+pair,"thorax",side,side*c.bodyWidth*.5f,0,z);
        }
        d.wings("thorax");d.tail("abdomen",0,0,-c.bodyLength*.6f);return d.done();
    }
    static MARigLayout crustacean(MARigConfig c){
        D d=new D("body",c);d.add("head","body",0,c.bodyHeight*.2f,c.bodyLength*.4f);
        d.tip("head",0,0,c.headLength);d.antennae("head");
        for(int pair=0;pair<c.legPairs;pair++)for(int side:new int[]{-1,1}){
            float z=c.legPairs==1?0:c.bodyLength*(.25f-.7f*pair/(c.legPairs-1));
            d.leg((side<0?"left":"right")+"Leg"+pair,"body",side,side*c.bodyWidth*.5f,0,z);
        }
        for(int side:new int[]{-1,1}){
            String pre=side<0?"left":"right";
            d.add(pre+"ClawArm","body",side*c.bodyWidth*.4f,0,c.bodyLength*.4f);
            d.add(pre+"Claw",""+pre+"ClawArm",side*c.armLength*.25f,0,c.armLength);
            d.add(pre+"Pincer",pre+"Claw",side*c.footLength*.35f,0,c.footLength*.35f);
            d.tip(pre+"Claw",0,0,c.footLength);d.tip(pre+"Pincer",0,0,c.footLength*.7f);
        }
        return d.done();
    }
    static MARigLayout crawler(MARigConfig c){
        D d=new D("head",c);d.tip("head",0,0,c.headLength);d.antennae("head");
        float step=c.bodyLength/c.bodySegments;String parent="head";
        for(int seg=0;seg<c.bodySegments;seg++){
            String n="body"+seg;d.add(n,parent,0,0,-step);d.tip(n,0,0,-step);parent=n;
            for(int pair=0;pair<c.legPairs;pair++)for(int side:new int[]{-1,1}){
                String pre=(side<0?"left":"right")+"Leg"+seg+"_"+pair;
                d.leg(pre,n,side,side*c.bodyWidth*.5f,0,-step*pair/Math.max(1,c.legPairs));
            }
        }
        return d.done();
    }
    static MARigLayout serpent(MARigConfig c){
        D d=new D("head",c);d.tip("head",0,0,c.headLength);
        float step=c.bodyLength/c.bodySegments;
        d.chain("body","head",c.bodySegments,0,0,-step,0,0,-step);return d.done();
    }
    static MARigLayout fish(MARigConfig c){
        D d=new D("body",c);
        if(c.verticalBody){
            d.head("body",0,c.bodyLength*.5f,0);
            d.chain("tail","body",c.tailSegments,0,-c.bodyLength*.5f,0,0,-c.tailLength/Math.max(1,c.tailSegments),0);
        }else{
            d.add("head","body",0,0,c.bodyLength*.5f);d.tip("head",0,0,c.headLength);
            d.tail("body",0,0,-c.bodyLength*.5f);
        }
        for(int side:new int[]{-1,1})
            d.chain(side<0?"leftFin":"rightFin","body",c.wingSegments,side*c.bodyWidth*.5f,0,0,side*c.wingLength/c.wingSegments,0,0);
        d.add("dorsalFin","body",0,c.bodyHeight*.5f,0);d.tip("dorsalFin",0,c.bodyHeight*.5f,0);
        if(c.tailSegments>0){d.add("tailFin","tail"+(c.tailSegments-1),0,c.verticalBody?-c.tailLength/c.tailSegments:0,c.verticalBody?0:-c.tailLength/c.tailSegments);d.tip("tailFin",0,c.bodyHeight*.5f,-c.headLength*.3f);}
        return d.done();
    }
    static MARigLayout tentacle(MARigConfig c){
        D d=new D("body",c);d.add("head","body",0,0,c.headLength*.25f);d.tip("head",0,0,c.headLength);
        d.add("shell","body",0,c.bodyHeight*.3f,-c.bodyLength*.2f);d.tip("shell",0,c.bodyHeight*.5f,0);
        for(int i=0;i<c.appendageCount;i++){
            float a=(float)(2*Math.PI*i/c.appendageCount),x=(float)Math.cos(a),z=(float)Math.sin(a);
            d.chain("tentacle"+i+"_","body",c.appendageSegments,x*c.bodyWidth*.4f,-c.bodyHeight*.5f,z*c.bodyLength*.4f,0,-c.appendageLength/c.appendageSegments,0);
        }
        return d.done();
    }
    static MARigLayout snail(MARigConfig c){
        D d=new D("foot",c);d.tip("foot",0,0,c.bodyLength*.5f);
        d.add("body","foot",0,c.bodyHeight*.2f,0);
        d.add("shell","body",0,c.bodyHeight*.5f,-c.bodyLength*.15f);d.tip("shell",0,c.bodyHeight,0);
        d.head("body",0,0,c.bodyLength*.4f);d.antennae("head");
        d.tail("foot",0,0,-c.bodyLength*.3f);
        for(int pair=0;pair<c.legPairs;pair++)for(int side:new int[]{-1,1})
            d.leg((side<0?"left":"right")+"Leg"+pair,"body",side,side*c.bodyWidth*.4f,0,c.bodyLength*(.3f-.6f*pair/Math.max(1,c.legPairs-1)));
        return d.done();
    }
    static MARigLayout plant(MARigConfig c){
        D d=new D("base",c);float step=c.bodyHeight/c.bodySegments;
        String last=d.chain("stem","base",c.bodySegments,0,step,0,0,step,0);
        d.add("crown",last,0,step,0);d.tip("crown",0,c.headLength,0);
        for(int i=0;i<c.appendageCount;i++){
            float a=(float)(2*Math.PI*i/c.appendageCount),x=(float)Math.cos(a),z=(float)Math.sin(a);
            d.chain("petal"+i+"_","crown",c.appendageSegments,x*c.bodyWidth*.2f,0,z*c.bodyWidth*.2f,
                x*c.appendageLength/c.appendageSegments,0,z*c.appendageLength/c.appendageSegments);
            d.chain("root"+i+"_","base",c.appendageSegments,x*c.bodyWidth*.2f,0,z*c.bodyWidth*.2f,
                x*c.appendageLength*.5f/c.appendageSegments,-c.appendageLength*.25f/c.appendageSegments,z*c.appendageLength*.5f/c.appendageSegments);
        }
        return d.done();
    }
    static MARigLayout spirit(MARigConfig c){
        D d=new D("core",c);d.tip("core",0,c.bodyHeight*.5f,0);
        d.add("head","core",0,c.bodyHeight*.4f,0);d.tip("head",0,c.headLength,0);
        d.add("ring","core",0,0,0);d.tip("ring",c.bodyWidth,0,0);
        for(int i=0;i<c.appendageCount;i++){
            float a=(float)(2*Math.PI*i/c.appendageCount);
            d.add("satellite"+i,"ring",(float)Math.cos(a)*c.appendageLength,0,(float)Math.sin(a)*c.appendageLength);
            d.tip("satellite"+i,0,c.headLength*.25f,0);
        }
        d.chain("wisp","head",c.appendageSegments,0,c.headLength*.5f,0,0,c.appendageLength/c.appendageSegments,0);
        d.tail("core",0,-c.bodyHeight*.3f,0);return d.done();
    }
}

