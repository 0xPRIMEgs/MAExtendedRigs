package MAExtendedRigs;

import MechaAmazing.MAMesh;
import java.util.Map;

/** Stem chain with crown, radial petal chains and root chains. */
public final class MAPlantRig extends MAExtendedRig {
    public static final int TYPE=1011;
    public static MARigConfig defaultConfig(){return MARigConfig.builder().body(.6f,.6f,1.2f).bodySegments(3).appendages(6,2,.7f).tail(0,1).build();}
    public MAPlantRig(){this(defaultConfig());}
    public MAPlantRig(MARigConfig config){super(TYPE,Layouts.plant(config),config);}
    /** Install authored parts and preserve their connectors. */
    public MAPlantRig(Map<String,MAMesh> parts){this();setParts(parts);}
    public MAPlantRig(MARigConfig config,Map<String,MAMesh> parts){this(config);setParts(parts);}
}

