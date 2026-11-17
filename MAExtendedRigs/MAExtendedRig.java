package MAExtendedRigs;

import MechaAmazing.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * A normal MARig with a reusable body layout. Constructors install empty
 * anchor parts, so any visible descendant works before its parent has art.
 * No rendering/animation implementation is duplicated from MechaAmazing.
 */
public sealed class MAExtendedRig extends MARig permits MABipedRig, MAQuadrupedRig,
        MABirdRig, MAInsectRig, MACrustaceanRig, MACrawlerRig, MASerpentRig,
        MAFishRig, MATentacleRig, MASnailRig, MAPlantRig, MASpiritRig {
    private final MARigLayout layout;
    private final MARigConfig config;
    public MAExtendedRig(int type,MARigLayout layout){this(type,layout,MARigConfig.builder().build());}
    public MAExtendedRig(int type,MARigLayout layout,MARigConfig config){
        super(type);this.layout=Objects.requireNonNull(layout,"layout");this.config=Objects.requireNonNull(config,"config");
        for(MARigLayout.Slot s:layout.slots())super.define(s.name(),s.parent(),s.connector());
        super.motionUnit(config.scale);super.setParts(layout.templates());
    }
    public final MARigLayout layout(){return layout;}
    public final MARigConfig config(){return config;}
    /** Topology belongs to the immutable layout; build a new layout to change it. */
    @Override public final MARig define(String name,String parent,int connector){
        throw new UnsupportedOperationException("Build a MARigLayout before constructing an extended rig");
    }
    @Override public final MARig connector(String name,int index){
        throw new UnsupportedOperationException("Connector indices belong to the immutable MARigLayout");
    }
    public MAMesh partTemplate(String name){return layout.template(name);}
    /** Hide one part's art while preserving its children and connectors. */
    public MAExtendedRig clearGeometry(String name){super.setPart(name,partTemplate(name));return this;}
    /**
     * Copy geometry and texture; replace child connectors with this layout.
     * The geometry's pivot position and orientation are respected. Mesh
     * dimensions are NOT automatically changed. Authored extra connectors
     * are discarded; use inherited setPart() to keep authored connections.
     */
    public MAMesh prepareGeometry(String name,MAMesh source){
        Objects.requireNonNull(source,"source");layout.slot(name);
        // MARig also checks that supplied parts are unassembled. Do not silently
        // strip an assembled hierarchy: inspect through a throwaway core rig.
        MARig probe=new MARig();probe.define("part",null,-1).setPart("part",source);
        MAMesh copy=new MAMesh(source);
        while(copy.childPointSize()>0)copy.childPointRemove(copy.childPointSize()-1);
        MAPivot p=copy.pivotPoint();float[] q=MAMeshMath.orientation(p);
        for(MARigLayout.Slot c:layout.children(name)){
            float[] v=rotate(q,c.x(),c.y(),c.z());
            float ax=q[0],ay=q[1],az=q[2],aw=q[3],bx=c.qx(),by=c.qy(),bz=c.qz(),bw=c.qw();
            copy.childPoint(new MAPivot().x(p.x()+v[0]).y(p.y()+v[1]).z(p.z()+v[2])
                .rotation(aw*bx+ax*bw+ay*bz-az*by,aw*by-ax*bz+ay*bw+az*bx,
                          aw*bz+ax*by-ay*bx+az*bw,aw*bw-ax*bx-ay*by-az*bz));
        }
        return copy;
    }
    private static float[] rotate(float[] q,float x,float y,float z){
        float tx=2*(q[1]*z-q[2]*y),ty=2*(q[2]*x-q[0]*z),tz=2*(q[0]*y-q[1]*x);
        return new float[]{x+q[3]*tx+q[1]*tz-q[2]*ty,y+q[3]*ty+q[2]*tx-q[0]*tz,z+q[3]*tz+q[0]*ty-q[1]*tx};
    }
    public MAExtendedRig setGeometry(String name,MAMesh geometry){super.setPart(name,prepareGeometry(name,geometry));return this;}
    /** Prepare all parts before making a single atomic core installation. */
    public MAExtendedRig setGeometries(Map<String,MAMesh> parts){
        Map<String,MAMesh> prepared=new LinkedHashMap<>();
        parts.forEach((name,m)->prepared.put(name,m==null?partTemplate(name):prepareGeometry(name,m)));
        super.setParts(prepared);return this;
    }
    /**
     * Load existing name.mamesh files. Missing files keep current parts.
     * useLayoutConnectors=true ignores authored child sockets; false preserves
     * them and lets the core validate required socket indices.
     * Runtime textures are set separately and remain caller-owned.
     */
    public MAExtendedRig loadParts(Path directory,boolean useLayoutConnectors)throws IOException{
        if(!Files.isDirectory(directory))throw new IOException("Missing part directory: "+directory);
        Map<String,MAMesh> parts=new LinkedHashMap<>();
        for(String name:layout.names()){
            Path file=directory.resolve(name+".mamesh");
            if(Files.exists(file))parts.put(name,MAIO.loadMAMesh(file.toString()));
        }
        if(useLayoutConnectors)setGeometries(parts);else setParts(parts);
        return this;
    }
    public MAExtendedRig loadParts(Path directory)throws IOException{return loadParts(directory,false);}
    /** Write connector-only authoring files. Existing files are never overwritten. */
    public void writeTemplates(Path directory)throws IOException{
        Files.createDirectories(directory);
        for(String n:layout.names())if(Files.exists(directory.resolve(n+".mamesh")))
            throw new FileAlreadyExistsException(directory.resolve(n+".mamesh").toString());
        for(String n:layout.names())MAIO.saveMAMesh(partTemplate(n),directory.resolve(n+".mamesh").toString());
    }
    @Override public MAExtendedRig setPart(String n,MAMesh m){super.setPart(n,m);return this;}
    @Override public MAExtendedRig setParts(Map<String,MAMesh> p){super.setParts(p);return this;}
    @Override public MAExtendedRig replace(String n,MAMesh m){super.replace(n,m);return this;}
    @Override public MAExtendedRig rotation(String n,float x,float y,float z){super.rotation(n,x,y,z);return this;}
    @Override public MAExtendedRig rotationQuaternion(String n,float x,float y,float z,float w){super.rotationQuaternion(n,x,y,z,w);return this;}
    @Override public MAExtendedRig translation(String n,float x,float y,float z){super.translation(n,x,y,z);return this;}
    @Override public MAExtendedRig position(float x,float y,float z){super.position(x,y,z);return this;}
    @Override public MAExtendedRig orientation(float x,float y,float z){super.orientation(x,y,z);return this;}
    @Override public final MAExtendedRig update(){super.update();return this;}
    /** Core installation invokes this hook during construction. Keep it inert. */
    @Override protected final void onUpdated(){}
    @Override public MAExtendedRig resetPose(){super.resetPose();return this;}
    @Override public MAExtendedRig setColor(int r,int g,int b,int a){super.setColor(r,g,b,a);return this;}
    @Override public MAExtendedRig setColor(String n,int r,int g,int b,int a){super.setColor(n,r,g,b,a);return this;}
    @Override public MAExtendedRig setTexture(String n,com.raylib.Raylib.Texture t){super.setTexture(n,t);return this;}
}

