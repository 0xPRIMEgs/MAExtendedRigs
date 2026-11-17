package MAExtendedRigs;

import MechaAmazing.*;
import java.util.*;

/**
 * Immutable named joint/connector layout. +Y is up, +Z is forward, named
 * left parts use -X. All offsets are in the parent's rest pivot frame.
 */
public final class MARigLayout {
    /** The connector belongs to parent; offset/frame describe that connector. */
    public record Slot(String name,String parent,int connector,float x,float y,float z,
                       float qx,float qy,float qz,float qw,float tipX,float tipY,float tipZ) {}
    private final Map<String,Slot> slots;
    private final Map<String,List<Slot>> children;
    private MARigLayout(LinkedHashMap<String,Slot> input){
        if(input.isEmpty())throw new IllegalArgumentException("A layout requires a root");
        slots=Collections.unmodifiableMap(new LinkedHashMap<>(input));
        Map<String,List<Slot>> lists=new LinkedHashMap<>();
        for(String n:slots.keySet())lists.put(n,new ArrayList<>());
        for(Slot s:slots.values())if(s.parent()!=null)lists.get(s.parent()).add(s);
        Map<String,List<Slot>> frozen=new LinkedHashMap<>();
        lists.forEach((n,v)->frozen.put(n,List.copyOf(v)));
        children=Collections.unmodifiableMap(frozen);
    }
    public static Builder builder(String root){return new Builder(root);}
    public Set<String> names(){return slots.keySet();}
    public Collection<Slot> slots(){return slots.values();}
    public String rootName(){return slots.keySet().iterator().next();}
    public Slot slot(String name){
        Slot s=slots.get(name);if(s==null)throw new IllegalArgumentException("Unknown slot: "+name);return s;
    }
    public List<Slot> children(String name){slot(name);return children.get(name);}
    public Builder toBuilder(){return new Builder(this);}
    /** Fresh empty part with an origin pivot and every required child connector. */
    public MAMesh template(String name){
        slot(name);
        MAMesh m=new MAMesh(new Vector<MATri>(),new MAPivot());
        for(Slot c:children(name))m.childPoint(new MAPivot().x(c.x()).y(c.y()).z(c.z()).rotation(c.qx(),c.qy(),c.qz(),c.qw()));
        return m;
    }
    public Map<String,MAMesh> templates(){
        Map<String,MAMesh> result=new LinkedHashMap<>();
        for(String name:names())result.put(name,template(name));
        return result;
    }
    public static final class Builder {
        private final LinkedHashMap<String,Slot> slots=new LinkedHashMap<>();
        private final Map<String,Integer> next=new HashMap<>();
        private Builder(String root){validName(root);slots.put(root,new Slot(root,null,-1,0,0,0,0,0,0,1,0,.2f,0));next.put(root,0);}
        private Builder(MARigLayout l){
            for(Slot s:l.slots()){slots.put(s.name(),s);next.put(s.name(),l.children(s.name()).size());}
        }
        private static void validName(String name){
            if(name==null||!name.matches("[A-Za-z][A-Za-z0-9_]*"))throw new IllegalArgumentException("Use a filename-safe joint name: "+name);
        }
        private static void finite(float... values){for(float v:values)if(!Float.isFinite(v))throw new IllegalArgumentException("Nonfinite layout coordinate");}
        public Builder joint(String name,String parent,float x,float y,float z){
            return joint(name,parent,new MAPivot().x(x).y(y).z(z));
        }
        public Builder joint(String name,String parent,MAPivot frame){
            validName(name);Objects.requireNonNull(frame,"frame");
            if(slots.containsKey(name))throw new IllegalArgumentException("Duplicate slot: "+name);
            if(!slots.containsKey(parent))throw new IllegalArgumentException("Define parent first: "+parent);
            if(slots.size()>=4096)throw new IllegalArgumentException("Layout exceeds 4096 joints");
            finite(frame.x(),frame.y(),frame.z(),frame.qx(),frame.qy(),frame.qz(),frame.qw());
            MAPivot q=new MAPivot().rotation(frame.qx(),frame.qy(),frame.qz(),frame.qw());
            int index=next.get(parent);next.put(parent,index+1);next.put(name,0);
            slots.put(name,new Slot(name,parent,index,frame.x(),frame.y(),frame.z(),q.qx(),q.qy(),q.qz(),q.qw(),0,.2f,0));
            return this;
        }
        /** Move an existing parent connector without changing topology/order. */
        public Builder offset(String name,float x,float y,float z){
            finite(x,y,z);Slot s=slots.get(name);
            if(s==null||s.parent()==null)throw new IllegalArgumentException("Expected a child slot: "+name);
            slots.put(name,new Slot(s.name(),s.parent(),s.connector(),x,y,z,s.qx(),s.qy(),s.qz(),s.qw(),s.tipX(),s.tipY(),s.tipZ()));
            return this;
        }
        /** Replace the parent connector frame without changing topology/order. */
        public Builder frame(String name,MAPivot frame){
            Slot s=slots.get(name);
            if(s==null||s.parent()==null)throw new IllegalArgumentException("Expected a child slot: "+name);
            finite(frame.x(),frame.y(),frame.z(),frame.qx(),frame.qy(),frame.qz(),frame.qw());
            MAPivot q=new MAPivot().rotation(frame.qx(),frame.qy(),frame.qz(),frame.qw());
            slots.put(name,new Slot(s.name(),s.parent(),s.connector(),frame.x(),frame.y(),frame.z(),q.qx(),q.qy(),q.qz(),q.qw(),s.tipX(),s.tipY(),s.tipZ()));
            return this;
        }
        /** Display/authoring guide endpoint only; it does not add a joint. */
        public Builder tip(String name,float x,float y,float z){
            finite(x,y,z);Slot s=slots.get(name);
            if(s==null)throw new IllegalArgumentException("Unknown slot: "+name);
            slots.put(name,new Slot(s.name(),s.parent(),s.connector(),s.x(),s.y(),s.z(),s.qx(),s.qy(),s.qz(),s.qw(),x,y,z));
            return this;
        }
        /**
         * Add prefix0..prefix(count-1). The first pivot is start; each later
         * pivot advances by step. Returns the last name, or parent for count 0.
         */
        public String chain(String prefix,String parent,int count,float x,float y,float z,float dx,float dy,float dz){
            if(count<0||count>256)throw new IllegalArgumentException("Chain count must be 0..256");
            String last=parent;
            for(int i=0;i<count;i++){
                String n=prefix+i;joint(n,last,i==0?x:dx,i==0?y:dy,i==0?z:dz).tip(n,dx,dy,dz);last=n;
            }
            return last;
        }
        public MARigLayout build(){return new MARigLayout(slots);}
    }
}

